package de.pfannkuchen.lotas.worldhacking;

import de.pfannkuchen.lotas.worldhacking.quack.HandleDuck;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class CustomRespawnPacket extends ClientboundRespawnPacket{

	private boolean force=false;
	
	public CustomRespawnPacket(DimensionType dimensionType, ResourceKey<Level> resourceKey, long l, GameType gameType, GameType gameType2, boolean bl, boolean bl2, boolean bl3, boolean force) {
		super(dimensionType, resourceKey, l, gameType, gameType2, bl, bl2, bl3);
		this.force=force;
	}

	@Override
	public void write(FriendlyByteBuf friendlyByteBuf) {
		super.write(friendlyByteBuf);
		friendlyByteBuf.writeBoolean(force);
	}
	
	@Override
	public void handle(ClientGamePacketListener clientGamePacketListener) {
		((HandleDuck) clientGamePacketListener).handleCustomPacket(this);
	}
	
	public boolean isForce() {
		return force;
	}
}
