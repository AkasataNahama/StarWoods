package nahama.starwoods.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.starwoods.item.ItemCrystal;
import nahama.starwoods.item.ItemMaterial;
import nahama.starwoods.item.ItemPruningScissors;
import nahama.starwoods.item.ItemTapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class StarWoodsItemCore {

	public static Item material;
	public static Item scissorsPruning;

	public static Item tapperIron;
	public static Item tapperGold;
	public static Item tapperDiamond;

	public static Item sap;
	public static Item crystal;

	// TODO debugger is enabled only developing.
	// public static Item debugger;

	/** アイテムを登録する処理。 */
	public static void registerItems() {
		material = new ItemMaterial(4)
				.setUnlocalizedName("starwoods.material")
				.setTextureName("starwoods:material");
		GameRegistry.registerItem(material, "material");

		scissorsPruning = new ItemPruningScissors()
				.setUnlocalizedName("starwoods.pruning_scissors")
				.setTextureName("starwoods:pruning_scissors")
				.setMaxDamage(249);
		GameRegistry.registerItem(scissorsPruning, "pruning_scissors");

		// 樹液採り
		tapperIron = new ItemTapper(3)
				.setUnlocalizedName("starwoods.tapper.iron")
				.setTextureName("starwoods:iron_tapper")
				.setMaxDamage(127);
		GameRegistry.registerItem(tapperIron, "iron_tapper");

		tapperGold = new ItemTapper(4)
				.setUnlocalizedName("starwoods.tapper.gold")
				.setTextureName("starwoods:gold_tapper")
				.setMaxDamage(255);
		GameRegistry.registerItem(tapperGold, "gold_tapper");

		tapperDiamond = new ItemTapper(5)
				.setUnlocalizedName("starwoods.tapper.diamond")
				.setTextureName("starwoods:diamond_tapper")
				.setMaxDamage(511);
		GameRegistry.registerItem(tapperDiamond, "diamond_tapper");

		// 樹液
		sap = new ItemCrystal()
				.setUnlocalizedName("starwoods.sap")
				.setTextureName("starwoods:sap");
		GameRegistry.registerItem(sap, "sap");
		OreDictionary.registerOre("sapStarWoods", new ItemStack(sap, 1, OreDictionary.WILDCARD_VALUE));

		// 結晶
		crystal = new ItemCrystal()
				.setUnlocalizedName("starwoods.crystal")
				.setTextureName("starwoods:crystal");
		GameRegistry.registerItem(crystal, "crystal");
		OreDictionary.registerOre("crystalStarWoods", new ItemStack(crystal, 1, OreDictionary.WILDCARD_VALUE));

		// TODO debugger is enabled only developing.
		// debugger = new ItemDebugger()
		// .setUnlocalizedName("starwoods.debuggor");
		// GameRegistry.registerItem(debugger, "debugger");
	}

}
