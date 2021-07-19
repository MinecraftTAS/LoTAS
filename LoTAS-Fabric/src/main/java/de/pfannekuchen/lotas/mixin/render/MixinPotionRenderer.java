package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.PotionRenderer;
//#if MC<=11404
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorItemRenderer;
//#else
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import net.minecraft.client.Camera;
//$$ import net.minecraft.client.Minecraft;
//$$ import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//$$ import net.minecraft.client.renderer.texture.OverlayTexture;
//$$ import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
//$$ import net.minecraft.client.renderer.RenderBuffers;
//$$ import de.pfannekuchen.lotas.core.utils.PotionRenderer;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.At.Shift;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import net.minecraft.world.item.ItemStack;
//#endif
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;

/**
 * Renders a Potion above the Hotbar
 * @author ScribbleLP
 */
@Mixin(GameRenderer.class)
public abstract class MixinPotionRenderer {

	@Shadow
	ItemInHandRenderer itemInHandRenderer;
	//#if MC<=11404
	@Inject(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;turnOffLightLayer()V", shift = Shift.AFTER))
	public void drawPotionAfter(CallbackInfo ci) {
		MCVer.matrixMode(5888);
		MCVer.loadIdentity();
		MCVer.pushMatrix(null);
		ItemStack stack2 = PotionRenderer.render();
		MCVer.disableLighting();
		((AccessorItemRenderer)itemInHandRenderer).getItemRenderer().renderStatic(stack2, ItemTransforms.TransformType.FIXED);
		MCVer.enableLighting();
		MCVer.popMatrix(null);
	}
	//#else
//$$ 	@Shadow @Final
//$$ 	public RenderBuffers renderBuffers;
//$$
//$$ 	@Inject(method = "renderItemInHand", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
//$$ 	public void drawPotionAfter(com.mojang.blaze3d.vertex.PoseStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
		//#if MC<=11605
//$$ 		RenderSystem.matrixMode(5888);
//$$ 		RenderSystem.loadIdentity();
//$$ 		RenderSystem.pushMatrix();
		//#endif
//$$ 		matrices.pushPose();
//$$ 		ItemStack stack2 = PotionRenderer.render(matrices);
//$$
//$$ 		BufferSource immediate = renderBuffers.bufferSource();
		//#if MC>=11700
//$$ 		Minecraft.getInstance().getItemRenderer().renderStatic(stack2, TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrices, immediate, 0);
		//#else
//$$ 		Minecraft.getInstance().getItemRenderer().renderStatic(stack2, TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrices, immediate);
		//#endif
//$$ 		matrices.popPose();
//$$ 		immediate.endBatch();
		//#if MC<=11605
//$$ 		RenderSystem.popMatrix();
		//#endif
//$$ 	}
	//#endif
}