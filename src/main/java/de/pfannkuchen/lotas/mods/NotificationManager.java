package de.pfannkuchen.lotas.mods;

import de.pfannkuchen.lotas.ClientLoTAS;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

/**
 * The notification manager manages notifications
 * @author Pancake
 */
public class NotificationManager {

	static final ResourceLocation NOTIFICATION_MANAGER_RL = new ResourceLocation("lotas", "notificationmanager");
	@Environment(EnvType.CLIENT)
	public Minecraft mc;
	public MinecraftServer mcserver;
	
	/**
	 * Adds notifications when receiving a packet
	 * @param p Incoming packet
	 */
	@Environment(EnvType.CLIENT)
	public void onClientPacket(ClientboundCustomPayloadPacket p) {
		if (NOTIFICATION_MANAGER_RL.equals(p.getIdentifier())) {
			ClientLoTAS.loscreenmanager.addNotification(Component.literal(p.getData().readUtf()));
		}
	}
	
	/**
	 * Resend when receiving a packet
	 * @param p Incoming packet
	 */
	public void onServerPacket(ServerboundCustomPayloadPacket p) {
		if (NOTIFICATION_MANAGER_RL.equals(p.getIdentifier())) {
			String notification = p.getData().readUtf();
			this.mcserver.getPlayerList().getPlayers().forEach(c -> {
				FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
				buf.writeUtf(notification);
				c.connection.send(new ClientboundCustomPayloadPacket(NOTIFICATION_MANAGER_RL, buf));
			});
		}
	}
	
	/**
	 * Requests a notification to be added
	 * @param notification Notification String
	 */
	@Environment(EnvType.CLIENT)
	public void requestNotification(String notification) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeUtf(notification);
		this.mc.getConnection().send(new ServerboundCustomPayloadPacket(NOTIFICATION_MANAGER_RL, buf));
	}
	
}
