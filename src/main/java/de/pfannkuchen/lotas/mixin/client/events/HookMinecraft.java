package de.pfannkuchen.lotas.mixin.client.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.ClientLoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/**
 * This mixin is purely responsible for the hooking up the events in {@link ClientLoTAS}.
 * @author Pancake
 */
@Mixin(Minecraft.class)
@Environment(EnvType.CLIENT)
public class HookMinecraft {

	/**
	 * Triggers an Event in {@link ClientLoTAS#onRenderInitialize(Minecraft)} before the game enters the game loop
	 * @param ci Callback Info
	 */
	@Inject(method = "run", at = @At("HEAD"))
	public void hookGameInitEvent(CallbackInfo ci) {
		ClientLoTAS.instance.onRenderInitialize((Minecraft) (Object) this);
	}

	/**
	 * Triggers an Event in {@link ClientLoTAS#onShutdown(Minecraft)} before the JVM shuts down.
	 * @param ci Callback Info
	 */
	@Inject(method = "close", at = @At("RETURN"))
	public void hookGameCloseEvent(CallbackInfo ci) {
		ClientLoTAS.instance.onShutdown((Minecraft) (Object) this);
	}

	/**
	 * Triggers an Event in {@link ClientLoTAS#onTick(Minecraft)} every tick.
	 * @param ci Callback Info
	 */
	@Inject(method = "tick", at = @At("HEAD"))
	public void hookTickEvent(CallbackInfo ci) {
		ClientLoTAS.instance.onTick((Minecraft) (Object) this);
	}

	/**
	 * Triggers an Event in {@link ClientLoTAS#onGameLoop(Minecraft)} every game logic loop.
	 * @param ci Callback Info
	 */
	@Inject(method = "runTick", at = @At("HEAD"))
	public void hookGameLoopEvent(CallbackInfo ci) {
		ClientLoTAS.instance.onGameLoop((Minecraft) (Object) this);
	}

	/**
	 * Triggers an Event in {@link ClientLoTAS#onGuiUpdate(Screen, Minecraft)} every screen change. Can also cancel the event.
	 * @param screen New Screen
	 * @param ci Callback Info
	 */
	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	public void hookScreenUpdateEvent(Screen screen, CallbackInfo ci) {
		if (ClientLoTAS.instance.onGuiUpdate(screen, (Minecraft) (Object) this)) ci.cancel();
	}

	/**
	 * Triggers an Event in {@link ClientLoTAS#onClientDisconnect()} if the player disconnects
	 * @param ci Callback Info
	 */
	@Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
	public void hookDisconnectEvent(CallbackInfo ci) {
		ClientLoTAS.instance.onClientDisconnect();
	}

}
