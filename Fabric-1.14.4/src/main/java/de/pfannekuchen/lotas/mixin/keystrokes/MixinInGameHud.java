package de.pfannekuchen.lotas.mixin.keystrokes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
    @Shadow public abstract TextRenderer getFontRenderer();

    @Shadow private int scaledHeight;

    @Inject(method = "render(F)V", at = @At("TAIL"))
    void render(float f, CallbackInfo info) {
        int i = 1;
        for (KeyBinding key : MinecraftClient.getInstance().options.keysAll) {
            if (!key.isPressed()) {
                continue;
            }
            String name = key.getLocalizedName().toUpperCase(Locale.ROOT);
            if (name.equalsIgnoreCase("left shift")) {
                name = "lshift".toUpperCase(Locale.ROOT);
            } else if (name.equalsIgnoreCase("right shift")) {
                name = "rshift".toUpperCase(Locale.ROOT);
            } else if (name.equalsIgnoreCase("left button")) {
                name = "lc".toUpperCase(Locale.ROOT);
            } else if (name.equalsIgnoreCase("right button")) {
                name = "rc".toUpperCase(Locale.ROOT);
            }
            this.getFontRenderer().drawWithShadow(name, i, this.scaledHeight - this.getFontRenderer().fontHeight - 1, 0xffffff);
            i += this.getFontRenderer().getStringWidth(name) + this.getFontRenderer().getStringWidth(" ");
        }
    }
}
