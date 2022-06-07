package de.pfannkuchen.lotas.loscreen;

import java.util.Map.Entry;
import java.util.Collections;
import java.util.TreeMap;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.gui.EmptyScreen;
import de.pfannkuchen.lotas.gui.MainLoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/**
 * Renders and manages the loscreens.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class LoScreenManager {

	// Currently drawn LoScreen
	private LoScreen screen;
	// Shared minecraft instance
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
	// Last tickadvance state
	private boolean lastTickadvance;
	// All notifications onscreen
	private TreeMap<Long, Component> timestamps = new TreeMap<>(Collections.reverseOrder());
	
	/**
	 * Returns whether a screen is opened.
	 * @return Is screen opened?
	 */
	public boolean isScreenOpened() {
		return this.screen != null;
	}

	/**
	 * Updates the current LoScreen and adds the minecraft instance. Can be null.
	 * @param screen Update LoScreen
	 */
	public void setScreen(@Nullable LoScreen screen) {
		this.screen = screen;
		if (screen != null) {
			screen.mc = this.mc;
			screen.reset(this.lastWidth, this.lastHeight);
			if (this.mc.screen == null) this.mc.setScreen(new EmptyScreen());
		} else
			if (this.mc.screen instanceof EmptyScreen) this.mc.setScreen(null);
		// Reinitialize current vanilla screen
		if (this.mc.screen != null) this.mc.screen.resize(this.mc, this.mc.getWindow().getGuiScaledWidth(), this.mc.getWindow().getGuiScaledHeight());
	}

	/**
	 * Updates the screen.
	 * @param mc Instance of minecraft
	 */
	public void onGameLoop(Minecraft mc) {
		// Update screen size
		final Window w = mc.getWindow();
		final int width = w.getWidth();
		final int height = w.getHeight();
		// Update inputs
		final double posX = mc.mouseHandler.xpos() / width;
		final double posY = mc.mouseHandler.ypos() / height;
		final boolean isLeftPressed = GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
		final boolean isMiddlePressed = GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS;
		final boolean isRightPressed = GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
		// Check whether clicks/moves have passed.
		if (this.screen != null) {
			if (width != this.lastWidth || height != this.lastHeight) {
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
	 * Adds a notification to the list of notifications
	 */
	public void addNotification(Component message) {
		this.timestamps.put(System.currentTimeMillis(), message);
	}

	/**
	 * Updates the minecraft instance once the game launches
	 * @param mc Instance of minecraft
	 */
	public void onGameInitialize(Minecraft mc) {
		this.mc = mc;
	}

	/**
	 * Renders the next LoScreen
	 * @param stack Pose Stack for rendering
	 * @param mc Instance of minecraft
	 */
	public void onGuiRender(PoseStack stack, Minecraft mc) {
		// Render LoScreen
		if (this.screen != null) this.screen.render(stack, this.lastPosX, this.lastPosY);
		// Render Notifications
		if (mc.player != null) {		
			stack.scale(0.66f, 0.66f, 0.66f);

			long currentTime = System.currentTimeMillis();
			int i = 0;
			for (Entry<Long, Component> entry : new TreeMap<>(this.timestamps).entrySet()) {
				String s = entry.getValue().getString();
				String s1 = s.split("_", 2)[0];
				String s2 = s.split("_", 2)[1];
				
				// Opacity modifier
				long millis = currentTime - entry.getKey() - 1000;
				if (millis > 1000)
					timestamps.remove(entry.getKey());
				float f = Mth.clamp(1 - (millis > 1000 ? 1 : (millis / 1000.0f)), 0, 1);
				int fb = (byte) (f*235)+20;
				// Render notification
				RenderSystem.enableBlend();
				RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, f);
				PlayerInfo playerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(UUID.fromString(s1));
				if (playerinfo != null) {
					RenderSystem.setShaderTexture(0, playerinfo.getSkinLocation());
					Gui.blit(stack, 5, 5+i*20, 16.0f, 16.0f, 16, 16, 128, 128);
				}
				Gui.drawString(stack, mc.font, s2, 30, 10+i*20, 0x00149b5b | (fb<<24));
				RenderSystem.disableBlend();
				i++;
			}
			
			
			stack.scale(2.0f, 2.0f, 2.0f);
		}
	}

	/**
	 * Closes the LoScreen when the vanilla one changes
	 * @param vanillaScreen Screen updated to
	 * @param mc Instance of minecraft
	 * @return Should cancel
	 */
	public boolean onScreenUpdate(Screen vanillaScreen, Minecraft mc) {
		if (vanillaScreen instanceof EmptyScreen) return false; // don't close on intended screen
		if (this.screen != null && vanillaScreen == null) { // close the screen
			this.toggleLoTASMenu(mc);
			return true;
		}
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

	/**
	 * Toggles on or off the LoTAS Menu and opens a gui Screen in case there isn't one to regain the cursor.
	 * @param mc Instance of minecraft
	 */
	public void toggleLoTASMenu(Minecraft mc) {
		if (mc.level == null) return;
		if (this.isScreenOpened()) {
			if (this.getScreen() instanceof MainLoScreen) {
				this.setScreen(null);
				// Disable tick advance if it was not on before
				if (!this.lastTickadvance) LoTAS.tickadvance.requestTickadvanceToggle();
			}
		} else {
			this.setScreen(new MainLoScreen());
			// Update tick advance if it is not enabled already
			this.lastTickadvance = LoTAS.tickadvance.isTickadvanceEnabled();
			if (!this.lastTickadvance) LoTAS.tickadvance.requestTickadvanceToggle();
		}
	}

	/**
	 * Sends a key press to all LoScreens
	 * @param key Key that was pressed
	 */
	public void onKeyPress(int key) {
		if (this.screen != null) {
			this.screen.press(key);
		}
	}
	
	/**
	 * Sends a mouse scroll to all LoScreens
	 * @param scroll Scroll amount
	 */
	public void onMouseScroll(double scroll) {
		if (this.screen != null) {
			this.screen.scroll(scroll);
		}
	}

}
