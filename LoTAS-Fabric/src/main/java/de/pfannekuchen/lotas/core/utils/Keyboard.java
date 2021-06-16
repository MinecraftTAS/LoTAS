package de.pfannekuchen.lotas.core.utils;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;

/**
 * A LWJGL style keyboard method
 * @author ScribbleLP
 *
 */
public class Keyboard {

	public static boolean isKeyDown(int keyCode) {
		MinecraftClient mc = MinecraftClient.getInstance();
		return GLFW.glfwGetKey(mc.window.getHandle(), keyCode) == GLFW.GLFW_KEY_DOWN;
	}

	public static boolean isPressed(int keyCode) {
		MinecraftClient mc = MinecraftClient.getInstance();
		return GLFW.glfwGetKey(mc.window.getHandle(), keyCode) == GLFW.GLFW_PRESS;
	}
}
