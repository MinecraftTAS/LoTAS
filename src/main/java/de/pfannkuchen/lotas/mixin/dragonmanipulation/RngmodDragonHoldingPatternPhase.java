package de.pfannkuchen.lotas.mixin.dragonmanipulation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannkuchen.lotas.LoTAS;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonHoldingPatternPhase;

/**
 * This mixin manipulates the rng of the dragon holding pattern phase
 * @author Pancake
 */
@Mixin(DragonHoldingPatternPhase.class)
public class RngmodDragonHoldingPatternPhase {

	/**
	 * Manipulates the addend multiplier for determining the height of the next path node
	 * @return Zero
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F"))
	public float redirect_nextFloat(RandomSource r) {
		return LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forceOptimalPath") ? 0.999f : r.nextFloat();
	}

	/**
	 * Manipulates the landing approach phase integer
	 * @return Zero
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 0))
	public int redirect_nextInt_perching(RandomSource r, int i) {
		return LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forceLandingApproach") ? 0 : r.nextInt(i);
	}

	/**
	 * Manipulates the strafing phase integer
	 * @return Zero
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 1))
	public int redirect_nextInt_strafing(RandomSource r, int i) {
		return LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forcePlayerStrafing") ? 0 : r.nextInt(i);
	}

	/**
	 * Manipulates the strafing phase integer - PART 2
	 * @return Zero
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 2))
	public int redirect_nextInt_strafing2(RandomSource r, int i) {
		return LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forcePlayerStrafing") ? 0 : r.nextInt(i);
	}

	/**
	 * Manipulates the rotation changing integer
	 * @return Zero
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 3))
	public int redirect_nextInt(RandomSource r, int i) {
		return LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forceCCWToggle") ? 0 : r.nextInt(i);
	}

}
