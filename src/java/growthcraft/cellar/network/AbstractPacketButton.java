package growthcraft.cellar.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import net.minecraft.util.BlockPos;

public abstract class AbstractPacketButton extends AbstractPacket
{
	public BlockPos.MutableBlockPos pos;

	public AbstractPacketButton() {}

	public AbstractPacketButton(BlockPos p_pos)
	{
		this.pos = new BlockPos.MutableBlockPos();
		this.pos.set(p_pos.getX(), p_pos.getY(), p_pos.getZ());
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(pos.getX());
		buffer.writeInt(pos.getY());
		buffer.writeInt(pos.getZ());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		final int xCoord = buffer.readInt();
		final int yCoord = buffer.readInt();
		final int zCoord = buffer.readInt();
		pos.set(xCoord, yCoord, zCoord);
	}
}
