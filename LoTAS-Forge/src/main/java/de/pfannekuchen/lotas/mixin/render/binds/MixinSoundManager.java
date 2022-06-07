package de.pfannekuchen.lotas.mixin.render.binds;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.pfannekuchen.lotas.core.utils.quack.SoundManagerDuck;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;

@Mixin(SoundManager.class)
public class MixinSoundManager implements SoundManagerDuck{
	
	@Shadow
	private boolean loaded;
	@Shadow
	private Map<String, ISound> playingSounds;
	@Shadow
	private SoundManager.SoundSystemStarterThread sndSystem;

	@Override
	public void updatePitch() {
		if (this.loaded)
        {
            playingSounds.forEach((sourceName, sound)->{
            	sndSystem.setPitch(sourceName, sound.getPitch());
            });
        }
	}
	
}
