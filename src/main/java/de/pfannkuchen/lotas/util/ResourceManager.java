package de.pfannkuchen.lotas.util;

import java.io.IOException;

import com.mojang.blaze3d.platform.NativeImage;

import de.pfannkuchen.lotas.LoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * A Resource Manager than loads texture for LoTAS
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class ResourceManager {

	public static final ResourceLocation UNSUPPORTED = new ResourceLocation("lotas", "unsupported");
	
	/**
	 * Initially load all assets packed within the LoTAS Jar
	 */
	public static void load(Minecraft mc) {
		try {
			mc.getTextureManager().register(UNSUPPORTED, new DynamicTexture(NativeImage.read(ResourceManager.class.getResourceAsStream("/unsupported.png"))));
		} catch (IOException e) {
			LoTAS.LOGGER.error("Unable to load textures.", e);
		}
	}
	
}
