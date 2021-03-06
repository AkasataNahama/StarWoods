package nahama.starwoods.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.tileentity.TileEntityCrystallizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCrystallizer extends Container {

	/** インベントリの第一スロットの番号 */
	private static final int index0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号 */
	private static final int index1 = 3;
	/** クイックスロットの第一スロットの番号 */
	private static final int index2 = 30;
	/** このコンテナの全体のスロット数 */
	private static final int index3 = 39;

	private TileEntityCrystallizer tileEntity;
	private int lastSmeltTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerCrystallizer(EntityPlayer player, TileEntityCrystallizer tileEntity) {
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new Slot(tileEntity, 0, 56, 17));
		this.addSlotToContainer(new Slot(tileEntity, 1, 56, 53));
		this.addSlotToContainer(new SlotUnputable(tileEntity, 2, 116, 35));
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting iCrafting) {
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, tileEntity.smeltTime);
		iCrafting.sendProgressBarUpdate(this, 1, tileEntity.burnTime);
		iCrafting.sendProgressBarUpdate(this, 2, tileEntity.currentItemBurnTime);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) crafters.get(i);
			if (lastSmeltTime != tileEntity.smeltTime) {
				icrafting.sendProgressBarUpdate(this, 0, tileEntity.smeltTime);
			}
			if (lastBurnTime != tileEntity.burnTime) {
				icrafting.sendProgressBarUpdate(this, 1, tileEntity.burnTime);
			}
			if (lastItemBurnTime != tileEntity.currentItemBurnTime) {
				icrafting.sendProgressBarUpdate(this, 2, tileEntity.currentItemBurnTime);
			}
		}
		lastSmeltTime = tileEntity.smeltTime;
		lastBurnTime = tileEntity.burnTime;
		lastItemBurnTime = tileEntity.currentItemBurnTime;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			tileEntity.smeltTime = par2;
		}
		if (par1 == 1) {
			tileEntity.burnTime = par2;
		}
		if (par1 == 2) {
			tileEntity.currentItemBurnTime = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) inventorySlots.get(slotNumber);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (slotNumber == 2) {
				if (!this.mergeItemStack(itemStack1, index1, index3, true)) {
					return null;
				}
				slot.onSlotChange(itemStack1, itemStack);
			} else if (slotNumber != 1 && slotNumber != 0) {
				if (tileEntity.getCrystallizingResult(itemStack1) != null) {
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
						return null;
					}
				} else if (tileEntity.isItemFuel(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 1, 2, false)) {
						return null;
					}
				} else if (slotNumber >= index1 && slotNumber < index2) {
					if (!this.mergeItemStack(itemStack1, index2, index3, false)) {
						return null;
					}
				} else if (slotNumber >= index2 && slotNumber < index3 && !this.mergeItemStack(itemStack1, index1, index2, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemStack1, index1, index3, false)) {
				return null;
			}

			if (itemStack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			if (itemStack1.stackSize == itemStack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, itemStack1);
		}
		return itemStack;
	}

}
