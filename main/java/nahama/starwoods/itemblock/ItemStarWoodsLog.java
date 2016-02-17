package nahama.starwoods.itemblock;

import nahama.starwoods.Log;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemStarWoodsLog extends ItemBlockWithMetadata {

	public ItemStarWoodsLog(Block block) {
		super(block, block);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		String[] str = super.getUnlocalizedName(itemStack).split(":");
		int type = -1;
		try {
			type = Integer.parseInt(str[1]);
		} catch (Exception e) {
			Log.error("UnlocalizedName setting error", "ItemStarWoodsLog", true);
			Log.error(e.toString());
		}

		int meta = itemStack.getItemDamage();
		int number = meta + type;

		switch (meta / 4) {
		case 1:
			number = (meta % 4) + type + 64;

		case 2:
			number = (meta % 4) + type + 128;
		}

		return str[0] + "." + number;
	}

}
