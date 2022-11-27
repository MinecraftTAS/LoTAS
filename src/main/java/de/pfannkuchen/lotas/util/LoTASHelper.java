package de.pfannkuchen.lotas.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

/**
 * Contains various STATIC Helper Methods
 * @author Pancake
 */
public class LoTASHelper {

	/**
	 * Takes a screenshot and resizes it to the given resolution. May cause lag spike
	 * @param mc Instance of minecraft
	 * @param width Scaled width of the image
	 * @param height Scaled height of the image
	 * @return The Screenshot taken from a buffered image
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
		int[] outArray = new int[width * height];
		img.getRGB(0, 0, width, height, outArray, 0, width);
		return outArray;
	}

	/**
	 * Writes a String to a Byte Buffer
	 * @param buf Buffer to read from
	 * @param string String to write
	 */
	public static void writeString(ByteBuffer buf, String string) {
		byte[] str = string.getBytes(StandardCharsets.UTF_8);
		buf.putInt(str.length);
		buf.put(str);
	}

	/**
	 * Reads a String from a Byte Buffer
	 * @param buf Buffer to read from
	 * @return String read from bytebuffer
	 */
	public static String readString(ByteBuffer buf) {
		byte[] str = new byte[buf.getInt()];
		buf.get(str);
		return new String(str, StandardCharsets.UTF_8);
	}

}
