package de.pfannekuchen.lotas.mixin;

import static rlog.RLogAPI.logError;

import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.LoTAS;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import rlog.RLogAPI;

@Mixin(BlazeEntity.class)
public class MixinBlaze {

	@Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
	public void replaceDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
		if (new Random().nextBoolean()) {
			RLogAPI.logDebug("[Sounds] Hijacking Blaze Sound");
			
			try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(LoTAS.class.getResourceAsStream("data.wav")));
				((FloatControl) clip.getControl(Type.MASTER_GAIN)).setValue(-10);
				clip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				logError(e1, "Sound couldn't be played #5");
				e1.printStackTrace();
				System.exit(1);
			}
			cir.setReturnValue(null);
			cir.cancel();
		} else {
			cir.setReturnValue(SoundEvents.ENTITY_BLAZE_DEATH);
			cir.cancel();
		}
	}
	
}
