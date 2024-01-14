package com.minecrafttas.lotas.mixin.dragonmanipulation;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecrafttas.lotas.mods.DragonManipulation.Phase;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonTakeoffPhase;
import net.minecraft.world.phys.Vec3;

import static com.minecrafttas.lotas.LoTAS.DRAGON_MANIPULATION;

/**
 * This mixin manipulates the rng of the dragon takeoff phase	
 * @author Pancake
 */
@Mixin(DragonTakeoffPhase.class)
public abstract class MixinDragonTakeoffPhase extends AbstractDragonPhaseInstance {
	public MixinDragonTakeoffPhase(EnderDragon enderDragon) { super(enderDragon); }

	/**
	 * Force optimal dragon path by (step 1) removing the 20 block addend
	 * @param r Random source
	 * @return Multiplier
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"))
	public float redirect_nextFloat(Random r) {
		return DRAGON_MANIPULATION.getPhase() == Phase.OFF ? r.nextFloat() : 0.0f;
	}
	
	/**
	 * Force optimal dragon path by (step 2) calculating the optimal block addend depending on the dragons position
	 * @param x Node x pos
	 * @param y Target y pos
	 * @param z Node z pos
	 * @return Target
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "NEW", target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)Lnet/minecraft/world/phys/Vec3;"))
	public Vec3 navigate_nodeInit(double x, double y, double z) {
		if (DRAGON_MANIPULATION.getPhase() == Phase.OFF)
			return new Vec3(x, y, z);

		double distance = Math.max(0, Math.min(20, dragon.position().y - y));
		return new Vec3(x, y + distance, z);
	}

}
