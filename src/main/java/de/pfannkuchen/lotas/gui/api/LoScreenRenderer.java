package de.pfannkuchen.lotas.gui.api;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * Renders and manages the LoScreens.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class LoScreenRenderer {
	
	// Currently drawn LoScreen
	private LoScreen screen;
	// Shared Minecraft Instance
	private Minecraft mc;
	// Last resolution in case of resizing
	private double lastWidth;
	private double lastHeight;
	// Last state of mouse buttons
	private boolean wasLeftPressed;
	private boolean wasMiddlePressed;
	private boolean wasRightPressed;
	// Last mouse position
	private double lastPosX;
	private double lastPosY;
	
	/**
	 * Updates the current LoScreen and adds the Minecraft Instance. Can be null.
	 * @param screen Update LoScreen
	 */
	public void setScreen(@Nullable LoScreen screen) {
		this.screen = screen;
		if (screen != null) {
			screen.mc = this.mc;
			screen.update(lastWidth, lastHeight);
		}
	}
	
	/**
	 * Updates the Screen.
	 * @param mc Instance of Minecraft
	 */
	public void onGameLoop(Minecraft mc) {
		// Update Inputs
		final double posX = mc.mouseHandler.xpos();
		final double posY = mc.mouseHandler.ypos();
		final boolean isLeftPressed = mc.mouseHandler.isLeftPressed();
		final boolean isMiddlePressed = mc.mouseHandler.isMiddlePressed();
		final boolean isRightPressed = mc.mouseHandler.isRightPressed();
		// Check whether clicks/moves have passed.
		if (screen != null) {
			if (!isLeftPressed && this.wasLeftPressed) this.screen.click(posX, posY, 0);
			if (!isMiddlePressed && this.wasMiddlePressed) this.screen.click(posX, posY, 2);
			if (!isRightPressed && this.wasRightPressed) this.screen.click(posX, posY, 1);
			if (isLeftPressed || isRightPressed || isMiddlePressed) this.screen.drag(this.lastPosX, this.lastPosY, posX, posY);
		}
		this.wasLeftPressed = isLeftPressed;
		this.wasMiddlePressed = isMiddlePressed;
		this.wasRightPressed = isRightPressed;
		this.lastPosX = posX;
		this.lastPosY = posY;
	}

	/**
	 * Updates the Minecraft Instance once the game launches
	 * @param mc Instance of Minecraft
	 */
	public void onGameInitialize(Minecraft mc) {
		this.mc = mc;
	}
	
	/**
	 * Renders the next LoScreen
	 * @param stack Pose Stack for rendering
	 * @param mc Instance of Minecraft
	 */
	public void onGuiRender(PoseStack stack, Minecraft mc) {
		if (this.screen != null) this.screen.render(stack, lastPosX, lastPosY);
	}

	/**
	 * Resizes the LoScreen.
	 * @param mc Instance of Minecraft
	 */
	public void onDisplayResize(Minecraft mc) {
		final Window w = mc.getWindow();
		// TODO: What fucking size goes here???
		this.lastWidth = w.getGuiScaledWidth();
		this.lastHeight = w.getGuiScaledHeight();
		if (this.screen != null) this.screen.update(this.lastWidth, this.lastHeight);
	}
	
}
