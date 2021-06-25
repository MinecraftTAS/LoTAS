package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.AccessorInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import de.pfannekuchen.lotas.core.utils.PotionRenderer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorItemRenderer;
import net.minecraft.client.MinecraftClient;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
//#if MC>=11502
//$$ import net.minecraft.client.network.ClientPlayerEntity;
//$$ import net.minecraft.client.render.VertexConsumerProvider;
//$$ import net.minecraft.client.render.VertexConsumerProvider.Immediate;
//$$ import net.minecraft.client.util.math.MatrixStack;
//$$ import net.minecraft.client.render.OverlayTexture;
//$$ import net.minecraft.client.render.BufferBuilderStorage;
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//#endif
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.client.render.model.json.ModelTransformation;

@Mixin(GameRenderer.class)
public abstract class MixinPotionRenderer {

	//#if MC<=11404

	@Shadow
	HeldItemRenderer firstPersonRenderer;


	@Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;disableLightmap()V", shift = Shift.AFTER))
	public void drawPotionAfter(CallbackInfo ci) {
		GlStateManager.matrixMode(5888);
       GlStateManager.loadIdentity();
		GlStateManager.pushMatrix();
		ItemStack stack2 = PotionRenderer.render();
		MinecraftClient mc = MinecraftClient.getInstance();
		GlStateManager.disableLighting();
		((AccessorItemRenderer)firstPersonRenderer).getItemRenderer().renderItem(stack2, ModelTransformation.Type.FIXED);
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
	//#else
//$$ 		@Shadow
//$$ 		HeldItemRenderer firstPersonRenderer;
//$$
//$$ 		@Shadow
//$$ 		BufferBuilderStorage buffers;
//$$
//$$ 		@Inject(method = "renderHand", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
//$$ 		public void drawPotionAfter(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
			//#if MC<=11605
//$$ 			RenderSystem.matrixMode(5888);
//$$ 			RenderSystem.loadIdentity();
//$$ 			GlStateManager.pushMatrix();
			//#else
//$$ 			MatrixStack.Entry entry = matrices.peek();
//$$ 	        entry.getModel().loadIdentity();
//$$ 	        entry.getNormal().loadIdentity();
			//#endif
//$$ 	        matrices.push();
//$$ 			ItemStack stack2 = PotionRenderer.render(matrices);
//$$
			//#if MC>=11700
//$$ 			VertexConsumerProvider.Immediate immediate = this.buffers.getEntityVertexConsumers();
//$$ 			MinecraftClient.getInstance().getItemRenderer().renderItem(stack2, ModelTransformation.Mode.FIXED, 15728880, OverlayTexture.DEFAULT_UV, matrices, immediate, 0);
			//#else
//$$ 			VertexConsumerProvider.Immediate immediate = this.buffers.getEntityVertexConsumers();
//$$ 			MinecraftClient.getInstance().getItemRenderer().renderItem(stack2, ModelTransformation.Mode.FIXED, 15728880, OverlayTexture.DEFAULT_UV, matrices, immediate);
			//#endif
//$$ 			matrices.pop();
//$$ 			immediate.draw();
			//#if MC<=11605
//$$ 			GlStateManager.popMatrix();
			//#endif
//$$ 		}
	//#endif

}
