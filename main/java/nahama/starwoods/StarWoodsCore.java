package nahama.starwoods;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import nahama.starwoods.core.StarWoodsBlockCore;
import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.core.StarWoodsGuiHandler;
import nahama.starwoods.core.StarWoodsInfoCore;
import nahama.starwoods.core.StarWoodsItemCore;
import nahama.starwoods.core.StarWoodsRecipeCore;
import nahama.starwoods.creativetab.StarWoodsTab;
import nahama.starwoods.manager.StarWoodsTreeManager;
import nahama.starwoods.manager.StarWoodsVEManager;
import nahama.starwoods.network.MExtractorDirection;
import nahama.starwoods.network.MExtractorVE;
import net.minecraft.creativetab.CreativeTabs;

/** @author Akasata Nahama */
@Mod(modid = StarWoodsCore.MODID, name = StarWoodsCore.MODNAME, version = StarWoodsCore.VERSION)
public class StarWoodsCore {

	public static final String MODID = "StarWoods";
	public static final String MODNAME = "Star Woods";
	public static final String VERSION = "[1.7.10]Beta 1.1.2";

	/** coreクラスのインスタンス */
	@Instance(MODID)
	public static StarWoodsCore instance;

	@Metadata(MODID)
	public static ModMetadata meta;

	public static final CreativeTabs tabStarWoodsItem = new StarWoodsTab("StarWoodsItemTab", false);
	public static final CreativeTabs tabStarWoodsBlock = new StarWoodsTab("StarWoodsBlockTab", true);

	public static final SimpleNetworkWrapper wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(StarWoodsCore.MODID);

	/** 初期化前処理 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// long start = System.currentTimeMillis();
		StarWoodsInfoCore.registerInfo(meta);
		StarWoodsConfigCore.loadConfig(event);
		StarWoodsItemCore.registerItems();
		StarWoodsBlockCore.registerBlocks();
		// Util.info("Complete Pre Initialization. Time : " + (System.currentTimeMillis() - start) + " ms", "StarWoodsCore");
	}

	/** 初期化処理 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// long start = System.currentTimeMillis();
		StarWoodsTreeManager.init();
		StarWoodsVEManager.init();
		StarWoodsRecipeCore.registerRecipes();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new StarWoodsGuiHandler());
		wrapper.registerMessage(MExtractorDirection.Handler.class, MExtractorDirection.class, 0, Side.SERVER);
		wrapper.registerMessage(MExtractorVE.Handler.class, MExtractorVE.class, 1, Side.CLIENT);
		// Util.info("Complete Initialization. Time : " + (System.currentTimeMillis() - start) + " ms", "StarWoodsCore");
	}

	/** 初期化後処理 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// long start = System.currentTimeMillis();
		StarWoodsRecipeCore.registerOtherRecipes();
		// Util.info("Complete Post Initialization. Time : " + (System.currentTimeMillis() - start) + " ms", "StarWoodsCore");
	}

}
