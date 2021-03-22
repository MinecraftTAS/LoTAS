package de.pfannekuchen.lotas.mixin;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.savestates.SavestateMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiGameOver.class)
public abstract class MixinGuiGameOver extends GuiScreen {

	@Shadow
	private int enableButtonsTimer;
	
	@Inject(method = "initGui", at = @At("RETURN"))
	public void injectinitGui(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120, "Loadstate"));
        this.buttonList.get(this.buttonList.size() - 1).enabled = false;
        enableButtonsTimer = 18;
	}

	@Inject(method = "updateScreen", at = @At("RETURN"))
	public void injectupdateScreen(CallbackInfo ci) {
		if (enableButtonsTimer == 20) {
			this.buttonList.get(this.buttonList.size() - 1).enabled = true;
		}
	}

	
	@Inject(method = "actionPerformed", at = @At("HEAD"))
	public void injectactionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 2) {
        	try {
				SavestateMod.savestate();
			} catch (IOException e) {
				System.err.println("Failed to load a savestate: "+e.getCause().toString());
				e.printStackTrace();
			}
        }
	}
	
}
