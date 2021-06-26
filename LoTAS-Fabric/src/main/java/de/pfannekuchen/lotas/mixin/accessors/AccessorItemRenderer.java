package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;

@Mixin(HeldItemRenderer.class)
public interface AccessorItemRenderer {
	@Accessor
	public ItemRenderer getItemRenderer();
}
