package de.pfannekuchen.lotas.mixin.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.gui.GuiConfiguration;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin(TitleScreen.class)
public abstract class InjectTitleScreen extends Screen {

    protected InjectTitleScreen(Text title) {
        super(title);
    }

    /**
     * This Redirect changes the splash screen
     */
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    private void redirectdrawSplash(TitleScreen titleScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
        titleScreen.drawCenteredString(textRenderer, "TaS iS cHeAtInG !!1", centerX, y, color);
    }

    /**
     * This Mixin removes the multiplayer and realms button from the Title Screen
     */
    @Inject(at = @At("RETURN"), method = "init")
    public void injectinit(CallbackInfo ci) {
        this.buttons.get(1).active = false; // Disable Multiplayer Button
        this.buttons.get(2).setMessage("Configuration");
        ((ButtonWidget) this.buttons.get(2)).onPress = c -> {
        	minecraft.openScreen(new GuiConfiguration());
        };
    }

}
