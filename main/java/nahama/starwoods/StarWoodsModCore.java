package nahama.starwoods;

import nahama.starwoods.core.StarWoodsBlockCore;
import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.core.StarWoodsGuiHandler;
import nahama.starwoods.core.StarWoodsInfoCore;
import nahama.starwoods.core.StarWoodsItemCore;
import nahama.starwoods.core.StarWoodsOreDicCore;
import nahama.starwoods.core.StarWoodsRecipeCore;
import nahama.starwoods.creativetab.StarWoodsTab;
import nahama.starwoods.generate.StarTreeGenerator;
import nahama.starwoods.mfr.StarWoodsMFR;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = StarWoodsModCore.MODID, name = StarWoodsModCore.MODNAME, version = StarWoodsModCore.VERSION)
public class StarWoodsModCore {

	public static final String MODID = "StarWoods";
	public static final String MODNAME = "Star Woods";
	public static final String VERSION = "[1.7.10]Beta 1.0.0";

	/**coreクラスのインスタンス*/
	@Instance(MODID)
	public static StarWoodsModCore instance;

	/**modsリストの情報設定用*/
	@Metadata(MODID)
	public static ModMetadata meta;

	//追加するクリエイティブタブ
	public static final CreativeTabs tabStarWoodsItem = new StarWoodsTab("StarWoodsItemTab", false);
	public static final CreativeTabs tabStarWoodsBlock = new StarWoodsTab("StarWoodsBlockTab", true);

	/**最初に行われる処理。アイテム・ブロックの追加などを行う*/
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//Infoファイルを読み込む
		StarWoodsInfoCore.loadInfo(meta);

		//configを追加するメソッドを実行
		StarWoodsConfigCore.registerConfig(event);

		//アイテムを追加するメソッドを実行
		StarWoodsItemCore.registerItem();

		//ブロックを追加するメソッドを実行
		StarWoodsBlockCore.registerBlock();

		//機械類のGUIを登録する
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new StarWoodsGuiHandler());
	}

	/**2番目に行われる処理。レシピの追加などを行う*/
	@EventHandler
	public void init (FMLInitializationEvent event) {
		if (StarWoodsConfigCore.enabledGenerator) {
			//星の木の苗木を自然生成させる
			GameRegistry.registerWorldGenerator(new StarTreeGenerator(), 0);
		}

		//レシピを追加するメソッドを実行
		StarWoodsRecipeCore.registerRecipe();
	}

	/**最後に行われる処理。鉱石辞書から取得したアイテムを使った処理などを行う*/
	@EventHandler
	public void postInit (FMLPostInitializationEvent event){
		//鉱石辞書からアイテムを取得するメソッドを実行
		StarWoodsOreDicCore.getItemFromOreDictionary();

		//鉱石辞書から取得したアイテムを使ったレシピを追加するメソッドを実行
		StarWoodsRecipeCore.registerOreRecipe();

		//MFRへの対応
		if (Loader.isModLoaded("MineFactoryReloaded")) {
			StarWoodsMFR.register();
		}
	}

}
