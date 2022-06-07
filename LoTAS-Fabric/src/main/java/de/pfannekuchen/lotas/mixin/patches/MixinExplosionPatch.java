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
	//#if MC>=11900
//$$ 	@ModifyArg(method = "finalizeExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootContext$Builder;withRandom(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/level/storage/loot/LootContext$Builder;"), index = 0)
//$$ 	public net.minecraft.util.RandomSource stupid(net.minecraft.util.RandomSource random) {
//$$ 		return ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance") ? new net.minecraft.util.RandomSource() {
//$$
//$$ 			@Override
//$$ 			public net.minecraft.util.RandomSource fork() {
//$$ 				return this;
//$$ 			}
//$$
//$$ 			@Override
//$$ 			public net.minecraft.world.level.levelgen.PositionalRandomFactory forkPositional() {
//$$ 				return null;
//$$ 			}
//$$
//$$ 			@Override
//$$ 			public boolean nextBoolean() {
//$$ 				return false;
//$$ 			}
//$$
//$$ 			@Override
//$$ 			public double nextDouble() {
//$$ 				return 0;
//$$ 			}
//$$
//$$ 			@Override
//$$ 			public float nextFloat() {
//$$ 				if (new Random().nextFloat() < (ConfigUtils.getInt("hidden", "explosionoptimization") / 100F))
//$$ 					return 0;
//$$ 				else
//$$ 					return 1;
//$$ 			}
//$$
//$$ 			@Override
//$$ 			public double nextGaussian() {
//$$ 				return 0;
//$$ 			}
//$$
//$$ 			@Override
//$$ 			public int nextInt() {
//$$ 				return 0;
//$$ 			}
//$$
//$$ 			@Override
//$$ 			public int nextInt(int i) {
//$$ 				return 0;
//$$ 			}
//$$
//$$ 			@Override
//$$ 			public long nextLong() {
//$$ 				return 0;
//$$ 			}
//$$
//$$ 			@Override
//$$ 			public void setSeed(long l) {
//$$
//$$ 			}
//$$ 		}: random;
	//#else
	@ModifyArg(method = "finalizeExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootContext$Builder;withRandom(Ljava/util/Random;)Lnet/minecraft/world/level/storage/loot/LootContext$Builder;"), index = 0)
	public Random stupid(Random random) {
		return ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance") ? new FakeRandom() : random;
	//#endif
	}

}
