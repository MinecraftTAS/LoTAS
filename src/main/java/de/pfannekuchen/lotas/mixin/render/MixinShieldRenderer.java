package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.util.ResourceLocation;

@Mixin(TileEntityItemStackRenderer.class)
public abstract class MixinShieldRenderer {
    
	// TODO: Test all exported Versions
	
	//#if MC>=11200
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V", ordinal = 1), method = "Lnet/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer;renderByItem(Lnet/minecraft/item/ItemStack;F)V", index = 0)
	//#else
//$$ 	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V", ordinal = 1), method = "renderByItem", index = 0)
	//#endif
	public ResourceLocation redoLoc(ResourceLocation original) {
		return LoTASModContainer.shield;
	}
	
}