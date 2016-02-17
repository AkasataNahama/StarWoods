package nahama.starwoods.core;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class StarWoodsOreDicCore {

	//鉱石辞書から取得したアイテムを格納するItemStackの定義
	public static ItemStack ingotCopper = null;
	public static ItemStack ingotTin = null;
	public static ItemStack ingotSilver = null;
	public static ItemStack ingotLead = null;

	public static ItemStack ingotNickel = null;
	public static ItemStack ingotAluminum = null;
	public static ItemStack ingotOsmium = null;
	public static ItemStack ingotPlatinum = null;

	public static ItemStack ingotMithril = null;
	public static ItemStack ingotCobalt = null;
	public static ItemStack ingotArdite = null;
	public static ItemStack ingotManganese = null;

	public static ItemStack gemRuby = null;
	public static ItemStack gemSapphire = null;
	public static ItemStack gemPeridot = null;
	public static ItemStack ingotZinc = null;

	public static ItemStack gemOfalenRed = null;
	public static ItemStack gemOfalenGreen = null;
	public static ItemStack gemOfalenBlue = null;
	public static ItemStack gemOfalenWhite = null;

	public static Block TakumiWoods = Blocks.tnt;
	public static Block TakumiLeaves = Blocks.tnt;

	private static StarWoodsConfigCore cfg = new StarWoodsConfigCore();

	/**鉱石辞書からアイテムを取得する*/
	public static void getItemFromOreDictionary() {
		//鉱石辞書からItemStackを取得する
		ArrayList<ItemStack> ingotCopperList = OreDictionary.getOres(cfg.names[0]);
		ArrayList<ItemStack> ingotTinList = OreDictionary.getOres(cfg.names[1]);
		ArrayList<ItemStack> ingotSilverList = OreDictionary.getOres(cfg.names[2]);
		ArrayList<ItemStack> ingotLeadList = OreDictionary.getOres(cfg.names[3]);

		ArrayList<ItemStack> ingotNickelList = OreDictionary.getOres(cfg.names[4]);
		ArrayList<ItemStack> ingotAluminmList = OreDictionary.getOres(cfg.names[5]);
		ArrayList<ItemStack> ingotOsmiumList = OreDictionary.getOres(cfg.names[6]);
		ArrayList<ItemStack> ingotPlatinumList = OreDictionary.getOres(cfg.names[7]);

		ArrayList<ItemStack> ingotMithrilList = OreDictionary.getOres(cfg.names[8]);
		ArrayList<ItemStack> ingotCobaltList = OreDictionary.getOres(cfg.names[9]);
		ArrayList<ItemStack> ingotArditeList = OreDictionary.getOres(cfg.names[10]);
		ArrayList<ItemStack> ingotManganeseList = OreDictionary.getOres(cfg.names[11]);

		ArrayList<ItemStack> gemRubyList = OreDictionary.getOres(cfg.names[12]);
		ArrayList<ItemStack> gemSapphireList = OreDictionary.getOres(cfg.names[13]);
		ArrayList<ItemStack> gemPeridotList = OreDictionary.getOres(cfg.names[14]);
		ArrayList<ItemStack> ingotZincList = OreDictionary.getOres(cfg.names[15]);

		ArrayList<ItemStack> gemOfalenRedList = OreDictionary.getOres("gemOfalenRed");
		ArrayList<ItemStack> gemOfalenGreenList = OreDictionary.getOres("gemOfalenGreen");
		ArrayList<ItemStack> gemOfalenBlueList = OreDictionary.getOres("gemOfalenBlue");
		ArrayList<ItemStack> gemOfalenWhiteList = OreDictionary.getOres("gemOfalenWhite");

		ArrayList<ItemStack> TakumiWoodsList = OreDictionary.getOres("TakumiWoods");
		ArrayList<ItemStack> TakumiLeavesList = OreDictionary.getOres("TakumiLeaves");

		//ItemStackを取得できていれば、代入する
		if (ingotCopperList.size() > 0) ingotCopper = new ItemStack(ingotCopperList.get(0).getItem(), 1, ingotCopperList.get(0).getItemDamage());
		if (ingotTinList.size() > 0) ingotTin = new ItemStack(ingotTinList.get(0).getItem(), 1, ingotTinList.get(0).getItemDamage());
		if (ingotSilverList.size() > 0) ingotSilver = new ItemStack(ingotSilverList.get(0).getItem(), 1, ingotSilverList.get(0).getItemDamage());
		if (ingotLeadList.size() > 0) ingotLead = new ItemStack(ingotLeadList.get(0).getItem(), 1, ingotLeadList.get(0).getItemDamage());

		if (ingotNickelList.size() > 0) ingotNickel = new ItemStack(ingotNickelList.get(0).getItem(), 1, ingotNickelList.get(0).getItemDamage());
		if (ingotAluminmList.size() > 0) ingotAluminum = new ItemStack(ingotAluminmList.get(0).getItem(), 1, ingotAluminmList.get(0).getItemDamage());
		if (ingotOsmiumList.size() > 0) ingotOsmium = new ItemStack(ingotOsmiumList.get(0).getItem(), 1, ingotOsmiumList.get(0).getItemDamage());
		if (ingotPlatinumList.size() > 0) ingotPlatinum = new ItemStack(ingotPlatinumList.get(0).getItem(), 1, ingotPlatinumList.get(0).getItemDamage());

		if (ingotMithrilList.size() > 0) ingotMithril = new ItemStack(ingotMithrilList.get(0).getItem(), 1, ingotMithrilList.get(0).getItemDamage());
		if (ingotCobaltList.size() > 0) ingotCobalt = new ItemStack(ingotCobaltList.get(0).getItem(), 1, ingotCobaltList.get(0).getItemDamage());
		if (ingotArditeList.size() > 0) ingotArdite = new ItemStack(ingotArditeList.get(0).getItem(), 1, ingotArditeList.get(0).getItemDamage());
		if (ingotManganeseList.size() > 0) ingotManganese = new ItemStack(ingotManganeseList.get(0).getItem(), 1, ingotManganeseList.get(0).getItemDamage());

		if (gemRubyList.size() > 0) gemRuby = new ItemStack(gemRubyList.get(0).getItem(), 1, gemRubyList.get(0).getItemDamage());
		if (gemSapphireList.size() > 0) gemSapphire = new ItemStack(gemSapphireList.get(0).getItem(), 1, gemSapphireList.get(0).getItemDamage());
		if (gemPeridotList.size() > 0) gemPeridot = new ItemStack(gemPeridotList.get(0).getItem(), 1, gemPeridotList.get(0).getItemDamage());
		if (ingotZincList.size() > 0) ingotZinc = new ItemStack(ingotZincList.get(0).getItem(), 1, ingotZincList.get(0).getItemDamage());

		if (gemOfalenRedList.size() > 0) gemOfalenRed = new ItemStack(gemOfalenRedList.get(0).getItem(), 1, gemOfalenRedList.get(0).getItemDamage());
		if (gemOfalenGreenList.size() > 0) gemOfalenGreen = new ItemStack(gemOfalenGreenList.get(0).getItem(), 1, gemOfalenGreenList.get(0).getItemDamage());
		if (gemOfalenBlueList.size() > 0) gemOfalenBlue = new ItemStack(gemOfalenBlueList.get(0).getItem(), 1, gemOfalenBlueList.get(0).getItemDamage());
		if (gemOfalenWhiteList.size() > 0) gemOfalenWhite = new ItemStack(gemOfalenWhiteList.get(0).getItem(), 1, gemOfalenWhiteList.get(0).getItemDamage());

		//もし匠Mod(Tom Kate作)が入っていれば、匠の木の幹と葉のブロックを変える
		if (StarWoodsConfigCore.enabledTakumiCraft) {
			if (TakumiWoodsList.size() > 0) TakumiWoods = Block.getBlockFromItem(TakumiWoodsList.get(0).getItem());
			if (TakumiLeavesList.size() > 0) TakumiLeaves = Block.getBlockFromItem(TakumiLeavesList.get(0).getItem());
		}
	}

}
