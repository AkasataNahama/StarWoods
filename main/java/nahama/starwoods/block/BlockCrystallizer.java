package nahama.starwoods.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.StarWoodsCore;
import nahama.starwoods.tileentity.TileEntityCrystallizer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrystallizer extends BlockContainer {

	private IIcon[] iicon = new IIcon[5];

	public BlockCrystallizer() {
		super(Material.rock);
		this.setCreativeTab(StarWoodsCore.tabStarWoodsBlock);
		this.setHardness(6.0F);
		this.setResistance(14.0F);
		this.setStepSound(Block.soundTypePiston);
		this.setHarvestLevel("pickaxe", 2);
	}

	/** TileEntityを生成して返す。 */
	@Override
	public TileEntity createNewTileEntity(World world, int par2) {
		return new TileEntityCrystallizer();
	}

	/** 右クリックされたときの処理。 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		// GUIを開く。
		player.openGui(StarWoodsCore.instance, 1, world, x, y, z);
		return true;
	}

	/** 設置された時の処理。 */
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		// 設置したEntityの向きによってメタデータを設定する。
		int meta = 0;
		switch (MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) {
		case 0:
			meta = 2;
			break;
		case 1:
			meta = 5;
			break;
		case 2:
			meta = 3;
			break;
		case 3:
			meta = 4;
		}
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		// 名づけがされていたら反映する。
		if (itemStack.hasDisplayName()) {
			((TileEntityCrystallizer) world.getTileEntity(x, y, z)).setCustomName(itemStack.getDisplayName());
		}
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
		if (meta == 0 && side == 3) {
			// item状態の正面
			return iicon[3];
		} else if (side == 1) {
			// 上
			return iicon[1];
		} else if (side == 0) {
			// 下
			return iicon[2];
		} else if (side == (meta & 7)) {
			// 正面
			return meta < 8 ? iicon[3] : iicon[4];
		}
		// その他(側面)
		return iicon[0];
	}

	/** 光源レベルを返す。 */
	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		if (iBlockAccess.getBlockMetadata(x, y, z) >= 8) {
			// 作業中は光源。
			return 13;
		}
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta < 8)
			return;
		float fx = x + 0.5F;
		float fy = y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
		float fz = z + 0.5F;
		float f = random.nextFloat() * 0.6F - 0.3F;
		// 製錬中なら、向きに応じてパーティクルを発生させる。
		if (meta == 12) {
			world.spawnParticle("smoke", fx - 0.52F, fy, fz + f, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", fx - 0.52F, fy, fz + f, 0.0D, 0.0D, 0.0D);
		} else if (meta == 13) {
			world.spawnParticle("smoke", fx + 0.52F, fy, fz + f, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", fx + 0.52F, fy, fz + f, 0.0D, 0.0D, 0.0D);
		} else if (meta == 10) {
			world.spawnParticle("smoke", fx + f, fy, fz - 0.52F, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", fx + f, fy, fz - 0.52F, 0.0D, 0.0D, 0.0D);
		} else if (meta == 11) {
			world.spawnParticle("smoke", fx + f, fy, fz + 0.52F, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", fx + f, fy, fz + 0.52F, 0.0D, 0.0D, 0.0D);
		}
	}

}
