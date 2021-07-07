package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
	//#endif
}