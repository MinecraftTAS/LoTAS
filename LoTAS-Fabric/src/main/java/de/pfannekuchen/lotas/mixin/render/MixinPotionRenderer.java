package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.core.utils.PotionRenderer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorItemRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

@Mixin(GameRenderer.class)
public abstract class MixinPotionRenderer {

	@Shadow
	ItemInHandRenderer itemInHandRenderer;


	@Inject(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;turnOffLightLayer()V", shift = Shift.AFTER))
	public void drawPotionAfter(CallbackInfo ci) {
		GlStateManager.matrixMode(5888);
       GlStateManager.loadIdentity();
		GlStateManager.pushMatrix();
		ItemStack stack2 = PotionRenderer.render();
		GlStateManager.disableLighting();
		((AccessorItemRenderer)itemInHandRenderer).getItemRenderer().renderStatic(stack2, ItemTransforms.TransformType.FIXED);
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}