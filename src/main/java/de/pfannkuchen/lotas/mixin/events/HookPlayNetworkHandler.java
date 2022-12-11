package de.pfannkuchen.lotas.mixin.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.system.ModSystem;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ModSystem}. It also cancelles a logger
 * @author Pancake
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class HookPlayNetworkHandler {

	/**
	 * Triggers an Event in {@link ModSystem#onServerPayload(ServerboundCustomPayloadPacket)} before the game enters the game loop
	 * @param ci Callback Info
	 */
	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	public void hookCustomPayloadEvent(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		ModSystem.onServerPayload(packet);
		ci.cancel();
	}

}
