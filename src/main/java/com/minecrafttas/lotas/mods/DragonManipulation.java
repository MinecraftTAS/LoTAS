/**
 * Here is the logic of the dragon manipulation mod:
 *
 * The enderdragon has a net of notes which are chained to build a path that the ender dragon follows. Two nodes are placed fairly high up while the others are just above the ground.
 * After finishing a path of 4 nodes (or in the inital perch 1) the dragon can switch phases. Additionally the nodes have a random height increase of up to 20 blocks where the dragon will fly to.
 * To make the ender dragon optimal we need to skip the high nodes and have the height bonus equal the height of the ender dragon rn. Then we can force a phase switch with rng manipulation. Additionally we can modify the path
 * to go into the direction the ender dragon is already facing.
 * 
 * Once the dragon manipulation is enabled, "phase" will be set to something other than null and the dragon will be fully optimized.
 * 
 * As noted by the @Environment annotations in front of methods, this code works on both client and server.
 * 
 * Every time the clients wants to change the manipulation phase it sends a request update packet to the server. #requestPhaseChange
 * The server proceeds sending a update packet to the clients causing them to process the packet on their own. The server processes the packet too. #onServerPayload
 * The clients listener finally updates the phase locally. #onClientPayload
 */
package com.minecrafttas.lotas.mods;

import com.minecrafttas.lotas.system.ModSystem.Mod;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Main Dragon Manipulation
 * @author Pancake
 */
public class DragonManipulation extends Mod {

	public static DragonManipulation instance;
	
	/**
	 * Initializes the dragon manipulation
	 */
	public DragonManipulation() {
		super(new ResourceLocation("lotas", "dragonmanipulation"));
		instance = this;
	}
	
	/**
	 * Dragon phases
	 * @author Pancake
	 */
	public static enum Phase {
		OFF, LANDINGAPPROACH, STRAFING, HOLDINGPATTERN
	}
	/**
	 * Current manipulation phase or OFF
	 */
	private Phase phase = Phase.OFF;

	/**
	 * Returns manipulation phase or OFF
	 * @return Manipulation phase
	 */
	public Phase getPhase() {
		return this.phase;
	}

	/**
	 * Updates local settings on packet
	 * @param buf Packet Data
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		this.phase = Phase.values()[buf.readInt()];
	}
	
	/**
	 * Resend when receiving a packet
	 * @param buf Packet Data
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		this.phase = Phase.values()[buf.readInt()];
		// Send packet to client
		for (ServerPlayer player : this.mcserver.getPlayerList().getPlayers()) {
			FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
			data.writeInt(this.phase.ordinal());
			this.sendPacketToClient(player, data);
		}
	}
	
	/**
	 * Client-Side only settings edit request. Sends a packet to the server contains the new phase setting
	 * @param phase New phase or null
	 */
	@Environment(EnvType.CLIENT)
	public void requestPhaseChange(Phase phase) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(phase.ordinal());
		this.sendPacketToServer(buf);
	}
	
}

/* Let me write down how the ender dragon ai works for future reference.

The dragon has a set of 24 nodes placed in determinated spots at a height depending on the terrain. Two nodes are generated higher then all others.
The dragon then creates a path through these nodes with a somewhat deteministic path. The beginning of the path is where the dragon is at. 
The length of the path is the shortest possible (shortest possible through minecrafts algorithm, 4 max it seems) and the end of the path is different depending on the orientation and phase of the dragon.
The path is also different if there are 0 end crystals remaining. The only actual RNG in the path is the end node, during the holding pattern phase. After finishing the previous path,
the dragon has a 1/8 chance to switch rotation, which then changes the last node of the path.

Therefore EnderDragon#findPath takes the start node, end node and optionally a custom end node, being around the player during strafing phase.

Now that the path is clear (no pun intended), lets look at the phases of the dragon.
At the end of every one of the holding pattern phase, the dragon does some math together with some randomness to decide which phase it goes to next... and that's it

*/
