package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

/**
 * Changes the Shield Texture
 * @author ScribbleLP, Pancake
 */
@Mixin(net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer.class)
public class MixinRenderShield {
	@ModifyVariable(method = "renderByItem", at = @At(value = "STORE"), index = 9, ordinal = 0)
	public com.mojang.blaze3d.vertex.VertexConsumer changeShield(com.mojang.blaze3d.vertex.VertexConsumer vertexconsumer, ItemStack itemStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j){
		if(itemStack.getTagElement("BlockEntityTag") != null||LoTASModContainer.shield==null) {
			return vertexconsumer;
		}else {
			return multiBufferSource.getBuffer(net.minecraft.client.renderer.RenderType.entitySolid(LoTASModContainer.shield));
		}
	}
}
//#else
//$$ @Mixin(net.minecraft.client.renderer.EntityBlockRenderer.class)
//$$ public class MixinRenderShield {
//$$
//$$ 	@org.spongepowered.asm.mixin.injection.ModifyArg(index = 0, method = "renderByItem", at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bind(Lnet/minecraft/resources/ResourceLocation;)V"))
//$$ 	public net.minecraft.resources.ResourceLocation modifyShieldTexture(net.minecraft.resources.ResourceLocation original) {
//$$ 		return de.pfannekuchen.lotas.core.LoTASModContainer.shield == null ? original : de.pfannekuchen.lotas.core.LoTASModContainer.shield;
//$$ 	}
//$$
//$$ }
//#endif
