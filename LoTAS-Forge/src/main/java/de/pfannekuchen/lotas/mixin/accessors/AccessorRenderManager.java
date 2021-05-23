package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.entity.RenderManager;

@Mixin(RenderManager.class)
public interface AccessorRenderManager {

	@Accessor("renderPosX")
	public double renderPosX();
	@Accessor("renderPosY")
	public double renderPosY();
	@Accessor("renderPosZ")
	public double renderPosZ();
	
}
