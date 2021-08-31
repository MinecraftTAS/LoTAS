package de.pfannekuchen.lotas.mixin.patches;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

/**
 * Allows for removing the inaccuracy from throwables
 * (is theoretically possible if the stars align on 3 gaussian randoms)
 *
 * @author CittyKat
 */
//#if MC>=11600
//$$ @Mixin(net.minecraft.world.entity.projectile.Projectile.class)
//#else
@Mixin(net.minecraft.world.entity.projectile.ThrowableProjectile.class)
//#endif
public class MixinInaccuracyPatch {
    @Redirect(method = "shoot(DDDFF)V", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", remap = false))
    private double redirect_internalShoot(Random random) {
        if (ConfigUtils.getBoolean("tools", "removeThrowableInaccuracy")) {
            return 0;
        } else {
            return random.nextGaussian();
        }
    }
}
