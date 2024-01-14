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

import com.minecrafttas.lotas.system.ConfigurationSystem;
import com.minecrafttas.lotas.system.ModSystem.Mod;

import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import static com.minecrafttas.lotas.LoTAS.TICK_ADVANCE;

/**
 * Tickrate changer mod
 *
 * @author Pancake
 */
public class TickrateChanger extends Mod {
	public TickrateChanger() {
		super(new ResourceLocation("lotas", "tickratechanger"));
	}

	/** Array of the most common tickrates available via decrease and increase tickrate keybinds */
	@Environment(EnvType.CLIENT)
	private static final double[] TICKRATES = { 0.5f, 1.0f, 2.0f, 4.0f, 5.0f, 10.0f, 20.0f };


	/** Should the game remember the tickrate in-between different levels */
	private boolean restoreLastSession;
	
	/** Tickrate of the last session, used for restoring tickrate (static for integrated server) */
	private static double lastSessionTickrate = 20.0;
	
	/** Current speed of the game */
	@Getter
	private double tickrate = 20.0, msPerTick = 50.0, gamespeed = 1.0;

	/** Previous game speed, clientside */
	@Environment(EnvType.CLIENT)
	private double prevGamespeed = 1.0;

	/** System time since last tickrate change (in milliseconds) */
	@Environment(EnvType.CLIENT)
	private long systemTimeSinceUpdate = System.currentTimeMillis();

	/** Game time since last tickrate change (in milliseconds) */
	@Environment(EnvType.CLIENT)
	private long gameTime = 0L;

	/**
	 * Initialize tickrate changer mod
	 */
	@Override
	protected void onInitialize() {
		this.restoreLastSession = ConfigurationSystem.getBoolean("tickratechanger_remembertickrate", true);
	}

	/**
	 * Request tickrate update by sendiong a packet to the server updating the tickrate.
	 * (Clientside only)
	 *
	 * @param tickrate Tickrate to update to
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickrateUpdate(double tickrate) {
		// request tickrate update
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeDouble(tickrate);
		this.sendPacketToServer(buf);
	}

	/**
	 * Update server tickrate on packet receive
	 *
	 * @param buf Packet
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		this.updateTickrate(buf.readDouble());
	}

	/**
	 * Update tickrate and send a packet to all players
	 * (Serverside only)
	 *
	 * @param tickrate Tickrate
	 */
	public void updateTickrate(double tickrate) {
		if (tickrate < 0.1)
			return;

		// change tickrate
		lastSessionTickrate = tickrate;
		this.internallyUpdateTickrate(tickrate);

		// update tickrate for all clients
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeDouble(tickrate);
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Internally update tickrate of game
	 *
	 * @param tickrate Tickrate
	 */
	private void internallyUpdateTickrate(double tickrate) {
		this.tickrate = tickrate;
		this.msPerTick = 1000.0 / tickrate;
		this.gamespeed = tickrate / 20;
	}

	/**
	 * Update client tickrate on packet receive
	 *
	 * @param buf Packet Data
	 */
	@Override
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		this.updateGameTime(this.prevGamespeed);
		this.internallyUpdateTickrate(buf.readDouble());
		this.prevGamespeed = this.gamespeed;
	}

	/**
	 * Update game time using gamespeed
	 *
	 * @param gamespeed Speed of game
	 */
	@Environment(EnvType.CLIENT)
	public void updateGameTime(double gamespeed) {
		this.gameTime += (long) ((System.currentTimeMillis() - this.systemTimeSinceUpdate) * gamespeed);
		this.systemTimeSinceUpdate = System.currentTimeMillis();
	}

	/**
	 * Send tickrate to newly connected client
	 *
	 * @param player Client connected
	 */
	@Override
	protected void onClientConnect(ServerPlayer player) {
		// update server tickrate on singleplayer connect
		if (this.mcserver.isSingleplayer() && this.restoreLastSession)
			this.internallyUpdateTickrate(lastSessionTickrate);

		// update client tickrate on connect
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeDouble(this.tickrate);
		this.sendPacketToClient(player, buf);
	}

	/**
	 * Reset tickrate on disconnect
	 */
	@Override @Environment(EnvType.CLIENT)
	public void onClientsideDisconnect() {
		this.internallyUpdateTickrate(20.0);
	}

	/**
	 * Advance game time by millis
	 *
	 * @param millis Milliseconds
	 */
	@Environment(EnvType.CLIENT)
	public void advanceGameTime(long millis) {
		this.gameTime += millis;
		this.systemTimeSinceUpdate = System.currentTimeMillis();
	}

	/**
	 * Get current game time in milliseconds. This is the time that the game would be at if the game was running at 20 tps.
	 * (Clientside only)
	 *
	 * @return Milliseconds passed in game time
	 */
	@Environment(EnvType.CLIENT)
	public long getMilliseconds() {
		// ignore time passed if tick advance is enabled and client is not ticking
		if (TICK_ADVANCE.isTickadvance() && !TICK_ADVANCE.shouldTickClient) {
			this.systemTimeSinceUpdate = System.currentTimeMillis();
			return this.gameTime;
		}

		// update game time
		long gameTimePassed = System.currentTimeMillis() - this.systemTimeSinceUpdate;
		gameTimePassed = (long) (gameTimePassed * this.gamespeed);
		return this.gameTime + gameTimePassed;
	}

	/**
	 * Decrease tickrate to nearest recommended value
	 */
	@Environment(EnvType.CLIENT)
	public void decreaseTickrate() {
		double newTickrate = this.tickrate;
		for (int i = TICKRATES.length - 1; i >= 0; i--) {
			newTickrate = TICKRATES[i];
			if (newTickrate < this.tickrate)
				break;
		}

		this.requestTickrateUpdate(newTickrate);
	}

	/**
	 * Increase tickrate to nearest recommended value
	 */
	@Environment(EnvType.CLIENT)
	public void increaseTickrate() {
		double newTickrate = this.tickrate;
		for (double tickrate2 : TICKRATES) {
			newTickrate = tickrate2;
			if (newTickrate > this.tickrate)
				break;
		}

		this.requestTickrateUpdate(newTickrate);
	}

}
