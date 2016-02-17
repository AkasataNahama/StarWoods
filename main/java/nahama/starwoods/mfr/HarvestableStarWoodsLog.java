package nahama.starwoods.mfr;

import net.minecraft.block.Block;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableStandard;

public class HarvestableStarWoodsLog extends HarvestableStandard {

	public HarvestableStarWoodsLog(Block block) {
		super(block, HarvestType.Tree);
	}

}
