package com.minecrafttas.lotas.mixin.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecrafttas.lotas.system.ModSystem;

import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ModSystem}.
 *
 * @author Pancake
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class HookPlayNetworkHandler {

	/**
	 * Trigger event in {@link ModSystem#onServerPayload(ServerboundCustomPayloadPacket)} when a custom payload packet is received.
	 *
	 * @param ci Callback Info
	 */
	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	public void hookCustomPayloadEvent(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		ModSystem.onServerPayload(packet);
		ci.cancel();
	}

}
