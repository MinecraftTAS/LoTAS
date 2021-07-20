package de.pfannekuchen.lotas.core.utils;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * File that can dynamically load Texture
 * @author Pancake
 * @since v2.0
 * @version v2.0
 */
public class TextureYoinker {

	public static ResourceLocation downloadShield(String uuid, InputStream image) {
		NativeImage img = null;
		try {
			img = NativeImage.read(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DynamicTexture nIBT = new DynamicTexture(img);
		nIBT.bind();
		return Minecraft.getInstance().getTextureManager().register(uuid.replace("-", ""), nIBT);
	}

	public static NativeImage parseImage(NativeImage image) {
		int imageWidth = image.getWidth() / 2;
		int imageHeight = image.getHeight() / 2;
		int imageSrcWidth = image.getWidth();
		int srcHeight = image.getHeight();

		for (int imageSrcHeight = image.getHeight(); imageWidth < imageSrcWidth || imageHeight < imageSrcHeight; imageHeight *= 2) {
			imageWidth *= 2;
		}

		NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
		for (int x = 0; x < imageSrcWidth; x++) {
			for (int y = 0; y < srcHeight; y++) {
				imgNew.setPixelRGBA(x, y, image.getPixelRGBA(x, y));
			}
		}
		image.close();
		return imgNew;
	}

	public static ResourceLocation download(String name, InputStream stream) {
		NativeImage img = null;
		try {
			img = NativeImage.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		DynamicTexture nIBT = new DynamicTexture(parseImage(img));
		return Minecraft.getInstance().getTextureManager().register(name, nIBT);
	}
}