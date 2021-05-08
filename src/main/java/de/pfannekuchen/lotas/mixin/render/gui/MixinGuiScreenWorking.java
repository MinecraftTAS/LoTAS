package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;

public abstract class MixinGuiScreenWorking extends GuiScreen {
	
	@ModifyArg(index = 0, method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
	public GuiScreen redirectNull(GuiScreen basicallyNull) {
		return ConfigUtils.getBoolean("tools", "hitEscape") ? new GuiIngameMenu() : (GuiScreen) null;
	}
	
}
