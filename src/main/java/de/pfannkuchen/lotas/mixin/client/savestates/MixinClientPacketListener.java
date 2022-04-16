package de.pfannkuchen.lotas.mixin.client.savestates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannkuchen.lotas.worldhacking.CustomRespawnPacket;
import de.pfannkuchen.lotas.worldhacking.quack.HandleDuck;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener implements HandleDuck{
	
	private boolean force;
	
	@Override
	public void handleCustomPacket(CustomRespawnPacket packet) {
		force=packet.isForce();
		handleRespawn(packet);
	}
	
	@Redirect(method = "handleRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;dimension()Lnet/minecraft/resources/ResourceKey;"))
	public ResourceKey<Level> redirect_handleRespawn(Level level){
		if(force) {
			return null;
		}
		return level.dimension();
	}
	
	@Shadow
	public abstract void handleRespawn(ClientboundRespawnPacket clientboundRespawnPacket);
}
