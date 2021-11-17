package de.pfannkuchen.lotas;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.loscreen.LoScreenManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * LoTAS Fabric Mod Core for the Client only.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class ClientLoTAS implements ClientModInitializer {

	// Client-side LoTAS Singleton
	public static ClientLoTAS instance;
	// LoScreen Manager Singleton
	public static LoScreenManager loscreenmanager;
	
	@Override
	public void onInitializeClient() {
		ClientLoTAS.instance = this;
		ClientLoTAS.loscreenmanager = new LoScreenManager();
	}

	/**
	 * Executed after the rendering engine launches.
	 * @param mc Instance of Minecraft
	 */
	public void onRenderInitialize(Minecraft mc) {
		// Initialize LoScreens
		ClientLoTAS.loscreenmanager.onGameInitialize(mc);
	}
	
	/**
	 * Executed before the JVM stops.
	 * @param mc Instance of Minecraft
	 */
	public void onShutdown(Minecraft mc) {
		
	}
	
	/**
	 * Executed every tick of the game.
	 * @param mc Instance of Minecraft
	 */
	public void onTick(Minecraft mc) {

	}
	
	/**
	 * Executed every time the game logic loops.
	 * @param mc Instance of Minecraft
	 */
	public void onGameLoop(Minecraft mc) {
		// Update LoScreens
		ClientLoTAS.loscreenmanager.onGameLoop(mc);
	}

	/**
	 * Executed after the gui screens are rendered. Only executed while in a world
	 * @param stack Pose Stack for rendering
	 * @param mc Instance of Minecraft
	 */
	public void onRenderScreen(PoseStack stack, Minecraft mc) {
		// Render LoScreen
		loscreenmanager.onGuiRender(stack, mc);
	}
	
}
