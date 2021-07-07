package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.Minecraft;

//#if MC>=11500
//$$ @Mixin(Minecraft.class)
//$$ public class MixinRenderShield {
//$$ 	// TODO: Fix Shields
//$$ }
//#else
@Mixin(net.minecraft.client.renderer.EntityBlockRenderer.class)
public class MixinRenderShield {

	@org.spongepowered.asm.mixin.injection.ModifyArg(index = 0, method = "renderByItem", at = @org.spongepowered.asm.mixin.injection.At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bind(Lnet/minecraft/resources/ResourceLocation;)V"))
	public net.minecraft.resources.ResourceLocation modifyShieldTexture(net.minecraft.resources.ResourceLocation original) {
		return de.pfannekuchen.lotas.core.LoTASModContainer.shield == null ? original : de.pfannekuchen.lotas.core.LoTASModContainer.shield;
	}

}
//#endif
