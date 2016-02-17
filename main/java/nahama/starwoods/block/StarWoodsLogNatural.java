package nahama.starwoods.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nahama.starwoods.StarWoodsModCore;
import nahama.starwoods.core.StarWoodsBlockCore;
import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.core.StarWoodsItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StarWoodsLogNatural extends BlockLog {

	protected IIcon[][] iicon = new IIcon[2][16];
	private int type;

	public StarWoodsLogNatural(int type) {
		super();
		this.setCreativeTab(StarWoodsModCore.tabStarWoodsBlock);
		this.setLightLevel(0.6F);
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
		this.type = type * 16;
	}

	/**ドロップアイテムの設定*/
	@Override
	public Item getItemDropped(int meta, Random random, int fotune) {
		return Item.getItemFromBlock(StarWoodsBlockCore.logStarWoods[(meta + type) / 4]);
	}

	/**ドロップ数の設定*/
	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return random.nextInt(this.getChancewithBonus(meta, fortune)) == 0 ? 2 : 1;
	}

	/**ドロップアイテムをItemStackのArrayListで返す*/
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		ret.add(new ItemStack(this.getItemDropped(meta, world.rand, fortune), 1, (meta + type) % 4));

		int chance = this.getChancewithBonus(meta, fortune);

		if (world.rand.nextInt(chance) == 0) {
			ret.add(new ItemStack(StarWoodsItemCore.sapStarWoods, 1, meta + type));
		}

		return ret;
	}

	/**メタデータからchanceを返す*/
	protected int getChance(int meta) {
		int chance = StarWoodsConfigCore.dropSap;

		switch (meta + type) {
		case 16://ネザースター
			chance = StarWoodsConfigCore.dropSap * 8;

		case 0://星
		case 3://ダイヤモンド
		case 17://マグマクリーム
		case 18://ガストの涙
		case 19://エンダーパール
			chance = StarWoodsConfigCore.dropSap * 4;
			break;

		case 9://金
		case 11://エメラルド
		case 20://スライムボール
		case 21://クモの目
		case 22://ブレイズロッド
		case 23://ウィザースケルトンの頭蓋骨
			chance = StarWoodsConfigCore.dropSap * 2;
		}
		if (meta >= 48) chance = StarWoodsConfigCore.dropSap * 2;
		return chance;
	}

	/**fortuneを適用したときのchanceを返す*/
	protected int getChancewithBonus(int meta, int fortune) {
		int chance = this.getChance(meta);

		if (fortune > 0) {
			chance -= 1 << fortune;
			if (chance < 1) chance = 1;
		}

		return chance;
	}

	/**メタデータによりドロップを変える*/
	@Override
	public int damageDropped(int meta) {
		return (meta + type) % 16;
	}

	/**シルクタッチで回収できないようにする*/
	@Override
	public boolean canSilkHarvest() {
		return false;
	}

	/**メタデータ違いのブロックを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 16; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	/**面の方角によってアイコンを変える*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if(side == 0 || side == 1) {
			return this.iicon[0][meta];
		} else if(side == 2 || side == 3 || side == 4 || side == 5 || side == 6) {
			return this.iicon[1][meta];
		} else {
			return null;
		}
	}

	/**ブロックのアイコンを設定する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		for (int i = 0; i < 16; i ++) {
			this.iicon[0][i] = iicon.registerIcon(this.getTextureName() + "_top-" + (i + type));
			this.iicon[1][i] = iicon.registerIcon(this.getTextureName() + "_side-" + (i + type));
		}
	}

	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName() + ":" + String.valueOf(this.type);
	}

	//BlockLog,BlockRotatedPillarでオーバーライドされたメソッドをBlockと同じ処理にオーバーライドしなおす。

	@Override
	public int getRenderType() {
		return 0;
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
		return meta;
	}

	@Override
	protected ItemStack createStackedBlock(int meta) {
		return new ItemStack(Item.getItemFromBlock(this), 1, meta);
	}


	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {}

	@Override
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z) {
		return false;
	}

}