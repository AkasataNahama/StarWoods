package nahama.starwoods.creativetab;

import nahama.starwoods.core.StarWoodsBlockCore;
import nahama.starwoods.core.StarWoodsItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StarWoodsTab extends CreativeTabs {

	private final boolean isBlock;

	public StarWoodsTab(String label, boolean isBlock) {
		super(label);
		this.isBlock = isBlock;
	}

	/**アイコンの設定*/
	@SideOnly(Side.CLIENT)
	@Override
	public Item getTabIconItem() {
		if (isBlock)
			return Item.getItemFromBlock(StarWoodsBlockCore.crystallizer);
		else
			return StarWoodsItemCore.itemStarWoods;
	}

}
