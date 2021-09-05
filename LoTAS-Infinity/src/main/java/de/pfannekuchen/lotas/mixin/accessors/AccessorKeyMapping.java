package de.pfannekuchen.lotas.mixin.accessors;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.KeyMapping;

@Mixin(KeyMapping.class)
public interface AccessorKeyMapping {
	@Accessor("CATEGORY_SORT_ORDER")
	public static Map<String, Integer> getCategorySorting() {
		throw new AssertionError();
	}
}
