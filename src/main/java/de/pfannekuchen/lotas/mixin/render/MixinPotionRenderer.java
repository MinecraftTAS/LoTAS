package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.PotionRenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class)
public abstract class MixinPotionRenderer {

	@Shadow @Final
	public Minecraft mc;
	@Shadow
	//#if MC>=10900
	public abstract void rotateArm(float partialTicks);
	@Shadow protected abstract void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded);
	//#else
//$$ 	public abstract void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks);
//$$ 	@Shadow public abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);
	//#endif
	
	//#if MC>=10900
	@Redirect(method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;rotateArm(F)V"))
	private void cancelRotateArm(ItemRenderer renderer, float pff) {
		
	}
	//#endif
	
	//#if MC>=10900
	@Inject(method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V"))
	//#else
	//$$ @Inject(method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V"))
	//#endif
	public void drawPotionAfter(CallbackInfo ci) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        ItemStack stack2 = PotionRenderingUtils.renderPotion();
        //#if MC>=10900
        this.renderItemSide(MCVer.player(mc), stack2, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, false);
        //#else
        //$$ this.renderItem(MCVer.player(mc), stack2, ItemCameraTransforms.TransformType.FIRST_PERSON);
        //#endif
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
	}
	

	//#if MC>=10900
	@Inject(method = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V"))
	public void rotateArmLater(CallbackInfo ci) {
		rotateArm(mc.getRenderPartialTicks());
	}
	//#endif
	
}