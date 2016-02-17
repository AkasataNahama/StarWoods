package nahama.starwoods.gui;

import org.lwjgl.opengl.GL11;

import nahama.starwoods.inventory.ContainerVEInjector;
import nahama.starwoods.tileentity.TileEntityVEInjector;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiVEInjector extends GuiContainer {

	private TileEntityVEInjector tileEntity;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("starwoods:textures/gui/container/injector.png");

	public GuiVEInjector(EntityPlayer player, TileEntityVEInjector tileEntity) {
		super(new ContainerVEInjector(player, tileEntity));
		this.tileEntity = tileEntity;
	}

	/** 前面レイヤーを描画する処理。 */
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		String s = tileEntity.hasCustomInventoryName() ? tileEntity.getInventoryName() : StatCollector.translateToLocal(tileEntity.getInventoryName());
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		s = tileEntity.getHoldingVE() + " VE";
		fontRendererObj.drawString(s, 168 - fontRendererObj.getStringWidth(s), 60, 4210752);
	}

	/** 背面レイヤーを描画する処理。 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUITEXTURE);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		int i1;
		i1 = tileEntity.getInjectProgressScaled(24);
		this.drawTexturedModalRect(k + 79, l + 34, 176, 0, i1 + 1, 16);
	}

}
