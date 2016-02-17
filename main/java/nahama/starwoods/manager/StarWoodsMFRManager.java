package nahama.starwoods.manager;

import net.minecraft.nbt.NBTTagCompound;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;

public class StarWoodsMFRManager {

	public static void registerSapling(int instanceNum) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Block", "StarWoods:sapling-" + instanceNum);
		FactoryRegistry.sendMessage("registerPlantable_Sapling", nbt);
		FactoryRegistry.sendMessage("registerFertilizable_Standard", nbt);
	}

	public static void registerLog(int instanceNum) {
		FactoryRegistry.sendMessage("registerHarvestable_Log", "StarWoods:log-" + instanceNum);
	}

	public static void registerLeaves(int instanceNum) {
		FactoryRegistry.sendMessage("registerHarvestable_Leaves", "StarWoods:leaves-" + instanceNum);
	}

}
