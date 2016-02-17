package nahama.starwoods.block;

import java.util.List;
import java.util.Random;

import nahama.starwoods.StarWoodsModCore;
import nahama.starwoods.generate.WorldGenCreeperTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
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
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreeperTreeSapling extends BlockSapling implements IGrowable, IPlantable {

	public CreeperTreeSapling() {
		super();
		this.setCreativeTab(StarWoodsModCore.tabStarWoodsBlock);
		this.setLightLevel(0.6F);
		this.setHardness(0.0F);
		this.setResistance(6000000.0F);
		this.setStepSound(soundTypeGrass);
		this.setTickRandomly(true);
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
	}

	/**更新時の処理*/
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
			checkAndDropBlock(world, x, y, z);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0) {
				growTree(world, x, y, z, random);
			}
		}
	}

	/**苗木を成長させる*/
	private void growTree(World world, int x, int y, int z, Random random) {
		int meta = world.getBlockMetadata(x, y, z);

		if ((meta & 8) == 0) {
			world.setBlockMetadataWithNotify(x, y, z, meta | 8, 4);
		} else {
			world.setBlock(x, y, z, Blocks.air, 0, 4);

			if (!this.generateTree(world, x, y, z, random, meta)) {
				world.setBlock(x, y, z, this, meta, 4);
			}
		}
	}

	/**木を生成する*/
	protected boolean generateTree(World world, int x, int y, int z, Random random, int meta) {
		return new WorldGenCreeperTree().generate(world, random, x, y, z);
	}

	/**真下のブロックが土などでなければ苗木をドロップさせる*/
	protected void checkAndDropBlock(World world, int x, int y, int z) {
		if (!this.canBlockStay(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, getBlockById(0), 0, 2);
		}
	}

	/**下が土や草などかどうか*/
	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	/**そこにブロックを置けるかどうか*/
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
	}

	/**不透明なブロックかどうか*/
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**普通のブロックとして描画するかどうか*/
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**レンダータイプを返す*/
	@Override
	public int getRenderType() {
		return 1;
	}

	/**周囲のブロックが更新された時の処理*/
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		this.checkAndDropBlock(world, x, y, z);
	}

	// 以下骨粉用のメソッド。(IGrowable)
	/**骨粉がかけられるかどうか(?)*/
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean flag) {
		return true;
	}

	/**骨粉が適用されるかどうか(?)*/
	@Override
	public boolean func_149852_a(World world, Random x, int y, int z, int meta) {
		return (double)world.rand.nextFloat() < 0.45D;
	}

	/**骨粉が適用された時の処理*/
	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z) {
		this.growTree(world, x, y, z, random);
	}

	//以下作物用のメソッド。(IPlantable)
	/**作物のタイプ*/
	@Override
	public EnumPlantType getPlantType(IBlockAccess iBlockAccess, int x, int y, int z) {
		return EnumPlantType.Plains;
	}

	/**このブロックを返す*/
	@Override
	public Block getPlant(IBlockAccess iBlockAccess, int x, int y, int z) {
		return this;
	}

	/**メタデータを返す*/
	@Override
	public int getPlantMetadata(IBlockAccess iBlockAccess, int x, int y, int z) {
		return iBlockAccess.getBlockMetadata(x, y, z);
	}

	//BlockSapling,BlockBushでオーバーライドされたメソッドをBlockと同じ処理にオーバーライドしなおす。

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.blockIcon;
	}

	@Override
	public int damageDropped(int meta) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		list.add(new ItemStack(item, 1, 0));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		this.blockIcon = iicon.registerIcon(this.getTextureName());
	}

}