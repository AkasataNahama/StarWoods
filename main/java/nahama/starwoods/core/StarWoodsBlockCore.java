package nahama.starwoods.core;

import nahama.starwoods.block.CreeperTreeSapling;
import nahama.starwoods.block.Crystallizer;
import nahama.starwoods.block.StarTreeSaplingNatural;
import nahama.starwoods.block.StarWoodsBlock;
import nahama.starwoods.block.StarWoodsLeaves;
import nahama.starwoods.block.StarWoodsLog;
import nahama.starwoods.block.StarWoodsLogNatural;
import nahama.starwoods.block.StarWoodsSapling;
import nahama.starwoods.itemblock.ItemStarWoodsBlock;
import nahama.starwoods.itemblock.ItemStarWoodsLog;
import nahama.starwoods.itemblock.ItemStarWoodsSapling;
import nahama.starwoods.tileentity.TileEntityCrystallizer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class StarWoodsBlockCore {

	//ブロックの定義
	public static Block crystallizer;
	public static Block plankMystery;

	//原木の定義
	public static Block[] logStarWoods = new Block[16];

	//原木(自然)の定義
	public static Block[] logStarWoodsNatural = new Block[4];

	//葉の定義
	public static Block[] leavesStarWoods = new Block[4];

	//苗木
	public static Block[] saplingStarWoods = new Block[8];
	public static Block saplingCreeperTree;
	public static Block saplingStarTreeNatural;

	/**ブロックを追加する*/
	public static void registerBlock() {
		//晶析装置
		crystallizer = new Crystallizer()
		.setBlockName("crystallizer")
		.setBlockTextureName("starwoods:crystallizer");
		GameRegistry.registerBlock(crystallizer, "crystallizer");
		GameRegistry.registerTileEntity(TileEntityCrystallizer.class, "TileEntityCrystallizer");

		//木材
		plankMystery = new StarWoodsBlock(Material.wood)
		.setBlockName("plankMystery")
		.setBlockTextureName("starwoods:mystery_plank")
		.setStepSound(Block.soundTypeWood)
		.setHardness(2.0F)
		.setResistance(5.0F);
		GameRegistry.registerBlock(plankMystery, "plankMystery");
		OreDictionary.registerOre("plankWood", plankMystery);

		//原木
		for (int i = 0; i < 16; i ++) {
			logStarWoods[i] = new StarWoodsLog(i)
			.setBlockName("logStarWoods")
			.setBlockTextureName("starwoods:log");
			GameRegistry.registerBlock(logStarWoods[i], ItemStarWoodsLog.class, "logStarWoods-" + i);
			OreDictionary.registerOre("logWood", new ItemStack(logStarWoods[i], 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("logStarWoods", new ItemStack(logStarWoods[i], 1, OreDictionary.WILDCARD_VALUE));
		}

		//原木 (自然)
		for (int i = 0; i < 4; i ++) {
			logStarWoodsNatural[i] = new StarWoodsLogNatural(i)
			.setBlockName("logStarWoodsNatural")
			.setBlockTextureName("starwoods:log");
			GameRegistry.registerBlock(logStarWoodsNatural[i], ItemStarWoodsBlock.class, "logStarWoodsNatural-" + i);
			OreDictionary.registerOre("logStarWoodsNatural", new ItemStack(logStarWoodsNatural[i], 1, OreDictionary.WILDCARD_VALUE));
		}

		//葉
		for (int i = 0; i < 4; i ++) {
			leavesStarWoods[i] = new StarWoodsLeaves(i)
			.setBlockName("leavesStarWoods")
			.setBlockTextureName("starwoods:leaves");
			GameRegistry.registerBlock(leavesStarWoods[i], ItemStarWoodsBlock.class, "leavesStarWoods-" + i);
			OreDictionary.registerOre("treeLeaves", new ItemStack(leavesStarWoods[i], 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("leavesStarWoods", new ItemStack(leavesStarWoods[i], 1, OreDictionary.WILDCARD_VALUE));
		}

		//苗木
		for (int i = 0; i < 8; i ++) {
			saplingStarWoods[i] = new StarWoodsSapling(i)
			.setBlockName("saplingStarWoods")
			.setBlockTextureName("starwoods:sapling");
			GameRegistry.registerBlock(saplingStarWoods[i], ItemStarWoodsSapling.class, "saplingStarWoods-" + i);
			OreDictionary.registerOre("treeSapling", new ItemStack(saplingStarWoods[i], 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("saplingStarWoods", new ItemStack(saplingStarWoods[i], 1, OreDictionary.WILDCARD_VALUE));
		}

		saplingCreeperTree = new CreeperTreeSapling()
		.setBlockName("saplingCreeperTree")
		.setBlockTextureName("starwoods:creeper_tree_sapling");
		GameRegistry.registerBlock(saplingCreeperTree, "saplingCreeperTree");
		OreDictionary.registerOre("treeSapling", new ItemStack(saplingCreeperTree, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("saplingStarWoods", new ItemStack(saplingCreeperTree, 1, OreDictionary.WILDCARD_VALUE));

		saplingStarTreeNatural = new StarTreeSaplingNatural()
		.setBlockName("saplingStarTreeNatural")
		.setBlockTextureName("starwoods:sapling-0");
		GameRegistry.registerBlock(saplingStarTreeNatural, "saplingStarTreeNatural");
		OreDictionary.registerOre("saplingStarWoods", new ItemStack(saplingStarTreeNatural, 1, OreDictionary.WILDCARD_VALUE));
	}

}
