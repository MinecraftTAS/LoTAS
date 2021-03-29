package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.renderer.PotionRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class) @SuppressWarnings("deprecation")
public abstract class MixinPotionEntityRenderer {

	/*
	 * This File adds the Potion above the Hotbar
	 */
	
	@Shadow
	public Minecraft mc;
	@Shadow
	public abstract void func_178101_a(float bruh, float partialTicks);
	@Shadow
	protected abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);
	
	@Redirect(method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;func_178101_a (FF)V"))
	private void cancelRotateArm(ItemRenderer renderer) {
		
	}
	
	@Inject(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V"))
	public void drawPotionAfter(CallbackInfo ci) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        ItemStack stack2 = PotionRenderer.render();
        this.renderItem(mc.thePlayer, stack2, ItemCameraTransforms.TransformType.FIRST_PERSON);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
	}
	

	@Inject(method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V"))
	public void rotateArmLater(CallbackInfo ci) {
		func_178101_a(mc.thePlayer.prevRotationPitch + (mc.thePlayer.rotationPitch - mc.thePlayer.prevRotationPitch) * mc.timer.renderPartialTicks, mc.thePlayer.prevRotationYaw + (mc.thePlayer.rotationYaw - mc.thePlayer.prevRotationYaw) * mc.timer.renderPartialTicks);
	}
	
}
