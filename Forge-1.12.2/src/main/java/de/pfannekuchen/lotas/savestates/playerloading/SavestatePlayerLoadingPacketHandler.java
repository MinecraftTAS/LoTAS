package de.pfannekuchen.lotas.savestates.playerloading;

import de.pfannekuchen.lotas.savestates.chunkloading.SavestatesChunkControl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Reads the playerdata coming from the server and also applies motion and relative motion from the player
 * 
 * @author ScribbleLP
 *
 */
public class SavestatePlayerLoadingPacketHandler implements IMessageHandler<SavestatePlayerLoadingPacket, IMessage>{

	@Override
	public IMessage onMessage(SavestatePlayerLoadingPacket message, MessageContext ctx) {
		if(ctx.side.isClient()) {
			Minecraft.getMinecraft().addScheduledTask(()->{
				EntityPlayerSP player=Minecraft.getMinecraft().player;
				NBTTagCompound compound = message.getNbtTagCompound();
				player.readFromNBT(compound);
				NBTTagList relmotion=compound.getTagList("RelMotion", 5);
				float rx=relmotion.getFloatAt(0);
				float ry=relmotion.getFloatAt(1);
				float rz=relmotion.getFloatAt(2);
				player.moveForward=rx;
				player.moveVertical=ry;
				player.moveStrafing=rz;
				
				SavestatesChunkControl.keepPlayerInLoadedEntityList(player);
			});
		}
		return null;
	}

}
