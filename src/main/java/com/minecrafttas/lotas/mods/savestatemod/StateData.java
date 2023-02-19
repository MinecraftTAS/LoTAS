package com.minecrafttas.lotas.mods.savestatemod;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import com.minecrafttas.lotas.LoTAS;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;

/**
 * Class managing the state data
 * @author Pancake
 */
public class StateData {

	/**
	 * File location of the world
	 */
	private File worldDir;
	
	/**
	 * File location of the savestates folder for the world
	 */
	private File worldSavestateDir;

	/**
	 * File location of the states.dat file
	 */
	private File worldStatesFile;
	
	/**
	 * Runs on server initialize
	 * @param mcserver Minecraft Server
	 */
	public void onServerInitialize(MinecraftServer mcserver) {
 		this.worldDir = mcserver.getStorageSource().getFile(mcserver.getLevelIdName(), "");
 		this.worldSavestateDir = new File(this.worldDir.getParentFile(), this.worldDir.getName() + " Savestates");
 		this.worldStatesFile = new File(this.worldSavestateDir, "states.dat");
	}
	
	/**
	 * List of all savestates mirrored between client and server
	 */
	private State[] states = {};
	
	/**
	 * Loads the states data of the world
	 * @throws IOException Filesystem Exception
	 */
	public void loadData() throws IOException {
		this.states = new State[0];
		if (!this.worldStatesFile.exists())
			return;
		this.deserializeData(Files.readAllBytes(this.worldStatesFile.toPath()));
		// verify that no states are missing
		for (State state : this.states)
			if (!new File(this.worldSavestateDir, state.getIndex() + "").exists())
				LoTAS.LOGGER.error("Savestate " + state.getIndex() + " was not found in " + this.worldSavestateDir.getAbsolutePath() + "!");
	}
	
	/**
	 * Saves the states data of the world
	 * @throws IOException Filesystem Exception
	 */
	public void saveData() throws IOException {
		if (!this.worldSavestateDir.exists())
			this.worldSavestateDir.mkdirs();
		Files.write(this.worldStatesFile.toPath(), this.serializeData(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
	
	/**
	 * Serializes the state data into a byte array
	 * @return Serialized Data
	 */
	public byte[] serializeData() {
		return SerializationUtils.serialize(this.states);
	}
	
	/**
	 * Deserializes the state data from a byte array
	 * @param data Serialized Data
	 */
	public void deserializeData(byte[] data) {
		this.states = SerializationUtils.deserialize(data);
	}
	
	/**
	 * Class holding the information of a state
	 * @author Pancake
	 */
	public static class State implements Serializable {

		/**
		 * Name of the state
		 */
		private String name;
		
		/**
		 * Timestamp of the state
		 */
		private long timestamp;
		
		/**
		 * Index of the state
		 */
		private int index;

		/**
		 * Initializes a new state
		 * @param name Name of the state
		 * @param timestamp Timestamp of the state
		 * @param index Index of the state
		 */
		public State(String name, long timestamp, int index) {
			this.name = name;
			this.timestamp = timestamp;
			this.index = index;
		}

		/**
		 * Returns the state name
		 * @return State Name
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Returns the state timestamp
		 * @return State Timestamp
		 */
		public long getTimestamp() {
			return this.timestamp;
		}

		/**
		 * Returns the state index
		 * @return State index
		 */
		public int getIndex() {
			return this.index;
		}
	}
	
	/**
	 * Returns state
	 * @param index Index to check
	 * @return State
	 */
	public State getState(int index) {
		return this.states[index];
	}

	/**
	 * Returns state count.
	 * @return State count
	 */
	public int getStateCount() {
		return this.states.length;
	}

	/**
	 * Clientside: Clear state data
	 */
	@Environment(EnvType.CLIENT)
	public void clearStates() {
		this.states = new State[0];
	}
	
	/**
	 * Returns the world dir
	 * @return World dir
	 */
	public File getWorldDir() {
		return worldDir;
	}

	/**
	 * Returns the world savestate dir
	 * @return World savestate dir
	 */
	public File getWorldSavestateDir() {
		return worldSavestateDir;
	}

	/**
	 * Adds a new state to the list of states
	 * @param state State
	 */
	public void addState(State state) {
		this.states = ArrayUtils.add(this.states, state);
	}

	/**
	 * Verifies a state exists
	 * @param i Index
	 * @return Is state valid
	 */
	public boolean isValid(int i) {
		if (i >= this.states.length)
			return false;
		return new File(this.worldSavestateDir, this.getState(i).getIndex() + "").exists();
	}

	/**
	 * Removes a state from the list of states
	 * @param i Index
	 */
	public void removeState(int i) {
		this.states =  ArrayUtils.remove(this.states, i);
	}
	
}
