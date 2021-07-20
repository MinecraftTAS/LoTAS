package de.pfannekuchen.lotas.core.utils;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;

/**
 * A LWJGL style keyboard method
 * @author ScribbleLP
 */
public class Keyboard {

	public static boolean isKeyDown(int keyCode) {
		return GLFW.glfwGetKey(Minecraft.getInstance().window.getWindow(), keyCode) == GLFW.GLFW_KEY_DOWN;
	}

	public static boolean isPressed(int keyCode) {
		return GLFW.glfwGetKey(Minecraft.getInstance().window.getWindow(), keyCode) == GLFW.GLFW_PRESS;
	}
}
