/**
 * Here is the logic of the dragon manipulation mod:
 *
 * As noted by the @Environment annotations in front of methods, this code works on both client and server.
 *
 * Every time the clients wants to update the servers dragon manipulation states it sends a request #requestState which is handled in #onServerPacket and resends the packets, which 
 * are being handled once again in #onClientPacket.
 */
package de.pfannkuchen.lotas.mods;

import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.gui.windows.DragonManipulationLoWidget;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Main dragon manipulation mod
 * @author Pancake
 */
public class DragonManipulationMod {

	static final ResourceLocation DRAGON_MANIPULATION_MOD_RL = new ResourceLocation("lotas", "dragonmanipulationwidget");
	@Environment(EnvType.CLIENT)
	public Minecraft mc;
	public MinecraftServer mcserver;
	
	/**
	 * Updates the local values on incoming packets
	 * @param p Incoming packet
	 */
	@Environment(EnvType.CLIENT)
	public void onClientPacket(ClientboundCustomPayloadPacket p) {
		if (DRAGON_MANIPULATION_MOD_RL.equals(p.getIdentifier())) {
			// Update Config Manager
			FriendlyByteBuf data = p.getData();
			LoTAS.configmanager.setBoolean("dragonmanipulationwidget", data.readUtf(), data.readBoolean());
			LoTAS.configmanager.save();
			DragonManipulationLoWidget.forceUpdate();
		}
	}

	/**
	 * Updates the variables and resends when receiving a packet
	 * @param p Incoming packet
	 */
	public void onServerPacket(ServerboundCustomPayloadPacket p) {
		if (DRAGON_MANIPULATION_MOD_RL.equals(p.getIdentifier())) {
			// Update Config Manager
			FriendlyByteBuf data = p.getData();
			String key = data.readUtf();
			boolean value = data.readBoolean();
			LoTAS.configmanager.setBoolean("dragonmanipulationwidget", key, value);
			LoTAS.configmanager.save();
			// Resend packet to all clients
			this.mcserver.getPlayerList().getPlayers().forEach(c -> {
				FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
				buf.writeUtf(key);
				buf.writeBoolean(value);
				c.connection.send(new ClientboundCustomPayloadPacket(DRAGON_MANIPULATION_MOD_RL, buf));
			});
		}
	}
	
	/**
	 * Client-Side only state update request. Sends a packet to the server containg the state to be modified
	 * @param key Key to change
	 * @param value Value to change
	 */
	@Environment(EnvType.CLIENT)
	public void requestState(String key, boolean value) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeUtf(key);
		buf.writeBoolean(value);
		this.mc.getConnection().send(new ServerboundCustomPayloadPacket(DRAGON_MANIPULATION_MOD_RL, buf));
		// Send notification
		String text = (value ? " enabled " : " disabled ") + switch (key) {
			case "forceOptimalPath": yield "force optimal ender dragon path";
			case "forceCCWToggle": yield "force cc/ccw toggle";
			case "forceLandingApproach": yield "force landing approach";
			case "forcePlayerStrafing": yield "force player strafing";
			default: yield " unknown ender dragon option";
		};
		LoTAS.notificationmanager.requestNotification(this.mc.player.getStringUUID() + "_" + this.mc.player.getName().getString() + text);
	}
	
	/**
	 * Updates client data on connect
	 */
	public void onConnect(ServerPlayer c) {
		final String[] keys = new String[] {
			"forceOptimalPath",
			"forceCCWToggle",
			"forceLandingApproach",
			"forcePlayerStrafing"
		};
		for (int i = 0; i < keys.length; i++) {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeUtf(keys[i]);
			buf.writeBoolean(LoTAS.configmanager.getBoolean("dragonmanipulationwidget", keys[i]));
			c.connection.send(new ClientboundCustomPayloadPacket(DRAGON_MANIPULATION_MOD_RL, buf));
		}
	}
	
}
