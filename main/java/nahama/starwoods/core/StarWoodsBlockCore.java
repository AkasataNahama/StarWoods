package nahama.starwoods.core;

import cpw.mods.fml.common.registry.GameRegistry;
import nahama.starwoods.block.BlockCrystallizer;
import nahama.starwoods.block.BlockMysteriousPlank;
import nahama.starwoods.block.BlockVEMachine;
import nahama.starwoods.itemblock.ItemVEMachine;
import nahama.starwoods.tileentity.TileEntityCrystallizer;
import nahama.starwoods.tileentity.TileEntityVEExtractor;
import nahama.starwoods.tileentity.TileEntityVEInjector;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

public class StarWoodsBlockCore {

	public static Block crystallizer;
	public static Block machine;
	public static Block plankMysterious;

	/** ブロックを登録する処理。 */
	public static void registerBlocks() {
		// 晶析装置
		crystallizer = new BlockCrystallizer()
				.setBlockName("starwoods.crystallizer")
				.setBlockTextureName("starwoods:crystallizer");
		GameRegistry.registerBlock(crystallizer, "crystallizer");
		GameRegistry.registerTileEntity(TileEntityCrystallizer.class, "TileEntityCrystallizer");

		// VE関連
		machine = new BlockVEMachine()
				.setBlockName("starwoods.ve_machine")
				.setBlockTextureName("starwoods:ve_machine");
		GameRegistry.registerBlock(machine, ItemVEMachine.class, "ve_machine");
		GameRegistry.registerTileEntity(TileEntityVEExtractor.class, "TileEntityVEExtractor");
		GameRegistry.registerTileEntity(TileEntityVEInjector.class, "TileEntityVEInjector");

		// 木材
		plankMysterious = new BlockMysteriousPlank()
				.setBlockName("starwoods.mysterious_plank")
				.setBlockTextureName("starwoods:mysterious_plank");
		GameRegistry.registerBlock(plankMysterious, "plankMysterious");
		OreDictionary.registerOre("plankWood", plankMysterious);
	}

}
