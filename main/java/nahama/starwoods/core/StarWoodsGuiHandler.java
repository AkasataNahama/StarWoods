package nahama.starwoods.core;

import cpw.mods.fml.common.network.IGuiHandler;
import nahama.starwoods.gui.GuiCrystallizer;
import nahama.starwoods.gui.GuiVEExtractor;
import nahama.starwoods.gui.GuiVEInjector;
import nahama.starwoods.inventory.ContainerCrystallizer;
import nahama.starwoods.inventory.ContainerVEExtractor;
import nahama.starwoods.inventory.ContainerVEInjector;
import nahama.starwoods.tileentity.TileEntityCrystallizer;
import nahama.starwoods.tileentity.TileEntityVEExtractor;
import nahama.starwoods.tileentity.TileEntityVEInjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class StarWoodsGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityCrystallizer) {
			return new ContainerCrystallizer(player, (TileEntityCrystallizer) tileEntity);
		} else if (tileEntity instanceof TileEntityVEExtractor) {
			return new ContainerVEExtractor(player, (TileEntityVEExtractor) tileEntity);
		} else if (tileEntity instanceof TileEntityVEInjector) {
			return new ContainerVEInjector(player, (TileEntityVEInjector) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityCrystallizer) {
			return new GuiCrystallizer(player, (TileEntityCrystallizer) tileEntity);
		} else if (tileEntity instanceof TileEntityVEExtractor) {
			return new GuiVEExtractor(player, (TileEntityVEExtractor) tileEntity);
		} else if (tileEntity instanceof TileEntityVEInjector) {
			return new GuiVEInjector(player, (TileEntityVEInjector) tileEntity);
		}
		return null;
	}

}