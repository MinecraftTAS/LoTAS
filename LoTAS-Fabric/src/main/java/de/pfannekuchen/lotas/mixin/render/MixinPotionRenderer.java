package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.core.utils.PotionRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

@Mixin(HeldItemRenderer.class)
public abstract class MixinPotionRenderer {

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
	
}
