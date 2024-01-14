package com.minecrafttas.lotas.mixin.dragonmanipulation;

import com.minecrafttas.lotas.mods.DragonManipulation.Phase;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonHoldingPatternPhase;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Random;

import static com.minecrafttas.lotas.LoTAS.DRAGON_MANIPULATION;

/**
 * This mixin manipulates the rng of the dragon holding pattern phase
 * @author Pancake
 */
@Mixin(DragonHoldingPatternPhase.class)
public abstract class MixinDragonHoldingPatternPhase extends AbstractDragonPhaseInstance {
	public MixinDragonHoldingPatternPhase(EnderDragon enderDragon) { super(enderDragon); }

	/**
	 * Force optimal dragon path by (step 1) removing the 20 block addend
	 * @param r Random source
	 * @return Multiplier
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"))
	public float redirect_nextFloat(Random r) {
		return DRAGON_MANIPULATION.getPhase() == Phase.OFF ? r.nextFloat() : 1.0f;
	}
	
	/**
	 * Force optimal dragon path by (step 2) calculating the optimal block addend depending on the dragons position
	 *
	 * @param x Node x pos
	 * @param y Target y pos
	 * @param z Node z pos
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
	private boolean clockwise;
	
	/**
	 * Force optimal dragon path by (step 3) calculating whether a direction change would result in the more optimal path.
	 * Coincidentally the length of the path does not need to be checked, because for some odd mathematical reason the path length will always be 1 in the outer circle
	 * @param dragon Ender Dragon
	 * @return Closest node
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;findClosestNode()I"))
	public int redirect_findClosestNode(EnderDragon dragon) {
		int j, closestNodeA, closestNodeB;
		closestNodeA = closestNodeB = j = dragon.findClosestNode();

		closestNodeB += 6;

		closestNodeA = this.clockwise ? ++closestNodeA : --closestNodeA;
		closestNodeB = this.clockwise ? --closestNodeB : ++closestNodeB;

		if ((closestNodeA %= 12) < 0)
			closestNodeA += 12;

		if ((closestNodeB %= 12) < 0)
			closestNodeB += 12;

		// get path and node
		Path noSwitchPath = dragon.findPath(j, closestNodeA, null);
		Path switchPath = dragon.findPath(j, closestNodeB, null);

		this.shouldCCWCWC = (noSwitchPath.nodes.size() <= switchPath.nodes.size()) ? 1 : 0;
		return j;
	}
	
	/**
	 * Force optimal dragon path by (step 4) applying the calculated math from redirect_findClosestNode
	 * @param r Random source
	 * @param i Bound
	 * @return Random int
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 3))
	public int redirect_nextInt(Random r, int i) {
		return DRAGON_MANIPULATION.getPhase() == Phase.OFF ? r.nextInt(i) : this.shouldCCWCWC;
	}
	
	/**
	 * Force dragon to enter landing approach phase after finishing its path
	 * @param r Random source
	 * @param i Bound
	 * @return Random int
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 0))
	public int redirect_nextInt_perching(Random r, int i) {
		switch (DRAGON_MANIPULATION.getPhase()) {
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
	 * Force dragon to enter strafing phase after finishing its path
	 * @param r Random source
	 * @param i Bound
	 * @return Random int
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 1))
	public int redirect_nextInt_strafing(Random r, int i) {
		switch (DRAGON_MANIPULATION.getPhase()) {
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
	 * Force dragon to enter strafing phase after finishing its path
	 * @param r Random source
	 * @param i Bound
	 * @return Random int
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 2))
	public int redirect_nextInt_strafing2(Random r, int i) {
		switch (DRAGON_MANIPULATION.getPhase()) {
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
