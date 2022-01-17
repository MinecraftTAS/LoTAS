package de.pfannekuchen.lotas.core.utils;

import org.lwjgl.glfw.GLFW;

import de.pfannekuchen.lotas.core.MCVer;

/**
 * A LWJGL style mouse method
 * @author ScribbleLP
 */
public class Mouse {

	public static boolean isKeyDown(int keyCode) {
		return GLFW.glfwGetMouseButton(MCVer.getGLWindow().getWindow(), keyCode) == GLFW.GLFW_PRESS;
	}
}
