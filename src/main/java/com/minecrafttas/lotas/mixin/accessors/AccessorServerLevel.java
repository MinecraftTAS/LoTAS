package com.minecrafttas.lotas.mixin.accessors;

import java.util.List;
import java.util.Queue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

@Mixin(ServerLevel.class)
public interface AccessorServerLevel {
	
	@Accessor("globalEntities")
    public List<Entity> globalEntities();
	
	@Accessor("entitiesById")
    public Int2ObjectMap<Entity> entitiesById();
	
	@Accessor("toAddAfterTick")
    public Queue<Entity> toAddAfterTick();
	
}
