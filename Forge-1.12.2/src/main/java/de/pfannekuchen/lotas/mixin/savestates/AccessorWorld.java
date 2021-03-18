package de.pfannekuchen.lotas.mixin.savestates;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

@Mixin(World.class)
public interface AccessorWorld {
	@Accessor("unloadedEntityList")
	public List<Entity> getUnloadedEntityList();
	
	@Accessor("worldInfo")
	public void setWorldInfo(WorldInfo worldInfo);
}
