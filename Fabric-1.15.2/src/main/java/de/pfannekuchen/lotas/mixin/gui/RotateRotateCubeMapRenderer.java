package de.pfannekuchen.lotas.mixin.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.RotatingCubeMapRenderer;

@Mixin(RotatingCubeMapRenderer.class)
public class RotateRotateCubeMapRenderer {

    @Shadow
    public float time;

    /**
     * This Mixin rotates the Cubemap by 45°
     */
    @Inject(at = @At("RETURN"), method = "<init>")
    public void injectinit(CallbackInfo ci) {
        time = 600;
    }

}
