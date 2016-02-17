package nahama.starwoods.generate;

import java.util.Random;

import nahama.starwoods.core.StarWoodsBlockCore;
import nahama.starwoods.core.StarWoodsConfigCore;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class StarTreeGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider instanceof WorldProviderSurface) {
			this.generateSapling(world, random, chunkX << 4, chunkZ << 4);
		}
	}

	private void generateSapling(World world, Random random, int x, int z) {
		Block block = StarWoodsBlockCore.saplingStarTreeNatural;
		int genX = x + random.nextInt(16);
		int genY = random.nextInt(256);
		int genZ = z + random.nextInt(16);
		if (block.canPlaceBlockAt(world, genX, genY, genZ) && world.getBlock(genX, genY - 1, genZ) == Blocks.grass) {
			if (random.nextInt(StarWoodsConfigCore.probabilityGeneration) == 0) world.setBlock(genX, genY, genZ, block);
		}
	}

}
