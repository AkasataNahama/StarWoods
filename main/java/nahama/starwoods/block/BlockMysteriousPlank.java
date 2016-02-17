package nahama.starwoods.block;

import nahama.starwoods.StarWoodsCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockMysteriousPlank extends Block {

	public BlockMysteriousPlank() {
		super(Material.wood);
		this.setCreativeTab(StarWoodsCore.tabStarWoodsBlock);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(Block.soundTypeWood);
		this.setLightLevel(0.6F);
	}

}
