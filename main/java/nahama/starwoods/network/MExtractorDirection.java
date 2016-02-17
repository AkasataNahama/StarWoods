package nahama.starwoods.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MExtractorDirection implements IMessage {

	public byte side;
	public int x, y, z;

	public MExtractorDirection() {}

	public MExtractorDirection(byte side, int x, int y, int z) {
		this.side = side;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		side = buf.readByte();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(side);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public static class Handler implements IMessageHandler<MExtractorDirection, IMessage> {

		@Override
		public IMessage onMessage(MExtractorDirection message, MessageContext ctx) {
			ctx.getServerHandler().playerEntity.worldObj.setBlockMetadataWithNotify(message.x, message.y, message.z, message.side, 2);
			return null;
		}

	}

}
