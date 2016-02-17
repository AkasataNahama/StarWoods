package nahama.starwoods.tileentity;

import nahama.starwoods.block.BlockStarWoodsSapling;
import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.core.StarWoodsItemCore;
import nahama.starwoods.itemblock.ItemStarWoodsSapling;
import nahama.starwoods.manager.StarWoodsTree;
import nahama.starwoods.manager.StarWoodsTreeManager;
import nahama.starwoods.manager.StarWoodsVEManager;
import nahama.starwoods.util.Util;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityVEInjector extends TileEntity implements IVEMachine, ISidedInventory {

	protected int limit = 200000000;
	protected int holdingVE;
	protected ItemStack[] itemStacks = new ItemStack[3];
	protected String customName;
	protected int injectingTime;

	@Override
	public int recieveVE(int amount) {
		if (amount + holdingVE <= limit) {
			holdingVE += amount;
			return 0;
		}
		amount = limit - holdingVE;
		holdingVE = limit;
		return amount;
	}

	/** 更新時の処理。 */
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (worldObj.isRemote)
			return;
		if (holdingVE < 1 || !Util.isItemStackSapling(itemStacks[0]) || itemStacks[1] == null)
			return;
		int usingVE;
		ItemStack result = null;
		if (Block.getBlockFromItem(itemStacks[1].getItem()) instanceof BlockStarWoodsSapling) {
			usingVE = StarWoodsVEManager.getVEFromTier(StarWoodsTreeManager.getTier(ItemStarWoodsSapling.getGeneralNum(itemStacks[1]))) * 2;
			result = new ItemStack(itemStacks[1].getItem(), 2, itemStacks[1].getItemDamage());
		} else if (Util.isItemStackSapling(itemStacks[1])) {
			usingVE = StarWoodsConfigCore.veStarting;
			result = new ItemStack(StarWoodsItemCore.material, 1, 0);
		} else {
			StarWoodsTree tree = StarWoodsTreeManager.getTreeFromMaterial(itemStacks[1]);
			if (tree == null) {
				injectingTime = 0;
				return;
			}
			usingVE = StarWoodsVEManager.getVEFromTier(tree.getTier());
			result = new ItemStack(StarWoodsTreeManager.getSaplingInstance(tree.getGeneralNum()), 1, tree.getGeneralNum() % StarWoodsTreeManager.SAPLING);
		}
		if (holdingVE < usingVE || result == null || result.getItem() == null || (itemStacks[2] != null && (!itemStacks[2].isItemEqual(result) || itemStacks[2].stackSize + result.stackSize > 64))) {
			injectingTime = 0;
			return;
		}
		injectingTime++;
		if (injectingTime < this.getInjectTime())
			return;
		injectingTime = 0;
		if (itemStacks[2] == null) {
			itemStacks[2] = result;
		} else {
			itemStacks[2].stackSize += result.stackSize;
		}
		this.decrStackSize(0, 1);
		this.decrStackSize(1, 1);
		holdingVE -= usingVE;
	}

	protected int getInjectTime() {
		return StarWoodsConfigCore.timeInjecting;
	}

	/** 木の材料になるか。 */
	public boolean isMaterial(ItemStack itemStack) {
		if (itemStack == null)
			return false;
		if (Block.getBlockFromItem(itemStack.getItem()) instanceof BlockStarWoodsSapling)
			return true;
		if (StarWoodsTreeManager.getTreeFromMaterial(itemStack) != null)
			return true;
		return false;
	}

	/** NBTに書き込む処理。 */
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("HoldingVE", holdingVE);
		nbt.setInteger("InjectingTime", injectingTime);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < itemStacks.length; i++) {
			if (itemStacks[i] == null)
				continue;
			NBTTagCompound nbt1 = new NBTTagCompound();
			nbt1.setByte("Slot", (byte) i);
			itemStacks[i].writeToNBT(nbt1);
			nbttaglist.appendTag(nbt1);
		}
		nbt.setTag("Items", nbttaglist);
		if (this.hasCustomInventoryName())
			nbt.setString("CustomName", customName);
	}

	/** NBTを読み込む処理。 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		holdingVE = nbt.getInteger("HoldingVE");
		injectingTime = nbt.getInteger("InjectingTime");
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		itemStacks = new ItemStack[3];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbt1.getByte("Slot");
			if (0 <= b0 && b0 < itemStacks.length) {
				itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}
		if (nbt.hasKey("CustomName", 8))
			customName = nbt.getString("CustomName");
	}

	public int getInjectingTime() {
		return injectingTime;
	}

	public int getHoldingVE() {
		return holdingVE;
	}

	public int getInjectProgressScaled(int par1) {
		return injectingTime * par1 / this.getInjectTime();
	}

	public void setInjectingTime(int injectingTime) {
		this.injectingTime = injectingTime;
	}

	public void setHoldingVE(int holdingVE) {
		this.holdingVE = holdingVE;
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 3;
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemStacks[slot];
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (itemStacks[slot] == null)
			return null;
		ItemStack itemstack;
		if (itemStacks[slot].stackSize <= amount) {
			itemstack = itemStacks[slot];
			itemStacks[slot] = null;
			return itemstack;
		}
		itemstack = itemStacks[slot].splitStack(amount);
		if (itemStacks[slot].stackSize < 1) {
			itemStacks[slot] = null;
		}
		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	/** スロットの中身を設定する。 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		itemStacks[slot] = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/** インベントリ名を返す。 */
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? customName : "container.starwoods.ve_injector";
	}

	/** 金床で設定された名前を持つかどうか。 */
	@Override
	public boolean hasCustomInventoryName() {
		return customName != null && customName.length() > 0;
	}

	/** このインベントリの最大スタック数を返す。 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/** プレイヤーが使用できるかどうか。 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	/** スロットにアクセスできるかどうか。 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 2 ? false : (slot == 1 ? this.isMaterial(itemStack) : Util.isItemStackSapling(itemStack));
	}

	/** 方向からアクセスできるスロットの配列を返す。 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? new int[] { 2 } : new int[] { 0, 1 };
	}

	/** スロットに搬入できるかどうか。 */
	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(slot, itemStack);
	}

	/** スロットから搬出できるかどうか。 */
	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return true;
	}

}
