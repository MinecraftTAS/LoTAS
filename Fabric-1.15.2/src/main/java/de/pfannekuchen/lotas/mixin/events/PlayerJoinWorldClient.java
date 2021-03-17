package de.pfannekuchen.lotas.mixin.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

@Mixin(ClientWorld.class)
public class PlayerJoinWorldClient {
	@Inject(method = "addPlayer", at = @At(value = "HEAD"))
	public void playerJoinWorldEvent(int id, AbstractClientPlayerEntity player, CallbackInfo ci) {
		TickrateChanger.onJoinWorld(player);
	}
}
