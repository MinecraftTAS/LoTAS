package de.pfannkuchen.lotas.mods;

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

	static final ResourceLocation TICK_ADVANCE_RL = new ResourceLocation("lotas", "tickadvance");
	static final ResourceLocation TICK_RL = new ResourceLocation("lotas", "tick");
	@Environment(EnvType.CLIENT)
	public Minecraft mc;
	public MinecraftServer mcserver;
	
	// Is Tick Advance enabled
	private boolean tickadvance;
	// Should tick advance
	public boolean shouldTick;
	
	/**
	 * Updates the Client tickadvance status when receiving a packet
	 */
	@Environment(EnvType.CLIENT)
	public void onClientPacket(ClientboundCustomPayloadPacket p) {
		if (TICK_ADVANCE_RL.equals(p.getIdentifier())) 
			this.tickadvance = p.getData().readBoolean();
		if (TICK_RL.equals(p.getIdentifier())) 
			this.shouldTick = true; // Tick the Client
	}
	
	/**
	 * Updates the Server tickadvance status and resend when receiving a packet
	 */
	public void onServerPacket(ServerboundCustomPayloadPacket p) {
		if (TICK_ADVANCE_RL.equals(p.getIdentifier())) 
			this.updateTickadvanceStatus(p.getData().readBoolean());
		if (TICK_RL.equals(p.getIdentifier())) 
			this.updateTickadvance();
	}
	

	/**
	 * Client-side only tick method
	 */
	@Environment(EnvType.CLIENT)
	public void onTick(Minecraft mc) {
		this.shouldTick = false;
	}
	
	/**
	 * Client-Side only tickadvance update request. Sends a packet to the server toggeling tickadvance.
	 * @param tickadvance Tickadvance Status
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickadvanceToggle() {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBoolean(!this.tickadvance);
		this.mc.getConnection().send(new ServerboundCustomPayloadPacket(TICK_ADVANCE_RL, buf));
	}
	
	/**
	 * Client-Side only tick advance request. Sends a packet to the server advancing a tick.
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickadvance() {
		if (!this.tickadvance || this.shouldTick) return;
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBoolean(false);
		this.mc.getConnection().send(new ServerboundCustomPayloadPacket(TICK_RL, buf));
	}
	
	/**
	 * Server-Side only tickadvance update. Sends a packet to all players
	 * @param tickadvance Tickadvance status
	 */
	public void updateTickadvanceStatus(boolean tickadvance) {
		this.tickadvance = tickadvance;
		// Update Tickadvance for all Clients
		this.mcserver.getPlayerList().getPlayers().forEach(c -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeBoolean(tickadvance);
			c.connection.send(new ClientboundCustomPayloadPacket(TICK_ADVANCE_RL, buf));
		});
	}
	
	/**
	 * Server-Side only tick advance. Sends a packet to all players
	 */
	public void updateTickadvance() {
		this.shouldTick = true;
		// Tick all Clients
		this.mcserver.getPlayerList().getPlayers().forEach(c -> {
			c.connection.send(new ClientboundCustomPayloadPacket(TICK_RL, new FriendlyByteBuf(Unpooled.buffer())));
		});
	}
	
	// Place Getters here to not confuse with public variables that shall not be set
	
	public boolean isTickadvanceEnabled() {
		return this.tickadvance;
	}
	
}
