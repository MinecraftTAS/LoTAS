package de.pfannkuchen.lotas;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.gui.MainLoScreen;
import de.pfannkuchen.lotas.gui.api.LoScreenRenderer;
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
	// LoScreen Renderer Singleton
	public static LoScreenRenderer loscreenrenderer;
	
	@Override
	public void onInitializeClient() {
		ClientLoTAS.instance = this;
		ClientLoTAS.loscreenrenderer = new LoScreenRenderer();
	}

	/**
	 * Executed after the rendering engine launches.
	 * @param mc Instance of Minecraft
	 */
	public void onRenderInitialize(Minecraft mc) {
		// Initialize LoScreens
		ClientLoTAS.loscreenrenderer.onGameInitialize(mc);
		// WARNING: EXAMPLE CODE
		ClientLoTAS.loscreenrenderer.setScreen(new MainLoScreen());
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
		ClientLoTAS.loscreenrenderer.onGameLoop(mc);
	}

	/**
	 * Executed after the gui screens are rendered. Only executed while in a world
	 * @param stack Pose Stack for rendering
	 * @param mc Instance of Minecraft
	 */
	public void onRenderScreen(PoseStack stack, Minecraft mc) {
		// Render LoScreen
		loscreenrenderer.onGuiRender(stack, mc);
	}
	
}
