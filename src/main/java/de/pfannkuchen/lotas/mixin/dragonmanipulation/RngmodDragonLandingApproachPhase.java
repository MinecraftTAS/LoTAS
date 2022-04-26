package de.pfannkuchen.lotas.mixin.dragonmanipulation;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannkuchen.lotas.LoTAS;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonLandingApproachPhase;

/**
 * This mixin manipulates the rng of the dragon landing approach phase
 * @author Pancake
 */
@Mixin(DragonLandingApproachPhase.class)
public class RngmodDragonLandingApproachPhase {

	/**
	 * Manipulates the addend multiplier for determining the height of the next path node
	 * @return Zero
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"))
	public float redirect_nextFloat(Random r) {
		return LoTAS.configmanager.getBoolean("dupemodwidget", "forceOptimalPath") ? 0.999f : r.nextFloat();
	}
	
}
