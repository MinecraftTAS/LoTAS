package de.pfannkuchen.lotas.mixin.dragonmanipulation;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannkuchen.lotas.LoTAS;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonStrafePlayerPhase;

/**
 * This mixin manipulates the rng of the dragon strafe player phase
 * @author Pancake
 */
@Mixin(DragonStrafePlayerPhase.class)
public class RngmodDragonStrafePlayerPhase {

	/**
	 * Manipulates the addend multiplier for determining the height of the next path node
	 * @return Zero
	 */
	@Redirect(method = "navigateToNextPathNode", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"))
	public float redirect_nextFloat(Random r) {
		return LoTAS.configmanager.getBoolean("dupemodwidget", "forceOptimalPath") ? 0.999f : r.nextFloat();
	}
	
	/**
	 * Manipulates the rotation changing integer
	 * @return Zero
	 */
	@Redirect(method = "findNewTarget", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
	public int redirect_nextInt(Random r, int i) {
		return LoTAS.configmanager.getBoolean("dupemodwidget", "forceCCWToggle") ? 0 : r.nextInt(i);
	}
	
}
