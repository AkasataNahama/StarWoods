package nahama.starwoods.itemblock;

import nahama.starwoods.Log;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemStarWoodsSapling extends ItemBlockWithMetadata {

	public ItemStarWoodsSapling(Block block) {
		super(block, block);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		String[] str = super.getUnlocalizedName(itemStack).split(":");
		int type = -1;
		try {
			type = Integer.parseInt(str[1]);
		} catch (Exception e) {
			Log.error("UnlocalizedName setting error", "ItemStarWoodsSapling", true);
			Log.error(e.toString());
		}

		int meta = itemStack.getItemDamage();
		int number = meta + type;

		switch (meta / 8) {
		case 1:
			number = (meta % 8) + type + 64;
		}

		return str[0] + "." + number;
	}

}
