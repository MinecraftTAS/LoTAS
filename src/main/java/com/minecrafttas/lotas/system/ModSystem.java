package com.minecrafttas.lotas.system;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mod manager managing events for mods
 *
 * @author Pancake
 */
public class ModSystem {

	/** Registered mods */
	private static final List<Mod> MODS = new ArrayList<>();

	/**
	 * Register a list of mods
	 *
	 * @param m Mods
	 */
	public static void registerMods(Mod... m) {
		MODS.addAll(Arrays.asList(m));
	}

	// 1: event handlers that update the mod

	public static void onServerLoad(MinecraftServer minecraftServer) {
		for (Mod mod : MODS) {
			mod.mcserver = minecraftServer;
			mod.onServerLoad();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideRenderInitialize(Minecraft minecraft) {
		for (Mod mod : MODS) {
			mod.mc = minecraft;
			mod.onClientsideRenderInitialize();
		}
	}

	// 2: event handlers that check for id

	public static void onServerPayload(ServerboundCustomPayloadPacket buf) {
		for (Mod mod : MODS)
			if (mod.id.equals(buf.identifier))
				mod.onServerPayload(buf.data);
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsidePayload(ClientboundCustomPayloadPacket buf) {
		for (Mod mod : MODS)
			if (mod.id.equals(buf.getIdentifier()))
				mod.onClientsidePayload(buf.getData());
	}

	// 3: simple event handlers

	public static void onInitialize() {
		for (Mod mod : MODS)
			mod.onInitialize();
	}

	public static void onServerTick() {
		for (Mod mod : MODS)
			mod.onServerTick();
	}

	public static void onClientConnect(ServerPlayer player) {
		for (Mod mod : MODS)
			mod.onClientConnect(player);
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideInitialize() {
		for (Mod mod : MODS)
			mod.onClientsideInitialize();
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideShutdown() {
		for (Mod mod : MODS)
			mod.onClientsideShutdown();
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideTick() {
		for (Mod mod : MODS)
			mod.onClientsideTick();
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideGameLoop() {
		for (Mod mod : MODS)
			mod.onClientsideGameLoop();
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsideDisconnect() {
		for (Mod mod : MODS)
			mod.onClientsideDisconnect();
	}

	@Environment(EnvType.CLIENT)
	public static void onClientsidePostRender() {
		for (Mod mod : MODS)
			mod.onClientsidePostRender();
	}

	/**
	 * Hull of a mod containing all events for the mod
	 *
	 * @author Pancake
	 */
	public static abstract class Mod {

		/** Instance of Minecraft */
		@Environment(EnvType.CLIENT)
		protected Minecraft mc;

		/** Instance of server or null */
		protected MinecraftServer mcserver;

		/** Id of the mod */
		protected final ResourceLocation id;

		/**
		 * Initialize a mod
		 *
		 * @param id Id of this mod
		 */
		public Mod(ResourceLocation id) {
			this.id = id;
		}

		/**
		 * Executed after the fabric launcher
		 * (mc and mcserver will still be null!)
		 */
		protected void onInitialize() {

		}

		/**
		 * Executed after the server launches.
		 * (mcserver will be set)
		 */
		protected void onServerLoad() {

		}

		/**
		 * Executed inbetween every tick on the server
		 */
		protected void onServerTick() {

		}

		/**
		 * Executed every time the server receives a custom payload packet.
		 *
		 * @param buf Packet
		 */
		protected void onServerPayload(FriendlyByteBuf buf) {

		}

		/**
		 * Executed if a client connects to the server
		 *
		 * @param player Client connected
		 */
		protected void onClientConnect(ServerPlayer player) {

		}

		/**
		 * Executed after the client initializes. mc is not set yet
		 */
		@Environment(EnvType.CLIENT)
		protected void onClientsideInitialize() {

		}

		/**
		 * Executed after the rendering engine launches.
		 * (mc will be set)
		 */
		@Environment(EnvType.CLIENT)
		protected void onClientsideRenderInitialize() {

		}

		/**
		 * Executed before the JVM stops on the clientside.
		 */
		@Environment(EnvType.CLIENT)
		protected void onClientsideShutdown() {

		}

		/**
		 * Executed every tick of the client.
		 */
		@Environment(EnvType.CLIENT)
		protected void onClientsideTick() {

		}

		/**
		 * Executed every time the clients game logic loops.
		 */
		@Environment(EnvType.CLIENT)
		protected void onClientsideGameLoop() {

		}

		/**
		 * Executed every time the client receives a custom payload packet.
		 *
		 * @param buf Packet
		 */
		@Environment(EnvType.CLIENT)
		protected void onClientsidePayload(FriendlyByteBuf buf) {

		}

		/**
		 * Executed if the client disconnects from a world or server
		 */
		@Environment(EnvType.CLIENT)
		protected void onClientsideDisconnect() {

		}

		/**
		 * Executed every time the client renders a frame
		 */
		@Environment(EnvType.CLIENT)
		protected void onClientsidePostRender() {
			
		}
		
		/**
		 * Send packet to a client
		 *
		 * @param player Player
		 * @param buf Data
		 */
		protected void sendPacketToClient(ServerPlayer player, FriendlyByteBuf buf) {
			player.connection.send(new ClientboundCustomPayloadPacket(this.id, buf));
		}

		/**
		 * Send packet from to server
		 *
		 * @param buf Data
		 */
		@Environment(EnvType.CLIENT)
		protected void sendPacketToServer(FriendlyByteBuf buf) {
			this.mc.getConnection().send(new ServerboundCustomPayloadPacket(this.id, buf));
		}
		
	}

}
