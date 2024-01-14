package com.minecrafttas.lotas.mods;

import com.minecrafttas.lotas.system.ConfigurationSystem;
import com.minecrafttas.lotas.system.ModSystem.Mod;

import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import static com.minecrafttas.lotas.LoTAS.TICKRATE_CHANGER;

/**
 * Tick advance mod
 * ~ same logic as tickrate changer
 * @author Pancake
 */
public class TickAdvance extends Mod {
	public TickAdvance() {
		super(new ResourceLocation("lotas", "tickadvance"));
	}

	/** Should tick advance when a player joins the server */
	private boolean freezeOnJoin;

	/** Is tick advance enabled */
	@Getter
	private boolean tickadvance;

	/** Should tick advance clientside */
	@Environment(EnvType.CLIENT)
	public boolean shouldTickClient;

	/** Should tick advance serverside */
	public boolean shouldTickServer;

	/**
	 * Initializes tick advance mod
	 */
	@Override
	protected void onInitialize() {
		this.freezeOnJoin = ConfigurationSystem.getBoolean("tickadvance_freezeonjoin", false);
	}

	/**
	 * Request tick advance toggle by sending packet to server
	 * (Clientside only)
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickadvanceToggle() {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(0); // status update
		buf.writeBoolean(!this.tickadvance); // new status
		this.sendPacketToServer(buf);
	}

	/**
	 * Request tick advance by sending packet to server
	 * (Clientside only)
	 */
	@Environment(EnvType.CLIENT)
	public void requestTickadvance() {
		if (!this.tickadvance || this.shouldTickClient)
			return;

		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(1); // tick update
		this.sendPacketToServer(buf);
	}

	/**
	 * Update server tickadvance status and resend packet when receiving a request
	 * @param buf Packet
	 */
	@Override
	protected void onServerPayload(FriendlyByteBuf buf) {
		if (buf.readInt() == 0)
			this.updateTickadvanceStatus(buf.readBoolean());
		else
			this.updateTickadvance();
	}

	/**
	 * Update server tickadvance status and update clients
	 * (Serverside only)
	 * @param tickadvance Tickadvance status
	 */
	public void updateTickadvanceStatus(boolean tickadvance) {
		this.tickadvance = tickadvance;

		// update tick advance for all clients
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeInt(0); // status update
			buf.writeBoolean(tickadvance); // new status
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Advance server tick and update clients
	 * (Serverside only)
	 * @param tickadvance Tickadvance status
	 */
	public void updateTickadvance() {
		this.shouldTickServer = true;

		// tick all clients
		this.mcserver.getPlayerList().getPlayers().forEach(player -> {
			FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
			buf.writeInt(1); // tick update
			this.sendPacketToClient(player, buf);
		});
	}

	/**
	 * Update client tickadvance status when receiving packet
	 * @param buf Packet
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsidePayload(FriendlyByteBuf buf) {
		if (buf.readInt() == 0) { // toggle tickadvance
			this.tickadvance = buf.readBoolean();
			
			TICKRATE_CHANGER.updateGameTime(TICKRATE_CHANGER.getGamespeed());
		} else {
			this.shouldTickClient = true; // tick client
		}
	}

	/**
	 * Reset tick advance state after tick passed
	 */
	@Environment(EnvType.CLIENT)
	@Override
	protected void onClientsideTick() {
		this.shouldTickClient = false;
		TICKRATE_CHANGER.advanceGameTime(50L);
	}

	/**
	 * Reset tick advance on disconnect clientside
	 */
	@Override
	@Environment(EnvType.CLIENT)
	protected void onClientsideDisconnect() {
		this.tickadvance = false;
	}

	/**
	 * Update client tick advance status on connect
	 * @param player Player
	 */
	@Override
	protected void onClientConnect(ServerPlayer player) {
		// freeze client if enabled in config
		if (this.freezeOnJoin && !this.tickadvance)
			this.updateTickadvanceStatus(true);

		// update tick advance status
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(0); // status update
		buf.writeBoolean(this.tickadvance); // new status
		this.sendPacketToClient(player, buf);
	}

}
