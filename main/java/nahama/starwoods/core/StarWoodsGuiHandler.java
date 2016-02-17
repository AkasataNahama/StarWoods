package nahama.starwoods.core;

import nahama.starwoods.gui.GuiCrystallizer;
import nahama.starwoods.inventory.ContainerCrystallizer;
import nahama.starwoods.tileentity.TileEntityCrystallizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class StarWoodsGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntityCrystallizer) {
			return new ContainerCrystallizer(player, (TileEntityCrystallizer) tileentity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;

		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileEntityCrystallizer) {
			return new GuiCrystallizer(player, (TileEntityCrystallizer) tileentity);
		}
		return null;
	}

}