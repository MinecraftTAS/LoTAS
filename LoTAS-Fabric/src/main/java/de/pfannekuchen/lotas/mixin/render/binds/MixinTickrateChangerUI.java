package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffects;

@Mixin(Gui.class)
public class MixinTickrateChangerUI {
	
	@Shadow
	private int screenWidth;
	@Shadow
	private int screenHeight;
	@Shadow
	private Minecraft minecraft;
	
	//#if MC>=11601
	//#if MC>=11701
//$$ 	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", ordinal = 3))
	//#else
//$$ 	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", ordinal = 10))
	//#endif
//$$
	//#if MC>=11904
//$$ 	private void redirect_renderPlayerHealth(com.mojang.blaze3d.vertex.PoseStack poseStack, int x, int y, int textureX, int textureY, int width, int height) {
	//#else
//$$ 	public void redirect_renderPlayerHealth(Gui gui, com.mojang.blaze3d.vertex.PoseStack poseStack, int x, int y, int textureX, int textureY, int width, int height) {
	//#endif
	//#else
	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(IIIIII)V", ordinal = 11))
	public void redirect_renderPlayerHealth(Gui parentIn, int x, int y, int textureX, int textureY, int width, int height) {
	//#endif
		//#if MC>=11601
		//#if MC>=11904
//$$ 		net.minecraft.client.gui.GuiComponent.blit(poseStack, x, y, textureX, textureY, width, height);
		//#else
//$$ 		gui.blit(poseStack, x, y, textureX, textureY, width, height);
		//#endif
		//#else
		parentIn.blit(x, y, textureX, textureY, width, height);
		//#endif
		int color = 0x000000;
		if (minecraft.player.hasEffect(MobEffects.HUNGER)) {
			color = 0x12410b;
		}
		int mask = 0xFF000000;
		MCVer.fill(x + 8, y + 8, x + 1 + 8, y + 1 + 8, mask + color);
		//#if MC<11701
		MCVer.color4f(255, 255, 255, 255);
		//#endif
	}
}
