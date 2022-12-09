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
		offsetX=MCVer.offsetX(this.width);
		offsetZ=MCVer.offsetZ(this.width);
	}
	
	@Inject(at = @At("TAIL"), method = "keyTyped")
	public void onKeyTyped(char key, int code, CallbackInfo ci) {
		if (inMoreWorldOptionsDisplay) {
			if (Character.isDigit(key)||key=='-'||key=='\b'||code==203||code==205||code==211) {
				offsetX.textboxKeyTyped(key, code);
				offsetZ.textboxKeyTyped(key, code);
				try {
					LoTASModContainer.offsetX = MCVer.clamp(Integer.parseInt(offsetX.getText()), -9, 9)*-1;
					LoTASModContainer.offsetZ = MCVer.clamp(Integer.parseInt(offsetZ.getText()), -9, 9)*-1;
				} catch (Exception e) {
					// Lazyness wins. (This is actually fine)
				}
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
			MCVer.textX(width);
			MCVer.textY(width);
		}
	}
	
}
