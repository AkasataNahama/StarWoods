package nahama.starwoods.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahama.starwoods.StarWoodsCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemMaterial extends Item {

	protected IIcon[] iicon;
	protected int type;

	public ItemMaterial(int type) {
		super();
		this.setCreativeTab(StarWoodsCore.tabStarWoodsItem);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.type = type;
	}

	/** メタデータを返す。 */
	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	/** 翻訳前の名前を返す。 */
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

	/** クリエイティブタブに登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < type; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	/** アイコンを登録する処理。 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		iicon = new IIcon[type];
		for (int i = 0; i < type; i++) {
			iicon[i] = register.registerIcon(this.getIconString() + "-" + i);
		}
	}

	/** アイコンを返す。 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta];
	}

}
