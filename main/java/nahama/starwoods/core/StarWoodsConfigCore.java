package nahama.starwoods.core;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import nahama.starwoods.util.Util;
import net.minecraftforge.common.config.Configuration;

public class StarWoodsConfigCore {

	public static boolean isConfigEnabled;
	public static boolean isDetailedDifficultyEnabled;

	public static int difficultyBase;
	public static int factorVEUsing;
	public static int veStarting;

	public static int probablilityDropSap;
	public static int probablilityDropSapling;

	public static int recipeLump;

	public static String[] customTrees;
	public static boolean canLeavesDecay;

	public static int timeCrystallizing;
	public static int timeExtracting;
	public static int timeInjecting;

	public static int levelFortuneTapperIron;
	public static int levelFortuneTapperGold;
	public static int levelFortuneTapperDiamond;

	private static final String GENERAL = "General";
	private static final String DIFFICULTY = GENERAL + ".Difficulty";
	private static final String DROP = DIFFICULTY + ".Drop";
	private static final String RECIPE = GENERAL + ".Recipe";
	private static final String CUSTOM = GENERAL + ".Custom";
	private static final String MACHINE = GENERAL + ".Machine";
	private static final String TAPPER = GENERAL + ".Tapper";

	/** Configを読み込む処理。 */
	public static void loadConfig(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile(), true);
		try {
			cfg.load();
			isConfigEnabled = cfg.getBoolean("enableConfig", GENERAL, false, "If true config is enabled.");
			isDetailedDifficultyEnabled = cfg.getBoolean("enableDetailedDifficulty", GENERAL, false, "If true detailed difficulty is enabled.");

			cfg.setCategoryComment(DIFFICULTY, "0:Peaceful, 1:Easy, 2:Normal, 3:Hard");
			difficultyBase = cfg.getInt("generalDifficulty", DIFFICULTY, 2, 0, 3, "Enabled only when \"enableDetailedDifficulty\" is false.");
			factorVEUsing = cfg.getInt("usingVEFactor", DIFFICULTY, 16384, 0, Integer.MAX_VALUE, "Enabled only when \"enableDetailedDifficulty\" is true.\nFactor of using VE to inject.");
			veStarting = cfg.getInt("usingVEToCreateStar", DIFFICULTY, 524288, 0, Integer.MAX_VALUE, "Enabled only when \"enableDetailedDifficulty\" is true.\nUsing VE to create star.");

			cfg.setCategoryComment(DROP, "Enabled only when \"enableDetailedDifficulty\" is true.\nDecreases the probability as the number increases.\nThe number is factor. More expensive ones are higher than this value.");
			probablilityDropSap = cfg.getInt("sapDropProbability", DROP, 8, 1, Integer.MAX_VALUE, "Probability of sap drop from natural logs.");
			probablilityDropSapling = cfg.getInt("saplingDropProbability", DROP, 48, 1, Integer.MAX_VALUE, "Probability of sapling drop from leaves.");

			recipeLump = cfg.getInt("lumpRecipe", RECIPE, 4, 0, 9, "0 1 2\n3 4 5\n6 7 8\nRecipe of 'Lump of Charcoal'. Number is location of space.");

			customTrees = cfg.getStringList("customTrees", CUSTOM, new String[] { "2,3,FFFF00,StarWoods:material,0" }, "You can customize kinds of tree.(NameType, Tier, Color, Name(, meta))");
			canLeavesDecay = cfg.getBoolean("decayLeaves", CUSTOM, false, "If true natural leaves of Star Woods will decay.");

			timeCrystallizing = cfg.getInt("crystallizingTime", MACHINE, 1200, 0, Integer.MAX_VALUE, "Working hours of 'Crystallizer'.");
			timeExtracting = cfg.getInt("extractingTime", MACHINE, 20, 0, Integer.MAX_VALUE, "Working hours of 'VE Extractor'.");
			timeInjecting = cfg.getInt("injectingTime", MACHINE, 200, 0, Integer.MAX_VALUE, "Working hours of 'VE Injector'.");

			levelFortuneTapperIron = cfg.getInt("ironTapperFortuneLevel", TAPPER, 3, 0, Integer.MAX_VALUE, "Fortune level of 'Iron Tapper'.");
			levelFortuneTapperGold = cfg.getInt("goldTapperFortuneLevel", TAPPER, 4, 0, Integer.MAX_VALUE, "Fortune level of 'Gold Tapper'.");
			levelFortuneTapperDiamond = cfg.getInt("diamondTapperFortuneLevel", TAPPER, 5, 0, Integer.MAX_VALUE, "Fortune level of 'Diamond Tapper'.");
		} catch (Exception e) {
			Util.error("Error on loading config.", "StarWoodsConfigCore");
		} finally {
			cfg.save();
		}

		if (isConfigEnabled) {
			if (!isDetailedDifficultyEnabled) {
				switch (difficultyBase) {
				case 0:
					factorVEUsing = 4096;
					veStarting = 131072;
					probablilityDropSap = 2;
					probablilityDropSapling = 12;
				case 1:
					factorVEUsing = 8192;
					veStarting = 262144;
					probablilityDropSap = 4;
					probablilityDropSapling = 24;
				case 2:
				default:
					factorVEUsing = 16384;
					veStarting = 524288;
					probablilityDropSap = 8;
					probablilityDropSapling = 48;
				case 3:
					factorVEUsing = 32768;
					veStarting = 1048576;
					probablilityDropSap = 16;
					probablilityDropSapling = 64;
				}
			}
		} else {
			factorVEUsing = 16384;
			veStarting = 524288;
			probablilityDropSap = 8;
			probablilityDropSapling = 48;
			recipeLump = 4;
			canLeavesDecay = false;
			timeCrystallizing = 1200;
			timeExtracting = 20;
			timeInjecting = 200;
			levelFortuneTapperIron = 3;
			levelFortuneTapperGold = 4;
			levelFortuneTapperDiamond = 5;
		}
	}

}
