package de.pfannkuchen.lotas.mods;

import de.pfannkuchen.lotas.system.ModSystem.Mod;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Tick advance for minecraft.
 * ~ same logic as tickrate changer
 * @author Pancake
 */
public class TickAdvance extends Mod {

	public static TickAdvance instance;

	/**
	 * Initializes the tick advance mod
	 */
	public TickAdvance() {
		super(new ResourceLocation("lotas", "tickadvance"));
		instance = this;
	}

	/**
	 *  Is tick advance enabled
	 */
	private boolean tickadvance;

	/**
	 *  Should tick advance clientside
	 */
	public boolean shouldTickClient;

	/**
	 *  Should tick advance serverside
	 */
	public boolean shouldTickServer;

	/**
	 * Updates the client tickadvance status when receiving a packet
	 * @param buf Packet Data
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		if (buf.readInt() == 0)
			this.tickadvance = buf.readBoolean();
		else
			this.shouldTickClient = true; // Tick the client
	}

	/**
	 * Updates the server tickadvance status and resend when receiving a packet
	 * @param buf Packet Data
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		if (buf.readInt() == 0)
			this.updateTickadvanceStatus(buf.readBoolean());
		else
			this.updateTickadvance();
	}

	@Environment(EnvType.CLIENT)
	@Override
	protected void onClientsideTick() {
		this.shouldTickClient = false;
	}

	/**
	 * Client-side only tickadvance update request. Sends a packet to the server toggeling tickadvance.
	 * @param tickadvance Tickadvance Status
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickadvanceToggle() {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(0); // status update
		buf.writeBoolean(!this.tickadvance); // new status
		this.sendPacketToServer(buf);
	}

	/**
	 * Client-Side only tick advance request. Sends a packet to the server advancing a tick.
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickadvance() {
		if (!this.tickadvance || this.shouldTickClient)
			return;
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(1); // tick update
		this.sendPacketToServer(buf);
	}

	/**
	 * Server-Side only tickadvance update. Sends a packet to all players
	 * @param tickadvance Tickadvance status
	 */
	public void updateTickadvanceStatus(boolean tickadvance) {
		this.tickadvance = tickadvance;
		// Update Tickadvance for all clients
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeInt(0); // status update
			buf.writeBoolean(tickadvance); // new status
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Server-Side only tick advance. Sends a packet to all players
	 */
	public void updateTickadvance() {
		this.shouldTickServer = true;
		// Tick all clients
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeInt(1); // tick update
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Clears local data on disconnect
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsideDisconnect() {
		this.tickadvance = false;
	}

	/**
	 * Updates client data on connect
	 */
	@Override
	protected void onClientConnect(ServerPlayer player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(0); // status update
		buf.writeBoolean(this.tickadvance); // new status
		this.sendPacketToClient(player, buf);
	}

	public boolean isTickadvanceEnabled() {
		return this.tickadvance;
	}

}
