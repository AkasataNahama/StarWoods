package nahama.starwoods.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.StarWoodsCore;
import nahama.starwoods.generate.WorldGenStarWoods;
import nahama.starwoods.manager.StarWoodsTreeManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockStarWoodsSapling extends BlockSapling {

	private int baseNum;

	public BlockStarWoodsSapling(int instanceNum) {
		this.setCreativeTab(StarWoodsCore.tabStarWoodsBlock);
		this.setLightLevel(0.6F);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.setTickRandomly(true);
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		baseNum = instanceNum * StarWoodsTreeManager.SAPLING;
	}

	/** ドロップする際のメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		return meta % StarWoodsTreeManager.SAPLING;
	}

	/** クリエイティブタブに登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < StarWoodsTreeManager.SAPLING; i++) {
			if (i + baseNum >= StarWoodsTreeManager.getAmountTreeKind())
				break;
			list.add(new ItemStack(item, 1, i));
		}
	}

	/** アイコンを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		blockIcon = register.registerIcon(this.getTextureName());
	}

	/** アイコンを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return blockIcon;
	}

	/** 更新時の処理。 */
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (world.isRemote)
			return;
		this.checkAndDropBlock(world, x, y, z);
		if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0)
			this.growTree(world, x, y, z, random);
	}

	/** 成長させる処理。 */
	private void growTree(World world, int x, int y, int z, Random random) {
		int meta = world.getBlockMetadata(x, y, z);
		if ((meta & StarWoodsTreeManager.SAPLING) == 0) {
			world.setBlockMetadataWithNotify(x, y, z, meta | StarWoodsTreeManager.SAPLING, 4);
			return;
		}
		world.setBlock(x, y, z, Blocks.air, 0, 4);
		if (!this.generateTree(world, x, y, z, random, meta))
			world.setBlock(x, y, z, this, meta, 4);
	}

	/** 木を生成する処理。 */
	protected boolean generateTree(World world, int x, int y, int z, Random random, int meta) {
		return new WorldGenStarWoods((meta % StarWoodsTreeManager.SAPLING) + baseNum).generate(world, random, x, y, z);
	}

	/** 設置された状態を維持できなければドロップする処理。 */
	@Override
	protected void checkAndDropBlock(World world, int x, int y, int z) {
		if (this.canBlockStay(world, x, y, z))
			return;
		this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		world.setBlock(x, y, z, getBlockById(0), 0, 2);
	}

	/** 設置された状態を維持できるか。 */
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}

	/** 当たり判定を返す。 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	/** 設置できるか。 */
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
	}

	/** 不透明か。 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/** 通常の描画か。 */
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/** 描画のタイプを返す。 */
	@Override
	public int getRenderType() {
		return 1;
	}

	/** 隣接するブロックが更新された時の処理。 */
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		this.checkAndDropBlock(world, x, y, z);
	}

	/** 翻訳前の名前を返す。 */
	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName() + ":" + String.valueOf(baseNum);
	}

	// 以下骨粉用のメソッド。(IGrowable)
	/** 骨粉がかけられるかどうか。(?) */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean flag) {
		return true;
	}

	/** 骨粉が適用されるかどうか。(?) */
	@Override
	public boolean func_149852_a(World world, Random x, int y, int z, int meta) {
		return world.rand.nextFloat() < 0.45D;
	}

	/** 骨粉が適用された時の処理。 */
	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z) {
		this.growTree(world, x, y, z, random);
	}

	// 以下作物用のメソッド。(IPlantable)
	/** 作物のタイプを返す。 */
	@Override
	public EnumPlantType getPlantType(IBlockAccess iBlockAccess, int x, int y, int z) {
		return EnumPlantType.Plains;
	}

	/** 作物のインスタンスを返す。 */
	@Override
	public Block getPlant(IBlockAccess iBlockAccess, int x, int y, int z) {
		return this;
	}

	/** メタデータを返す。 */
	@Override
	public int getPlantMetadata(IBlockAccess iBlockAccess, int x, int y, int z) {
		return iBlockAccess.getBlockMetadata(x, y, z);
	}

	/** 色を返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta) {
		return StarWoodsTreeManager.getColor((meta % StarWoodsTreeManager.SAPLING) + baseNum);
	}

	/** 色を返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess iBlockAccess, int x, int y, int z) {
		return StarWoodsTreeManager.getColor((iBlockAccess.getBlockMetadata(x, y, z) % StarWoodsTreeManager.SAPLING) + baseNum);
	}

	public int getBaseNum() {
		return baseNum;
	}

}
