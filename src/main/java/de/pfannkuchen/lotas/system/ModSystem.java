package de.pfannkuchen.lotas.system;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Mod Manager managing different mods
 * @author Pancake
 *
 */
public class ModSystem {

	// @formatter:off
	private static Mod[] mods = {

	};
	// @formatter:on

	// 1: event handlers that update the mod

	public static void onServerLoad(MinecraftServer minecraftServer) {
		for (Mod mod : mods) {
			mod.mcserver = minecraftServer;
			mod.onServerLoad();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideRenderInitialize(Minecraft minecraft) {
		for (Mod mod : mods) {
			mod.mc = minecraft;
			mod.onClientsideRenderInitialize();
		}
	}

	// 2: event handlers that check for id

	public static void onServerPayload(ServerboundCustomPayloadPacket buf) {
		for (Mod mod : mods)
			if (mod.id.equals(buf.getIdentifier()))
				mod.onServerPayload(buf.getData());
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsidePayload(ClientboundCustomPayloadPacket buf) {
		for (Mod mod : mods)
			if (mod.id.equals(buf.getIdentifier()))
				mod.onClientsidePayload(buf.getData());
	}

	// 3: simple event handlers

	public static void onInitialize() {
		for (Mod mod : mods)
			mod.onInitialize();
	}

	public static void onServerTick() {
		for (Mod mod : mods)
			mod.onServerTick();
	}

	public static void onClientConnect(ServerPlayer player) {
		for (Mod mod : mods)
			mod.onClientConnect(player);
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideInitialize() {
		for (Mod mod : mods)
			mod.onClientsideInitialize();
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideShutdown() {
		for (Mod mod : mods)
			mod.onClientsideShutdown();
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideTick() {
		for (Mod mod : mods)
			mod.onClientsideTick();
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideGameLoop() {
		for (Mod mod : mods)
			mod.onClientsideGameLoop();
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideDisconnect() {
		for (Mod mod : mods)
			mod.onClientsideDisconnect();
	}

	/**
	 * Hull of a mod containing events for the mod
	 * @author Pancake
	 */
	public static abstract class Mod {

		/**
		 * Instance of Minecraft
		 */
		@Environment(EnvType.CLIENT)
		protected Minecraft mc;

		/**
		 * Instance of Server or null
		 */
		protected MinecraftServer mcserver;

		/**
		 * Id of the mod
		 */
		private ResourceLocation id;

		/**
		 * Initializes a mod
		 * @param id Id of the mod
		 */
		public Mod(ResourceLocation id) {
			this.id = id;
		}

		/**
		 * Executed after the fabric launcher. Mc and mcserver will still be null
		 */
		public void onInitialize() {

		}

		/**
		 * Executed after the server launches. This will set mcserver
		 */
		public void onServerLoad() {

		}

		/**
		 * Executed inbetween every tick on the server
		 * @param server Server Instance
		 */
		public void onServerTick() {

		}

		/**
		 * Executed every time the server receives a custom payload packet.
		 * @param buf Packet
		 */
		public void onServerPayload(FriendlyByteBuf buf) {

		}

		/**
		 * Executed if a client connects to the server
		 * @param player Client connected
		 */
		public void onClientConnect(ServerPlayer player) {

		}

		/**
		 * Executed after the client initializes. mc is not set yet
		 */
		@Environment(EnvType.CLIENT)
		public void onClientsideInitialize() {

		}

		/**
		 * Executed after the rendering engine launches. mc will be set here
		 */
		@Environment(EnvType.CLIENT)
		public void onClientsideRenderInitialize() {

		}

		/**
		 * Executed before the JVM stops on the clientside.
		 */
		@Environment(EnvType.CLIENT)
		public void onClientsideShutdown() {

		}

		/**
		 * Executed every tick of the client.
		 */
		@Environment(EnvType.CLIENT)
		public void onClientsideTick() {

		}

		/**
		 * Executed every time the clients game logic loops.
		 */
		@Environment(EnvType.CLIENT)
		public void onClientsideGameLoop() {

		}

		/**
		 * Executed every time the client receives a custom payload packet.
		 * @param buf Packet
		 */
		@Environment(EnvType.CLIENT)
		public void onClientsidePayload(FriendlyByteBuf buf) {

		}

		/**
		 * Executed if the client disconnects from a world or server
		 */
		@Environment(EnvType.CLIENT)
		public void onClientsideDisconnect() {

		}

	}

}
