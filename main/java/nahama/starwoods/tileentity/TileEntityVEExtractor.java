package nahama.starwoods.tileentity;

import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.manager.StarWoodsVEManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;

public class TileEntityVEExtractor extends TileEntity implements IVEMachine, IInventory {

	protected final int limit = 200000000;
	protected int holdingVE;
	protected ItemStack itemStack;
	protected String customName;
	protected int extractingTime;

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
		if (itemStack != null && StarWoodsVEManager.isItemStackHoldingVE(itemStack)) {
			extractingTime++;
			if (extractingTime >= this.getExtractTime()) {
				extractingTime = this.getExtractTime();
				int generatedVE = StarWoodsVEManager.getItemStackVE(itemStack);
				if (holdingVE + generatedVE <= limit) {
					extractingTime = 0;
					holdingVE += generatedVE;
					itemStack.stackSize--;
					if (itemStack.stackSize < 1)
						itemStack = null;
				}
			}
		} else {
			extractingTime = 0;
		}
		if (holdingVE > 0) {
			IVEMachine machine = this.getVEMachine(this.getBlockMetadata());
			if (machine == null)
				return;
			holdingVE = machine.recieveVE(holdingVE);
		}
	}

	protected int getExtractTime() {
		return StarWoodsConfigCore.timeExtracting;
	}

	/** 生命力関連装置があれば取得して返す。 */
	protected IVEMachine getVEMachine(int side) {
		TileEntity tileEntity = worldObj.getTileEntity(xCoord + Facing.offsetsXForSide[side], yCoord + Facing.offsetsYForSide[side], zCoord + Facing.offsetsZForSide[side]);
		if (!(tileEntity instanceof IVEMachine))
			return null;
		return (IVEMachine) tileEntity;
	}

	/** NBTに書き込む処理。 */
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("HoldingVE", holdingVE);
		if (itemStack != null) {
			NBTTagCompound nbt1 = new NBTTagCompound();
			itemStack.writeToNBT(nbt1);
			nbt.setTag("ItemStack", nbt1);
		}
		nbt.setInteger("ExtractingTime", extractingTime);
		if (this.hasCustomInventoryName())
			nbt.setString("CustomName", customName);
	}

	/** NBTを読み込む処理。 */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		holdingVE = nbt.getInteger("HoldingVE");
		if (nbt.hasKey("ItemStack"))
			itemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt.getTag("ItemStack"));
		extractingTime = nbt.getInteger("ExtractingTime");
		if (nbt.hasKey("CustomName", 8))
			customName = nbt.getString("CustomName");
	}

	public int getExtractingTime() {
		return extractingTime;
	}

	public int getHoldingVE() {
		return holdingVE;
	}

	public int getExtractProgressScaled(int par1) {
		return extractingTime * par1 / this.getExtractTime();
	}

	public void setHoldingVE(int holdingVE) {
		this.holdingVE = holdingVE;
	}

	public void setExtractingTime(int extractingTime) {
		this.extractingTime = extractingTime;
	}

	/** インベントリのスロット数を返す。 */
	@Override
	public int getSizeInventory() {
		return 1;
	}

	/** スロットのアイテムを返す。 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemStack;
	}

	/** スロットのスタック数を減らす。 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (itemStack == null)
			return null;
		ItemStack itemstack;
		if (itemStack.stackSize <= amount) {
			itemstack = itemStack;
			itemStack = null;
			return itemstack;
		}
		itemstack = itemStack.splitStack(amount);
		if (itemStack.stackSize < 1) {
			itemStack = null;
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
		this.itemStack = itemStack;
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/** インベントリ名を返す。 */
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? customName : "container.starwoods.ve_extractor";
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
		return StarWoodsVEManager.isItemStackHoldingVE(itemStack);
	}

}
