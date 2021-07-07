package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import net.minecraft.client.renderer.EntityBlockRenderer;
import net.minecraft.resources.ResourceLocation;

@Mixin(EntityBlockRenderer.class)
public class MixinRenderShield {

	@ModifyArg(index = 0, method = "renderByItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bind(Lnet/minecraft/resources/ResourceLocation;)V"))
	public ResourceLocation modifyShieldTexture(ResourceLocation original) {
		return LoTASModContainer.shield == null ? original : LoTASModContainer.shield;
	}

}
