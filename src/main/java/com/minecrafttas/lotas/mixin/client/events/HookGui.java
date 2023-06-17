package com.minecrafttas.lotas.mixin.client.events;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecrafttas.lotas.utils.RegistryUtils;

import net.minecraft.client.gui.Gui;

@Mixin(Gui.class)
public class HookGui {
	
	@Shadow
	private int screenWidth;
	@Shadow
	private int screenHeight;
	
	@Inject(method="renderExperienceBar", at=@At(value="HEAD"))
	//# 1.16.1
//$$ 	public void mixinRenderExperienceBar(Object poseStack, int i, CallbackInfo ci) { //@PoseStack;
	//# def
	public void mixinRenderExperienceBar(CallbackInfo ci) {
	//# end
		int xPos = (this.screenWidth / 2)-7;
		int yPos = this.screenHeight - 31 - 19;
        
		//#1.16.1
//$$		RegistryUtils.applyRegistry(poseStack, xPos, yPos, 0.3);
		//#def
		RegistryUtils.applyRegistry(null, xPos, yPos, 15);
		//#end
	}
}
