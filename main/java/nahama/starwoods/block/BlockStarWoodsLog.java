package nahama.starwoods.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.StarWoodsCore;
import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.core.StarWoodsItemCore;
import nahama.starwoods.manager.StarWoodsTreeManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStarWoodsLog extends BlockLog {

	private IIcon[] iicon = new IIcon[2];
	private int baseNum;

	public BlockStarWoodsLog(int instanceNum) {
		this.setCreativeTab(StarWoodsCore.tabStarWoodsBlock);
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
		this.setLightLevel(0.9F);
		baseNum = instanceNum * StarWoodsTreeManager.LOG;
	}

	/** クリエイティブタブに登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < StarWoodsTreeManager.LOG; i++) {
			if (i + baseNum >= StarWoodsTreeManager.getAmountTreeKind())
				break;
			list.add(new ItemStack(item, 1, i));
			list.add(new ItemStack(item, 1, i + 12));
		}
	}

	/** アイコンを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		iicon[0] = register.registerIcon(this.getTextureName() + "_top");
		iicon[1] = register.registerIcon(this.getTextureName());
	}

	/** 上下面のアイコンを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getTopIcon(int meta) {
		return iicon[0];
	}

	/** 側面のアイコンを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int meta) {
		return iicon[1];
	}

	/** 翻訳前の名前を返す。 */
	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName() + ":" + String.valueOf(baseNum);
	}

	/** ドロップアイテムのリストを返す。 */
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		// (自然)でなければ、通常の処理をする。
		if (meta < 12)
			return super.getDrops(world, x, y, z, meta, fortune);
		int num = this.getGeneralNum(meta);
		// 無効なら、通常の処理をする。
		if (num >= StarWoodsTreeManager.getAmountTreeKind())
			return super.getDrops(world, x, y, z, meta, fortune);
		// 確率で樹液をドロップアイテムに加える。
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.addAll(super.getDrops(world, x, y, z, meta, fortune));
		int chance = this.getChanceWithBonus((meta % StarWoodsTreeManager.LOG), fortune);
		if (world.rand.nextInt(chance) == 0)
			ret.add(new ItemStack(StarWoodsItemCore.sap, 1, num));
		return ret;
	}

	/** 樹液のドロップ確率を返す。 */
	protected int getChance(int meta) {
		int chance = StarWoodsConfigCore.probablilityDropSap;
		int tier = StarWoodsTreeManager.getTier(this.getGeneralNum(meta));
		if (tier > 0)
			chance *= Math.pow(2, tier);
		return chance;
	}

	/** 幸運を適用したドロップ確率を返す。 */
	public int getChanceWithBonus(int meta, int fortune) {
		int chance = this.getChance((meta % StarWoodsTreeManager.LOG));
		if (fortune > 0) {
			chance -= 1 << fortune;
			if (chance < 1)
				chance = 1;
		}
		return chance;
	}

	public int getGeneralNum(int meta) {
		return (meta % StarWoodsTreeManager.LOG) + baseNum;
	}

	/** ドロップする際のメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		return meta % StarWoodsTreeManager.LOG;
	}

	/** シルクタッチが適用できるか。 */
	@Override
	public boolean canSilkHarvest() {
		return false;
	}

	/** アイコンを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		// (自然)でなければ、通常の処理をする。
		if (meta < 12)
			return super.getIcon(side, meta);
		if (side == 0 || side == 1)
			return iicon[0];
		return iicon[1];
	}

	/** 破壊された時の処理。 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (StarWoodsConfigCore.canLeavesDecay)
			super.breakBlock(world, x, y, z, block, meta);
	}

	/** 葉を支えられるか。 */
	@Override
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
		return StarWoodsConfigCore.canLeavesDecay;
	}

	/** 原木か。 */
	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z) {
		return true;
	}

	/** 設置された時の処理。 */
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float posX, float posY, float posZ, int meta) {
		if (meta < 12)
			return super.onBlockPlaced(world, x, y, z, side, posX, posY, posZ, meta);
		return meta;
	}

	/** 色を返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta) {
		return StarWoodsTreeManager.getColor(this.getGeneralNum(meta));
	}

	/** 色を返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess iBlockAccess, int x, int y, int z) {
		return StarWoodsTreeManager.getColor(this.getGeneralNum(iBlockAccess.getBlockMetadata(x, y, z)));
	}

}
