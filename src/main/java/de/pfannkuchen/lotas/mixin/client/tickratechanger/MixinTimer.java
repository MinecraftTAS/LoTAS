package de.pfannkuchen.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.pfannkuchen.lotas.LoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Timer;

/**
 * This Mixin slows down the integrated Timer making the game run slower
 * @author Pancake
 */
@Mixin(Timer.class)
@Environment(EnvType.CLIENT)
public class MixinTimer {

	// FIXME: Mixin wouldn't allow ModifyVariable, so i had to do this override. 
	
	@Shadow
	public float partialTick;
    @Shadow
    public float tickDelta;
    @Shadow
    private long lastMs;

    @Overwrite
    public int advanceTime(long l) {
        this.tickDelta = (float)(l - this.lastMs) / (float) LoTAS.tickratechanger.getMsPerTick();
        this.lastMs = l;
        this.partialTick += this.tickDelta;
        int i = (int)this.partialTick;
        this.partialTick -= (float)i;
        return i;
    }
	
}
