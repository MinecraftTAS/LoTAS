package de.pfannekuchen.lotas.savestates.playerloading;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SavestatePlayerLoadingPacket implements IMessage{
	private NBTTagCompound compound;

	public SavestatePlayerLoadingPacket() {
	}
	public SavestatePlayerLoadingPacket(NBTTagCompound nbttagcompound) {
		compound=nbttagcompound;
	};
	
	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer buffi=new PacketBuffer(buf);
		try {
			compound=buffi.readCompoundTag();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer buffi=new PacketBuffer(buf);
		buffi.writeCompoundTag(compound);
	}
	
	public NBTTagCompound getNbtTagCompound() {
        return compound;
	}
	
}
