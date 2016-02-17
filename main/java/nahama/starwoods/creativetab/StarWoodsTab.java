package nahama.starwoods.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.core.StarWoodsBlockCore;
import nahama.starwoods.core.StarWoodsItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class StarWoodsTab extends CreativeTabs {

	private final boolean isBlock;

	public StarWoodsTab(String label, boolean isBlock) {
		super(label);
		this.isBlock = isBlock;
	}

	/** アイコンのアイテムを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		if (isBlock)
			return Item.getItemFromBlock(StarWoodsBlockCore.plankMysterious);
		else
			return StarWoodsItemCore.material;
	}

}
