package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;

@Mixin(SoundHandler.class)
public interface AccessorSoundManager {
	@Accessor
	public SoundManager getSndManager();
}
