package nahama.starwoods.itemblock;

import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.manager.StarWoodsTreeManager;
import nahama.starwoods.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemStarWoodsLeaves extends ItemBlockWithMetadata {

	public ItemStarWoodsLeaves(Block block) {
		super(block, block);
	}

	/** メタデータを返す。 */
	@Override
	public int getMetadata(int meta) {
		if (StarWoodsConfigCore.canLeavesDecay)
			return meta | 4;
		return meta;
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
		ItemStack product = StarWoodsTreeManager.getProduct((itemStack.getItemDamage() % StarWoodsTreeManager.LEAVES) + num);
		String name = "starwoods.unknown";
		if (product != null)
			name = product.getUnlocalizedName() + ".name";
		return StatCollector.translateToLocal(name) + StatCollector.translateToLocal("starwoods.leaves");
	}

}
