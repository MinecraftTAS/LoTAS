package de.pfannekuchen.lotas.savestates;

import de.pfannekuchen.lotas.savestates.exceptions.SavestateException;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Handles savestate networking
 * @author ScribbleLP
 * @see SavestatePacket
 */
public class SavestatePacketHandler implements IMessageHandler<SavestatePacket, IMessage>{

	@Override
	public IMessage onMessage(SavestatePacket message, MessageContext ctx) {
		if(ctx.side.isServer()) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(()->{
				EntityPlayerMP player=ctx.getServerHandler().player;
				if (!ctx.getServerHandler().player.canUseCommand(2, "tickrate")) {
					player.sendMessage(new TextComponentString(TextFormatting.RED+"You don't have permission to do that"));
					return;
				}
				try {
					SavestateHandler.saveState();
				} catch (SavestateException e) {
					player.sendMessage(new TextComponentString(TextFormatting.RED+"Failed to create a savestate: "+e.getMessage()));
					
				} catch (Exception e) {
					player.sendMessage(new TextComponentString(TextFormatting.RED+"Failed to create a savestate: "+e.getCause().toString()));
					e.printStackTrace();
				}finally {
					SavestateHandler.isSaving=false;
				}
			});
		}else {
			Minecraft mc=Minecraft.getMinecraft();
			mc.addScheduledTask(()->{
				if(!(mc.currentScreen instanceof GuiSavestateSavingScreen)) {
					mc.displayGuiScreen(new GuiSavestateSavingScreen());
				}else {
					mc.displayGuiScreen(null);
				}
			});
		}
		return null;
	}

}
