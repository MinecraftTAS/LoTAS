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
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
//#if MC>=11502
//$$ import net.minecraft.client.network.ClientPlayerEntity;
//$$ import net.minecraft.client.render.VertexConsumerProvider;
//$$ import net.minecraft.client.render.VertexConsumerProvider.Immediate;
//$$ import net.minecraft.client.util.math.MatrixStack;
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
//$$ 		@Shadow protected abstract void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
//$$ 		@Inject(method = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", ordinal = 1))
//$$ 		public void drawPotionAfter(float tickDelta, MatrixStack matrices, Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
			//#if MC<=11605
//$$ 			GlStateManager.pushMatrix();
			//#endif
//$$ 			matrices.push();
//$$ 			ItemStack stack2 = PotionRenderer.render(matrices);
			//#if MC>=11700
//$$ 			renderFirstPersonItem(player, tickDelta, player.getPitch(), Hand.MAIN_HAND, 0f, stack2, 0f, matrices, vertexConsumers, light);
			//#else
//$$ 			renderFirstPersonItem(player, tickDelta, player.pitch, Hand.MAIN_HAND, 0f, stack2, 0f, matrices, vertexConsumers, light);
			//#endif
//$$ 			matrices.pop();
			//#if MC<=11605
//$$ 			GlStateManager.popMatrix();
			//#endif
//$$ 		}
	//#endif

}
