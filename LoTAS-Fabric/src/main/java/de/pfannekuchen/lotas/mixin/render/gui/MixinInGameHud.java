package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.RegistryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

@Mixin(Gui.class)
public abstract class MixinInGameHud {
	
//	ResourceLocation potion = new ResourceLocation("lotas:heck/potion.png");
	@Shadow
	private int screenWidth;
	@Shadow
	private int screenHeight;
	@Shadow
	private Minecraft minecraft;
	
	@Inject(method="renderExperienceBar", at=@At(value="HEAD"))
	//#if MC>=11601
//$$ 	public void mixinRenderExperienceBar(com.mojang.blaze3d.vertex.PoseStack poseStack, int i, CallbackInfo ci) {
	//#else
	public void mixinRenderExperienceBar(CallbackInfo ci) {
	//#endif
		int xPos = (this.screenWidth / 2)-6;
        int yPos = this.screenHeight - 31 - 19;
        
        //#if MC>=11700
        //$$ RegistryUtils.applyRegistry(MCVer.stack, xPos, yPos, 0.3);
        //#else
        RegistryUtils.applyRegistry(null, xPos, yPos, 15);
        //#endif
	}
}
