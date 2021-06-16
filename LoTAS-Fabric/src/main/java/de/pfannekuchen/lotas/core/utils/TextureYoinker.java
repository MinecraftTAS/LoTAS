package de.pfannekuchen.lotas.core.utils;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

public class TextureYoinker {

	public static Identifier downloadShield(String uuid, InputStream image) {
		NativeImage img = null;
		try {
			img = NativeImage.read(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

		NativeImageBackedTexture nIBT = new NativeImageBackedTexture(img);
		nIBT.bindTexture();
		return MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(uuid.replace("-", ""), nIBT);
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
				//#if MC>=11601
//$$ 						imgNew.setPixelColor(x, y, image.getPixelColor(x, y));
				//#else
				imgNew.setPixelRgba(x, y, image.getPixelRgba(x, y));
				//#endif
			}
		}
		image.close();
		return imgNew;
	}

	public static Identifier download(String name, InputStream stream) {
		NativeImage img = null;
		try {
			img = NativeImage.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		NativeImageBackedTexture nIBT = new NativeImageBackedTexture(parseImage(img));
		return MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(name, nIBT);
	}
}