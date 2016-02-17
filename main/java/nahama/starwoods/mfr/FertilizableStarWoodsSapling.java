package nahama.starwoods.mfr;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;

public class FertilizableStarWoodsSapling implements IFactoryFertilizable {

	private Block block;

	public FertilizableStarWoodsSapling(Block block) {
		this.block = block;
	}

	@Override
	public Block getPlant() {
		return block;
	}

	@Override
	public boolean canFertilize(World world, int x, int y, int z, FertilizerType type) {
		return type == FertilizerType.GrowPlant;
	}

	@Override
	public boolean fertilize(World world, Random random, int x, int y, int z, FertilizerType type) {
		if (!(block instanceof IGrowable)) {
			return false;
		}
		((IGrowable) block).func_149853_b(world, random, x, y, z);
		return true;
	}

}
