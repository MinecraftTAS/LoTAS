package de.pfannkuchen.lotas.loscreen;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.gui.EmptyScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/**
 * Renders and manages the LoScreens.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class LoScreenManager {
	
	// Currently drawn LoScreen
	private LoScreen screen;
	// Shared Minecraft Instance
	private Minecraft mc;
	// Last resolution in case of resizing
	private int lastWidth;
	private int lastHeight;
	// Last state of mouse buttons
	private boolean wasLeftPressed;
	private boolean wasMiddlePressed;
	private boolean wasRightPressed;
	// Last mouse position
	private double lastPosX;
	private double lastPosY;
	
	/**
	 * Returns whether a screen is opened.
	 * @return Is screen opened?
	 */
	public boolean isScreenOpened() {
		return this.screen != null;
	}
	
	/**
	 * Updates the current LoScreen and adds the Minecraft Instance. Can be null.
	 * @param screen Update LoScreen
	 */
	public void setScreen(@Nullable LoScreen screen) {
		this.screen = screen;
		if (screen != null) {
			screen.mc = this.mc;
			screen.reset(this.lastWidth, this.lastHeight);
			if (mc.screen == null) mc.setScreen(new EmptyScreen());
		} else
			if (mc.screen instanceof EmptyScreen) mc.setScreen(null);
		// Reinitialize current vanilla screen
		if (this.mc.screen != null) this.mc.screen.resize(this.mc, this.mc.getWindow().getGuiScaledWidth(), this.mc.getWindow().getGuiScaledHeight());
	}
	
	/**
	 * Updates the Screen.
	 * @param mc Instance of Minecraft
	 */
	public void onGameLoop(Minecraft mc) {
		// Update Screen size
		final Window w = mc.getWindow();
		final int width = w.getWidth();
		final int height = w.getHeight();
		// Update Inputs
		final double posX = mc.mouseHandler.xpos() / width;
		final double posY = mc.mouseHandler.ypos() / height;
		final boolean isLeftPressed = GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
		final boolean isMiddlePressed = GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS;
		final boolean isRightPressed = GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
		// Check whether clicks/moves have passed.
		if (screen != null) {
			if (width != lastWidth || height != lastHeight) {
				this.lastWidth = width;
				this.lastHeight = height;
				this.setScreen(this.screen);
			}
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
	 * Closes the LoScreen when the vanilla one changes
	 * @param vanillaScreen Screen updated to
	 * @param mc Instance of Minecraft
	 * @return Should cancel
	 */
	public boolean onScreenUpdate(Screen vanillaScreen, Minecraft mc) {
		if (vanillaScreen instanceof EmptyScreen) return false; // don't close on intended screen
		if (this.screen != null)
			return true;
		return false;	
	}
	
	/**
	 * Getter
	 * @return the screen
	 */
	public LoScreen getScreen() {
		return this.screen;
	}
	
}
