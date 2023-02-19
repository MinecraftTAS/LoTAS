/**
 * Here is the logic of the dupe mod:
 *
 * As noted by the @Environment annotations in front of methods, this code works on both client and server.
 *
 * Every time the clients wants to load or save playerdata it sends a request playerdata update packet to the server. #requestDupe
 * The server proceeds sending a update packet to the clients causing them to process the packet on their own. The server processes the packet too. #onServerPacket
 * The clients listener finally saves or loads the playerdata. #onClientPayload
 */
package com.minecrafttas.lotas.mods;

import java.util.HashMap;
import java.util.List;

import com.minecrafttas.lotas.LoTAS;
import com.minecrafttas.lotas.system.ModSystem.Mod;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

/**
 * Main dupe mod
 * @author Pancake
 */
public class DupeMod extends Mod {

	public static DupeMod instance;

	/**
	 * Initializes the dupe mod
	 */
	public DupeMod() {
		super(new ResourceLocation("lotas", "dupemod"));
		instance = this;
	}

	/**
	 * A copy of the clients local players playerdata
	 */
	@Environment(EnvType.CLIENT)
	private CompoundTag localPlayer;

	/**
	 * A copy of all serverside players
	 */
	private HashMap<ServerPlayer, CompoundTag> onlineClients = new HashMap<>();

	/**
	 * Saves or loads when receiving a packet
	 * @param buf Packet Data
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		boolean saveOLoad = buf.readBoolean();
		if (saveOLoad) {
			this.localPlayer = new CompoundTag();
			this.mc.player.saveWithoutId(this.localPlayer);
			this.localPlayer.putInt("lotas_playerGameType", buf.readInt());
		} else if (this.localPlayer != null) {
			this.mc.player.load(this.localPlayer);
			this.mc.player.teleportTo(this.mc.player.x, this.mc.player.y, this.mc.player.z);
			this.mc.gameMode.setLocalMode(GameType.byId(this.localPlayer.getInt("lotas_playerGameType")));
		}
	}

	/**
	 * Resend when receiving a packet
	 * @param buf Packet Data
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		boolean saveOLoad = buf.readBoolean();
		List<ServerPlayer> players = this.mcserver.getPlayerList().getPlayers();
		if (saveOLoad) {
			// Save all client data in the hash map
			this.onlineClients.clear();
			for (ServerPlayer player : players) {
				// Save playerdata
				CompoundTag tag = new CompoundTag();
				player.saveWithoutId(tag);
				this.onlineClients.put(player, tag);

				// Send packet to client
				FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
				data.writeBoolean(saveOLoad);
				data.writeInt(tag.getInt("playerGameType"));
				this.sendPacketToClient(player, data);
			}
		} else
			// Load all client data from the hash map
			for (ServerPlayer player : players) {
				CompoundTag tag = this.onlineClients.get(player);
				if (tag == null) {
					LoTAS.LOGGER.warn("No playerdata stored for {}.", player.getName().getString());
					continue;
				}

				//# 1.16.1
//$$				if (!tag.getString("Dimension").equals(player.getLevel().dimension().location().toString())) {
				//# def
//$$				if (tag.getInt("Dimension") != player.getLevel().getDimension().getType().getId()) {
				//# end
					LoTAS.LOGGER.warn("Unable to load playerdata for {} as they are in a different dimension!", player.getName().getString());
					continue;
				}

				if (!player.isAlive()) { // Instead of reviving the player just don't load playerdata - this will be multiplayer safe
					LoTAS.LOGGER.warn("Unable to load playerdata for {} as they are not alive.", player.getName().getString());
					continue;
				}

				// Load playerdata
				player.load(tag);
				player.teleportTo(player.x, player.y, player.z);

				// Send packet to client
				FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
				data.writeBoolean(saveOLoad);
				this.sendPacketToClient(player, data);
			}
	}

	/**
	 * Client-Side only dupe request. Sends a packet to the server contains a save or load boolean
	 * @param saveOLoad Whether player data should be loaded or saved
	 */
	@Environment(EnvType.CLIENT)
	public void requestDupe(boolean saveOLoad) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBoolean(saveOLoad);
		this.sendPacketToServer(buf);
	}

	/**
	 * Clears local data on disconnect
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsideDisconnect() {
		this.localPlayer = null;
	}

	/**
	 * Updates client data on connect
	 */
	@Override
	protected void onClientConnect(ServerPlayer player) {
		// Save player
		CompoundTag tag = new CompoundTag();
		player.saveWithoutId(tag);
		this.onlineClients.put(player, tag);
	}

}
