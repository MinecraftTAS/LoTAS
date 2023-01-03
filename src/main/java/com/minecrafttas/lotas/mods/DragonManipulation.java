/**
 * Here is the logic of the dragon manipulation mod:
 *
 * The enderdragon has a net of notes which are chained to build a path that the ender dragon follows. Two nodes are placed fairly high up while the others are just above the ground.
 * After finishing a path of 4 nodes (or in the inital perch 1) the dragon can switch phases. Additionally the nodes have a random height increase of up to 20 blocks where the dragon will fly to.
 * To make the ender dragon optimal we need to skip the high nodes and have the height bonus equal the height of the ender dragon rn. Then we can force a phase switch with rng manipulation.
 * 
 * Controlled bye values (specified in settings class)
 * - forceOptimalDragonPath
 * - forceDragonLandingApproach
 * - forceDragonStrafingPhase
 * 
 * As noted by the @Environment annotations in front of methods, this code works on both client and server.
 * 
 * Every time the clients wants to change the settings it sends a request update packet to the server. #requestEdit
 * The server proceeds sending a update packet to the clients causing them to process the packet on their own. The server processes the packet too. #onServerPayload
 * The clients listener finally saves or loads the playerdata. #onClientPayload
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
 * Main Dragon Manipulation
 * @author Pancake
 */
public class DragonManipulation extends Mod {

	public static DragonManipulation instance;
	
	/**
	 * Initializes the dragon manipulation
	 */
	public DragonManipulation() {
		super(new ResourceLocation("lotas", "dragonmanipulation"));
		instance = this;
	}
	
	/**
	 * Manipulation Settings
	 * @author Pancake
	 */
	public static class Settings {
		
		/**
		 * The dragons flight path will be optimized to end the path quicker allowing a quicker phase switch
		 */
		private boolean forceOptimalDragonPath;
		
		/**
		 * The dragon will be forced into landing approach
		 */
		private boolean forceDragonLandingApproach;
		
		/**
		 * The dragon will be forced into strafing phase (does not work with forceDragonLandingApproach)
		 */
		private boolean forceDragonStrafingPhase;

		/**
		 * Creates a new setting for the dragon manipulation
		 * @param forceOptimalDragonPath Optimize dragon path
		 * @param forceDragonLandingApproach Force landing approach
		 * @param forceDragonStrafingPhase Force strafing phase
		 */
		public Settings(boolean forceOptimalDragonPath, boolean forceDragonLandingApproach, boolean forceDragonStrafingPhase) {
			this.forceOptimalDragonPath = forceOptimalDragonPath;
			this.forceDragonLandingApproach = forceDragonLandingApproach;
			this.forceDragonStrafingPhase = forceDragonStrafingPhase;
		}

		public boolean isForceOptimalDragonPath() { return this.forceOptimalDragonPath; }
		public boolean isForceDragonLandingApproach() { return this.forceDragonLandingApproach; }
		public boolean isForceDragonStrafingPhase() { return this.forceDragonStrafingPhase; }
		
	}
	
	/**
	 * Current manipulation settings or null
	 */
	private Settings currentSettings;

	/**
	 * Returns current manipulation settings
	 * @return Manipulation Settings	
	 */
	public Settings getCurrentSettings() {
		return this.currentSettings;
	}
	
	public boolean isForceOptimalDragonPath() {
		return this.currentSettings == null ? false : this.currentSettings.isForceOptimalDragonPath();
	}

	public boolean isForceDragonLandingApproach() {
		return this.currentSettings == null ? false : this.currentSettings.isForceDragonLandingApproach();
	}

	public boolean isForceDragonStrafingPhase() {
		return this.currentSettings == null ? false : this.currentSettings.isForceDragonStrafingPhase();
	}

	/**
	 * Updates local settings on packet
	 * @param buf Packet Data
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		if (buf.readBoolean()) 
			this.currentSettings = new Settings(buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
		else
			this.currentSettings = null;
	}
	
	/**
	 * Resend when receiving a packet
	 * @param buf Packet Data
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		if (buf.readBoolean()) {
			this.currentSettings = new Settings(buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
			for (ServerPlayer player : this.mcserver.getPlayerList().getPlayers()) {
				// Send packet to client
				FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
				data.writeBoolean(true);
				data.writeBoolean(this.currentSettings.isForceOptimalDragonPath());
				data.writeBoolean(this.currentSettings.isForceDragonLandingApproach());
				data.writeBoolean(this.currentSettings.isForceDragonStrafingPhase());
				this.sendPacketToClient(player, data);
			}
		} else {
			this.currentSettings = null;
			for (ServerPlayer player : this.mcserver.getPlayerList().getPlayers()) {
				// Send packet to client
				FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
				data.writeBoolean(false);
				this.sendPacketToClient(player, data);
			}
		}
	}
	
	/**
	 * Client-Side only settings edit request. Sends a packet to the server contains the new settings
	 * @param settings New settings or null
	 */
	@Environment(EnvType.CLIENT)
	public void requestEdit(Settings settings) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		if (settings != null) {
			buf.writeBoolean(true);
			buf.writeBoolean(settings.isForceOptimalDragonPath());
			buf.writeBoolean(settings.isForceDragonLandingApproach());
			buf.writeBoolean(settings.isForceDragonStrafingPhase());
		} else {
			buf.writeBoolean(false);
		}
		this.sendPacketToServer(buf);
	}
	
}
