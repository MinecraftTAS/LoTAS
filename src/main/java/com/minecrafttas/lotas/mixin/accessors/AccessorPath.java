package com.minecrafttas.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

/**
 * This mixin accessor makes the nodes accessible for every version. Getters were removed in 1.16.5, why mojang???
 * @author Pancake
 */
@Mixin(Path.class)
public interface AccessorPath {

	/**
	 * This Accessor opens the private field containing the nodes for of the path
	 * @return Nodes
	 */
	@Accessor("nodes")
	public List<Node> nodes();
	
}
