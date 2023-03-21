package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.RegistryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;

@Mixin(GuiIngameForge.class)
public abstract class MixinInGameHud {
	
	@Inject(method="renderExperience", at=@At(value="HEAD"), remap = false)
	public void mixinApplyRegistry(CallbackInfo ci) {
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
		int m = (scaledresolution.getScaledWidth() / 2)-6;
        int n = scaledresolution.getScaledHeight() - 31 - 19;
        RegistryUtils.applyRegistry(m, n, 1);
	}
}
