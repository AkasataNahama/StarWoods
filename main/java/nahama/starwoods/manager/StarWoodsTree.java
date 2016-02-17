package nahama.starwoods.manager;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.starwoods.util.ItemKind;
import net.minecraft.item.ItemStack;

public class StarWoodsTree {

	private boolean isValid;
	private int generalNum;

	private int tier;
	private int color;
	private ItemKind material;

	public StarWoodsTree(int generalNum, int nameType, int tier, int color, String name, int meta) {
		this.generalNum = generalNum;
		switch (nameType) {
		case 1:
			material = new ItemKind(name);
			break;
		case 2:
			material = new ItemKind(GameRegistry.findItem(name.split(":")[0], name.substring(name.indexOf(':') + 1)), meta);
			break;
		}
		this.tier = tier;
		this.color = color;
		if (material != null && material.getItem() != null)
			isValid = true;
	}

	public boolean isValid() {
		return isValid;
	}

	public ItemStack getProduct() {
		if (!isValid)
			return null;
		return material.getSampleStack();
	}

	public boolean isItemMaterial(ItemStack itemStack) {
		if (!isValid || itemStack == null)
			return false;
		return material.equals(itemStack);
	}

	public int getGeneralNum() {
		return generalNum;
	}

	public int getTier() {
		return tier;
	}

	public int getColor() {
		return color;
	}

}
