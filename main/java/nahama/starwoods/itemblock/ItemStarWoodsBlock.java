package nahama.starwoods.itemblock;

import nahama.starwoods.Log;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemStarWoodsBlock extends ItemBlockWithMetadata {

	public ItemStarWoodsBlock(Block block) {
		super(block, block);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		String[] str = super.getUnlocalizedName(itemStack).split(":");
		int type = -1;
		try {
			type = Integer.parseInt(str[1]);
		} catch (Exception e) {
			Log.error("UnlocalizedName setting error", "StarWoodsItemBlock", true);
			Log.error(e.toString());
		}
		return str[0] + "." + (type + itemStack.getItemDamage());
	}

}
