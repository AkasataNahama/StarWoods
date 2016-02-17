package nahama.starwoods.mfr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.farmables.harvestables.HarvestableStandard;

public class HarvestableStarWoodsLeaves extends HarvestableStandard {

	public HarvestableStarWoodsLeaves(Block paramBlock) {
		super(paramBlock, HarvestType.TreeLeaf);
	}

	public List<ItemStack> getDrops(World world, Random random, Map<String, Boolean> map, int x, int y, int z) {
		Block localBlock = world.getBlock(x, y, z);
		if (map.get("silkTouch") == Boolean.TRUE) {
			ArrayList<ItemStack> list = new ArrayList<ItemStack>();
			list.add(new ItemStack(Item.getItemFromBlock(localBlock), 1, world.getBlockMetadata(x, y, z)));
			return list;
		}
		return localBlock.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
	}

}
