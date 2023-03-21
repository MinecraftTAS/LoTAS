package com.minecrafttas.lotas.mixin.client.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffects;


//# 1.17.1
//# def
//$$import com.mojang.blaze3d.platform.GlStateManager;
//# end

//# 1.16.1
//$$import com.mojang.blaze3d.vertex.PoseStack;
//# end

/**
 * This Mixin slows down many aspects of the ingame hud
 * @author Scribble
 */
@Mixin(Gui.class)
public class MixinGui {

	@Shadow
	private int screenWidth;
	
	@Shadow
	private int screenHeight;
	
	@Shadow
	private Minecraft minecraft;

	// # 1.16.1
	// ## 1.17.1
//$$	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", ordinal = 3))
	// ## def
//$$	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", ordinal = 10))
	// ## end
//$$	public void redirect_renderPlayerHealth(Gui gui, PoseStack poseStack, int x, int y, int textureX, int textureY, int width, int height) {
	// # def
//$$	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(IIIIII)V", ordinal = 11))
//$$	public void redirect_renderPlayerHealth(Gui parentIn, int x, int y, int textureX, int textureY, int width, int height) {
	// # end
		
		// # 1.16.1
//$$		gui.blit(poseStack, x, y, textureX, textureY, width, height);
		// # def
//$$		parentIn.blit(x, y, textureX, textureY, width, height);
		// # end
		
		int color = this.minecraft.player.hasEffect(MobEffects.HUNGER) ? 0x12410b : 0x000000;
		int mask = 0xFF000000;
		
		// # 1.17.1
//$$		Gui.fill(poseStack, x + 8, y + 8, x + 1 + 8, y + 1 + 8, mask + color);
		// # 1.16.1
//$$		Gui.fill(poseStack, x + 8, y + 8, x + 1 + 8, y + 1 + 8, mask + color);
//$$		GlStateManager._color4f(255, 255, 255, 255);
		// # 1.15.2
//$$		Gui.fill(x + 8, y + 8, x + 1 + 8, y + 1 + 8, mask + color);
//$$		GlStateManager._color4f(255, 255, 255, 255);
		// # def
//$$		Gui.fill(x + 8, y + 8, x + 1 + 8, y + 1 + 8, mask + color);
//$$		GlStateManager.color4f(255, 255, 255, 255);
		// # end
	}
	
}
