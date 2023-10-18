package com.minecrafttas.lotas.mods.savestatemod;

import com.minecrafttas.lotas.LoTAS;
import lombok.Data;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static com.minecrafttas.lotas.LoTAS.LOGGER;

/**
 * This class represents the state data of a world.
 * @author Pancake
 */
@Getter
public class StateData {

	/** Location of the world */
	private File worldDir;
	
	/** Location of the savestates folder */
	private File worldSavestateDir;

	/** Location of the states.dat file */
	private File worldStatesFile;

	/** List of all savestates mirrored between client and server */
	private State[] states = {};

	/**
	 * Initialize state data
	 * @param mcserver Minecraft server instance
	 */
	public void onServerInitialize(MinecraftServer mcserver) {
		this.worldDir = mcserver.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).toFile().getParentFile();
 		this.worldSavestateDir = new File(this.worldDir.getParentFile(), this.worldDir.getName() + " Savestates");
 		this.worldStatesFile = new File(this.worldSavestateDir, "states.dat");
	}
	
	/**
	 * Load states data of world
	 * @throws IOException If an exception occurs while loading
	 */
	public void loadData() throws IOException {
		this.states = new State[0];
		if (!this.worldStatesFile.exists())
			return;

		this.deserializeData(Files.readAllBytes(this.worldStatesFile.toPath()));

		// verify that no states are missing
		for (State state : this.states)
			if (!new File(this.worldSavestateDir, state.getIndex() + "").exists())
				LOGGER.error("Savestate " + state.getIndex() + " was not found in " + this.worldSavestateDir.getAbsolutePath() + "!");
	}
	
	/**
	 * Save state data of world
	 * @throws IOException If an exception occurs while saving
	 */
	public void saveData() throws IOException {
		if (!this.worldSavestateDir.exists())
			this.worldSavestateDir.mkdirs();

		Files.write(this.worldStatesFile.toPath(), SerializationUtils.serialize(this.states), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}

	/**
	 * Deserialize state data from byte array
	 * @param data Serialized Data
	 */
	public void deserializeData(byte[] data) {
		this.states = SerializationUtils.deserialize(data);
	}

	/**
	 * Clear state data
	 * (Clientside only)
	 */
	@Environment(EnvType.CLIENT)
	public void clearStates() {
		this.states = new State[0];
	}

	/**
	 * Add state to list of states
	 * @param state State
	 */
	public void addState(State state) {
		this.states = ArrayUtils.add(this.states, state);
	}

	/**
	 * Verify state exists
	 * @param i Index
	 * @return Is state valid
	 */
	public boolean isValid(int i) {
		if (i >= this.states.length)
			return false;

		return new File(this.worldSavestateDir, this.states[i].getIndex() + "").exists();
	}

	/**
	 * Remove state from list of states
	 * @param i Index
	 */
	public void removeState(int i) {
		this.states = ArrayUtils.remove(this.states, i);
	}

	/**
	 * This class represents a state
	 * @author Pancake
	 */
	@Data
	public static class State implements Serializable {
		/** Name of the state */
		private final String name;
		/** Timestamp */
		private final long timestamp;
		/** Index */
		private final int index;
	}

}
