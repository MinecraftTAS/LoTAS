/**
 * Here goes the logic of the Savestate Mod.. eventually...
 */
package de.pfannkuchen.lotas.mods;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.gui.StateLoScreen;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;

/**
 * Main Savestate Mod
 * @author Pancake
 */
public class SavestateMod {

	static final ResourceLocation SAVESTATE_MOD_RL = new ResourceLocation("lotas", "savestatemod");
	@Environment(EnvType.CLIENT)
	public Minecraft mc;
	public MinecraftServer mcserver;
	
	// Server-side Todo list
	private boolean savestate = false;
	private int loadstate = -1;
	private int deletestate = -1;
	// Server-side folder structure
	private File savestatesDir;
	private File worldDir;
	// Client-side state count
	private int stateCount;
	
	/**
	 * Saves or Loads when receiving a packet
	 * @param p Incoming Packet
	 */
	@Environment(EnvType.CLIENT)
	public void onClientPacket(ClientboundCustomPayloadPacket p) {
		if (SAVESTATE_MOD_RL.equals(p.getIdentifier())) {
			FriendlyByteBuf buf = p.getData();
			boolean lockOUnlock = buf.readBoolean();
			stateCount = buf.readInt();
			if (lockOUnlock)
				ClientLoTAS.loscreenmanager.setScreen(new StateLoScreen());
			else
				StateLoScreen.allowUnlocking();
		}
	}
	
	/**
	 * Resend when receiving a packet
	 * @param p Incoming Packet
	 */
	public void onServerPacket(ServerboundCustomPayloadPacket p) {
		if (SAVESTATE_MOD_RL.equals(p.getIdentifier())) {
			FriendlyByteBuf buf = p.getData();
			switch (buf.readInt()) {
				case 1:
					savestate = true;
					break;
				case 2:
					loadstate = buf.readInt();
					break;
				case 3:
					deletestate = buf.readInt();
					break;
			}
		}
	}

	/**
	 * Server-Side: Prepares all folders if not already set
	 */
	private void prepareFolders(MinecraftServer mcserver) {
		this.worldDir = mcserver.getWorldPath(LevelResource.ROOT).toFile().getParentFile();
		this.savestatesDir = new File(this.worldDir.getParentFile(), this.worldDir.getName() + " Savestates");
		this.savestatesDir.mkdirs();
	}

	/**
	 * Savestates/Loadstates/Deletestates after the tick
	 */
	public void afterServerTick(MinecraftServer mcserver) {
		// Savestate
		if (this.savestate) {
			savestate(mcserver);
			this.savestate = false;
		}
		// Loadstate
		if (this.loadstate != -1) {
			loadstate(this.loadstate, mcserver);
			this.loadstate = -1;
		}
		// Deletestate
		if (this.deletestate != -1) {
			deletestate(this.deletestate, mcserver);
			this.deletestate = -1;
		}
	}
	
	public void freezeClient(boolean freezeOrNot, MinecraftServer mcserver) {
		mcserver.getPlayerList().getPlayers().forEach(c -> {
			// Freeze Client Packet
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeBoolean(freezeOrNot); // Write True - Lock
			buf.writeInt(stateCount); // Write amount of states too
			c.connection.send(new ClientboundCustomPayloadPacket(SAVESTATE_MOD_RL, buf));
			// Fake Tickrate Packet
			LoTAS.tickadvance.updateTickadvanceStatus(freezeOrNot);
		});
	}
	
	/**
	 * Saves a new state of the world
	 */
	@SuppressWarnings("resource")
	private void savestate(MinecraftServer mcserver) {
		// Freeze Client
		freezeClient(true, mcserver);
		// Save Playerdata
		mcserver.getPlayerList().saveAll();
		// Save Worlds
		mcserver.saveAllChunks(false, true, false);
		// Wait until everything was fully saved
		for (ServerLevel world : mcserver.getAllLevels())
			world.getChunkSource().chunkMap.flushWorker();
		// Prepare Folders
		prepareFolders(mcserver);
		File worldSavestateDir = new File(this.savestatesDir, this.savestatesDir.listFiles().length + "");
		this.stateCount = this.savestatesDir.listFiles().length;
		// Copy full folder
		try {
			FileUtils.copyDirectory(this.worldDir, worldSavestateDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Unfreeze Client
		freezeClient(false, mcserver);
	}
	
	/**
	 * Loads a state of the world
	 * @param i Index to load
	 */
	private void loadstate(int i, MinecraftServer mcserver) {
		
	}

	/**
	 * Deletes a state of the world
	 * @param i Index to delete
	 */
	private void deletestate(int i, MinecraftServer mcserver) {
		// Freeze Client
		freezeClient(true, mcserver);
		// Prepare folders
		prepareFolders(mcserver);
		File worldSavestateDir = new File(savestatesDir, i + "");
		this.stateCount = this.savestatesDir.listFiles().length;
		// Delete Folder if it exists
		try {
			if (worldSavestateDir.exists()) 
				FileUtils.deleteDirectory(worldSavestateDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Re-enumerate all other savestates ahead
		for (File file : this.savestatesDir.listFiles()) {
			int id = Integer.parseInt(file.getName());
			if (id > i) file.renameTo(new File(file.getParentFile(), (id-1) + ""));
		}
		// Unfreeze Client
		freezeClient(false, mcserver);
	}

	/**
	 * Returns the Description of a state or the date of it, if non given
	 * @param index Index to check
	 * @return Description of state
	 */
	@Environment(EnvType.CLIENT)
	public String getSavestateInfo(int index) {
		return "Savestate " + index;
	}
	
	/**
	 * Client-Side only way to check if or how many savestates exists.
	 * @return Whether a state is present to load from
	 */
	@Environment(EnvType.CLIENT)
	public int getStateCount() {
		return stateCount;
	}
	
	/**
	 * Client-Side only state request. Sends a packet to the server contains a save or load int and an index to load
	 * @param state state 1 is save, state 2 is load, state 3 is delete
	 * @param index Index of Loadstate
	 */
	@Environment(EnvType.CLIENT)
	public void requestState(int state, int index) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(state);
		buf.writeInt(index);
		this.mc.getConnection().send(new ServerboundCustomPayloadPacket(SAVESTATE_MOD_RL, buf));
	}
	
}