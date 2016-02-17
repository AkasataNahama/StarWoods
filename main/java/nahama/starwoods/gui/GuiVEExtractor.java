package nahama.starwoods.gui;

import org.lwjgl.opengl.GL11;

import nahama.starwoods.StarWoodsCore;
import nahama.starwoods.inventory.ContainerVEExtractor;
import nahama.starwoods.network.MExtractorDirection;
import nahama.starwoods.tileentity.TileEntityVEExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiVEExtractor extends GuiContainer {

	protected TileEntityVEExtractor tileEntity;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("starwoods:textures/gui/container/extractor.png");

	public GuiVEExtractor(EntityPlayer player, TileEntityVEExtractor tileEntity) {
		super(new ContainerVEExtractor(player, tileEntity));
		this.tileEntity = tileEntity;
	}

	/** 初期化処理。 */
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButtonExtractor(-1, guiLeft + 154, guiTop + 64, 0));
		buttonList.add(new GuiButtonExtractor(-2, guiLeft + 146, guiTop + 56, 1));
		buttonList.add(new GuiButtonExtractor(-3, guiLeft + 146, guiTop + 48, 2));
		buttonList.add(new GuiButtonExtractor(-4, guiLeft + 146, guiTop + 64, 3));
		buttonList.add(new GuiButtonExtractor(-5, guiLeft + 138, guiTop + 56, 4));
		buttonList.add(new GuiButtonExtractor(-6, guiLeft + 154, guiTop + 56, 5));
	}

	/** ボタンが押された時の処理。 */
	@Override
	protected void actionPerformed(GuiButton button) {
		byte side = (byte) Math.abs(button.id + 1);
		StarWoodsCore.wrapper.sendToServer(new MExtractorDirection(side, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
		tileEntity.getWorldObj().setBlockMetadataWithNotify(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, side, 2);
		tileEntity.markDirty();
	}

	/** 前面レイヤーを描画する処理。 */
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		String s = tileEntity.hasCustomInventoryName() ? tileEntity.getInventoryName() : StatCollector.translateToLocal(tileEntity.getInventoryName());
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		s = tileEntity.getHoldingVE() + " VE";
		fontRendererObj.drawString(s, 168 - fontRendererObj.getStringWidth(s), 30, 4210752);
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
		i1 = tileEntity.getExtractProgressScaled(29);
		this.drawTexturedModalRect(k + 82, l + 15 + 29 - i1, 176, 29 - i1, 12, i1);
	}

	public class GuiButtonExtractor extends GuiButton {
		private byte side;

		public GuiButtonExtractor(int id, int x, int y, int side) {
			super(id, x, y, 8, 8, "");
			this.side = (byte) side;
		}

		/** ボタンを描画する処理。 */
		@Override
		public void drawButton(Minecraft minecraft, int cursorX, int cursorY) {
			if (!visible)
				return;
			minecraft.getTextureManager().bindTexture(GUITEXTURE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			// カーソルがあっているか
			field_146123_n = cursorX >= xPosition && cursorY >= yPosition && cursorX < xPosition + width && cursorY < yPosition + height;
			int offY = (field_146123_n || tileEntity.getBlockMetadata() == side) ? height : 0;
			this.drawTexturedModalRect(xPosition, yPosition, 188, offY, width, height);
		}
	}

}
