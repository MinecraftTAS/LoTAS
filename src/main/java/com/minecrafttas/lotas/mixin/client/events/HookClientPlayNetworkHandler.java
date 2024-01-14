package com.minecrafttas.lotas.mixin.client.events;

import com.minecrafttas.lotas.system.ModSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ModSystem}.
 * 
 * @author Pancake
 */
@Mixin(ClientPacketListener.class)
@Environment(EnvType.CLIENT)
public class HookClientPlayNetworkHandler {

	/**
	 * Trigger event in {@link ModSystem#onClientsidePayload(ClientboundCustomPayloadPacket)} )} when a custom payload packet is received
	 * 
	 * @param ci Callback Info
	 */
	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	public void hookCustomPayloadEvent(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
		ModSystem.onClientsidePayload(packet);
		ci.cancel();
	}

}
