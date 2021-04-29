package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.config.ConfigManager;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;

@Mixin(GuiScreenWorking.class)
/**
 * Displays the ingame menu after loading a world
 * 
 * @author Pancake
 */
public abstract class MixinGuiScreenWorking extends GuiScreen {
	
	@ModifyArg(index = 0, method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
	public GuiScreen redirectNull(GuiScreen basicallyNull) {
		return ConfigManager.getBoolean("tools", "hitEscape") ? new GuiIngameMenu() : (GuiScreen) null;
	}
	
}
