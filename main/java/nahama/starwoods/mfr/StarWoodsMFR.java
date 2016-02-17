package nahama.starwoods.mfr;

import nahama.starwoods.core.StarWoodsBlockCore;
import powercrystals.minefactoryreloaded.MFRRegistry;

public class StarWoodsMFR {

	public static void register(){
		for (int i = 0; i < StarWoodsBlockCore.logStarWoodsNatural.length; i ++) {
			MFRRegistry.registerHarvestable(new HarvestableStarWoodsLog(StarWoodsBlockCore.logStarWoodsNatural[i]));
		}

		for (int i = 0; i < StarWoodsBlockCore.leavesStarWoods.length; i ++) {
			MFRRegistry.registerHarvestable(new HarvestableStarWoodsLeaves(StarWoodsBlockCore.leavesStarWoods[i]));
		}

		for (int i = 0; i < StarWoodsBlockCore.saplingStarWoods.length; i ++) {
			MFRRegistry.registerPlantable(new PlantableStarWoodsSapling(StarWoodsBlockCore.saplingStarWoods[i]));
			MFRRegistry.registerFertilizable(new FertilizableStarWoodsSapling(StarWoodsBlockCore.saplingStarWoods[i]));
		}
	}

}
