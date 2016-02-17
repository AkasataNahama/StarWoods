package nahama.starwoods.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.StarWoodsCore;
import nahama.starwoods.tileentity.TileEntityVEExtractor;
import nahama.starwoods.tileentity.TileEntityVEInjector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockVEMachine extends BlockContainer {

	private IIcon[] iicon = new IIcon[4];

	public BlockVEMachine() {
		super(Material.rock);
		this.setCreativeTab(StarWoodsCore.tabStarWoodsBlock);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	/** TileEntityを生成して返す。 */
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		switch (meta / 8) {
		case 0:
			return new TileEntityVEExtractor();
		case 1:
			return new TileEntityVEInjector();
		}
		return null;
	}

	/** 右クリックされたときの処理。 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// GUIを開く。
		player.openGui(StarWoodsCore.instance, 1, world, x, y, z);
		return true;
	}

	/** 破壊された時の処理。 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		// TileEntityの内部にあるアイテムをドロップさせる。
		IInventory tileEntity = (IInventory) world.getTileEntity(x, y, z);
		Random random = new Random();
		if (tileEntity != null) {
			for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
				ItemStack itemStack = tileEntity.getStackInSlot(i);
				if (itemStack == null)
					continue;
				float fx = random.nextFloat() * 0.6F + 0.1F;
				float fy = random.nextFloat() * 0.6F + 0.1F;
				float fz = random.nextFloat() * 0.6F + 0.1F;
				while (itemStack.stackSize > 0) {
					int j = random.nextInt(21) + 10;
					if (j > itemStack.stackSize)
						j = itemStack.stackSize;
					itemStack.stackSize -= j;

					EntityItem entityItem = new EntityItem(world, x + fx, y + fy, z + fz, new ItemStack(itemStack.getItem(), j, itemStack.getItemDamage()));
					if (itemStack.hasTagCompound())
						entityItem.getEntityItem().setTagCompound(((NBTTagCompound) itemStack.getTagCompound().copy()));
					entityItem.motionX = (float) random.nextGaussian() * 0.025F;
					entityItem.motionY = (float) random.nextGaussian() * 0.025F + 0.1F;
					entityItem.motionZ = (float) random.nextGaussian() * 0.025F;
					world.spawnEntityInWorld(entityItem);
				}
			}
			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	/** アイコンを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for (int i = 0; i < iicon.length; i++) {
			iicon[i] = register.registerIcon(this.getTextureName() + "-" + i);
		}
	}

	/** アイコンを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (meta >= 8) {
			if (side < 2)
				return Blocks.gold_block.getIcon(side, 0);
			return iicon[3];
		}
		if (side == meta) {
			if (side < 2)
				return iicon[2];
			return iicon[1];
		}
		if (side < 2)
			return Blocks.iron_block.getIcon(side, 0);
		return iicon[0];
	}

	/** クリエイティブタブに登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 8));
	}

	/** ドロップする際のメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		return meta & 8;
	}

}
