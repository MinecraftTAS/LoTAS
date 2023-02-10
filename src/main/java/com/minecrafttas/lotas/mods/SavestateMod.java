/**
 * Here is the logic of the savestate mod..
 *
 * The class 'state' represents a savestate. It contains some data such as name, timestamp and more.
 * There is a list of states, which is being synchronized between the client and the server whenever a state action occurs (save load delete)
 *
 * in onClientsidePayload the client reacts to the server, once it sends a packet. The packet contains all states of a server.
 *
 * The client can request and action in requestState().
 */
package com.minecrafttas.lotas.mods;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.minecrafttas.lotas.LoTAS;
import com.minecrafttas.lotas.system.ModSystem.Mod;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Main savestate mod
 * @author Pancake
 */
public class SavestateMod extends Mod {

	public static SavestateMod instance;
	
	/**
	 * Initializes the savestate mod
	 * @param id
	 */
	public SavestateMod() {
		super(new ResourceLocation("lotas", "savestatemod"));
		instance = this;
	}

	// Server-side Todo list
	private String doSavestate = null;
	private int doLoadstate = -1;
	private int doDeletestate = -1;
	
	// Server-side folder structure
	private File savestatesDir;
	private File worldDir;
	
	// Mirrored State List
	private State[] states = {};

	/**
	 * Client-Side only state request. Sends a packet to the server contains a save or load int and an index to load
	 * @param state state 0 is save, state 1 is load, state 2 is delete
	 * @param index Index of Load/Deletestate
	 * @param name Name of the Savestate
	 */
	@Environment(EnvType.CLIENT)
	public void requestState(int state, int index, String name) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(state);
		if (state == 0)
			buf.writeUtf(name);
		else
			buf.writeInt(index);
		this.sendPacketToServer(buf);
	}
	
	/**
	 * Triggers a Load/delete/savestate when a client packet is incoming and then resends the packet to all clients.
	 * @param buf Packet Data
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		// The Server has to be in tickrate zero AFTER THE ACTION for this to work
		TickAdvance.instance.lock = true;
		TickAdvance.instance.updateTickadvanceStatus(false);
		switch (buf.readInt()) {
			case 0:
				this.doSavestate = buf.readUtf();
				break;
			case 1:
				this.doLoadstate = buf.readInt();
				break;
			case 2:
				this.doDeletestate = buf.readInt();
				break;
		}
	}

	/**
	 * Savestates/loadstates/deletestates after the tick
	 */
	@Override
	protected void onServerTick() {
		// Savestate
		if (this.doSavestate != null) {
			this.savestate();
			this.doSavestate = null;
		}
		// Loadstate
		if (this.doLoadstate != -1) {
			this.loadstate(this.doLoadstate);
			this.doLoadstate = -1;
		}
		// Deletestate
		if (this.doDeletestate != -1) {
			this.deletestate(this.doDeletestate);
			this.doDeletestate = -1;
		}
		TickAdvance.instance.lock = false;
	}
	
	/**
	 * Server-Side: Prepares all folders if not already set
	 */
	private void prepareFolders() {
		this.worldDir = this.mcserver.getStorageSource().getBaseDir().resolve(this.mcserver.getLevelIdName()).toFile();
		this.savestatesDir = new File(this.worldDir.getParentFile(), this.worldDir.getName() + " Savestates");
		if (!this.savestatesDir.exists()) {
			this.states = new State[0];
			this.savestatesDir.mkdirs();
			try {
				Files.write(new File(this.savestatesDir, "states.dat").toPath(), new FriendlyByteBuf(Unpooled.buffer()).writeVarInt(0).array(), StandardOpenOption.CREATE_NEW);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sends all states to the client
	 */
	public void sendStates() {
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeInt(this.states.length);
			for (State s : this.states)
				buf.writeByteArray(s.serialize());
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Saves a new state of the world
	 */
	@SuppressWarnings("resource")
	private void savestate() {
		// Enable tickrate zero
		TickAdvance.instance.updateTickadvanceStatus(true);
		// Save Playerdata
		this.mcserver.getPlayerList().saveAll();
		// Save Worlds
		this.mcserver.saveAllChunks(false, true, false);
		// Prepare Folders
		this.prepareFolders();
		File worldSavestateDir = new File(this.savestatesDir, this.savestatesDir.listFiles().length - 1 + "");
		try {
			// Reread state file
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(Files.readAllBytes(new File(this.savestatesDir, "states.dat").toPath())));
			this.states = new State[buf.readVarInt()];
			for (int ii = 0; ii < this.states.length; ii++) {
				this.states[ii] = State.deserialize(buf.readByteArray());
			}
			// Copy full folder
			Files.walkFileTree(this.worldDir.toPath(), new FileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					try {
						worldSavestateDir.toPath().resolve(SavestateMod.this.worldDir.toPath().relativize(dir)).toFile().mkdirs();
					} catch (Exception e) {
						System.err.println("Unable to mkdir: " + dir);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path dir, BasicFileAttributes attrs) throws IOException {
					try {
						Files.copy(dir, worldSavestateDir.toPath().resolve(SavestateMod.this.worldDir.toPath().relativize(dir)), StandardCopyOption.REPLACE_EXISTING);
					} catch (Exception e) {
						System.err.println("Unable to copy: " + dir);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
			// Add the savesate to the list of states
			this.states = ArrayUtils.add(this.states, new State(this.doSavestate, Instant.now().getEpochSecond(), worldSavestateDir.getAbsolutePath()));
			// Write savestate file
			FriendlyByteBuf buf2 = new FriendlyByteBuf(Unpooled.buffer());
			buf2.writeVarInt(this.states.length);
			for (State s : this.states) {
				buf2.writeByteArray(s.serialize());
			}
			Files.write(new File(this.savestatesDir, "states.dat").toPath(), buf2.array(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads a state of the world
	 * @param i Index to load
	 */
	private void loadstate(int i) {
		if (i >= this.states.length) {
			LoTAS.LOGGER.warn("Trying to loadstate a nonexistant state: " + i);
			return;
		}
		
	}

	/**
	 * Deletes a state of the world
	 * @param i Index to delete
	 */
	private void deletestate(int i) {
		if (i >= this.states.length) {
			LoTAS.LOGGER.warn("Trying to deletestate a nonexistant state: " + i);
			return;
		}
		// Enable tickrate zero
		TickAdvance.instance.updateTickadvanceStatus(true);
		// Prepare folders
		this.prepareFolders();
		File worldSavestateDir = new File(this.savestatesDir, i + "");
		try {
			// Reread state file
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(Files.readAllBytes(new File(this.savestatesDir, "states.dat").toPath())));
			this.states = new State[buf.readVarInt()];
			for (int ii = 0; ii < this.states.length; ii++) {
				this.states[ii] = State.deserialize(buf.readByteArray());
			}
			// Delete Folder if it exists
			if (worldSavestateDir.exists())
				FileUtils.deleteDirectory(worldSavestateDir);
			// Remove the savestate from the array
			this.states = ArrayUtils.remove(this.states, i);
			// Write savestate file
			FriendlyByteBuf buf2 = new FriendlyByteBuf(Unpooled.buffer());
			buf2.writeVarInt(this.states.length);
			for (State s : this.states) {
				buf2.writeByteArray(s.serialize());
			}
			Files.write(new File(this.savestatesDir, "states.dat").toPath(), buf2.array(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Re-enumerate all other savestates ahead
		for (File file : this.savestatesDir.listFiles()) {
			if ("states.dat".equals(file.getName()))
				continue;
			int id = Integer.parseInt(file.getName());
			if (id > i)
				file.renameTo(new File(file.getParentFile(), id - 1 + ""));
		}
		// Disable tickrate zero (FIXME: do we really wants this?)
		TickAdvance.instance.updateTickadvanceStatus(false);
		// Send all states to the client
		this.sendStates();
	}
	
	
	/**
	 * Updates the state list on incoming packet
	 * @param buf Packet Data
	 */
	@Override
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		this.states = new State[buf.readInt()];
		for (int i = 0; i < this.states.length; i++) {
			this.states[i] = State.deserialize(buf.readByteArray());
		}
	}
	
	/**
	 * Updates client data on connect
	 */
	public void onConnect(ServerPlayer c) {
		// Load everything
		FriendlyByteBuf buf;
		try {
			this.prepareFolders();
			buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(Files.readAllBytes(new File(this.savestatesDir, "states.dat").toPath())));
			this.states = new State[buf.readVarInt()];
			for (int i = 0; i < this.states.length; i++) {
				this.states[i] = State.deserialize(buf.readByteArray());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.sendStates(); // send the states to all clients, can't hurt
	}

	/**
	 * Returns the Description of a state or the date of it, if non given
	 * @param index Index to check
	 * @return Description of state
	 */
	@Environment(EnvType.CLIENT)
	public State getSavestateInfo(int index) {
		return this.states[index];
	}

	/**
	 * Client-Side only way to check if or how many savestates exists.
	 * @return Whether a state is present to load from
	 */
	@Environment(EnvType.CLIENT)
	public int getStateCount() {
		return this.states.length;
	}

	/**
	 * Clears local data on disconnect
	 */
	@Environment(EnvType.CLIENT)
	public void onDisconnect() {
		this.states = new State[0];
	}

	/**
	 * State information holder
	 * @author Pancake
	 */
	public static class State {

		// Information of any state
		private String name;
		private long timestamp;

		// Data of the state
		private File savelocation;

		public State(String name, long timestamp, String savelocation) {
			this.name = name;
			this.timestamp = timestamp;
			this.savelocation = new File(savelocation);
		}

		public byte[] serialize() {
			FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
			buffer.writeUtf(this.name);
			buffer.writeLong(this.timestamp);
			buffer.writeUtf(this.savelocation.getAbsolutePath());
			return buffer.array();
		}

		public static State deserialize(byte[] data) {
			FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
			String s1 = buffer.readUtf();
			long l2 = buffer.readLong();
			String s3 = buffer.readUtf();
			return new State(s1, l2, s3);
		}

		public String getName() {
			return this.name;
		}

		public long getTimestamp() {
			return this.timestamp;
		}

		public File getSavelocation() {
			return this.savelocation;
		}
	}

}
