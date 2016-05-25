package nahama.starwoods.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import nahama.starwoods.inventory.ContainerVEExtractor;
import nahama.starwoods.inventory.ContainerVEInjector;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public class MExtractorVE implements IMessage {

	public int ve;

	public MExtractorVE() {}

	public MExtractorVE(int ve) {
		this.ve = ve;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		ve = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(ve);
	}

	public static class Handler implements IMessageHandler<MExtractorVE, IMessage> {

		@Override
		public IMessage onMessage(MExtractorVE message, MessageContext ctx) {
			Container container = Minecraft.getMinecraft().thePlayer.openContainer;
			if (container instanceof ContainerVEExtractor || container instanceof ContainerVEInjector)
				container.updateProgressBar(0, message.ve);
			return null;
		}

	}

}
