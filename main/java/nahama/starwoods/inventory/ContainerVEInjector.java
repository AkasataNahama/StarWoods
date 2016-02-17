package nahama.starwoods.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.tileentity.TileEntityVEInjector;
import nahama.starwoods.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerVEInjector extends Container {

	/** インベントリの第一スロットの番号 */
	private static final int index0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号 */
	private static final int index1 = 3;
	/** クイックスロットの第一スロットの番号 */
	private static final int index2 = 30;
	/** このコンテナの全体のスロット数 */
	private static final int index3 = 39;

	private TileEntityVEInjector tileEntity;
	private int lastHoldingVE;
	private int lastInjectingTime;

	public ContainerVEInjector(EntityPlayer player, TileEntityVEInjector tileEntity) {
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new Slot(tileEntity, 0, 36, 34));
		this.addSlotToContainer(new Slot(tileEntity, 1, 60, 34));
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
		iCrafting.sendProgressBarUpdate(this, 0, tileEntity.getHoldingVE());
		iCrafting.sendProgressBarUpdate(this, 1, tileEntity.getInjectingTime());
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) crafters.get(i);
			if (lastHoldingVE != tileEntity.getHoldingVE()) {
				icrafting.sendProgressBarUpdate(this, 0, tileEntity.getHoldingVE());
			}
			if (lastInjectingTime != tileEntity.getInjectingTime()) {
				icrafting.sendProgressBarUpdate(this, 1, tileEntity.getInjectingTime());
			}
		}
		lastHoldingVE = tileEntity.getHoldingVE();
		lastInjectingTime = tileEntity.getInjectingTime();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			tileEntity.setHoldingVE(par2);
		}
		if (par1 == 1) {
			tileEntity.setInjectingTime(par2);
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
			} else if (slotNumber != 1 && slotNumber != 0) {
				if (Util.isItemStackSapling(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
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
