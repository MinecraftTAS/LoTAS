/**
 * Here is the logic of the savestate mod..
 *
 * The class 'state' represents a savestate. It contains some data such as name, timestamp, picture and more.
 * There is a list of states, which is being synchronized between the client and the server whenever a sld occurs (save load delete)
 *
 * in onClientPacket the client reacts to the server, once it sends a packet. There are 2 types of packets which have to come after each other.
 * The first one contains the action and the amount of states. It locks the client during the savestate and lets the client update the state list.
 * The second packet contains the actual state after the first packet mentions how many state there are.
 *
 * The client can only receive these packets before and after the server savestate(), loadstate() or deletestate()s. This is being triggered at the end of a tick
 * if a packet is being recieved from any client. Then this is being stored in the "Server Todolist" and processed after the tick as mentioned.
 *
 * The client can request and action in requestState().
 *
 * Another thing to note is that the images have to be registered to minecraft for future rendering. The
 * client has a "Todo list" for that too and registeres the textures in the render loop, so that opengl is available.
 *
 */
package de.pfannkuchen.lotas.mods;

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
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;

import de.pfannkuchen.lotas.LoTAS;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;

/**
 * Main savestate mod
 * @author Pancake
 */
public class SavestateMod {

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
		private int[] screenshot;

		// Client-side resource location
		@Environment(EnvType.CLIENT)
		public ResourceLocation texture;

		public State(String name, long timestamp, String savelocation, int[] screenshot) {
			this.name = name;
			this.timestamp = timestamp;
			this.savelocation = new File(savelocation);
			this.screenshot = screenshot;
		}

		public byte[] serialize() {
			FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
			buffer.writeUtf(this.name);
			buffer.writeLong(this.timestamp);
			buffer.writeUtf(this.savelocation.getAbsolutePath());
			buffer.writeVarIntArray(this.screenshot);
			return buffer.array();
		}

		public static State deserialize(byte[] data) {
			FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
			String s1 = buffer.readUtf();
			long l2 = buffer.readLong();
			String s3 = buffer.readUtf();
			int[] a4 = buffer.readVarIntArray();
			return new State(s1, l2, s3, a4);
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

		public int[] getScreenshot() {
			return this.screenshot;
		}
	}

	static final ResourceLocation SAVESTATE_MOD_RL = new ResourceLocation("lotas", "savestatemod");
	@Environment(EnvType.CLIENT)
	public Minecraft mc;

	// Server-side Todo list
	private String doSavestate = null;
	private int[] doSavestatePicture = null;
	private int doLoadstate = -1;
	private int doDeletestate = -1;
	// Client-side Todo list
	//	private boolean shouldReload;
	//	private State[] unload = null;
	// Server-side folder structure
	private File savestatesDir;
	private File worldDir;
	// Mirrored State List
	private State[] states = {};

	/**
	 * Save/loadstates and updates the state list on incoming packet
	 * @param p Incoming Packet
	 */
	@Environment(EnvType.CLIENT)
	public void onClientPacket(ClientboundCustomPayloadPacket p) {
		if (SAVESTATE_MOD_RL.equals(p.getIdentifier())) {
			FriendlyByteBuf buf = p.getData();
			// Switch between packet types
			if (buf.readInt() == 0) {
				// Should the Client screen be locked or unlocked
				boolean lockOUnlock = buf.readBoolean();
				RenderSystem.recordRenderCall(() -> {
					this.lock(lockOUnlock);
				});
				// Should a savestate or a loadstate occur
				int opcode = buf.readInt();
				if (opcode == 0) {
					if (lockOUnlock) {
						LoTAS.LOGGER.info("The Server will savestate now.");
					} else {
						LoTAS.LOGGER.info("The Server finished savestating.");
					}
				} else if (opcode == 1) {
					if (lockOUnlock) {
						LoTAS.LOGGER.info("The Server will loadstate now.");
					} else {
						LoTAS.LOGGER.info("The Server finished loadstating.");
					}
				} else if (opcode == 2) {
					if (lockOUnlock) {
						LoTAS.LOGGER.info("The Server will deletestate now.");
					} else {
						LoTAS.LOGGER.info("The Server finished deletestating.");
					}
				}
				//				this.unload = this.states;
				// Update the State List
				this.states = new State[buf.readVarInt()];
			} else {
				// Second packet type contains the state
				int index = buf.readInt();
				this.states[index] = State.deserialize(buf.readByteArray());
				if (index == this.states.length - 1) {
					LoTAS.LOGGER.info("The Client has " + this.states.length + " savestates stored in the mirrored list. Reloading all textures...");
					//					this.shouldReload = true;
				}
			}
		}
	}

