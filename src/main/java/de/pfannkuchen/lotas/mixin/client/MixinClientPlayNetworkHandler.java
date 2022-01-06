package de.pfannkuchen.lotas.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.ClientLoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ClientLoTAS}. It also cancelles the logger
 * @author Pancake
 */
@Mixin(ClientPacketListener.class)
@Environment(EnvType.CLIENT)
public class MixinClientPlayNetworkHandler {

	/**
	 * Triggers an Event in {@link ClientLoTAS#onClientPayload(ClientboundCustomPayloadPacket)} whenever a custom payload packet is received
	 * @param ci Callback Info
	 */
	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	public void hookCustomPayloadEvent(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
		ClientLoTAS.instance.onClientPayload(packet);
		ci.cancel();
	}
	
}
