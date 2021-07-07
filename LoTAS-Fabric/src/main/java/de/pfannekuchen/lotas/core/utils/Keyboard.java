package de.pfannekuchen.lotas.core.utils;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * A LWJGL style keyboard method
 * @author ScribbleLP
 *
 */
public class Keyboard {

	public static boolean isKeyDown(int keyCode) {
		Minecraft mc = Minecraft.getInstance();
		return GLFW.glfwGetKey(mc.window.getWindow(), keyCode) == GLFW.GLFW_KEY_DOWN;
	}

	public static boolean isPressed(int keyCode) {
		Minecraft mc = Minecraft.getInstance();
		return GLFW.glfwGetKey(mc.window.getWindow(), keyCode) == GLFW.GLFW_PRESS;
	}
}
