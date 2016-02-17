package nahama.starwoods.itemblock;

import nahama.starwoods.block.BlockStarWoodsSapling;
import nahama.starwoods.manager.StarWoodsTreeManager;
import nahama.starwoods.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemStarWoodsSapling extends ItemBlockWithMetadata {

	private int generalNum;

	public ItemStarWoodsSapling(Block block) {
		super(block, block);
		if (block instanceof BlockStarWoodsSapling)
			generalNum = ((BlockStarWoodsSapling) block).getBaseNum();
	}

	/** 表示名を返す。 */
	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		String[] str = super.getUnlocalizedName(itemStack).split(":");
		int num = -1;
		try {
			num = Integer.parseInt(str[1]);
		} catch (Exception e) {
			Util.error("Error on getting display name.", "ItemStarWoodsSapling");
		}
		ItemStack product = StarWoodsTreeManager.getProduct((itemStack.getItemDamage() % StarWoodsTreeManager.SAPLING) + num);
		String name = "starwoods.unknown";
		if (product != null)
			name = product.getUnlocalizedName() + ".name";
		return StatCollector.translateToLocal(name) + StatCollector.translateToLocal("starwoods.sapling");
	}

	/** 色を返す。 */
	@Override
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		return StarWoodsTreeManager.getColor((itemStack.getItemDamage() % StarWoodsTreeManager.SAPLING) + generalNum);
	}

	public static int getGeneralNum(ItemStack sapling) {
		if (!(sapling.getItem() instanceof ItemStarWoodsSapling))
			return -1;
		return ((ItemStarWoodsSapling) sapling.getItem()).generalNum + (sapling.getItemDamage() % StarWoodsTreeManager.SAPLING);
	}

}
