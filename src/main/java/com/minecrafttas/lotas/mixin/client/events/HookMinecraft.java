package com.minecrafttas.lotas.mixin.client.events;

import com.minecrafttas.lotas.system.KeybindSystem;
import com.minecrafttas.lotas.system.ModSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ModSystem}.
 *
 * @author Pancake
 */
@Mixin(Minecraft.class)
@Environment(EnvType.CLIENT)
public class HookMinecraft {

	/**
	 * Trigger event in {@link ModSystem#onClientsideRenderInitialize(Minecraft)} before the game enters the game loop.
	 *
	 * @param ci Callback Info
	 */
	@Inject(method = "run", at = @At("HEAD"))
	public void hookGameInitEvent(CallbackInfo ci) {
		ModSystem.onClientsideRenderInitialize((Minecraft) (Object) this);
	}

	/**
	 * Trigger event in {@link ModSystem#onClientsideShutdown()} before the JVM shuts down.
	 *
	 * @param ci Callback Info
	 */
	@Inject(method = "close", at = @At("RETURN"))
	public void hookGameCloseEvent(CallbackInfo ci) {
		ModSystem.onClientsideShutdown();
	}

	/**
	 * Trigger event in {@link ModSystem#onClientsideTick()} every tick.
	 *
	 * @param ci Callback Info
	 */
	@Inject(method = "tick", at = @At("HEAD"))
	public void hookTickEvent(CallbackInfo ci) {
		ModSystem.onClientsideTick();
	}

	/**
	 * Trigger event in {@link ModSystem#onClientsideGameLoop()} every game logic loop and updates the keybind system in {@link KeybindSystem#onGameLoop(Minecraft)}
	 *
	 * @param ci Callback Info
	 */
	@Inject(method = "runTick", at = @At("HEAD"))
	public void hookGameLoopEvent(CallbackInfo ci) {
		ModSystem.onClientsideGameLoop();
		KeybindSystem.onGameLoop((Minecraft) (Object) this);
	}

	/**
	 * Trigger event in {@link ModSystem#onClientsideDisconnect()} when a player disconnects
	 *
	 * @param ci Callback Info
	 */
	@Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
	public void hookDisconnectEvent(CallbackInfo ci) {
		ModSystem.onClientsideDisconnect();
	}
	
	/**
	 * Trigger event in {@link ModSystem#onClientsidePostRender()} after the game has rendered the frame
	 *
	 * @param ci Callback Info
	 */
	@Inject(method = "runTick", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/gui/components/toasts/ToastComponent;render(Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
	public void hookPostRenderEvent(CallbackInfo ci) {
		ModSystem.onClientsidePostRender();
	}

}
