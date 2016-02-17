package nahama.starwoods.core;

import nahama.starwoods.block.StarWoodsSapling;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class StarWoodsRecipeCore {

	/**森の星*/
	private static final ItemStack starWoods = new ItemStack(StarWoodsItemCore.itemStarWoods, 1, 0);
	/**万能の苗木*/
	private static final ItemStack saplingAlmighty = new ItemStack(StarWoodsItemCore.itemStarWoods, 1, 1);

	private static final StarWoodsBlockCore BLOCK = new StarWoodsBlockCore();
	private static final StarWoodsItemCore ITEM = new StarWoodsItemCore();
	private static final StarWoodsOreDicCore OREDIC = new StarWoodsOreDicCore();

	private static final String allx = "XXX";
	private static final String xyx = "XYX";
	private static String saplingx;
	/**結晶からできるアイテムの配列。nullの可能性あり*/
	public static ItemStack[] materials = new ItemStack[64];

	/**レシピを追加する*/
	public static void registerRecipe() {
		materials = new ItemStack[] {
				starWoods,
				new ItemStack(Items.coal),
				new ItemStack(Items.redstone),
				new ItemStack(Items.diamond),
				new ItemStack(Items.glowstone_dust),
				new ItemStack(Blocks.netherrack),
				new ItemStack(Blocks.soul_sand),
				new ItemStack(Items.quartz),

				new ItemStack(Items.iron_ingot),
				new ItemStack(Items.gold_ingot),
				new ItemStack(Blocks.obsidian),
				new ItemStack(Items.emerald),
				OREDIC.ingotCopper,
				OREDIC.ingotTin,
				OREDIC.ingotSilver,
				OREDIC.ingotLead,

				new ItemStack(Items.nether_star),
				new ItemStack(Items.magma_cream),
				new ItemStack(Items.ghast_tear),
				new ItemStack(Items.ender_pearl),
				new ItemStack(Items.slime_ball),
				new ItemStack(Items.spider_eye),
				new ItemStack(Items.blaze_rod),
				new ItemStack(Items.skull, 1, 1),

				new ItemStack(Items.rotten_flesh),
				new ItemStack(Items.bone),
				new ItemStack(Items.gunpowder),
				new ItemStack(Items.string),
				new ItemStack(Items.egg),
				new ItemStack(Items.feather),
				new ItemStack(Blocks.wool),
				new ItemStack(Items.leather),

				new ItemStack(Items.dye, 1, 0),
				new ItemStack(Items.dye, 1, 1),
				new ItemStack(Items.dye, 1, 2),
				new ItemStack(Items.dye, 1, 3),
				new ItemStack(Items.dye, 1, 4),
				new ItemStack(Items.dye, 1, 5),
				new ItemStack(Items.dye, 1, 6),
				new ItemStack(Items.dye, 1, 7),

				new ItemStack(Items.dye, 1, 8),
				new ItemStack(Items.dye, 1, 9),
				new ItemStack(Items.dye, 1, 10),
				new ItemStack(Items.dye, 1, 11),
				new ItemStack(Items.dye, 1, 12),
				new ItemStack(Items.dye, 1, 13),
				new ItemStack(Items.dye, 1, 14),
				new ItemStack(Items.dye, 1, 15),

				OREDIC.ingotNickel,
				OREDIC.ingotAluminum,
				OREDIC.ingotOsmium,
				OREDIC.ingotPlatinum,
				OREDIC.ingotMithril,
				OREDIC.ingotCobalt,
				OREDIC.ingotArdite,
				OREDIC.ingotManganese,

				OREDIC.gemRuby,
				OREDIC.gemSapphire,
				OREDIC.gemPeridot,
				OREDIC.ingotZinc,
				OREDIC.gemOfalenRed,
				OREDIC.gemOfalenGreen,
				OREDIC.gemOfalenBlue,
				OREDIC.gemOfalenWhite
		};

		//木炭の塊のレシピのConfigへの対応
		String[] recipeArray = new String[]{"XXX", "XXX", "XXX"};
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

		//苗木のレシピのConfigへの対応
		switch (StarWoodsConfigCore.difficultyRecipeSapling) {
		case 0:
			saplingx = "   ";
			break;
		case 1:
			saplingx = " X ";
			break;
		case 2:
		default:
			saplingx = "XXX";
			break;
		case 3:
			saplingx = "XZX";
		}

		//星の苗木のレシピ
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.saplingStarWoods[0],
				" X ", xyx, " X ", 'X', Items.nether_star, 'Y', "treeSapling"));

		//万能の苗木のレシピ
		GameRegistry.addRecipe(new ShapedOreRecipe(saplingAlmighty,
				allx, xyx, allx, 'X', starWoods, 'Y', new ItemStack(BLOCK.saplingStarWoods[0])));

		//枝切ばさみのレシピ
		GameRegistry.addRecipe(new ShapedOreRecipe(ITEM.scissorsPruning,
				"X X", " X ", "Y Y", 'X', Items.iron_ingot, 'Y', Items.stick));

		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.crystallizer,
				"SIS", "IFI", "GGG", 'S', starWoods, 'I', "blockIron", 'F', Blocks.furnace, 'G', "blockGold"));

		//木炭の塊のレシピ
		GameRegistry.addRecipe(new ShapedOreRecipe(new  ItemStack(ITEM.itemStarWoods, 1, 2),
				recipeArray, 'X', new ItemStack(Items.coal, 1, 1)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.itemStarWoods, 1, 3),
				recipeArray, 'X', new  ItemStack(ITEM.itemStarWoods, 1, 2)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.itemStarWoods, 1, 4),
				recipeArray, 'X', new  ItemStack(ITEM.itemStarWoods, 1, 3)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.coal, 8, 1),
				"X", 'X', new ItemStack(ITEM.itemStarWoods, 1, 2)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.itemStarWoods, 8, 2),
				"X", 'X', new ItemStack(ITEM.itemStarWoods, 1, 3)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.itemStarWoods, 8, 3),
				"X", 'X', new ItemStack(ITEM.itemStarWoods, 1, 4)));

		//木材のレシピ
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.plankMystery, 4),
				"X", 'X', "logStarWoods"));

		//苗木のレシピ
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.saplingStarWoods[0], 1, 0),
				saplingx, xyx, saplingx, 'X', starWoods, 'Y', "treeSapling", 'Z', starWoods));

		saplingRecipe(1, Items.coal);
		saplingRecipe(2, "dustRedstone");
		saplingRecipe(3, "gemDiamond");
		saplingRecipe(4, "dustGlowstone");
		saplingRecipe(5, Blocks.netherrack);
		saplingRecipe(6, Blocks.soul_sand);
		saplingRecipe(7, "gemQuartz");

		saplingRecipe(8, "ingotIron");
		saplingRecipe(9, "ingotGold");
		saplingRecipe(10, Blocks.obsidian);
		saplingRecipe(11, "gemEmerald");
		saplingRecipe(12, "ingotCopper");
		saplingRecipe(13, "ingotTin");
		saplingRecipe(14, "ingotSilver");
		saplingRecipe(15, "ingotLead");

		for (int i = 16; i < 48; i ++) {
			if (materials[i] != null)
				saplingRecipe(i, materials[i]);
		}

		saplingRecipe(48, "ingotNickel");
		saplingRecipe(49, "ingotAluminum");
		saplingRecipe(50, "ingotOsmium");
		saplingRecipe(51, "ingotPlatinum");
		saplingRecipe(52, "ingotMithril");
		saplingRecipe(53, "ingotCobalt");
		saplingRecipe(54, "ingotArdite");
		saplingRecipe(55, "ingotManganese");

		saplingRecipe(56, "gemRuby");
		saplingRecipe(57, "gemSapphire");
		saplingRecipe(58, "gemPeridot");
		saplingRecipe(59, "ingotZinc");
		saplingRecipe(60, "gemOfalenRed");
		saplingRecipe(61, "gemOfalenGreen");
		saplingRecipe(62, "gemOfalenBlue");
		saplingRecipe(63, "gemOfalenWhite");

		//その他の苗木のレシピ
		GameRegistry.addRecipe(new ShapedOreRecipe(BLOCK.saplingCreeperTree,
				saplingx, xyx, saplingx, 'X', new ItemStack(Items.skull, 1, 4), 'Y', saplingAlmighty, 'Z', starWoods));

		//樹液から結晶へのレシピ
		for (int i = 0; i < 64; i ++) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ITEM.crystalStarWoods, 1, i),
					allx, allx, allx, 'X', new ItemStack(ITEM.sapStarWoods, 1, i)));
		}

		//原木から木炭への製錬レシピ
		for (int i = 0; i < 16; i ++) {
			GameRegistry.addSmelting(BLOCK.logStarWoods[i], new ItemStack(Items.coal, 1, 1), 0.15F);
		}

		//苗木を燃料として登録
		GameRegistry.registerFuelHandler(new IFuelHandler(){
			public int getBurnTime(ItemStack fuel){
				Item[] saplings = new Item[] {
						new ItemStack(BLOCK.saplingCreeperTree).getItem(),
						new ItemStack(BLOCK.saplingStarTreeNatural).getItem()
				};
				for (Item sapling : saplings) {
					if (fuel.getItem() == sapling){
						return 100;
					}
				}
				if (Block.getBlockFromItem(fuel.getItem()) instanceof StarWoodsSapling) return 100;
				if (fuel.getItem() == ITEM.itemStarWoods) {
					int damage = fuel.getItemDamage();
					if (damage > 1 ) {
						if (damage == 2) return 12800;
						if (damage == 3) return 102400;
						if (damage == 4) return 819200;
					}
				}
				return 0;
			}
		});

	}

	public static void saplingRecipe(int meta, Object material) {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BLOCK.saplingStarWoods[meta / 8], 1, meta % 8),
				saplingx, xyx, saplingx, 'X', material, 'Y', saplingAlmighty, 'Z', starWoods));
	}

	/**鉱石辞書から取得したアイテムを使ったレシピを設定する*/
	public static void registerOreRecipe() {
		materials = new ItemStack[] {
				starWoods,
				new ItemStack(Items.coal),
				new ItemStack(Items.redstone),
				new ItemStack(Items.diamond),
				new ItemStack(Items.glowstone_dust),
				new ItemStack(Blocks.netherrack),
				new ItemStack(Blocks.soul_sand),
				new ItemStack(Items.quartz),

				new ItemStack(Items.iron_ingot),
				new ItemStack(Items.gold_ingot),
				new ItemStack(Blocks.obsidian),
				new ItemStack(Items.emerald),
				OREDIC.ingotCopper,
				OREDIC.ingotTin,
				OREDIC.ingotSilver,
				OREDIC.ingotLead,

				new ItemStack(Items.nether_star),
				new ItemStack(Items.magma_cream),
				new ItemStack(Items.ghast_tear),
				new ItemStack(Items.ender_pearl),
				new ItemStack(Items.slime_ball),
				new ItemStack(Items.spider_eye),
				new ItemStack(Items.blaze_rod),
				new ItemStack(Items.skull, 1, 1),

				new ItemStack(Items.rotten_flesh),
				new ItemStack(Items.bone),
				new ItemStack(Items.gunpowder),
				new ItemStack(Items.string),
				new ItemStack(Items.egg),
				new ItemStack(Items.feather),
				new ItemStack(Blocks.wool),
				new ItemStack(Items.leather),

				new ItemStack(Items.dye, 1, 0),
				new ItemStack(Items.dye, 1, 1),
				new ItemStack(Items.dye, 1, 2),
				new ItemStack(Items.dye, 1, 3),
				new ItemStack(Items.dye, 1, 4),
				new ItemStack(Items.dye, 1, 5),
				new ItemStack(Items.dye, 1, 6),
				new ItemStack(Items.dye, 1, 7),

				new ItemStack(Items.dye, 1, 8),
				new ItemStack(Items.dye, 1, 9),
				new ItemStack(Items.dye, 1, 10),
				new ItemStack(Items.dye, 1, 11),
				new ItemStack(Items.dye, 1, 12),
				new ItemStack(Items.dye, 1, 13),
				new ItemStack(Items.dye, 1, 14),
				new ItemStack(Items.dye, 1, 15),

				OREDIC.ingotNickel,
				OREDIC.ingotAluminum,
				OREDIC.ingotOsmium,
				OREDIC.ingotPlatinum,
				OREDIC.ingotMithril,
				OREDIC.ingotCobalt,
				OREDIC.ingotArdite,
				OREDIC.ingotManganese,

				OREDIC.gemRuby,
				OREDIC.gemSapphire,
				OREDIC.gemPeridot,
				OREDIC.ingotZinc,
				OREDIC.gemOfalenRed,
				OREDIC.gemOfalenGreen,
				OREDIC.gemOfalenBlue,
				OREDIC.gemOfalenWhite
		};

		for (int i = 0; i < 64; i ++) {
			if (materials[i] != null)
				GameRegistry.addRecipe(new ShapedOreRecipe(materials[i],
						allx, allx, allx, 'X', new ItemStack(ITEM.crystalStarWoods, 1, i)));
		}
	}

}
