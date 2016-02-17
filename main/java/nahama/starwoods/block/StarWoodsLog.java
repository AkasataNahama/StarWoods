package nahama.starwoods.block;

import java.util.List;

import nahama.starwoods.StarWoodsModCore;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StarWoodsLog extends BlockRotatedPillar {

	private IIcon[][] iicon = new IIcon[2][4];
	private int type;

	public StarWoodsLog (int type) {
		super(Material.wood);
		this.setCreativeTab(StarWoodsModCore.tabStarWoodsBlock);
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
		this.setLightLevel(0.9F);
		this.type = type * 4;
	}

	/**メタデータ違いのブロックを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for (int i = 0; i < 4; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	/**メタデータ違いのテクスチャを登録する*/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iicon) {
		for (int i = 0; i < 4; i ++) {
			this.iicon[0][i] = iicon.registerIcon(this.getTextureName() + "_top-" + (i + type));
			this.iicon[1][i] = iicon.registerIcon(this.getTextureName() + "_side-" + (i + type));
		}
	}

	/**上面のテクスチャを返す*/
	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getTopIcon(int meta) {
		return this.iicon[0][meta];
	}

	/**側面のテクスチャを返す*/
	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int meta) {
		return this.iicon[1][meta];
	}

	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName() + ":" + String.valueOf(this.type);
	}

}
