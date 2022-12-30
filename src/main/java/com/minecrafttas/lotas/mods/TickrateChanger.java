/**
 * Here is the logic of the tickrate changer:
 *
 * As noted by the @Environment annotations in front of methods, this code works on both client and server.
 *
 * Every time the clients wants to change the tickrate it sends a request tickrate change packet to the server. #requestTickrateUpdate ~~> #requestTickrateUpdate
 * The server proceeds by changing it's tickrate and sends a tickrate change packet to the client from it's listener. #onServerPacket -> #updateTickrate
 * The clients listener finally updates the client tickrate too. #onClientPacket -> #internallyUpdateTickrate
 */
package com.minecrafttas.lotas.mods;

import com.minecrafttas.lotas.system.ModSystem.Mod;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Main tickrate changer
 * @author Pancake
 */
public class TickrateChanger extends Mod {

	public static TickrateChanger instance;

	/**
	 * Initializes the tickrate changer
	 */
	public TickrateChanger() {
		super(new ResourceLocation("lotas", "tickratechanger"));
		instance = this;
	}

	/**
	 * Tickrate of the game
	 */
	private double tickrate = 20.0;

	/**
	 * Tickrate of the game stored in milliseconds per tick
	 */
	private double msPerTick = 50.0;

	/**
	 * Tickrate of the game stored in game speed percentage
	 */
	private double gamespeed = 1.0;

	/**
	 * Array of the most common tickrates available via decrease and increase tickrate keybinds
	 */
	@Environment(EnvType.CLIENT)
	private double[] tickrates = { 0.5f, 1.0f, 2.0f, 4.0f, 5.0f, 10.0f, 20.0f };

	/**
	 * System time in milliseconds since last tickrate change
	 */
	@Environment(EnvType.CLIENT)
	private long timeSinceTC = System.currentTimeMillis();

	/**
	 * Game time in milliseconds since last tickrate change
	 */
	@Environment(EnvType.CLIENT)
	private long fakeTimeSinceTC = System.currentTimeMillis();

	/**
	 * Updates the Client tickrate when receiving a packet
	 * @param buf Packet Data
	 */
	@Override
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		this.internallyUpdateTickrate(buf.readDouble());
		// Update the local time
		long time = System.currentTimeMillis() - this.timeSinceTC;
		this.fakeTimeSinceTC += time * this.gamespeed;
		this.timeSinceTC = System.currentTimeMillis();
	}

	/**
	 * Updates the Server tickrate and resend when receiving a packet
	 * @param buf Packet Data
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		this.updateTickrate(buf.readDouble());
	}

	/**
	 * Client-Side only tickrate update request. Sends a packet to the server updating the tickrate.
	 * @param tickrate Tickrate to update to
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickrateUpdate(double tickrate) {
		// Request tickrate update
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeDouble(tickrate);
		this.sendPacketToServer(buf);
	}

	/**
	 * Server-Side only tickrate update. Sends a packet to all players
	 * @param tickrate Tickrate to update to
	 */
	public void updateTickrate(double tickrate) {
		if (tickrate < 0.1)
			return;
		this.internallyUpdateTickrate(tickrate);
		// Update Tickrate for all clients
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeDouble(tickrate);
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Internally update the tickrate for the game
	 * @param tickrate Tickrate to update to
	 */
	private void internallyUpdateTickrate(double tickrate) {
		this.tickrate = tickrate;
		this.msPerTick = 1000.0 / tickrate;
		this.gamespeed = tickrate / 20;
	}

	/**
	 * Client-side only slowed down millisecond counter
	 * @return
	 */
	@Environment(EnvType.CLIENT)
	public long getMilliseconds() {
		long time = System.currentTimeMillis() - this.timeSinceTC;
		time *= this.gamespeed;
		return this.fakeTimeSinceTC + time;
	}

	/**
	 * Clears local data on disconnect
	 */
	@Override
	@Environment(EnvType.CLIENT)
	public void onClientsideDisconnect() {
		this.internallyUpdateTickrate(20.0);
	}

	@Override
	protected void onClientConnect(ServerPlayer player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeDouble(this.tickrate);
		this.sendPacketToClient(player, buf);
	}

	/**
	 * Decreases the tickrate to the nearest recommended value
	 */
	@Environment(EnvType.CLIENT)
	public void decreaseTickrate() {
		double newTickrate = this.tickrate;
		for (int i = this.tickrates.length - 1; i >= 0; i--) {
			newTickrate = this.tickrates[i];
			if (newTickrate < this.tickrate)
				break;
		}

		this.requestTickrateUpdate(newTickrate);
	}

	/**
	 * Increases the tickrate to the nearest recommended value
	 */
	@Environment(EnvType.CLIENT)
	public void increaseTickrate() {
		double newTickrate = this.tickrate;
		for (double tickrate2 : this.tickrates) {
			newTickrate = tickrate2;
			if (newTickrate > this.tickrate)
				break;
		}

		this.requestTickrateUpdate(newTickrate);
	}

	public double getTickrate() {
		return this.tickrate;
	}

	public double getMsPerTick() {
		return this.msPerTick;
	}

	public double getGamespeed() {
		return this.gamespeed;
	}

}
