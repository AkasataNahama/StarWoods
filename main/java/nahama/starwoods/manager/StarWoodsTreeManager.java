package nahama.starwoods.manager;

import java.util.ArrayList;
import java.util.Iterator;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import nahama.starwoods.block.BlockStarWoodsLeaves;
import nahama.starwoods.block.BlockStarWoodsLog;
import nahama.starwoods.block.BlockStarWoodsSapling;
import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.itemblock.ItemStarWoodsLeaves;
import nahama.starwoods.itemblock.ItemStarWoodsLog;
import nahama.starwoods.itemblock.ItemStarWoodsSapling;
import nahama.starwoods.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class StarWoodsTreeManager {

	public static final int SAPLING = 8;
	public static final int LOG = 4;
	public static int LEAVES;
	public static int CRYSTAL;

	private static ArrayList<StarWoodsTree> treeList = new ArrayList<StarWoodsTree>();
	private static int amountTreeKind;

	private static ArrayList<Block> saplingList = new ArrayList<Block>();
	private static ArrayList<Block> logList = new ArrayList<Block>();
	private static ArrayList<Block> leavesList = new ArrayList<Block>();

	public static boolean isMFRLoaded;

	/** 初期化処理。 */
	public static void init() {
		isMFRLoaded = Loader.isModLoaded("MineFactoryReloaded");
		LEAVES = StarWoodsConfigCore.canLeavesDecay ? 4 : 16;
		amountTreeKind = 0;
		for (String s : StarWoodsConfigCore.customTrees) {
			String[] array = s.split(",");
			try {
				int nameType = Integer.parseInt(array[0]);
				int meta = 0;
				if (nameType == 2 && array.length > 4 && array[4].length() > 0)
					meta = Integer.parseInt(array[4]);
				StarWoodsTree tree = new StarWoodsTree(amountTreeKind, nameType, Integer.parseInt(array[1]), Integer.parseInt(array[2], 16), array[3], meta);
				if (!tree.isValid())
					continue;
				treeList.add(tree);
				if (amountTreeKind % SAPLING == 0) {
					int instanceNum = amountTreeKind / SAPLING;
					Block sapling = new BlockStarWoodsSapling(instanceNum)
							.setBlockName("saplingStarWoods")
							.setBlockTextureName("starwoods:sapling");
					saplingList.add(sapling);
					GameRegistry.registerBlock(sapling, ItemStarWoodsSapling.class, "sapling-" + instanceNum);
					OreDictionary.registerOre("treeSapling", new ItemStack(sapling, 1, OreDictionary.WILDCARD_VALUE));
					OreDictionary.registerOre("saplingStarWoods", new ItemStack(sapling, 1, OreDictionary.WILDCARD_VALUE));
					if (isMFRLoaded)
						StarWoodsMFRManager.registerSapling(instanceNum);
				}
				if (amountTreeKind % LOG == 0) {
					int instanceNum = amountTreeKind / LOG;
					Block log = new BlockStarWoodsLog(instanceNum)
							.setBlockName("logStarWoods")
							.setBlockTextureName("log_oak");
					logList.add(log);
					GameRegistry.registerBlock(log, ItemStarWoodsLog.class, "log-" + instanceNum);
					OreDictionary.registerOre("logWood", new ItemStack(log, 1, OreDictionary.WILDCARD_VALUE));
					OreDictionary.registerOre("logStarWoods", new ItemStack(log, 1, OreDictionary.WILDCARD_VALUE));
					if (isMFRLoaded)
						StarWoodsMFRManager.registerLog(instanceNum);
				}
				if (amountTreeKind % LEAVES == 0) {
					int instanceNum = amountTreeKind / LEAVES;
					Block leaves = new BlockStarWoodsLeaves(instanceNum)
							.setBlockName("leavesStarWoods")
							.setBlockTextureName("leaves_oak");
					leavesList.add(leaves);
					GameRegistry.registerBlock(leaves, ItemStarWoodsLeaves.class, "leaves-" + instanceNum);
					OreDictionary.registerOre("treeLeaves", new ItemStack(leaves, 1, OreDictionary.WILDCARD_VALUE));
					OreDictionary.registerOre("leavesStarWoods", new ItemStack(leaves, 1, OreDictionary.WILDCARD_VALUE));
					if (isMFRLoaded)
						StarWoodsMFRManager.registerLeaves(instanceNum);
				}
			} catch (Exception e) {
				Util.error("Error on customizing tree. kind:" + amountTreeKind + " is invalid now.", "StarWoodsTreeManager");
				continue;
			}
			amountTreeKind++;
		}
		CRYSTAL = amountTreeKind;
	}

	/** 有効な木の種類数を返す。 */
	public static int getAmountTreeKind() {
		return amountTreeKind;
	}

	/** 有効な番号か。 */
	public static boolean isNumValid(int generalNum) {
		return generalNum < amountTreeKind;
	}

	/** 材料から作れる木を返す。 */
	public static StarWoodsTree getTreeFromMaterial(ItemStack material) {
		Iterator<StarWoodsTree> iterator = treeList.iterator();
		while (iterator.hasNext()) {
			StarWoodsTree tree = iterator.next();
			if (tree.isItemMaterial(material))
				return tree;
		}
		return null;
	}

	/** 木の樹液から作れるアイテムを返す。 */
	public static ItemStack getProduct(int generalNum) {
		if (generalNum >= amountTreeKind)
			return null;
		return treeList.get(generalNum).getProduct();
	}

	/** 木の希少度を返す。 */
	public static int getTier(int generalNum) {
		if (generalNum >= amountTreeKind)
			return 0;
		return treeList.get(generalNum).getTier();
	}

	/** 木の色を返す。 */
	public static int getColor(int generalNum) {
		if (generalNum >= amountTreeKind)
			return 0xFFFFFF;
		return treeList.get(generalNum).getColor();
	}

	/** 苗木のインスタンスを返す。 */
	public static BlockStarWoodsSapling getSaplingInstance(int generalNum) {
		if (generalNum >= amountTreeKind)
			return null;
		return (BlockStarWoodsSapling) saplingList.get(generalNum / SAPLING);
	}

	/** 原木のインスタンスを返す。 */
	public static Block getLogInstance(int generalNum) {
		if (generalNum >= amountTreeKind)
			return null;
		return logList.get(generalNum / LOG);
	}

	/** 葉のインスタンスを返す。 */
	public static Block getLeavesInstance(int generalNum) {
		if (generalNum >= amountTreeKind)
			return null;
		return leavesList.get(generalNum / LEAVES);
	}

}
