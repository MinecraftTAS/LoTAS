package de.pfannekuchen.lotas.mixin.patches;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.FakeRandom;
import net.minecraft.world.explosion.Explosion;

@Mixin(Explosion.class)
public class MixinExplosionPatch {

	@ModifyArg(method = "affectWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/context/LootContext$Builder;setRandom(Ljava/util/Random;)Lnet/minecraft/loot/context/LootContext$Builder;"), index = 0)
	public Random stupid(Random random) {
		return ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance") ? new FakeRandom() : random;
	}
	
}
