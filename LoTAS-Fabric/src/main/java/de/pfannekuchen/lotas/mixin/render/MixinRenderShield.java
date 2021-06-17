package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.util.Identifier;

@Mixin(BuiltinModelItemRenderer.class)
public class MixinRenderShield {

	//#if MC<=11404
	@ModifyArg(index = 0, method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V"))
	public Identifier modifyShieldTexture(Identifier original) {
		return LoTASModContainer.shield == null ? original : LoTASModContainer.shield;
	}
	//#endif

}
