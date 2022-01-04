package de.pfannkuchen.lotas.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

/**
 * Contains various STATIC Helper Methods
 * @author Pancake
 */
public class LoTASHelper {

	/**
	 * Takes a Screenshot and resizes it to the given Resolution. May cause lag spike
	 * @param mc Instance of Minecraft
	 * @param width Scaled Width of the image
	 * @param height Scaled Height of the image
	 * @return The Screenshot taken from a Buffered Image
	 */
	@SuppressWarnings("deprecation")
	public static int[] takeScreenshot(Minecraft mc, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage oimg = new BufferedImage(mc.getWindow().getScreenWidth(), mc.getWindow().getScreenHeight(), BufferedImage.TYPE_INT_RGB);
		NativeImage nimg = Screenshot.takeScreenshot(mc.getMainRenderTarget());
		oimg.setRGB(0, 0, oimg.getWidth(), oimg.getHeight(), nimg.makePixelArray(), 0, oimg.getWidth()); // FIXME: panic panic panic panic panic panic panic panic deprecated aaaaaaaaahhhhhhhhhhhh
		Graphics2D g = img.createGraphics();
		g.drawImage(oimg, 0, 0, width, height, 0, 0, oimg.getWidth(), oimg.getHeight(), null);
		int[] outArray = new int[width*height];
		img.getRGB(0, 0, width, height, outArray, 0, width);
		return outArray;
	}
	
}
