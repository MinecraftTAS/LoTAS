package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

@Mixin(GuiGameOver.class)
public abstract class MixinGuiGameOver extends GuiScreen {

	@Shadow
	private int enableButtonsTimer;
	
	@Inject(method = "initGui", at = @At("RETURN"))
	public void injectinitGui(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120, I18n.format("pausegui.lotas.buttontext.unshift.loadstate")));
	}

	@Inject(method = "actionPerformed", at = @At("HEAD"))
	public void injectactionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 2) {
        	mc.displayGuiScreen(new GuiIngameMenu());
        	KeybindsUtils.shouldLoadstate = true;
        }
	}
	
}
