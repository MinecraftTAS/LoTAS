/**
 * Here is the logic of the dupe mod:
 *
 * As noted by the @Environment annotations in front of methods, this code works on both client and server.
 *
 * Every time the clients wants to load or save playerdata it sends a request playerdata update packet to the server. #requestDupe
 * The server proceeds sending a update packet to the clients causing them to process the packet on their own. The server processes the packet too. #onServerPacket
 * The clients listener finally saves or loads the playerdata. #onClientPacket
 */
package de.pfannkuchen.lotas.mods;

import java.util.HashMap;
import java.util.List;

import de.pfannkuchen.lotas.LoTAS;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Main dupe mod
 * @author Pancake
 */
public class DupeMod {

	static final ResourceLocation DUPE_MOD_RL = new ResourceLocation("lotas", "dupemod");
	@Environment(EnvType.CLIENT)
	public Minecraft mc;
	public MinecraftServer mcserver;

	@Environment(EnvType.CLIENT)
	private CompoundTag localPlayer;
	private HashMap<ServerPlayer, CompoundTag> onlineClients = new HashMap<>();

	/**
	 * Saves or loads when receiving a packet
	 * @param p Incoming packet
	 */
	@Environment(EnvType.CLIENT)
	public void onClientPacket(ClientboundCustomPayloadPacket p) {
		if (DUPE_MOD_RL.equals(p.getIdentifier())) {
			boolean saveOLoad = p.getData().readBoolean();
			if (saveOLoad) {
				this.localPlayer = new CompoundTag();
				this.mc.player.saveWithoutId(this.localPlayer);
			} else {
				if (this.localPlayer != null)
					this.mc.player.load(this.localPlayer);
			}
		}
	}

	/**
	 * Resend when receiving a packet
	 * @param p Incoming packet
	 */
	public void onServerPacket(ServerboundCustomPayloadPacket p) {
		if (DUPE_MOD_RL.equals(p.getIdentifier())) {
			boolean saveOLoad = p.getData().readBoolean();
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
					FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
					buf.writeBoolean(saveOLoad);
					player.connection.send(new ClientboundCustomPayloadPacket(DUPE_MOD_RL, buf));
				}
			} else {
				// Load all client data from the hash map
				for (ServerPlayer player : players) {
					CompoundTag tag = this.onlineClients.get(player);
					if (tag != null) {
						if (!tag.getString("Dimension").equals(player.getLevel().dimension().location().toString())) {
							System.out.println("Unable to load playerdata for " + player.getName().getString() + " as they are in a different dimension!");
							continue;
						}

						if (!player.isAlive()) { // Instead of reviving the player just don't load playerdata - this will be multiplayer safe
							System.out.println("Unable to load playerdata for " + player.getName().getString() + " as they are not alive.");
							continue;
						}

						// Load playerdata
						player.load(tag);

						// Send packet to client
						FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
						buf.writeBoolean(saveOLoad);
						player.connection.send(new ClientboundCustomPayloadPacket(DUPE_MOD_RL, buf));
					}
				}
			}
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
		this.mc.getConnection().send(new ServerboundCustomPayloadPacket(DUPE_MOD_RL, buf));
		LoTAS.LOGGER.info(this.mc.player.getName().getString() + (saveOLoad ? " saved playerdata" : " loaded playerdata"));
	}

	/**
	 * Clears local data on disconnect
	 */
	@Environment(EnvType.CLIENT)
	public void onDisconnect() {
		this.localPlayer = null;
	}

	/**
	 * Updates client data on connect
	 */
	public void onConnect(ServerPlayer c) {
		// Save player
		CompoundTag tag = new CompoundTag();
		c.saveWithoutId(tag);
		this.onlineClients.put(c, tag);
	}

}
