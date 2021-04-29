package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.Binds;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiGameOver.class)
public abstract class MixinGuiGameOver extends GuiScreen {

	
	@Inject(method = "initGui", at = @At("RETURN"))
	public void injectinitGui(CallbackInfo ci) {
        buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120, "Loadstate"));
	}
	
	@Inject(method = "actionPerformed", at = @At("HEAD"))
	public void injectactionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 2) {
        	mc.displayGuiScreen(new GuiIngameMenu());
        	Binds.shouldLoadstate = true;
        }
	}
	
}
