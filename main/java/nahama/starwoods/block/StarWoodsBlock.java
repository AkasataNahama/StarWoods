package nahama.starwoods.block;

import nahama.starwoods.StarWoodsModCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class StarWoodsBlock extends Block {

	public StarWoodsBlock(Material material) {
		super(material);
		this.setCreativeTab(StarWoodsModCore.tabStarWoodsBlock);
		this.setLightLevel(0.6F);
	}

}
