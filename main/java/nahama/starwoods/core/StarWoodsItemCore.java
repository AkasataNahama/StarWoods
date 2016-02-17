package nahama.starwoods.core;

import nahama.starwoods.item.PruningScissors;
import nahama.starwoods.item.StarWoodsCrystal;
import nahama.starwoods.item.StarWoodsItem;
import nahama.starwoods.item.StarWoodsSap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class StarWoodsItemCore {

	public static Item itemStarWoods;
	public static Item scissorsPruning;
	public static Item sapStarWoods;
	public static Item crystalStarWoods;

	/**アイテムを追加する*/
	public static void registerItem() {
		itemStarWoods = new StarWoodsItem(5)
		.setUnlocalizedName("itemStarWoods")
		.setTextureName("starwoods:item");
		GameRegistry.registerItem(itemStarWoods, "itemStarWoods");

		scissorsPruning = new PruningScissors()
		.setUnlocalizedName("scissorsPruning")
		.setTextureName("starwoods:pruning_scissors")
		.setMaxDamage(249);
		GameRegistry.registerItem(scissorsPruning, "scissorsPruning");

		//樹液
		sapStarWoods = new StarWoodsSap()
		.setUnlocalizedName("sapStarWoods")
		.setTextureName("starwoods:sap");
		GameRegistry.registerItem(sapStarWoods, "sapStarWoods");
		OreDictionary.registerOre("sapStarWoods", new ItemStack(sapStarWoods, 1, OreDictionary.WILDCARD_VALUE));

		//結晶
		crystalStarWoods = new StarWoodsCrystal()
		.setUnlocalizedName("crystalStarWoods")
		.setTextureName("starwoods:crystal");
		GameRegistry.registerItem(crystalStarWoods, "crystalStarWoods");
		OreDictionary.registerOre("crystalStarWoods", new ItemStack(crystalStarWoods, 1, OreDictionary.WILDCARD_VALUE));
	}

}
