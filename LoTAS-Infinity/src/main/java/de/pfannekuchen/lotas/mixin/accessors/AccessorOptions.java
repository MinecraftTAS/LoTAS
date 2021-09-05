package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;

@Mixin(Options.class)
public interface AccessorOptions {
	@Accessor("keyMappings")
	public void setKeyMappings(KeyMapping[] mappings);
}
