package com.minecrafttas.lotas.mixin.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecrafttas.lotas.system.ModSystem;

import net.minecraft.server.MinecraftServer;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ModSystem}.
 * @author Pancake
 */
@Mixin(MinecraftServer.class)
public class HookMinecraftServer {

	/**
	 * Trigger event in {@link ModSystem#onServerTick(MinecraftServer)} after the server has ticked
	 * @param ci Callback Info
	 */
	@Inject(method = "runServer", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"))
	public void hookTickEvent(CallbackInfo ci) {
		ModSystem.onServerTick();
	}

	/**
	 * Trigger event in {@link ModSystem#onServerLoad(MinecraftServer)} before the server enters the tick loop
	 * @param ci Callback Info
	 */
	@Inject(method = "<init>", at = @At("RETURN"))
	public void hookInitEvent(CallbackInfo ci) {
		ModSystem.onServerLoad((MinecraftServer) (Object) this);
	}

}
