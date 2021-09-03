package de.pfannekuchen.lotas.mixin.patches;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.entity.projectile.EntityThrowable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

/**
 * Allows for removing the inaccuracy from throwables
 * (is theoretically possible if the stars align on 3 gaussian randoms)
 *
 * @author CittyKat
 */
@Mixin(EntityThrowable.class)
public abstract class MixinInaccuracyPatch {
	//#if MC>=11200
	@Redirect(method = "shoot(DDDFF)V", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", remap = false))
	//#else
//$$ 	@Redirect(method = "setThrowableHeading", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", remap = false))
	//#endif
    private double redirect_internalShoot(Random random) {
        if (ConfigUtils.getBoolean("tools", "removeThrowableInaccuracy")) {
            return 0;
        } else {
            return random.nextGaussian();
        }
    }
}
