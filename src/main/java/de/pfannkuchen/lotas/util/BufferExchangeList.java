package de.pfannkuchen.lotas.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * A Thread safe list of byte buffers which can be exchanged, locked and.. well.. used...
 * @author Pancake
 */
@Environment(EnvType.CLIENT) // Currently only used on the Client, remove if nessecary
public class BufferExchangeList {

	private ByteBuffer[] buffers;
	private boolean[] locked;
	private boolean[] filled;
	
	/**
	 * Creates and fills a new list of ByteBuffers
	 * @param length Amount of Byte Buffers
	 * @param size Length of Byte Buffers
	 */
	public BufferExchangeList(int length, int size) {
		// Prepare Array
		this.buffers = new ByteBuffer[length];
		this.locked = new boolean[length];
		this.filled = new boolean[length];
		for (int i = 0; i < this.buffers.length; i++) this.buffers[i] = ByteBuffer.allocateDirect(size);
	}
	
	/**
	 * Tries to find a unlocked and unfilled byte buffer
	 * @return Does a buffer exist
	 */
	public boolean containsUnfilledUnlocked() {
		for (int i = 0; i < this.locked.length; i++) {
			if (!this.locked[i] && !this.filled[i]) return true;
		}
		return false;
	}
	
	/**
	 * Tries to find a unlocked and filled byte buffer
	 * @return Does a buffer exist
	 */
	public boolean containsFilledUnlocked() {
		for (int i = 0; i < this.locked.length; i++) {
			if (!this.locked[i] && this.filled[i]) return true;
		}
		return false;
	}
	
	/**
	 * Tries to find a unlocked and unfilled byte buffer
	 * @return Buffer Index or size of SecureList
	 */
	public int findFilled() {
		int i = 0;
		for (i = 0; i < this.locked.length; i++) {
			if (!this.locked[i] && this.filled[i]) break;
		}
		return i;
	}
	
	/**
	 * Tries to find a unlocked and filled byte buffer
	 * @return Buffer Index or size of SecureList
	 */
	public int findUnfilled() {
		int i = 0;
		for (i = 0; i < this.locked.length; i++) {
			if (!this.locked[i] && !this.filled[i]) break;
		}
		return i;
	}
	
	/**
	 * Locks and updates the state of a Buffer
	 * @param i Index to lock
	 * @param fill Whether fill or not
	 * @return Byte Buffer locked
	 */
	public ByteBuffer getAndLock(int i, boolean fill) {
		if (this.locked[i]) return null;
		this.locked[i] = true;
		this.filled[i] = fill;
		if (fill) this.buffers[i].clear();
		return this.buffers[i];
	}
	
	/**
	 * Unlockes a Byte Buffer
	 * @param index Index to Lock
	 */
	public void unlock(int index) {
		this.locked[index] = false;
	}

	/**
	 * Clears the entire List
	 */
	public void clear() {
		Arrays.fill(this.locked, false);
		Arrays.fill(this.filled, false);
		for (int i = 0; i < this.buffers.length; i++) this.buffers[i].clear();
	}
	
}
