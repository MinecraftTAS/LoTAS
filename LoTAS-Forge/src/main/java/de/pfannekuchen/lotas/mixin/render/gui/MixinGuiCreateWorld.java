package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

@Mixin(GuiCreateWorld.class)
public abstract class MixinGuiCreateWorld extends GuiScreen {

	@Shadow
	private boolean inMoreWorldOptionsDisplay;
	
	@Unique
	GuiTextField offsetX;
	@Unique
	GuiTextField offsetZ;
	
	@Inject(at = @At("TAIL"), method = "initGui")
	public void onInitGui(CallbackInfo ci) {
		offsetX = new GuiTextField(0, MCVer.getFontRenderer(Minecraft.getMinecraft()), this.width / 2 + 6, 125, 70, 20);
		offsetX.setText("X");
		offsetZ = new GuiTextField(0, MCVer.getFontRenderer(Minecraft.getMinecraft()), this.width / 2 + 14 + 70, 125, 70, 20);
		offsetZ.setText("Z");
	}

	@Inject(at = @At("TAIL"), method = "keyTyped")
	public void onKeyTyped(char key, int code, CallbackInfo ci) {
		if (inMoreWorldOptionsDisplay) {
			offsetX.textboxKeyTyped(key, code);
			offsetZ.textboxKeyTyped(key, code);
			try {
				LoTASModContainer.offsetX = MCVer.clamp(Integer.parseInt(offsetX.getText()), 0, 9);
				LoTASModContainer.offsetZ = MCVer.clamp(Integer.parseInt(offsetZ.getText()), 0, 9);
			} catch (Exception e) {
				// Lazyness wins. (This is actually fine)
			}
		}
	}	
	
	@Inject(at = @At("TAIL"), method = "mouseClicked")
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
		if (inMoreWorldOptionsDisplay) {
			offsetX.mouseClicked(mouseX, mouseY, mouseButton);
			offsetZ.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}	
	
	@Inject(at = @At("TAIL"), method = "drawScreen")
	public void onDrawScreen(CallbackInfo ci) {
		if (inMoreWorldOptionsDisplay) {
			offsetX.drawTextBox();
			offsetZ.drawTextBox();
		}
	}	
}
