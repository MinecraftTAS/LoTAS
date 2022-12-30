package com.minecrafttas.lotas.mixin.client.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecrafttas.lotas.system.KeybindSystem;
import com.minecrafttas.lotas.system.ModSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ModSystem}.
 * @author Pancake
 */
@Mixin(Minecraft.class)
@Environment(EnvType.CLIENT)
public class HookMinecraft {

	/**
	 * Triggers an Event in {@link ModSystem#onClientsideRenderInitialize(Minecraft)} before the game enters the game loop
	 * @param ci Callback Info
	 */
	@Inject(method = "run", at = @At("HEAD"))
	public void hookGameInitEvent(CallbackInfo ci) {
		ModSystem.onClientsideRenderInitialize((Minecraft) (Object) this);
	}

	/**
	 * Triggers an Event in {@link ModSystem#onClientsideShutdown(Minecraft)} before the JVM shuts down.
	 * @param ci Callback Info
	 */
	@Inject(method = "close", at = @At("RETURN"))
	public void hookGameCloseEvent(CallbackInfo ci) {
		ModSystem.onClientsideShutdown();
	}

	/**
	 * Triggers an Event in {@link ModSystem#onClientsideTick(Minecraft)} every tick.
	 * @param ci Callback Info
	 */
	@Inject(method = "tick", at = @At("HEAD"))
	public void hookTickEvent(CallbackInfo ci) {
		ModSystem.onClientsideTick();
	}

	/**
	 * Triggers an Event in {@link ModSystem#onClientsideGameLoop(Minecraft)} every game logic loop and updates the keybind system in {@link KeybindSystem#onGameLoop(Minecraft)}
	 * @param ci Callback Info
	 */
	@Inject(method = "runTick", at = @At("HEAD"))
	public void hookGameLoopEvent(CallbackInfo ci) {
		ModSystem.onClientsideGameLoop();
		KeybindSystem.onGameLoop((Minecraft) (Object) this);
	}

	/**
	 * Triggers an Event in {@link ModSystem#onClientsideDisconnect()} if the player disconnects
	 * @param ci Callback Info
	 */
	@Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
	public void hookDisconnectEvent(CallbackInfo ci) {
		ModSystem.onClientsideDisconnect();
	}

}
