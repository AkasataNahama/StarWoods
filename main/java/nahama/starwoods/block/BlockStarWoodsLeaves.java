package nahama.starwoods.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.StarWoodsCore;
import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.item.ItemPruningScissors;
import nahama.starwoods.manager.StarWoodsTreeManager;
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

public class BlockStarWoodsLeaves extends BlockLeaves {

	private IIcon[] iicon = new IIcon[2];
	private int baseNum;
	private int[] array;

	public BlockStarWoodsLeaves(int instanceNum) {
		this.setCreativeTab(StarWoodsCore.tabStarWoodsBlock);
		this.setLightLevel(1.0F);
		this.setTickRandomly(StarWoodsConfigCore.canLeavesDecay);
		baseNum = instanceNum * StarWoodsTreeManager.LEAVES;
	}

	/** 横の面を描画するか。 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
		return true;
	}

	/** アイコンを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iicon[this.isOpaqueCube() ? 1 : 0];
	}

	/** 不透明か。 */
	@Override
	public boolean isOpaqueCube() {
		return Blocks.leaves.isOpaqueCube();
	}

	/** ドロップアイテムのリストを返す。 */
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		int count = quantityDropped(meta, fortune, world.rand);
		for (int i = 0; i < count; i++) {
			Item item = getItemDropped(meta, world.rand, fortune);
			if (item != null) {
				ret.add(new ItemStack(item, 1, ((meta % StarWoodsTreeManager.LEAVES) + baseNum) % StarWoodsTreeManager.SAPLING));
			}
		}
		return ret;
	}

	/** ドロップする際のメタデータを返す。 */
	@Override
	public int damageDropped(int meta) {
		return meta % StarWoodsTreeManager.LEAVES;
	}

	/** クリエイティブタブに登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < StarWoodsTreeManager.LEAVES; i++) {
			if (i + baseNum >= StarWoodsTreeManager.getAmountTreeKind())
				break;
			list.add(new ItemStack(item, 1, i));
		}
	}

	/** アイコンを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		iicon[0] = register.registerIcon(this.getTextureName());
		iicon[1] = register.registerIcon(this.getTextureName() + "_opaque");
	}

	/** 回収された時の処理。 */
	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
		ItemStack itemStack = player.getHeldItem();
		if (itemStack == null) {
			super.harvestBlock(world, player, x, y, z, meta);
			return;
		}
		// プレイヤーが持っているアイテムを取得する。
		Item usingItem = itemStack.getItem();
		if (!(usingItem instanceof ItemPruningScissors)) {
			// バニラのハサミで破壊したのなら、耐久値を消費させる。
			if (usingItem == Items.shears)
				itemStack.damageItem(1, player);
			// 通常の処理をする。
			super.harvestBlock(world, player, x, y, z, meta);
			return;
		}
		// 枝切りハサミで破壊したのなら、葉ブロックとしてドロップする。
		ItemStack dropItem = this.createStackedBlock(meta);
		this.dropBlockAsItem(world, x, y, z, dropItem);
		itemStack.damageItem(1, player);
	}

	/** シルクタッチが適用できるか。 */
	@Override
	public boolean canSilkHarvest() {
		return true;
	}

	/** 幸運を適用したドロップ量を返す。 */
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		int chance = this.getChance();
		if (fortune > 0) {
			chance -= 1 << fortune;
			if (chance < 1)
				chance = 1;
		}
		// fortune:chanceで、0:48,1:46,2:44,3:40
		return random.nextInt(chance) == 0 ? 1 : 0;
	}

	/** ドロップ確率を返す。 */
	protected int getChance() {
		return StarWoodsConfigCore.probablilityDropSapling;
	}

	/** ドロップアイテムを返す。 */
	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return Item.getItemFromBlock(StarWoodsTreeManager.getSaplingInstance(baseNum + (meta % StarWoodsTreeManager.LEAVES)));
	}

	/** 翻訳前の名前を返す。 */
	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName() + ":" + String.valueOf(baseNum);
	}

	/** 色を返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return 0xFFFFFF;
	}

	/** 色を返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta) {
		return StarWoodsTreeManager.getColor((meta % StarWoodsTreeManager.LEAVES) + baseNum);
	}

	/** 色を返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess iBlockAccess, int x, int y, int z) {
		return StarWoodsTreeManager.getColor((iBlockAccess.getBlockMetadata(x, y, z) % StarWoodsTreeManager.LEAVES) + baseNum);
	}

	/** 破壊された時の処理。 */
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (!StarWoodsConfigCore.canLeavesDecay || !world.checkChunksExist(x - 2, y - 2, z - 2, x + 2, y + 2, z + 2))
			return;
		// 葉の自然消滅が有効で、チャンクが読み込まれているなら、周囲の葉の腐食を始めさせる。
		for (int ix = -1; ix <= 1; ix++) {
			for (int iy = -1; iy <= 1; iy++) {
				for (int iz = -1; iz <= 1; iz++) {
					Block block1 = world.getBlock(x + ix, y + iy, z + iz);
					if (block1.isLeaves(world, x + ix, y + iy, z + iz))
						block1.beginLeavesDecay(world, x + ix, y + iy, z + iz);
				}
			}
		}
	}

	/** 更新時の処理。 */
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		// 葉の自然消滅が無効か、クライアント側か、チャンクが読み込まれていないなら終了。
		if (!StarWoodsConfigCore.canLeavesDecay || world.isRemote || !world.checkChunksExist(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5))
			return;
		int meta = world.getBlockMetadata(x, y, z);
		// 腐食が開始していないか、自然生成されたものでないなら終了。
		if ((meta & 8) == 0 || (meta & 4) != 0)
			return;
		if (array == null)
			array = new int[32 * 32 * 32];
		for (int ix = -4; ix <= 4; ++ix) {
			for (int iy = -4; iy <= 4; ++iy) {
				for (int iz = -4; iz <= 4; ++iz) {
					Block block = world.getBlock(x + ix, y + iy, z + iz);
					if (!block.canSustainLeaves(world, x + ix, y + iy, z + iz)) {
						if (block.isLeaves(world, x + ix, y + iy, z + iz)) {
							array[(ix + 16) * 1024 + (iy + 16) * 32 + iz + 16] = -2;
						} else {
							array[(ix + 16) * 1024 + (iy + 16) * 32 + iz + 16] = -1;
						}
					} else {
						array[(ix + 16) * 1024 + (iy + 16) * 32 + iz + 16] = 0;
					}
				}
			}
		}
		for (int i = 1; i <= 4; ++i) {
			for (int ix = -4; ix <= 4; ++ix) {
				for (int iy = -4; iy <= 4; ++iy) {
					for (int iz = -4; iz <= 4; ++iz) {
						if (array[(ix + 16) * 1024 + (iy + 16) * 32 + iz + 16] == i - 1) {
							if (array[(ix + 16 - 1) * 1024 + (iy + 16) * 32 + iz + 16] == -2) {
								array[(ix + 16 - 1) * 1024 + (iy + 16) * 32 + iz + 16] = i;
							}

							if (array[(ix + 16 + 1) * 1024 + (iy + 16) * 32 + iz + 16] == -2) {
								array[(ix + 16 + 1) * 1024 + (iy + 16) * 32 + iz + 16] = i;
							}

							if (array[(ix + 16) * 1024 + (iy + 16 - 1) * 32 + iz + 16] == -2) {
								array[(ix + 16) * 1024 + (iy + 16 - 1) * 32 + iz + 16] = i;
							}

							if (array[(ix + 16) * 1024 + (iy + 16 + 1) * 32 + iz + 16] == -2) {
								array[(ix + 16) * 1024 + (iy + 16 + 1) * 32 + iz + 16] = i;
							}

							if (array[(ix + 16) * 1024 + (iy + 16) * 32 + (iz + 16 - 1)] == -2) {
								array[(ix + 16) * 1024 + (iy + 16) * 32 + (iz + 16 - 1)] = i;
							}

							if (array[(ix + 16) * 1024 + (iy + 16) * 32 + iz + 16 + 1] == -2) {
								array[(ix + 16) * 1024 + (iy + 16) * 32 + iz + 16 + 1] = i;
							}
						}
					}
				}
			}
		}
		int i = array[16 * 1024 + 16 * 32 + 16];
		if (i >= 0) {
			world.setBlockMetadataWithNotify(x, y, z, meta & 7, 4);
			return;
		}
		this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
		world.setBlockToAir(x, y, z);
	}

	/** ドロップ数を返す。 */
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	/** ItemStackとして返す。 */
	@Override
	protected ItemStack createStackedBlock(int meta) {
		return new ItemStack(this, 1, meta % StarWoodsTreeManager.LEAVES);
	}

	/** ハサミで回収可能か。 */
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
		return false;
	}

	/** ハサミで回収された時のドロップアイテムを返す。 */
	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		return ret;
	}

	/** 葉の腐食を開始する。 */
	@Override
	public void beginLeavesDecay(World world, int x, int y, int z) {
		if (!StarWoodsConfigCore.canLeavesDecay)
			return;
		int meta = world.getBlockMetadata(x, y, z);
		if ((meta & 4) != 0)
			return;
		world.setBlockMetadataWithNotify(x, y, z, meta | 8, 4);
	}

	@Override
	public String[] func_150125_e() {
		return new String[] { "" };
	}

}
