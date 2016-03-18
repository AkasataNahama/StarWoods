package nahama.starwoods.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemKind {

	private boolean isNamed;
	private Item item;
	private int meta;
	private String name;

	public ItemKind(Block block) {
		this(block, OreDictionary.WILDCARD_VALUE);
	}

	public ItemKind(Block block, int meta) {
		this(Item.getItemFromBlock(block), meta);
	}

	public ItemKind(Item item) {
		this(item, OreDictionary.WILDCARD_VALUE);
	}

	public ItemKind(Item item, int meta) {
		this.item = item;
		this.meta = meta;
	}

	public ItemKind(ItemStack itemStack) {
		this.item = itemStack.getItem();
		this.meta = itemStack.getItemDamage();
	}

	public ItemKind(String oreName) {
		isNamed = true;
		name = oreName;
	}

	public Item getItem() {
		if (!this.isValid())
			return null;
		if (!isNamed)
			return item;
		return OreDictionary.getOres(name).get(0).getItem();
	}

	public int getMeta() {
		if (!this.isValid())
			return OreDictionary.WILDCARD_VALUE;
		if (!isNamed)
			return meta;
		return OreDictionary.getOres(name).get(0).getItemDamage();
	}

	public ItemKind copy() {
		if (!this.isValid())
			return null;
		if (!isNamed)
			return new ItemKind(item, meta);
		return new ItemKind(OreDictionary.getOres(name).get(0));
	}

	public ItemStack getSampleStack() {
		if (!this.isValid())
			return null;
		if (!isNamed)
			return new ItemStack(item, 1, meta);
		return new ItemStack(OreDictionary.getOres(name).get(0).getItem(), 1, OreDictionary.getOres(name).get(0).getItemDamage());
	}

	public boolean isValid() {
		if (!isNamed)
			return item != null;
		return name != null && OreDictionary.doesOreNameExist(name) && OreDictionary.getOres(name).size() > 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ItemStack)
			return this.equals(new ItemKind((ItemStack) obj));
		if (!(obj instanceof ItemKind))
			return false;

		if (this.isNamed)
			return OreDictionary.getOres(name).contains(obj);
		ItemKind itemKind = (ItemKind) obj;
		if (itemKind.isNamed)
			return OreDictionary.getOres(itemKind.name).contains(this);
		if (this.getItem() != itemKind.getItem())
			return false;
		if (this.getMeta() == OreDictionary.WILDCARD_VALUE || itemKind.getMeta() == OreDictionary.WILDCARD_VALUE)
			return true;
		return this.getMeta() == itemKind.getMeta();
	}

	@Override
	public String toString() {
		if (!isNamed)
			return item.getUnlocalizedName() + "," + meta;
		return name;
	}

}
