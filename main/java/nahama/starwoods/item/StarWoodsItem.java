package nahama.starwoods.item;

import java.util.List;

import nahama.starwoods.StarWoodsModCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StarWoodsItem extends Item {

	private IIcon[] iicon;

	private final int type;

	public StarWoodsItem (int type) {
		super ();
		this.setCreativeTab(StarWoodsModCore.tabStarWoodsItem);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.type = type;
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	/**メタデータにより内部名を変える*/
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

	/**メタデータ違いのアイテムを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < type; i ++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	/**メタデータ違いのテクスチャを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iicon) {
		this.iicon = new IIcon[type];
		for (int i = 0; i < type; i ++) {
			this.iicon[i] =  iicon.registerIcon(this.getIconString() + "-" + i);
		}
	}

	/**メタデータにより返すIIconを変える*/
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return iicon[meta];
	}

}
