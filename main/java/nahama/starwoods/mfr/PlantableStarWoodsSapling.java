package nahama.starwoods.mfr;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import powercrystals.minefactoryreloaded.api.ReplacementBlock;
import powercrystals.minefactoryreloaded.farmables.plantables.PlantableStandard;

public class PlantableStarWoodsSapling extends PlantableStandard {

	private Block block;

	public PlantableStarWoodsSapling(Block block) {
		super(Item.getItemFromBlock(block), block, OreDictionary.WILDCARD_VALUE, new ReplacementBlock(block).setMeta(true));
	}

}
