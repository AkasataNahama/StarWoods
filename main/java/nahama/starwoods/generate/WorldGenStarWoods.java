package nahama.starwoods.generate;

import java.util.Random;

import nahama.starwoods.block.StarWoodsSapling;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenStarWoods extends WorldGenAbstractTree {

	/**原木・葉のメタデータ*/
	private final int meta;
	/**生成する前の苗木*/
	private final StarWoodsSapling genSapling;
	/**生成する原木*/
	private final Block genWood;
	/**生成する葉*/
	private final Block genLeaves;

	public WorldGenStarWoods(int meta, StarWoodsSapling genSapling, Block genWood, Block genLeaves) {
		super(true);
		this.meta = meta;
		this.genSapling = genSapling;
		this.genWood = genWood;
		this.genLeaves = genLeaves;
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		//木の高さ
		final int h = random.nextInt(3) + 5;

		//高さの制限
		if ((y < 1) || (y + h > 256)) {
			return false;
		}

		//下のブロックの判定
		Block bottom = world.getBlock(x, y - 1, z);
		if (!bottom.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this.genSapling)) {
			return false;
		}

		//成長予定の場所の調査
		for (int iy = y; iy <= y + 1 + h; iy ++) {
			//x・z方向の調べる範囲。デフォルトは3*3
			byte l= 1;

			//1段目・2段目は1*1
			if (iy <= y + 1) {
				l = 0;
			}

			//最上段の1つ上(空気のまま)・最上段(幹は無い)・その下(幹の最上段)は5*5
			if (iy >= y + h - 1) {
				l = 2;
			}

			for (int ix = x - l; ix <= x + l; ix ++) {
				for (int iz = z - l; iz <= z + l; iz ++) {
					//調査場所が置換不可なら生成失敗
					if (!super.isReplaceable(world, ix, iy, iz)) {
						return false;
					}
				}
			}
		}

		//1つ下のブロックの、上で植物が育った時の処理を行う。
		bottom.onPlantGrow(world, x, y - 1, z, x, y, z);

		//葉ブロックを生成する。
		for (int iy = y + h - 3; iy <= y + h; iy ++) {
			/*-3～0。1周するごとに+1。*/
			int h2 = iy - (y + h);
			/*2~1。2周すると-1。*/
			int l = 1 - (h2 / 2);

			for (int ix = x - l; ix <= x + l; ix ++) {
					for (int iz = z - l; iz <= z + l; iz ++) {
						//角に当たる部分で、最上段でないならばランダムで生成。
						if (Math.abs(ix - x) != l || Math.abs(iz - z) != l || (h2 != 0 && random.nextInt(2) == 0)) {
							Block block = world.getBlock(ix, iy, iz);
							//空気ブロックか葉ブロックならば、葉ブロックに置き換える。
							if (block.isAir(world, ix, iy, iz) || block.isLeaves(world, ix, iy, iz)) {
								this.setBlockAndNotifyAdequately(world, ix, iy, iz, this.genLeaves, this.meta);
							}
						}
					}
				}
			}

		//原木ブロックを生成する。
		for (int iy = 0; iy < h; iy ++) {
			Block block = world.getBlock(x, y + iy, z);
			//空気ブロックか葉ブロックならば、原木ブロックに置き換える。
			if (block.isAir(world, x, y + iy, z) || block.isLeaves(world, x, y + iy, z)) {
				this.setBlockAndNotifyAdequately(world, x, y + iy, z, this.genWood, this.meta);
			}
		}

		return true;
	}

}
