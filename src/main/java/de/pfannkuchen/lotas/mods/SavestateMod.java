/**
 * Here goes the logic of the Savestate Mod.. eventually...
 */
package de.pfannkuchen.lotas.mods;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

/**
 * Main Savestate Mod
 * @author Pancake
 */
public class SavestateMod {

	private static final ResourceLocation SAVESTATE_MOD_RL = new ResourceLocation("lotas", "savestatemod");
	@Environment(EnvType.CLIENT)
	public Minecraft mc;
	public MinecraftServer mcserver;
	
	/**
	 * Saves or Loads when receiving a packet
	 * @param p Incoming Packet
	 */
	@Environment(EnvType.CLIENT)
	public void onClientPacket(ClientboundCustomPayloadPacket p) {
		if (SAVESTATE_MOD_RL.equals(p.getIdentifier())) {
			
		}
	}
	
	/**
	 * Resend when receiving a packet
	 * @param p Incoming Packet
	 */
	public void onServerPacket(ServerboundCustomPayloadPacket p) {
		if (SAVESTATE_MOD_RL.equals(p.getIdentifier())) {
			
		}
	}
	
	/**
	 * Returns the Description of a state or the date of it, if non given
	 * @param index Index to check
	 * @return Description of state
	 */
	@Environment(EnvType.CLIENT)
	public String getSavestateInfo(int index) {
		return index + " - Did cool stuff.";
	}
	
	/**
	 * Client-Side only way to check if or how many savestates exists.
	 * @return Whether a state is present to load from
	 */
	@Environment(EnvType.CLIENT)
	public int getStateCount() {
		return 4;
	}
	
	/**
	 * Client-Side only state request. Sends a packet to the server contains a save or load boolean and in case of loading an index to load
	 * @param saveOLoad Savestate or Loadstate request
	 * @param index Index of Loadstate
	 */
	@Environment(EnvType.CLIENT)
	public void requestState(boolean saveOLoad, int index) {
		System.out.println(saveOLoad ? "Savestating.." : ("Loading state " + index + ".."));
	}

	/**
	 * Client-Side only state deletion request
	 * @param i State to delete
	 */
	public void requestDelete(Integer i) {
		System.out.println("Deleting state " + i + "..");
	}
	
}