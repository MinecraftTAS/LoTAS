package de.pfannkuchen.lotas.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.blaze3d.platform.InputConstants.Key;

import de.pfannkuchen.lotas.mods.KeybindManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;

/**
 * This mixin accessor makes the categories array for {@link KeybindManager} accessible.
 * @author Scribble
 */
@Mixin(KeyMapping.class)
@Environment(EnvType.CLIENT)
public interface MixinKeyMappingsAccessor {

	/**
	 * This Accessor opens the static field containing all Categories with orders
	 * @return CATEGORY_SORT_ORDER
	 */
	@Accessor("CATEGORY_SORT_ORDER")
	public static Map<String, Integer> getCategorySorting() {
		throw new AssertionError();
	}
	
	/**
	 * This Accessor opens the private field containing the Key for the Key Mapping
	 * @return
	 */
	@Accessor
	public Key getKey();
	
}
