package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import de.pfannekuchen.lotas.core.utils.PotionRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.HeldItemRenderer;
//#if MC>=11502
//$$ import net.minecraft.client.network.ClientPlayerEntity;
//$$ import net.minecraft.client.render.VertexConsumerProvider;
//$$ import net.minecraft.client.render.VertexConsumerProvider.Immediate;
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif

@Mixin(HeldItemRenderer.class)
public abstract class MixinPotionRenderer {

	//#if MC<=11404
	@Shadow
	public abstract void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float f, ItemStack item, float equipProgress);

	@Shadow
	public abstract void applyLightmap();

	@Inject(method = "rotate", at = @At("TAIL"))
	public void drawPotionAfter(float x, float y, CallbackInfo ci) {
		GlStateManager.pushMatrix();
		ItemStack stack2 = PotionRenderer.render();
		MinecraftClient mc = MinecraftClient.getInstance();
		applyLightmap();
		renderFirstPersonItem(mc.player, mc.getTickDelta(), mc.player.pitch, Hand.MAIN_HAND, 0f, stack2, 0f);
		GlStateManager.popMatrix();
	}
	//#else
//$$ 		@Shadow protected abstract void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
//$$ 		@Inject(method = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", ordinal = 1))
//$$ 		public void drawPotionAfter(float tickDelta, MatrixStack matrices, Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
//$$ 			GlStateManager.pushMatrix();
//$$ 			matrices.push();
//$$ 			ItemStack stack2 = PotionRenderer.render(matrices);
//$$ 			renderFirstPersonItem(player, tickDelta, player.pitch, Hand.MAIN_HAND, 0f, stack2, 0f, matrices, vertexConsumers, light);
//$$ 			matrices.pop();
//$$ 			GlStateManager.popMatrix();
//$$ 		}
	//#endif

}
