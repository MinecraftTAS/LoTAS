package de.pfannkuchen.lotas.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Checks the operating system
 * @author Pancake
 *
 */
@Environment(EnvType.CLIENT) // Currently only used on the Client, remove if necessary
public class OSUtils {
	public enum OS {
		WINDOWS, LINUX, MACOS
	}

	// Cache
	private static OS os = null;

	/**
	 * Try to get the operating system set via java
	 * @return OS Operating System
	 */
	public static final OS getOS() {
		if (OSUtils.os == null) {
			final String osname = System.getProperty("os.name").toLowerCase();
			if (osname.contains("win")) {
				OSUtils.os = OS.WINDOWS;
			} else if (osname.contains("nix") || osname.contains("nux") || osname.contains("aix")) {
				OSUtils.os = OS.LINUX;
			} else if (osname.contains("mac")) {
				OSUtils.os = OS.MACOS;
			}
		}
		return OSUtils.os;
	}
}