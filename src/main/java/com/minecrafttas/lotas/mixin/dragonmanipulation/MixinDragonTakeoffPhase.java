package com.minecrafttas.lotas.mixin.dragonmanipulation;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecrafttas.lotas.mods.DragonManipulation.Phase;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonTakeoffPhase;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.minecrafttas.lotas.LoTAS.DRAGON_MANIPULATION;

/**
 * This mixin manipulates the rng of the dragon takeoff phase
 *
 * @author Pancake
 */
@Mixin(DragonTakeoffPhase.class)
public abstract class MixinDragonTakeoffPhase extends AbstractDragonPhaseInstance {
	public MixinDragonTakeoffPhase(EnderDragon enderDragon) { super(enderDragon); }

	/**
	 * Force optimal dragon path by (step 1) removing the 20 block addend
	 *
	 * @param r Random source
	 * @return Multiplier
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"))
	public float redirect_nextFloat(Random r) {
		return DRAGON_MANIPULATION.getPhase() == Phase.OFF ? r.nextFloat() : 0.0f;
	}

	/**
	 * Force optimal dragon path by (step 2) calculating the optimal block addend depending on the dragons position
	 *
	 * @param args Arguments
	 */
	@ModifyArgs(method = "navigateToNextPathNode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V"))
	public void navigate_nodeInit(Args args) {
		if (DRAGON_MANIPULATION.getPhase() == Phase.OFF)
			return;

		double distance = Math.max(0, Math.min(20, dragon.position().y - (double) args.get(1)));
		args.set(1, (double) args.get(1) + distance);
	}

}
