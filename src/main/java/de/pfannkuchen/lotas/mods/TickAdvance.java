package de.pfannkuchen.lotas.mods;

import de.pfannkuchen.lotas.gui.widgets.TickrateChangerLoWidget;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

/**
 * Tick Advance for Minecraft.
 * ~ same logic as Tickrate Changer
 * @author Pancake
 */
public class TickAdvance {

	private static final ResourceLocation TICK_ADVANCE_RL = new ResourceLocation("lotas", "tickadvance");
	@Environment(EnvType.CLIENT)
	public Minecraft mc;
	public MinecraftServer mcserver;
	
	// Is Tick Advance enabled
	private boolean tickadvance;
	
	/**
	 * Updates the Client tickadvance status when receiving a packet
	 */
	@Environment(EnvType.CLIENT)
	public void onClientPacket(ClientboundCustomPayloadPacket p) {
		if (TICK_ADVANCE_RL.equals(p.getIdentifier())) {
			tickadvance = p.getData().readBoolean();
			// Update the Tickrate Changer LoWidget
			TickrateChangerLoWidget.updateTickAdvance(tickadvance);;
		}
	}
	
	/**
	 * Updates the Server tickadvance status and resend when receiving a packet
	 */
	public void onServerPacket(ServerboundCustomPayloadPacket p) {
		if (TICK_ADVANCE_RL.equals(p.getIdentifier())) updateTickadvance(p.getData().readBoolean());
	}
	
	
	/**
	 * Client-Side only tickadvance update request. Sends a packet to the server toggeling tickadvance.
	 * @param tickadvance Tickadvance Status
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickadvance(boolean tickadvance) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBoolean(tickadvance);
		ServerboundCustomPayloadPacket p = new ServerboundCustomPayloadPacket(TICK_ADVANCE_RL, buf);
		mc.getConnection().send(p);
	}
	
	/**
	 * Server-Side only tickadvance update. Sends a packet to all players
	 * @param tickadvance Tickadvance status
	 */
	public void updateTickadvance(boolean tickadvance) {
		this.tickadvance = tickadvance;
		// Update Tickadvance for all Clients
		mcserver.getPlayerList().getPlayers().forEach(c -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeBoolean(tickadvance);
			ClientboundCustomPayloadPacket p = new ClientboundCustomPayloadPacket(TICK_ADVANCE_RL, buf);
			c.connection.send(p);
		});
	}
	
	// Place Getters here to not confuse with public variables that shall not be set
	
	public boolean isTickadvance() {
		return this.tickadvance;
	}
	
}
