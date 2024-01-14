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

import com.minecrafttas.lotas.system.ModSystem.Mod;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

import static com.minecrafttas.lotas.LoTAS.LOGGER;

/**
 * Dupe mod
 *
 * @author Pancake
 */
public class DupeMod extends Mod {
	public DupeMod() {
		super(new ResourceLocation("lotas", "dupemod"));
	}

	/** Copy of the clients local players playerdata */
	@Environment(EnvType.CLIENT)
	private CompoundTag localPlayer;

	/** Copy of all serverside players */
	private final HashMap<ServerPlayer, CompoundTag> onlineClients = new HashMap<>();

	/**
	 * Request dupe by sending a packet to the server
	 * (Clientside only)
	 *
	 * @param saveOLoad Whether player data should be loaded or saved
	 */
	@Environment(EnvType.CLIENT)
	public void requestDupe(boolean saveOLoad) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBoolean(saveOLoad);
		this.sendPacketToServer(buf);
	}

	/**
	 * Trigger dupe and resend packet to all clients when receiving packet
	 *
	 * @param buf Packet
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		boolean saveOLoad = buf.readBoolean();
		List<ServerPlayer> players = this.mcserver.getPlayerList().getPlayers();
		if (saveOLoad) {
			// save all client data in the hash map
			this.onlineClients.clear();
			for (ServerPlayer player : players) {
				// save playerdata
				CompoundTag tag = new CompoundTag();
				player.saveWithoutId(tag);
				this.onlineClients.put(player, tag);

				// send packet to client
				FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
				data.writeBoolean(true);
				data.writeInt(tag.getInt("playerGameType"));
				this.sendPacketToClient(player, data);
			}
		} else
			// load all client data from the hash map
			for (ServerPlayer player : players) {
				CompoundTag tag = this.onlineClients.get(player);
				if (tag == null) {
					LOGGER.warn("No playerdata stored for {}.", player.getName().getString());
					continue;
				}

				if (!tag.getString("Dimension").equals(player.getLevel().dimension().location().toString())) {
					LOGGER.warn("Unable to load playerdata for {} as they are in a different dimension!", player.getName().getString());
					continue;
				}

				if (!player.isAlive()) { // instead of reviving the player just don't load playerdata - this will be multiplayer safe
					LOGGER.warn("Unable to load playerdata for {} as they are not alive.", player.getName().getString());
					continue;
				}

				// load playerdata
				player.load(tag);
				Vec3 pos = player.position();
				player.teleportTo(pos.x(), pos.y(), pos.z());

				// send packet to client
				FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
				data.writeBoolean(false);
				this.sendPacketToClient(player, data);
			}
	}

	/**
	 * Save or load when receiving packet
	 *
	 * @param buf Packet
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
			Vec3 pos = this.mc.player.position();
			this.mc.player.teleportTo(pos.x(), pos.y(), pos.z());
			this.mc.gameMode.setLocalMode(GameType.byId(this.localPlayer.getInt("lotas_playerGameType")));
		}
	}

	/**
	 * Save client data on connect
	 *
	 * @param player Player
	 */
	@Override
	protected void onClientConnect(ServerPlayer player) {
		CompoundTag tag = new CompoundTag();
		player.saveWithoutId(tag);
		this.onlineClients.put(player, tag);
	}

	/**
	 * Clear local data on disconnect
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsideDisconnect() {
		this.localPlayer = null;
	}

}
