package nahama.starwoods.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.starwoods.core.StarWoodsConfigCore;
import nahama.starwoods.util.ArrayMap;
import nahama.starwoods.util.ItemKind;
import nahama.starwoods.util.Util;
import net.minecraft.item.ItemStack;

public class StarWoodsVEManager {

	public static ArrayMap<ItemKind, Integer> map = new ArrayMap<ItemKind, Integer>();

	/** 初期化処理。 */
	public static void init() {
		load();
	}

	/** StarWoodsVEList.txtを読み込む処理。 */
	private static void load() {
		File file = new File(new File(".\\").getParentFile(), "config\\StarWoodsVEList.txt");
		if (!file.exists() || !file.isFile() || !file.canRead())
			setUp(file);
		map.clear();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s;
			while ((s = br.readLine()) != null) {
				String[] array0 = s.split(",");
				String[] array1 = array0[0].split(":");
				if (array1.length < 2) {
					map.put(new ItemKind(array1[0]), Integer.parseInt(array0[array0.length - 1]));
					continue;
				}
				map.put(new ItemKind(GameRegistry.findItem(array1[0], array1[1]), Integer.parseInt(array0[array0.length - 2])), Integer.parseInt(array0[array0.length - 1]));
			}
			br.close();
		} catch (IOException e) {
			Util.error("Error on loading VEList.", "StarWoodsVEManager");
		}
	}

	/** 生命力を持っているか。 */
	public static boolean isItemStackHoldingVE(ItemStack itemStack) {
		return map.containsKey(new ItemKind(itemStack));
	}

	/** 生命力の量を返す。 */
	public static int getItemStackVE(ItemStack itemStack) {
		return map.get(new ItemKind(itemStack));
	}

	/** 希少度に対応した生命力量を返す。 */
	public static int getVEFromTier(int tier) {
		return (int) Math.round(Math.pow(2, tier) * StarWoodsConfigCore.factorVEUsing);
	}

	/** StarWoodsVEList.txtを生成、初期化する処理。 */
	private static void setUp(File file) {
		try {
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("StarWoods:material,0,65536");
			bw.newLine();
			bw.write("minecraft:golden_apple,1,16384");
			bw.newLine();
			bw.write("minecraft:hay_block,0,9216");
			bw.newLine();
			bw.write("minecraft:golden_apple,0,8192");
			bw.newLine();
			bw.write("minecraft:speckled_melon,0,4096");
			bw.newLine();
			bw.write("minecraft:golden_carrot,0,4096");
			bw.newLine();
			bw.write("minecraft:pumpkin,0,1024");
			bw.newLine();
			bw.write("minecraft:melon_block,0,1024");
			bw.newLine();
			bw.write("minecraft:apple,0,1024");
			bw.newLine();
			bw.write("cropWheat,1024");
			bw.newLine();
			bw.write("cropCarrot,1024");
			bw.newLine();
			bw.write("cropPotato,1024");
			bw.newLine();
			bw.write("treeSapling,512");
			bw.newLine();
			bw.write("logWood,512");
			bw.newLine();
			bw.write("treeLeaves,512");
			bw.newLine();
			bw.write("minecraft:tallgrass,32767,512");
			bw.newLine();
			bw.write("minecraft:yellow_flower,32767,512");
			bw.newLine();
			bw.write("minecraft:red_flower,32767,512");
			bw.newLine();
			bw.write("minecraft:brown_mushroom_block,0,512");
			bw.newLine();
			bw.write("minecraft:red_mushroom_block,0,512");
			bw.newLine();
			bw.write("minecraft:brown_mushroom,0,512");
			bw.newLine();
			bw.write("minecraft:red_mushroom,0,512");
			bw.newLine();
			bw.write("minecraft:waterlily,0,512");
			bw.newLine();
			bw.write("minecraft:reeds,0,512");
			bw.newLine();
			bw.write("minecraft:cactus,0,512");
			bw.newLine();
			bw.write("minecraft:double_plant,32767,512");
			bw.newLine();
			bw.write("minecraft:wheat_seeds,0,512");
			bw.newLine();
			bw.write("minecraft:pumpkin_seeds,0,512");
			bw.newLine();
			bw.write("minecraft:melon_seeds,0,512");
			bw.newLine();
			bw.write("minecraft:nether_wart,0,512");
			bw.newLine();
			bw.write("minecraft:poisonous_potato,0,512");
			bw.newLine();
			bw.write("minecraft:grass,0,256");
			bw.newLine();
			bw.write("minecraft:deadbush,0,256");
			bw.newLine();
			bw.write("minecraft:mossy_cobblestone,0,256");
			bw.newLine();
			bw.write("minecraft:vine,0,256");
			bw.newLine();
			bw.write("minecraft:mycelium,0,256");
			bw.newLine();
			bw.write("minecraft:melon,0,128");
			bw.close();
		} catch (IOException e) {
			Util.error("Error on creating VEList.", "StarWoodsVEManager");
		}
	}

}
