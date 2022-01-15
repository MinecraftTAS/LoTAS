package de.pfannkuchen.lotas.mixin.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.LoTAS;
import net.minecraft.server.MinecraftServer;

/**
 * This mixin is purely responsible for the hooking up the events in {@link LoTAS}.
 * @author Pancake
 */
@Mixin(MinecraftServer.class)
public class HookMinecraftServer {

	/**
	 * Triggers an Event in {@link LoTAS#onServerTick(MinecraftServer)} after the server ticks
	 * @param ci Callback Info
	 */
	@Inject(method = "runServer", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/server/MinecraftServer;endMetricsRecordingTick()V"))
	public void hookTickEvent(CallbackInfo ci) {
		LoTAS.instance.onServerTick((MinecraftServer) (Object) this);
	}

	/**
	 * Triggers an Event in {@link LoTAS#onServerLoad(MinecraftServer)} before the game enters the game loop
	 * @param ci Callback Info
	 */
	@Inject(method = "<init>", at = @At("RETURN"))
	public void hookInitEvent(CallbackInfo ci) {
		LoTAS.instance.onServerLoad((MinecraftServer) (Object) this);
	}

}
