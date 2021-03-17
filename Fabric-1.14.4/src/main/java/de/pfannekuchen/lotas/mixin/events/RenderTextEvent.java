package de.pfannekuchen.lotas.mixin.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public class RenderTextEvent {

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay()V"), method = "Lnet/minecraft/client/gui/hud/InGameHud;render(F)V")
    public void injectrender(float tickDelta, CallbackInfo ci) {
        Timer.onDraw();
        TickrateChanger.onDraw();
    }

}
