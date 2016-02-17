package nahama.starwoods.tileentity;

import nahama.starwoods.block.Crystallizer;
import nahama.starwoods.core.StarWoodsItemCore;
import nahama.starwoods.core.StarWoodsRecipeCore;
import nahama.starwoods.item.StarWoodsCrystal;
import nahama.starwoods.item.StarWoodsSap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityCrystallizer extends TileEntity implements ISidedInventory {

	private static final int[] slotsTop = new int[] {0};
	private static final int[] slotsBottom = new int[] {2, 1};
	private static final int[] slotsSides = new int[] {1};
	/**0・・・製錬前スロット 1・・・燃料スロット 2・・・完成品スロット*/
	protected ItemStack[] itemStacks = new ItemStack[3];
	/**燃え尽きるまでの残り時間*/
	public int burnTime;
	/**現在の燃焼時間*/
	public int currentItemBurnTime;
	/**製錬にかかる残り時間*/
	public int smeltTime;
	/**金床で設定された名前*/
	private String customName;

	@Override
	public int getSizeInventory() {
		return this.itemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.itemStacks[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int number) {
		if (this.itemStacks[slot] != null) {
			ItemStack itemstack;

			if (this.itemStacks[slot].stackSize <= number) {
				itemstack = this.itemStacks[slot];
				this.itemStacks[slot] = null;
				return itemstack;
			} else {
				itemstack = this.itemStacks[slot].splitStack(number);

				if (this.itemStacks[slot].stackSize == 0) {
					this.itemStacks[slot] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.itemStacks[slot] != null) {
			ItemStack itemstack = this.itemStacks[slot];
			this.itemStacks[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.itemStacks[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**インベントリ名の設定*/
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.crystallizer";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && this.customName.length() > 0;
	}

	/**NBTタグの読み取り*/
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		this.itemStacks = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.itemStacks.length) {
				this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.burnTime = nbt.getShort("BurnTime");
		this.smeltTime = nbt.getShort("SmeltTime");
		this.currentItemBurnTime = getItemBurnTime(this.itemStacks[1]);

		if (nbt.hasKey("CustomName", 8)) {
			this.customName = nbt.getString("CustomName");
		}
	}

	/**NBTタグの書き込み*/
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("BurnTime", (short)this.burnTime);
		nbt.setShort("SmeltTime", (short)this.smeltTime);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.itemStacks.length; ++i) {
			if (this.itemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.itemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);

		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.customName);
		}
	}

	/**インベントリのスタックリミット*/
	@Override
	public int getInventoryStackLimit() {
		return 65536;
	}

	@SideOnly(Side.CLIENT)
	public int getSmeltProgressScaled(int par1) {
		return this.smeltTime * par1 / this.getSmeltTime();
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int par1) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = 200;
		}

		return this.burnTime * par1 / this.currentItemBurnTime;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}

	/**アップデート時の処理*/
	@Override
	public void updateEntity() {
		boolean flag = this.burnTime > 0;
		boolean flag1 = false;

		//燃えているなら、burnTimeを減らす
		if (this.burnTime > 0) {
			--this.burnTime;
		}

		//サーバー側のみでの処理
		if (!this.worldObj.isRemote) {
			//燃えているもしくは燃料スロットに何か入っているうえで、製錬前スロットが空でないとき
			if (this.burnTime != 0 || this.itemStacks[1] != null && this.itemStacks[0] != null) {

				//燃焼時間がなく、製錬可能ならば
				if (this.burnTime == 0 && this.canSmelt()) {
					//燃料スロットのアイテムの燃焼時間をburnTimeとcurrentItemBurnTimeに代入する
					this.currentItemBurnTime = this.burnTime = getItemBurnTime(this.itemStacks[1]);
					//その結果burnTimeが0以上のとき
					if (this.burnTime > 0) {
						//flag1をtrueにする
						flag1 = true;
						//また、燃料スロットにアイテムが入っていれば
						if (this.itemStacks[1] != null) {
							//そのアイテムの数を減らす
							--this.itemStacks[1].stackSize;
							//その結果アイテムの数が0になったら
							if (this.itemStacks[1].stackSize == 0) {
								//更新する?
								this.itemStacks[1] = itemStacks[1].getItem().getContainerItem(itemStacks[1]);
							}
						}
					}
				}

				//燃焼中で、製錬可能ならば
				if (this.isBurning() && this.canSmelt()) {
					//製錬時間を増やし
					++this.smeltTime;
					//製錬時間が完成時間に達したら
					if (this.smeltTime >= this.getSmeltTime()) {
						//製錬時間を初期化し、
						this.smeltTime = 0;
						//製錬処理を行い
						this.smeltItem();
						//flag1をtrueにする
						flag1 = true;
					}
				//違うなら、
				} else {
					//製錬時間を0にする
					this.smeltTime = 0;
				}

			} else {
				this.smeltTime = 0;
			}
			if (flag != this.burnTime > 0) {
				flag1 = true;
				Crystallizer.updateBlockState(this.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}

		if (flag1) {
			//保存処理?
			this.markDirty();
		}
	}

	/**製錬が完成するまでの時間*/
	protected int getSmeltTime() {
		return 1200;
	}

	/**製錬可能かどうかの判定*/
	protected boolean canSmelt() {
		//製錬前スロットが空ならば
		if (this.itemStacks[0] == null) {
			//変換不可
			return false;
		//前スロットと変換色スロットのアイテムが同じで、ダメージが違うなら
		} else if (this.itemStacks[0].getItem() instanceof StarWoodsSap || this.itemStacks[0].getItem() instanceof StarWoodsCrystal || this.itemStacks[0].stackSize > 7) {
			ItemStack result = null;
			if (this.itemStacks[0].getItem() instanceof StarWoodsSap) result = new ItemStack(StarWoodsItemCore.crystalStarWoods, 1, this.itemStacks[0].getItemDamage());
			if (this.itemStacks[0].getItem() instanceof StarWoodsCrystal) result = StarWoodsRecipeCore.materials[this.itemStacks[0].getItemDamage()];
			if (result == null) {
				this.moveItemStack();
				return false;
			}
			return true;
		}
		//アイテムは入っているが修繕できないときはスロットを移動させる
		this.moveItemStack();
		return false;
	}

	/**製錬処理*/
	public void smeltItem() {
		//製錬可能ならば
		if (this.canSmelt()) {
			ItemStack result = null;
			if (this.itemStacks[0].getItem() instanceof StarWoodsSap) result = new ItemStack(StarWoodsItemCore.crystalStarWoods, 1, this.itemStacks[0].getItemDamage());
			if (this.itemStacks[0].getItem() instanceof StarWoodsCrystal) result = StarWoodsRecipeCore.materials[this.itemStacks[0].getItemDamage()];
			if (result == null) {
				this.moveItemStack();
				return;
			}
			//完成品スロットが空ならば
			if (this.itemStacks[2] == null) {
				//完成品スロットに製錬結果を代入する
				this.itemStacks[2] = new ItemStack(result.getItem(), 1, result.getItemDamage());
			//完成品スロットと変換結果が一致したら
			} else if (this.itemStacks[2].isItemEqual(result)) {
				//アイテム数を増やす
				this.itemStacks[2].stackSize ++;
			}

			//変換前スロットのアイテム数を減らす
			this.itemStacks[0].stackSize -= 8;

			//変換前アイテムがなくなったら
			if (this.itemStacks[0].stackSize < 1) {
				//変換前スロットを空にする
				this.itemStacks[0] = null;
			}
		} else {
			this.moveItemStack();
		}
	}

	/**製錬前スロットから完成品スロットへ移動させる*/
	private void moveItemStack() {
		//完成品スロットが空ならば
		if (this.itemStacks[2] == null) {
			//完成品スロットに製錬前スロットのアイテムを代入する
			this.itemStacks[2] = itemStacks[0].copy();
			//製錬前スロットを空にする
			this.itemStacks[0] = null;
		//完成品スロットと製錬前スロットが一致したら
		} else if (this.itemStacks[2].getItem() == itemStacks[0].getItem()) {
			//完成品スロットのアイテム数と製錬前スロットのアイテム数を足した数
			int result = itemStacks[2].stackSize + 1;
			//resultがインベントリ・アイテムのスタックリミット以下なら
			if (result <= getInventoryStackLimit() && result <= this.itemStacks[2].getMaxStackSize()) {
				//アイテム数を増やす
				this.itemStacks[2].stackSize += 1;
				//製錬前スロットのアイテム数を減らす
				--this.itemStacks[0].stackSize;
			}
			//製錬前アイテムがなくなったら
			if (this.itemStacks[0].stackSize <= 0) {
				//製錬前スロットを空にする
				this.itemStacks[0] = null;
			}
		}
	}

	/**そのアイテムの燃焼時間を返す*/
	public static int getItemBurnTime(ItemStack itemStack) {
		//受け取ったItemStackが空ならば
		if (itemStack == null) {
			//0を返す
			return 0;
		} else {
			return TileEntityFurnace.getItemBurnTime(itemStack);
		}
	}

	/**そのアイテムが燃料かどうかの判定*/
	public static boolean isItemFuel(ItemStack itemStack) {
		//燃焼時間があるならばtrueを返す
		return getItemBurnTime(itemStack) > 0;
	}

@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 2 ? false : (slot == 1 ? isItemFuel(itemStack) : true);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return side != 0 || slot != 1 || itemStack.getItem() == Items.bucket;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public void setCustomName(String displayName) {
		this.customName = displayName;
	}
}