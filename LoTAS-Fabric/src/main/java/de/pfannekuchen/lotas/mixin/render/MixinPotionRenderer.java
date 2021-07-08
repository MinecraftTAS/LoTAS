package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import de.pfannekuchen.lotas.core.utils.PotionRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
//#if MC<=11404
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.PotionRenderer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorItemRenderer;
//#endif
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;

@Mixin(GameRenderer.class)
public abstract class MixinPotionRenderer {

	@Shadow
	ItemInHandRenderer itemInHandRenderer;
	//#if MC<=11404
	@Inject(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;turnOffLightLayer()V", shift = Shift.AFTER))
	public void drawPotionAfter(CallbackInfo ci) {
		MCVer.matrixMode(5888);
		MCVer.loadIdentity();
		MCVer.pushMatrix();
		ItemStack stack2 = PotionRenderer.render();
		MCVer.disableLighting();
		((AccessorItemRenderer)itemInHandRenderer).getItemRenderer().renderStatic(stack2, ItemTransforms.TransformType.FIXED);
		MCVer.enableLighting();
		MCVer.popMatrix();
	}
	//#else
//$$ 	@Shadow @Final
//$$ 	public RenderBuffers renderBuffers;
//$$
//$$ 	@Inject(method = "renderItemInHand", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
//$$ 	public void drawPotionAfter(com.mojang.blaze3d.vertex.PoseStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
//$$ 		RenderSystem.matrixMode(5888);
//$$ 		RenderSystem.loadIdentity();
//$$ 		RenderSystem.pushMatrix();
//$$ 		matrices.pushPose();
//$$ 		ItemStack stack2 = PotionRenderer.render(matrices);
//$$
//$$ 		BufferSource immediate = renderBuffers.bufferSource();
//$$ 		Minecraft.getInstance().getItemRenderer().renderStatic(stack2, TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrices, immediate);
//$$ 		matrices.popPose();
//$$ 		immediate.endBatch();
//$$ 		RenderSystem.popMatrix();
//$$ 	}
	//#endif
}