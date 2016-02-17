package nahama.starwoods.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.StarWoodsCore;
import nahama.starwoods.manager.StarWoodsTreeManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemCrystal extends Item {

	public ItemCrystal() {
		this.setCreativeTab(StarWoodsCore.tabStarWoodsItem);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	/** メタデータを返す。 */
	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	/** クリエイティブタブに登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < StarWoodsTreeManager.CRYSTAL; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	/** 表示名を返す。 */
	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		ItemStack product = StarWoodsTreeManager.getProduct(itemStack.getItemDamage() % StarWoodsTreeManager.CRYSTAL);
		String name = "StarWoods.Default";
		if (product != null)
			name = product.getUnlocalizedName() + ".name";
		return StatCollector.translateToLocal(name) + StatCollector.translateToLocal(this.getUnlocalizedName().substring(5));
	}

	/** 色を返す。 */
	@Override
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		return StarWoodsTreeManager.getColor(itemStack.getItemDamage() % StarWoodsTreeManager.CRYSTAL);
	}

}