	/**
	 * Locks the Client while not loading the client side classes on the server
	 */
	@Environment(EnvType.CLIENT)
	private void lock(boolean lockOUnlock) {
		try {
			if (lockOUnlock)
				de.pfannkuchen.lotas.ClientLoTAS.loscreenmanager.setScreen(new de.pfannkuchen.lotas.gui.StateLoScreen());
			else
				de.pfannkuchen.lotas.gui.StateLoScreen.allowUnlocking();
		} catch (Exception e) {
			e.printStackTrace(); // just to be sure, this didn't happen after I implemented the recordRenderCall()
		}
	}

	/**
	 * Reloads the texture if needed in the render thread
	 */
	@Environment(EnvType.CLIENT)
	public void onRender() {
		/*if (!this.mc.hasSingleplayerServer()) return; // Savestate textures are not supported at all. /////// on multiplayer servers
		if (!this.shouldReload) return;
		this.shouldReload = false;
		RenderSystem.recordRenderCall(() -> {
			if (this.unload != null) for (State s : this.unload) if (s.texture != null) this.mc.getTextureManager().release(s.texture);
			this.unload = null;
			for (State s : this.states) {
				if (s == null)
					continue;
				NativeImage n = new NativeImage(256, 144, true);
				BufferedImage img = new BufferedImage(256, 144, BufferedImage.TYPE_INT_ARGB);
				img.setRGB(0, 0, 256, 144, s.screenshot, 0, 256);
				for (int i = 0; i < 256; i++) {
					for (int j = 0; j < 144; j++) {
						n.setPixelRGBA(i, j, img.getRGB(i, j));
					}
				}
				recreateTexture(n, s);
			}
		});*/
	}

	/**
	 * Registers the texture
	 */
	@Environment(EnvType.CLIENT)
	public void recreateTexture(NativeImage n, State s) {
		this.mc.getTextureManager().register(s.texture = new ResourceLocation("lotas", (s.name + s.timestamp).replace(' ', '_').replaceAll("[^a-z0-9_.-]", "")), new DynamicTexture(n));
	}

