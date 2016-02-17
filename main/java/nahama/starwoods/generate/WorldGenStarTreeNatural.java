package nahama.starwoods.generate;

import java.util.Random;

import nahama.starwoods.block.StarTreeSaplingNatural;
import nahama.starwoods.core.StarWoodsBlockCore;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;
public class WorldGenStarTreeNatural extends WorldGenAbstractTree {

	public WorldGenStarTreeNatural() {
		super(true);
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		//木の高さ。6で固定
		final int h = 6;

		//高さの制限
		if ((y < 1) || (y + h > 256)) {
			return false;
		}

		//下のブロックの判定
		Block bottom = world.getBlock(x, y - 1, z);
		if (!bottom.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (StarTreeSaplingNatural)StarWoodsBlockCore.saplingStarTreeNatural)) {
			return false;
		}

		//成長予定の場所の調査
		for (int iy = y; iy <= y + 2 + h; iy ++) {
			//x・z方向の調べる範囲。デフォルトは3*3
			byte l= 1;

			//起点とその1ブロック上/葉ブロックの範囲(最も高い原木の2ブロック下)から上は7 * 7範囲を調査
			if (iy <= y + 1 || iy >= y + h - 2) {
				l = 3;
			}

			for (int ix = x - l; ix <= x + l; ix ++) {
				for (int iz = z - l; iz <= z + l; iz ++) {
					//調査場所が置換不可なら生成失敗
					if (!this.isReplaceable(world, ix, iy, iz) && world.getBlock(ix, iy, iz) != Blocks.tallgrass) {
						return false;
					}
				}
			}
		}

		//1つ下のブロックの、上で植物が育った時の処理を行う。
		bottom.onPlantGrow(world, x, y - 1, z, x, y, z);

		//葉を生成する。
		for (int iy = y + h - 2; iy <= y + h + 2; iy ++) {
			//1~5。下からの段数
			int ly = iy - y - 3;
			//生成する範囲。デフォルトは5*5。
			int l = 2;

			switch (ly) {
			//下から2・3段目なら7*7。
			case 2:
			case 3:
				l = 3;
				break;
			//最上段なら3*3。
			case 5:
				l = 1;
			}

			//x,zのループ。
			for (int ix = x - l; ix <= x + l; ix ++) {
				for (int iz = z - l; iz <= z + l; iz ++) {

					int absX = Math.abs(ix - x);
					int absZ = Math.abs(iz - z);

					/*ここを葉に置き換えるかどうか*/
					boolean put = true;
					switch (ly) {
					//最下段・上から2段目で、
					case 1:
					case 4:
						//角のブロックなら置き換えない。
						if (absX == 2 && absZ == 2) {
							put = false;
						}
						break;

					//下から2段目で、
					case 2:
						//xかzの絶対値が3なら(一番外側の辺なら)、
						if (absX == 3 || absZ == 3) {
							//xかzの絶対値が0(辺の真ん中)でなければ置き換えない。
							put = (absX == 0 || absZ == 0)? true : false;
						}
						break;

					//下から3段目で、
					case 3:
						//xとzの絶対値が3か2なら(4隅の2*2なら)、
						if ((absX == 3 || absX == 2) && (absZ == 3 || absZ == 2)) {
							//x・zの絶対値が2でなければ置き換えない。
							put = (absX == 2 && absZ == 2)? true : false;
						}
					}

					if (put) {
						Block block = world.getBlock(ix, iy, iz);
						//空気ブロックか葉ブロックなら
						if (block.isAir(world, ix, iy, iz) || block.isLeaves(world, ix, iy, iz)) {
							//置き換える
							this.setBlockAndNotifyAdequately(world, ix, iy, iz, StarWoodsBlockCore.leavesStarWoods[0], 0);
						}
					}

				}
			}
		}

		//原木の生成
		for (int iy = y; iy <= y + h; iy ++) {
			//1～7(h+1)。下からの段数。
			int ly = iy - y + 1;
			//生成する範囲。デフォルトは3*3。
			int l = 1;

			switch (ly) {
			//最下段なら7*7。
			case 1:
				l = 3;
				break;
			//下から2段目なら5*5。
			case 2:
				l = 2;
				break;
			//最上段なら1*1。
			case h + 1:
				l = 0;
			}

			for (int ix = x - l; ix <= x + l; ix ++) {
				for (int iz = z - l; iz <= z + l; iz ++) {

					int absX = Math.abs(ix- x);
					int absZ = Math.abs(iz- z);

					/*ここを原木に置き換えるかどうか*/
					boolean put = true;
					switch (ly) {
					//最下段で、
					case 1:
						//xとzの絶対値が3か2なら(4隅の2*2なら)、
						if ((absX == 3 || absX == 2) && (absZ == 3 || absZ == 2)) {
							//xとzの絶対値が2でなければ置き換えない。
							put = (absX == 2 && absZ == 2)? true : false;
						}
						break;

					//下から2段目で、
					case 2:
						//xとzの絶対値が2なら(4隅なら)置き換えない。
						if (absX == 2 && absZ == 2) {
							put = false;
						}
						break;

					//下から5・6段目(上から2・3段目)で、
					case 5:
					case 6:
						//xとzの絶対値が1なら(4隅なら)置き換えない。
						if (absX == 1 && absZ == 1) {
							put = false;
						}
					}

					if (put) {
						Block block = world.getBlock(ix, iy, iz);
						//空気ブロックか葉ブロックなら
						if (block.isAir(world, ix, iy, iz) || block.isLeaves(world, ix, iy, iz) || block == Blocks.tallgrass) {
							//置き換える
							this.setBlockAndNotifyAdequately(world, ix, iy, iz, StarWoodsBlockCore.logStarWoods[0], 0);
						}
					}

				}
			}
		}

		return true;
	}

}
