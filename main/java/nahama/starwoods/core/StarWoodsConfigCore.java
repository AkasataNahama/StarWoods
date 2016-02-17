package nahama.starwoods.core;

import nahama.starwoods.Log;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class StarWoodsConfigCore {

	//config設定項目の定義
	public static boolean enabledConfig;
	public static boolean enabledDifficultyDetailed;

	public static int difficultyBase;
	public static int difficultyRecipeSapling;
	public static int dropSap;
	public static int dropSapling;

	public static boolean enabledGenerator;
	public static int probabilityGeneration;

	public static int recipeLump;

	public static boolean enabledTakumiCraft;

	public static String[] names = new String[16];
	public static String[] namesDefault = new String[16];

	/**configを追加する*/
	public static void registerConfig(FMLPreInitializationEvent event) {

		namesDefault = new String[] {
				"ingotCopper",
				"ingotTin",
				"ingotSilver",
				"ingotLead",
				"ingotNickel",
				"ingotAluminum",
				"ingotOsmium",
				"ingotPlatinum",
				"ingotMithril",
				"ingotCobalt",
				"ingotArdite",
				"ingotManganese",
				"gemRuby",
				"gemSapphire",
				"gemPeridot",
				"ingotZinc"
		};

		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile(), true);

		try {
			cfg.load();
			enabledConfig = cfg.get("General", "ConfigEnabled", false, "If true config enabled[false]").getBoolean();
			enabledDifficultyDetailed = cfg.get("General", "DetailedDifficultyEnabled", false, "If true detailed difficulty enabled[false]").getBoolean();

			cfg.setCategoryComment("General.Difficulty", "0:Peaceful, 1:Easy, 2:Normal, 3:Hard");
			difficultyBase = cfg.get("General.Difficulty", "BaseDifficulty", 2, "Enabled only when \"DetailedDifficultyEnabled\" is false.\nDifficulty of all[2]").getInt();

			difficultyRecipeSapling = cfg.get("General.Difficulty.Recipe", "SaplingRecipeDifficulty", 2, "Enabled only when \"DetailedDifficultyEnabled\" is true.\nDifficulty of all sapling recipes[2]").getInt();

			cfg.setCategoryComment("General.Difficulty.Drop", "Enabled only when \"DetailedDifficultyEnabled\" is true.\nDecreases the probability as the number increases.\nThe number is base. More expensive ones are higher than this value.");
			dropSap = cfg.get("General.Difficulty.Drop", "SapDrop", 8, "Probability of sap drop from natural log[8]").getInt();
			dropSapling = cfg.get("General.Difficulty.Drop", "SaplingDrop", 48, "Probability of sapling drop from leaves[48]").getInt();

			enabledGenerator = cfg.get("General.Generate", "GeneratorEnabled", true, "If true Star Sapling Generator enabled[true]").getBoolean();
			probabilityGeneration = cfg.get("General.Generate", "GenerationProbability", 1000, "Enubled only when \"GeneratorEnabled\" is true.\nGeneration probability of Natural Star Sapling[1000]").getInt();

			recipeLump = cfg.get("General.Recipe", "LumpRecipe", 4, "Recipe of Lump of Charcoal. Number is location of space[4]\n0 1 2\n3 4 5\n6 7 8").getInt();

			enabledTakumiCraft = cfg.get("General.OtherMod", "TakumiCraftEnabled", true, "If true load Takumi Craft[true]").getBoolean();

			for (int i = 0; i < 16; i ++) {
				String name = "Name";
				if (i < 10) {
					name = "Name0";
				}
				names[i] = cfg.get("General.OreDictionary", name + i, namesDefault[i]).getString();
			}
		} catch (Exception error) {
			Log.error("Config loading error", "StarWoodsConfigCore", true);
		} finally {
			cfg.save();
		}

		if (enabledConfig) {
			if (!enabledDifficultyDetailed) {
				difficultyRecipeSapling = difficultyBase;

				switch (difficultyBase) {
				case 0:
					dropSap = 4;
					dropSapling = 24;
				case 1:
					dropSap = 6;
					dropSapling = 36;
				case 2:
				default:
					dropSap = 8;
					dropSapling = 48;
				case 3:
					dropSap = 16;
					dropSapling = 56;
				}
			}
		} else {
			difficultyRecipeSapling = 2;
			dropSap = 8;
			dropSapling = 48;
			enabledGenerator = true;
			probabilityGeneration = 1000;
			recipeLump = 4;
			enabledTakumiCraft = true;

			for (int i = 0; i < 16; i ++) {
				names[i] = namesDefault[i];
			}
		}
	}

}