	/**
	 * Triggers a Load/delete/savestate when a client packet is incoming and then resends the packet to all clients.
	 * @param p Incoming Packet
	 */
	public void onServerPacket(ServerboundCustomPayloadPacket p) {
		if (SAVESTATE_MOD_RL.equals(p.getIdentifier())) {
			// The Server has to be in tickrate zero AFTER THE ACTION for this to work
			if (LoTAS.tickadvance.isTickadvanceEnabled())
				LoTAS.tickadvance.updateTickadvanceStatus(false);
			FriendlyByteBuf buf = p.getData();
			switch (buf.readInt()) {
				case 0:
					this.doSavestate = buf.readUtf();
					this.doSavestatePicture = buf.readVarIntArray();
					break;
				case 1:
					this.doLoadstate = buf.readInt();
					break;
				case 2:
					this.doDeletestate = buf.readInt();
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
	 * Savestates/loadstates/deletestates after the tick
	 */
	public void afterServerTick(MinecraftServer mcserver) {
		// Savestate
		if (this.doSavestate != null) {
			this.savestate(mcserver);
			this.doSavestate = null;
		}
		// Loadstate
		if (this.doLoadstate != -1) {
			this.loadstate(this.doLoadstate, mcserver);
			this.doLoadstate = -1;
		}
		// Deletestate
		if (this.doDeletestate != -1) {
			this.deletestate(this.doDeletestate, mcserver);
			this.doDeletestate = -1;
		}
	}

	/**
	 * Sends a packet to the client about the stating progress
	 * @param lockOUnlock Whether the client should be locked or not
	 * @param opcode The Opcode of the operation
	 * @param mcserver The Server to which all packets shall be send
	 */
	public void sendPacketToClient(boolean lockOUnlock, int opcode, MinecraftServer mcserver) {
		mcserver.getPlayerList().getPlayers().forEach(c -> {
			// Freeze client packet
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeInt(0); // WRITE THE PACKET TYPE FIRST
			buf.writeBoolean(lockOUnlock); // Write the lock state of the client
			buf.writeInt(opcode); // Write the action of the server
			buf.writeVarInt(this.states.length);
			c.connection.send(new ClientboundCustomPayloadPacket(SAVESTATE_MOD_RL, buf));
			// Send all state packets
			int i = 0;
			for (State s : this.states) {
				buf = new FriendlyByteBuf(Unpooled.buffer());
				buf.writeInt(1); // WRITE THE PACKET TYPE FIRST
				buf.writeInt(i); // Index of the savestate
				buf.writeByteArray(s.serialize()); // Serialized savestate
				c.connection.send(new ClientboundCustomPayloadPacket(SAVESTATE_MOD_RL, buf));
				i++;
			}
			if (lockOUnlock)
				LoTAS.tickadvance.updateTickadvanceStatus(true);
		});
	}

	/**
	 * Saves a new state of the world
	 */
	@SuppressWarnings("resource")
	private void savestate(MinecraftServer mcserver) {
		// Freeze Client
		this.sendPacketToClient(true, 0, mcserver);
		// Save Playerdata
		mcserver.getPlayerList().saveAll();
		// Save Worlds
		mcserver.saveAllChunks(false, true, false);
		// Wait until everything was fully saved
		for (ServerLevel world : mcserver.getAllLevels())
			world.getChunkSource().chunkMap.flushWorker();
		// Prepare Folders
		this.prepareFolders(mcserver);
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
			this.states = ArrayUtils.add(this.states, new State(this.doSavestate, Instant.now().getEpochSecond(), worldSavestateDir.getAbsolutePath(), this.doSavestatePicture));
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
		// Unfreeze Client
		this.sendPacketToClient(false, 0, mcserver);
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
		this.sendPacketToClient(true, 2, mcserver);
		// Prepare folders
		this.prepareFolders(mcserver);
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
			ArrayList<State> stateList = new ArrayList<>(Arrays.asList(this.states));
			stateList.remove(i);
			this.states = stateList.toArray(State[]::new);
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
		// Unfreeze Client
		this.sendPacketToClient(false, 2, mcserver);
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
	 * Client-Side only state request. Sends a packet to the server contains a save or load int and an index to load
	 * @param state state 0 is save, state 1 is load, state 2 is delete
	 * @param index Index of Load/Deletestate
	 * @param name Name of the Savestate
	 * @param is 256x144 image of the world
	 */
	@Environment(EnvType.CLIENT)
	public void requestState(int state, int index, String name, int[] is) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(state);
		if (state == 0) {
			buf.writeUtf(name);
			buf.writeVarIntArray(is);
		} else
			buf.writeInt(index);
		this.mc.getConnection().send(new ServerboundCustomPayloadPacket(SAVESTATE_MOD_RL, buf));
	}

	/**
	 * Clears local data on disconnect
	 */
	@Environment(EnvType.CLIENT)
	public void onDisconnect() {
		//		this.shouldReload = true;
		//		this.unload = this.states;
		this.states = new State[0];
	}

	/**
	 * Updates client data on connect
	 */
	public void onConnect(ServerPlayer c) {
		// Load everything
		FriendlyByteBuf buf;
		try {
			this.prepareFolders(c.getServer());
			buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(Files.readAllBytes(new File(this.savestatesDir, "states.dat").toPath())));
			this.states = new State[buf.readVarInt()];
			for (int ii = 0; ii < this.states.length; ii++) {
				this.states[ii] = State.deserialize(buf.readByteArray());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Freeze Client Packet
		buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(0); // WRITE THE PACKET TYPE FIRST
		buf.writeBoolean(false); // Write the lock state of the client
		buf.writeInt(-1); // Write the action of the server
		buf.writeVarInt(this.states.length);
		c.connection.send(new ClientboundCustomPayloadPacket(SAVESTATE_MOD_RL, buf));
		// Send all state packets
		int i = 0;
		for (State s : this.states) {
			buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeInt(1); // WRITE THE PACKET TYPE FIRST
			buf.writeInt(i); // Index of the savestate
			buf.writeByteArray(s.serialize()); // Serialized savestate
			c.connection.send(new ClientboundCustomPayloadPacket(SAVESTATE_MOD_RL, buf));
			i++;
		}
	}

}
