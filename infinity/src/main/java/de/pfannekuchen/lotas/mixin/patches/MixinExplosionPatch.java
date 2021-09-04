package de.pfannekuchen.lotas.mixin.patches;

import java.util.Random;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.FakeRandom;

/**
 * This Mixin redirects the blocks to be destroyed, on an explosion
 * @author Pancake
 */
@Mixin(Explosion.class)
public class MixinExplosionPatch {

	/**
	 * Redirect the Randomness of an Explosion
	 */
	@ModifyArg(method = "finalizeExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootContext$Builder;withRandom(Ljava/util/Random;)Lnet/minecraft/world/level/storage/loot/LootContext$Builder;"), index = 0)
	public Random stupid(Random random) {
		return ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance") ? new FakeRandom() : random;
	}

}
