package nahama.starwoods.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.StarWoodsCore;
import nahama.starwoods.manager.StarWoodsVEManager;
import nahama.starwoods.network.MExtractorVE;
import nahama.starwoods.tileentity.TileEntityVEExtractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerVEExtractor extends Container {

	/** インベントリの第一スロットの番号 */
	private static final int index0 = 0;
	/** プレイヤーのインベントリの第一スロットの番号 */
	private static final int index1 = 1;
	/** クイックスロットの第一スロットの番号 */
	private static final int index2 = 28;
	/** このコンテナの全体のスロット数 */
	private static final int index3 = 37;

	private TileEntityVEExtractor tileEntity;
	private int lastHoldingVE;
	private int lastExtractingTime;

	public ContainerVEExtractor(EntityPlayer player, TileEntityVEExtractor tileEntity) {
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new Slot(tileEntity, 0, 80, 49));
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
		if (iCrafting instanceof EntityPlayerMP) {
			StarWoodsCore.wrapper.sendTo(new MExtractorVE(tileEntity.getHoldingVE()), (EntityPlayerMP) iCrafting);
		}
		iCrafting.sendProgressBarUpdate(this, 1, tileEntity.getExtractingTime());
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); i++) {
			ICrafting iCrafting = (ICrafting) crafters.get(i);
			if (lastHoldingVE != tileEntity.getHoldingVE()) {
				if (iCrafting instanceof EntityPlayerMP) {
					StarWoodsCore.wrapper.sendTo(new MExtractorVE(tileEntity.getHoldingVE()), (EntityPlayerMP) iCrafting);
				}
			}
			if (lastExtractingTime != tileEntity.getExtractingTime()) {
				iCrafting.sendProgressBarUpdate(this, 1, tileEntity.getExtractingTime());
			}
		}
		lastHoldingVE = tileEntity.getHoldingVE();
		lastExtractingTime = tileEntity.getExtractingTime();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			tileEntity.setHoldingVE(par2);
		}
		if (par1 == 1) {
			tileEntity.setExtractingTime(par2);
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
			if (slotNumber == index0) {
				if (!this.mergeItemStack(itemStack1, index1, index3, true)) {
					return null;
				}
			} else {
				if (StarWoodsVEManager.isItemStackHoldingVE(itemStack1)) {
					if (!this.mergeItemStack(itemStack1, index0, index1, false)) {
						return null;
					}
				} else if (slotNumber >= index1 && slotNumber < index2) {
					if (!this.mergeItemStack(itemStack1, index2, index3, false)) {
						return null;
					}
				} else if (slotNumber >= index2 && slotNumber < index3 && !this.mergeItemStack(itemStack1, index1, index2, false)) {
					return null;
				}
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
