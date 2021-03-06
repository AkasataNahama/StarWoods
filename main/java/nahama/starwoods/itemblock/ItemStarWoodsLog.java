package nahama.starwoods.itemblock;

import nahama.starwoods.manager.StarWoodsTreeManager;
import nahama.starwoods.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemStarWoodsLog extends ItemBlockWithMetadata {

	public ItemStarWoodsLog(Block block) {
		super(block, block);
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
		int meta = itemStack.getItemDamage();
		ItemStack product = StarWoodsTreeManager.getProduct((meta % StarWoodsTreeManager.LOG) + num);
		String name = "starwoods.unknown";
		if (product != null)
			name = product.getUnlocalizedName() + ".name";
		String s = "starwoods.log.natural";
		if (meta < 12)
			s = "starwoods.log";
		return StatCollector.translateToLocal(name) + StatCollector.translateToLocal(s);
	}

}
