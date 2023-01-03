package com.minecrafttas.lotas.mixin.dragonmanipulation;

import net.minecraft.util.RandomSource; // @RandomSourceImport

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecrafttas.lotas.mixin.accessors.AccessorPath;
import com.minecrafttas.lotas.mods.DragonManipulation;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonStrafePlayerPhase;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

/**
 * This mixin manipulates the rng of the dragon strafe player phase
 * @author Pancake
 */
@Mixin(DragonStrafePlayerPhase.class)
public abstract class MixinDragonStrafePlayerPhase extends AbstractDragonPhaseInstance {
	public MixinDragonStrafePlayerPhase(EnderDragon enderDragon) { super(enderDragon); }

	/**
	 * Forces an optimal dragon path by (step 1) removing the 20 block addend
	 * @param r Random source
	 * @return Multiplier
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F")) // @RandomSourceDescriptor
	public float redirect_nextFloat(RandomSource r) { // @RandomSourceSourceClass
		return DragonManipulation.instance.isForceOptimalDragonPath() ? 0f : r.nextFloat();
	}
	
	/**
	 * Forces an optimal dragon path by (step 3) calculating a path that skips the two high nodes
	 * @param dragon Ender Dragon
	 * @param i Path creation parameter
	 * @param j Path creation parameter
	 * @param n path creation parameter
	 * @return Optimal path
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;findPath(IILnet/minecraft/world/level/pathfinder/Node;)Lnet/minecraft/world/level/pathfinder/Path;"))
	public Path redirect_findPath(EnderDragon dragon, int i, int j, Node n) {
		if (!DragonManipulation.instance.isForceOptimalDragonPath())
			return dragon.findPath(i, j, n);
		// 20 attempts at generating an optimal path
		Path path = null;
		for (int k = 0; k < 20; k++) {
			path = dragon.findPath(i, j, n);
			boolean in = true;
			for (Node node : ((AccessorPath) path).nodes())
				if (node.y > 85) {
					in = false;
					break;
				}
			if (in)
				return path;
		}
		return path;
	}
	
	/**
	 * Forces an optimal dragon path by (step 2) calculating the optimal block addend depending on the dragons position
	 * @param x Node x pos
	 * @param y Target y pos
	 * @param z Node z pos
	 * @return Target
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "NEW", target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)Lnet/minecraft/world/phys/Vec3;"))
	public Vec3 navigate_nodeInit(double x, double y, double z) {
		if (!DragonManipulation.instance.isForceOptimalDragonPath())
			return new Vec3(x, y, z);
		
		double distance = Math.max(0, Math.min(20, dragon.position().y - y));
		return new Vec3(x, y + distance, z);
	}

}
