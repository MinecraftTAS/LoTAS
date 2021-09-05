package de.pfannekuchen.lotas.mixin.accessors;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemInHandRenderer.class)
public interface AccessorItemRenderer {
	@Accessor
	public ItemRenderer getItemRenderer();
}
