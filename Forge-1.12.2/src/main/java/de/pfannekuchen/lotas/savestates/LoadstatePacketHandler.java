package de.pfannekuchen.lotas.savestates;

import de.pfannekuchen.lotas.savestates.chunkloading.SavestatesChunkControl;
import de.pfannekuchen.lotas.savestates.exceptions.LoadstateException;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoadstatePacketHandler implements IMessageHandler<LoadstatePacket, IMessage>{

	@Override
	public IMessage onMessage(LoadstatePacket message, MessageContext ctx) {
		if(ctx.side.isServer()) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(()->{
				EntityPlayerMP player=ctx.getServerHandler().player;
				if(!player.canUseCommand(2, "tickrate")) {
					player.sendMessage(new TextComponentString(TextFormatting.RED+"You don't have permission to do that"));
					return;
				}
				try {
					SavestateHandler.loadState();
				} catch (LoadstateException e) {
					player.sendMessage(new TextComponentString(TextFormatting.RED+"Failed to load a savestate: "+e.getMessage()));
					
				} catch (Exception e) {
					player.sendMessage(new TextComponentString(TextFormatting.RED+"Failed to load a savestate: "+e.getCause().toString()));
					e.printStackTrace();
				} finally {
					SavestateHandler.isLoading=false;
				}
			});
		}else {
			Minecraft.getMinecraft().addScheduledTask(()->{
				if(message.isRewind()) {
				}else {
					SavestatesChunkControl.unloadAllClientChunks();
				}
			});
		}
		return null;
	}

}
