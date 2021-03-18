package de.pfannekuchen.lotas.savestates;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class LoadstatePacket implements IMessage{
	private boolean isRewind;
	
	public LoadstatePacket() {
		this.isRewind=false;
	}
	public LoadstatePacket(boolean isRewind) {
		this.isRewind=isRewind;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		isRewind=buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(isRewind);
	}
	public boolean isRewind() {
		return isRewind;
	}
}
