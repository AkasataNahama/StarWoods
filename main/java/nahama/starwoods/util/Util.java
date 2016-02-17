package nahama.starwoods.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nahama.starwoods.StarWoodsCore;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Util {

	private static Logger logger = LogManager.getLogger(StarWoodsCore.MODID);

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void info(String msg, String data) {
		Util.info(msg + " [" + data + "]");
	}

	public static void info(String msg, String data, boolean flag) {
		if (flag)
			Util.info(msg + " [" + data + "]");
	}

	public static void error(String msg) {
		logger.error(msg);
	}

	public static void error(String msg, String data) {
		Util.error(msg + " [" + data + "]");
	}

	public static void error(String msg, String data, boolean flag) {
		if (flag)
			Util.error(msg + " [" + data + "]");
	}

	/** 苗木として鉱石辞書に登録されているか。 */
	public static boolean isItemStackSapling(ItemStack itemStack) {
		if (itemStack == null)
			return false;
		return OreDictionary.getOres("treeSapling").contains(new ItemKind(itemStack));
	}

}
