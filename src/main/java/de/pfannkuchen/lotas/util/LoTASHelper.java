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
	public static int[] takeScreenshot(Minecraft mc, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage oimg = new BufferedImage(mc.getWindow().getScreenWidth(), mc.getWindow().getScreenHeight(), BufferedImage.TYPE_INT_RGB);
		NativeImage nimg = Screenshot.takeScreenshot(mc.getMainRenderTarget());
		
		int[] is = new int[nimg.getWidth() * nimg.getHeight()];
        for (int i = 0; i < nimg.getHeight(); ++i) {
            for (int j = 0; j < nimg.getWidth(); ++j) {
                int k = nimg.getPixelRGBA(j, i);
                int l = NativeImage.getA(k);
                int o = NativeImage.getB(k);
                int n = NativeImage.getG(k);
                int m = NativeImage.getR(k);
                is[j + i * nimg.getWidth()] = l << 24 | o << 16 | n << 8 | m;
            }
        }
		
		oimg.setRGB(0, 0, oimg.getWidth(), oimg.getHeight(), is, 0, oimg.getWidth());
		Graphics2D g = img.createGraphics();
		g.drawImage(oimg, 0, 0, width, height, 0, 0, oimg.getWidth(), oimg.getHeight(), null);
		int[] outArray = new int[width*height];
		img.getRGB(0, 0, width, height, outArray, 0, width);
		return outArray;
	}
	
}
