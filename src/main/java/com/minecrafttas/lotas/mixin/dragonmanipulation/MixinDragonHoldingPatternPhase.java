package com.minecrafttas.lotas.mixin.dragonmanipulation;

import net.minecraft.util.RandomSource; // @RandomSourceImport

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecrafttas.lotas.mixin.accessors.AccessorPath;
import com.minecrafttas.lotas.mods.DragonManipulation;
import com.minecrafttas.lotas.mods.DragonManipulation.Phase;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonHoldingPatternPhase;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

/**
 * This mixin manipulates the rng of the dragon holding pattern phase
 * @author Pancake
 */
@Mixin(DragonHoldingPatternPhase.class)
public abstract class MixinDragonHoldingPatternPhase extends AbstractDragonPhaseInstance {
	public MixinDragonHoldingPatternPhase(EnderDragon enderDragon) { super(enderDragon); }

	/**
	 * Forces an optimal dragon path by (step 1) removing the 20 block addend
	 * @param r Random source
	 * @return Multiplier
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F")) // @RandomSourceDescriptor
	public float redirect_nextFloat(RandomSource r) { // @RngSourceClass
		return DragonManipulation.instance.getPhase() == Phase.OFF ? r.nextFloat() : 0.0f;
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
		if (DragonManipulation.instance.getPhase() == Phase.OFF)
			return new Vec3(x, y, z);
		
		double distance = Math.max(0, Math.min(20, dragon.position().y - y));
		return new Vec3(x, y + distance, z);
	}
	
	@Unique
	public int shouldCCWCWC = 1; // should counter clock wise clock wise change, lol. 0 == switch, anything else == no switch
	
	@Shadow
	public boolean clockwise;
	
	/**
	 * Forces an optimal dragon path by (step 3) calculating whether a direction change would result in the more optimal path.
	 * Coincidentally the length of the path does not need to be checked, because for some odd mathematical reason the path length will always be 1 in the outer circle
	 * @param dragon Ender Dragon
	 * @return Closest node
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;findClosestNode()I"))
	public int redirect_findClosestNode(EnderDragon dragon) {
		this.shouldCCWCWC = 1; // reset

		int closestNode = dragon.findClosestNode();

		// do math
		int i = closestNode;
		i = this.clockwise ? ++i : --i;
		if ((i %= 12) < 0) i += 12;
		
		// get path and node
		Path path = dragon.findPath(i, closestNode, null);
		for (Node node : ((AccessorPath) path).nodes())
			if (node.y > 85) {
				this.shouldCCWCWC = 0;
				break;
			}
		
		return closestNode;
	}
	
	/**
	 * Forces an optimal dragon path by (step 4) applying the calculated math from redirect_findClosestNode
	 * @param r Random source
	 * @param i Bound
	 * @return Random int
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 3)) // @RandomSourceDescriptor
	public int redirect_nextInt(RandomSource r, int i) { // @RngSourceClass
		return DragonManipulation.instance.getPhase() == Phase.OFF ? r.nextInt(i) : this.shouldCCWCWC;
	}
	
	/**
	 * Forces the dragon to enter landing approach phase after finishing its path
	 * @param r Random source
	 * @param i Bound
	 * @return Random int
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 0)) // @RandomSourceDescriptor
	public int redirect_nextInt_perching(RandomSource r, int i) { // @RngSourceClass
		switch (DragonManipulation.instance.getPhase()) {
			case LANDINGAPPROACH:
				return 0;
			case HOLDINGPATTERN:
			case STRAFING:
				return 1;
			default:
				return r.nextInt(i);
		}
	}

	/**
	 * Forces the dragon to enter strafing phase after finishing its path
	 * @param r Random source
	 * @param i Bound
	 * @return Random int
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 1)) // @RandomSourceDescriptor
	public int redirect_nextInt_strafing(RandomSource r, int i) { // @RngSourceClass
		switch (DragonManipulation.instance.getPhase()) {
			case STRAFING:
				return 0;
			case LANDINGAPPROACH:
			case HOLDINGPATTERN:
				return 1;
			default:
				return r.nextInt(i);
		}
	}

	/**
	 * Forces the dragon to enter strafing phase after finishing its path
	 * @param r Random source
	 * @param i Bound
	 * @return Random int
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 2)) // @RandomSourceDescriptor
	public int redirect_nextInt_strafing2(RandomSource r, int i) { // @RngSourceClass
		switch (DragonManipulation.instance.getPhase()) {
			case STRAFING:
				return 0;
			case LANDINGAPPROACH:
			case HOLDINGPATTERN:
				return 1;
			default:
				return r.nextInt(i);
		}
	}
	
}
