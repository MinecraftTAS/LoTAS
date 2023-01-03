package com.minecrafttas.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

@Mixin(Path.class)
public interface AccessorPath {

	@Accessor("nodes")
	public List<Node> nodes();
	
}
