package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;

@Mixin(SoundSystem.class)
public class BindSound {

    /**
     * @reason IntelliJ wants me to put this here idk.
     * @author Pancake
     */
    @Overwrite
    private float getAdjustedPitch(SoundInstance soundInstance) {
        return soundInstance.getPitch() * (TickrateChanger.tickrate / 20F);
    }

}

