/**
 * Here is the logic of the Tickrate Changer:
 * 
 * As noted by the @Environment annotations in front of methods, this code works on both client and server.
 * 
 * Every time the clients wants to change the tickrate it sends a Request Tickrate Change Packet to the server. #requestTickrateUpdate ~~> #requestTickrateUpdate
 * The server proceeds by changing it's tickrate and sends a tickrate change packet to the client from it's listener. #onServerPacket -> #updateTickrate
 * The clients listener finally updates the client tickrate too. #onClientPacket -> #internallyUpdateTickrate
 */
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
 * Main Tickrate Changer
 * @author Pancake
 */
public class TickrateChanger {

	private static final ResourceLocation TICKRATE_CHANGER_RL = new ResourceLocation("lotas", "tickratechanger");
	@Environment(EnvType.CLIENT)
	public Minecraft mc;
	public MinecraftServer mcserver;
	
	// Tickrate Variables in various formats
	private double tickrate;
	private long msPerTick;
	private double gamespeed;
	
	/**
	 * Updates the Client tickrate when receiving a packet
	 */
	@Environment(EnvType.CLIENT)
	public void onClientPacket(ClientboundCustomPayloadPacket p) {
		if (TICKRATE_CHANGER_RL.equals(p.getIdentifier())) internallyUpdateTickrate(p.getData().readDouble());
	}
	
	/**
	 * Updates the Server tickrate and resend when receiving a packet
	 */
	public void onServerPacket(ServerboundCustomPayloadPacket p) {
		if (TICKRATE_CHANGER_RL.equals(p.getIdentifier())) updateTickrate(p.getData().readDouble());
	}
	
	
	/**
	 * Client-Side only tickrate update request. Sends a packet to the server updating the tickrate.
	 * @param tickrate Tickrate to update to
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickrateUpdate(double tickrate) {
		System.out.println("test");
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeDouble(tickrate);
		ServerboundCustomPayloadPacket p = new ServerboundCustomPayloadPacket(TICKRATE_CHANGER_RL, buf);
		mc.getConnection().send(p);
	}
	
	/**
	 * Server-Side only tickrate update. Sends a packet to all players
	 * @param tickrate Tickrate to update to
	 */
	public void updateTickrate(double tickrate) {
		if (tickrate < 0.1) return;
		internallyUpdateTickrate(tickrate);
		// Update Tickrate for all Clients
		mcserver.getPlayerList().getPlayers().forEach(c -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeDouble(tickrate);
			ClientboundCustomPayloadPacket p = new ClientboundCustomPayloadPacket(TICKRATE_CHANGER_RL, buf);
			c.connection.send(p);
		});
	}
	
	/**
	 * Internally update the tickrate for the game
	 * @param tickrate Tickrate to update to
	 */
	private void internallyUpdateTickrate(double tickrate) {
		System.out.println("Updating Tickrate to " + tickrate + "!");
		this.tickrate = tickrate;
		this.msPerTick = (long) (1000L / tickrate);
		this.gamespeed = tickrate / 20;
	}

	// Place Getters here to not confuse with public variables that shall not be set
	
	public double getTickrate() {
		return this.tickrate;
	}

	public long getMsPerTick() {
		return this.msPerTick;
	}

	public double getGamespeed() {
		return this.gamespeed;
	}
	
}
