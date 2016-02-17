package nahama.starwoods.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUnputable extends Slot {

	public SlotUnputable(IInventory iinventory, int index, int x, int y) {
		super(iinventory, index, x, y);
	}

	/** アイテムを置けるか。 */
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return false;
	}

}
