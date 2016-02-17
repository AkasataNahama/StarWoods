package nahama.starwoods.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemVEMachine extends ItemBlockWithMetadata {

	public ItemVEMachine(Block block) {
		super(block, block);
	}

	/** 翻訳前の名前を返す。 */
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return super.getUnlocalizedName() + "." + itemStack.getItemDamage() / 8;
	}

}
