package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import net.minecraft.world.item.Items;



/**
 * Changes the Shield Texture
 * @author ScribbleLP, Pancake
 */
//#if MC>=11500
//$$ //@Mixin(net.minecraft.client.resources.model.ModelBakery.class)
//$$ @Mixin(net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer.class)
//$$ public class MixinRenderShield {
//$$
//$$
//$$ //	@Redirect(method = "renderByItem", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", ordinal = 1))
//$$ //	public void changeShield(ModelPart modelpart, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, float f, float g, float h, float k, ItemStack itemStack, PoseStack poseStack2, MultiBufferSource multiBufferSource, int i2, int j2) {
//$$ //		modelpart.render(poseStack, multiBufferSource.getBuffer(RenderType.entitySolid(LoTASModContainer.shield)), i, j, 1.0F, 1.0F, 1.0F, 1.0F);
//$$ //	}
//$$
//$$ 	@ModifyVariable(method = "renderByItem", at = @At(value = "STORE"), index = 9, ordinal = 0)
	//#if MC>=11600
//$$ 	public com.mojang.blaze3d.vertex.VertexConsumer changeShield(com.mojang.blaze3d.vertex.VertexConsumer vertexconsumer, net.minecraft.world.item.ItemStack itemStack, net.minecraft.client.renderer.block.model.ItemTransforms.TransformType transformType, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, int j){
	//#else
//$$ 	public com.mojang.blaze3d.vertex.VertexConsumer changeShield(com.mojang.blaze3d.vertex.VertexConsumer vertexconsumer, net.minecraft.world.item.ItemStack itemStack, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, int j){
	//#endif
//$$ 		if(itemStack.getTagElement("BlockEntityTag") != null||LoTASModContainer.shield==null) {
//$$ 			return vertexconsumer;
//$$ 		}else if(itemStack.getItem()== Items.SHIELD){
//$$ 			return multiBufferSource.getBuffer(net.minecraft.client.renderer.RenderType.entitySolid(LoTASModContainer.shield));
//$$ 		}else {
//$$ 			return vertexconsumer;
//$$ 		}
//$$ 	}
//$$ }
//#else
@Mixin(net.minecraft.client.renderer.EntityBlockRenderer.class)
public class MixinRenderShield {

	@org.spongepowered.asm.mixin.injection.ModifyArg(index = 0, method = "renderByItem", at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bind(Lnet/minecraft/resources/ResourceLocation;)V"))
	public net.minecraft.resources.ResourceLocation modifyShieldTexture(net.minecraft.resources.ResourceLocation original) {
		if(original.getPath().contains("trident")){
			return original;
		}
		return de.pfannekuchen.lotas.core.LoTASModContainer.shield == null ? original : de.pfannekuchen.lotas.core.LoTASModContainer.shield;
	}

}
//#endif
