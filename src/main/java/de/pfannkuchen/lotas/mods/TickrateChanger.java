/**
 * Here is the logic of the Tickrate Changer:
 * 
 * As noted by the @Environment annotations in front of methods, this code works on both client and server.
 * 
 * When first initializing the Tickrate Changer it prepares packet listeners for Requesting a tickrate change and Tickrate change. #<init>
 * Every time the clients wants to change the tickrate it sends a Request Tickrate Change Packet to the server. #requestTickrateUpdate ~~> #requestTickrateUpdate
 * The server proceeds by changing it's tickrate and sends a tickrate change packet to the client from it's listener. #<init> -> #updateTickrate
 * The clients listener finally updates the client tickrate too. #<init> -> #internallyUpdateTickrate
 */
package de.pfannkuchen.lotas.mods;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Main Tickrate Changer
 * @author Pancake
 */
public class TickrateChanger {

	// Tickrate Variables in various formats
	private double tickrate;
	private long msPerTick;
	private double gamespeed;
	
	/**
	 * Initializes the Tickrate Changer and adds an event handler for incoming/outgoing packets
	 */
	public TickrateChanger() {
		// TODO: Setup Tickrate packet listener
	}
	
	/**
	 * Client-Side only tickrate update request. Sends a packet to the server updating the tickrate.
	 * @param tickrate Tickrate to update to
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickrateUpdate(double tickrate) {
		// TODO: Send Tickrate request packet
	}
	
	/**
	 * Server-Side only tickrate update. Sends a packet to all players
	 * @param tickrate Tickrate to update to
	 */
	@Environment(EnvType.SERVER)
	public void updateTickrate(double tickrate) {
		if (tickrate < 0.1) return;
		internallyUpdateTickrate(tickrate);
		// TODO: Send Tickrate update packet
	}
	
	/**
	 * Internally update the tickrate for the game
	 * @param tickrate Tickrate to update to
	 */
	private void internallyUpdateTickrate(double tickrate) {
		this.tickrate = tickrate;
		this.msPerTick = (long) (1000L / tickrate);
		this.gamespeed = tickrate / 20;
	}

	// Place Getters here to not confuse with public variables that shall not be set
	
	public double getTickrate() {
		return this.tickrate;
	}

	public long getMsPerTick() {
		return this.msPerTick;
	}

	public double getGamespeed() {
		return this.gamespeed;
	}
	
}
