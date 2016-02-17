package nahama.starwoods.core;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import nahama.starwoods.block.BlockStarWoodsSapling;
import nahama.starwoods.manager.StarWoodsTreeManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class StarWoodsRecipeCore {

	private static final StarWoodsBlockCore BLOCK = new StarWoodsBlockCore();
	private static final StarWoodsItemCore ITEM = new StarWoodsItemCore();

	private static final String allx = "XXX";
	private static final String xyx = "XYX";
	private static String saplingx;

	/** レシピを登録する処理。 */
	public static void registerRecipes() {
		// 木炭の塊のレシピのConfigへの対応
		String[] recipeArray = new String[] { allx, allx, allx };
		String recipeType = "X X";
		switch (StarWoodsConfigCore.recipeLump % 3) {
		case 0:
			recipeType = " XX";
			break;
		case 2:
			recipeType = "XX ";
			break;
		}
		recipeArray[StarWoodsConfigCore.recipeLump / 3] = recipeType;

		// 枝切ばさみ
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.scissorsPruning,
				"X X", " X ", "Y Y", 'X', Items.iron_ingot, 'Y', Items.stick));

		// 晶析装置
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.crystallizer,
				"SIS", "IFI", "GGG", 'S', new ItemStack(ITEM.material, 1, 0), 'I', "blockIron", 'F', Blocks.furnace, 'G', "blockGold"));

		// 生命力系装置
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.machine,
				"LLL", "LIL", "LLL", 'L', "treeLeaves", 'I', "blockIron"));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.machine, 8),
				"LLL", "LGL", "LLL", 'L', "treeLeaves", 'I', "blockGold"));

		// 木炭の塊
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.material, 1, 1),
				recipeArray, 'X', new ItemStack(Items.coal, 1, 1)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.material, 1, 2),
				recipeArray, 'X', new ItemStack(ITEM.material, 1, 1)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.material, 1, 3),
				recipeArray, 'X', new ItemStack(ITEM.material, 1, 2)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.coal, 8, 1),
				"X", 'X', new ItemStack(ITEM.material, 1, 1)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.material, 8, 1),
				"X", 'X', new ItemStack(ITEM.material, 1, 2)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.material, 8, 2),
				"X", 'X', new ItemStack(ITEM.material, 1, 3)));

		// 木材のレシピ
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.plankMysterious, 4),
				"X", 'X', "logStarWoods"));

		// 燃料
		GameRegistry.registerFuelHandler(new IFuelHandler() {
			@Override
			public int getBurnTime(ItemStack fuel) {
				if (Block.getBlockFromItem(fuel.getItem()) instanceof BlockStarWoodsSapling)
					return 100;
				if (fuel.getItem() == ITEM.material) {
					int damage = fuel.getItemDamage();
					if (damage > 1) {
						if (damage == 2)
							return 12800;
						if (damage == 3)
							return 102400;
						if (damage == 4)
							return 819200;
					}
				}
				return 0;
			}
		});

	}

	/** 木のレシピを登録する処理。 */
	public static void registerTreeRecipes() {
		for (int i = 0; i < StarWoodsTreeManager.getAmountTreeKind(); i++) {
			// 樹液から結晶へのレシピ
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.crystal, 1, i),
					allx, allx, allx, 'X', new ItemStack(ITEM.sap, 1, i)));
			// 結晶から各種アイテムへのレシピ
			GameRegistry.addRecipe(new ShapedOreRecipe(StarWoodsTreeManager.getProduct(i),
					allx, allx, allx, 'X', new ItemStack(ITEM.crystal, 1, i)));
			// 原木から苗木へのレシピ
			GameRegistry.addSmelting(StarWoodsTreeManager.getLogInstance(i), new ItemStack(Items.coal, 1, 1), 0.15F);
		}
	}

}
