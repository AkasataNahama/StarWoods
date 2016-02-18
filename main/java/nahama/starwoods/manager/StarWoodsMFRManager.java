package nahama.starwoods.manager;

import net.minecraft.nbt.NBTTagCompound;
import powercrystals.minefactoryreloaded.api.FactoryRegistry;

public class StarWoodsMFRManager {

	public static void registerSapling(int instanceNum) {
		String id = "StarWoods:sapling-" + instanceNum;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("sapling", id);
		FactoryRegistry.sendMessage("registerPlantable_Sapling", nbt);
		NBTTagCompound nbt1 = new NBTTagCompound();
		nbt1.setString("plant", id);
		FactoryRegistry.sendMessage("registerFertilizable_Standard", nbt1);
	}

	public static void registerLog(int instanceNum) {
		FactoryRegistry.sendMessage("registerHarvestable_Log", "StarWoods:log-" + instanceNum);
	}

	public static void registerLeaves(int instanceNum) {
		FactoryRegistry.sendMessage("registerHarvestable_Leaves", "StarWoods:leaves-" + instanceNum);
	}

}
