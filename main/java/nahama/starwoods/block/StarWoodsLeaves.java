package nahama.starwoods.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nahama.starwoods.StarWoodsModCore;
import nahama.starwoods.core.StarWoodsBlockCore;
import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.item.PruningScissors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StarWoodsLeaves extends BlockLeaves {

	private IIcon[][] iicon = new IIcon[2][16];
	private int type;

	public StarWoodsLeaves(int type) {
		super();
		this.setCreativeTab(StarWoodsModCore.tabStarWoodsBlock);
		this.setLightLevel(1.0F);
		this.setTickRandomly(false);
		this.type = type * 16;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
		return true;
	}

	/**メタデータにより返すIIconを変える*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[this.isOpaqueCube() ? 1 : 0][meta];
	}

	/**不透明なブロックかどうか。*/
	@Override
	public boolean isOpaqueCube() {
		return Blocks.leaves.isOpaqueCube();
	}

	/**ドロップアイテムのリストを返す。*/
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		int count = quantityDropped(meta, fortune, world.rand);
		for(int i = 0; i < count; i++) {
			Item item = getItemDropped(meta, world.rand, fortune);
			if (item != null) {
				ret.add(new ItemStack(item, 1, (meta + type) % 8));
			}
		}
		return ret;
	}

	/**メタデータによりドロップアイテムを変える*/
	@Override
	public int damageDropped(int meta) {
		return (meta + type) % 16;
	}

	/**メタデータ違いのブロックを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 16; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	/**メタデータ違いのテクスチャを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		String s = "-";
		String s2 = "_opaque-";
		for (int i = 0; i < 16; i ++) {
			this.iicon[0][i] = iicon.registerIcon(this.getTextureName() + s + (i + type));
			this.iicon[1][i] = iicon.registerIcon(this.getTextureName() + s2 +(i + type));
		}
	}

	/**ブロックを破壊する際に呼ばれる。枝切りハサミでもシルクタッチと同様になるように*/
	@Override
	public void harvestBlock(World world, EntityPlayer entityPlayer, int x, int y, int z, int meta) {
		//プレイヤーが持っているアイテムを取得
		Item usingItem = null;
		if (entityPlayer.getHeldItem() != null) usingItem =  entityPlayer.getHeldItem().getItem();
		//枝切りハサミで破壊したら
		if (PruningScissors.isHarvest) {
			//ブロックをドロップさせる
			ItemStack dropItem = this.createStackedBlock(meta);
			this.dropBlockAsItem(world, x, y, z, dropItem);
			//回収不可にする
			PruningScissors.isHarvest = false;
		//バニラのハサミで破壊したら
		} else if (usingItem == Items.shears) {
			//耐久値を消費して、
			entityPlayer.getHeldItem().damageItem(1, entityPlayer);
			//普通の処理をする
			super.harvestBlock(world, entityPlayer, x, y, z, meta);
		} else {
			//普通の処理をする
			super.harvestBlock(world, entityPlayer, x, y, z, meta);
		}
	}

	/**シルクタッチが適用可能か*/
	@Override
	public boolean canSilkHarvest() {
		return true;
	}

	/**ドロップ数の設定*/
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		int chance = this.getChance();

		if (fortune > 0) {
			chance -= 1 << fortune;
			if (chance < 1) chance = 1;
		}

		//fortune:chanceで、0:48,1:46,2:44,3:40
		return random.nextInt(chance) == 0 ? 1 : 0;
	}

	/**chanceを求める*/
	protected int getChance() {
		return StarWoodsConfigCore.dropSapling;
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return Item.getItemFromBlock(StarWoodsBlockCore.saplingStarWoods[(meta + type) / 8]);
	}

	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName() + ":" + String.valueOf(this.type);
	}

	//BlockLeaves,BlockLeavesBaseでオーバーライドされたメソッドをBlockと同じ処理にオーバーライドしなおす。

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int par1) {
		return 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess iBlockAccess, int x, int y, int z) {
		return 16777215;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	protected ItemStack createStackedBlock(int meta) {
		Item item = Item.getItemFromBlock(this);
		return new ItemStack(item, 1, meta);
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		return ret;
	}

	@Override
	public void beginLeavesDecay(World world, int x, int y, int z){}

	@Override
	public String[] func_150125_e() {
		return new String[]{""};
	}

}
