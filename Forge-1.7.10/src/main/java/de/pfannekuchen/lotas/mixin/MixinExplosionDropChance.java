package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.config.ConfigManager;
import net.minecraft.world.Explosion;

@Mixin(Explosion.class)
public class MixinExplosionDropChance {

	@ModifyArg(method = "doExplosionB", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;dropBlockAsItemWithChance(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/Block;FI)V"), index = 3)
	public float hijackExplosion(float orig) {
		return ConfigManager.getBoolean("tools", "manipulateExplosionDropChance") ? (ConfigManager.getInt("hidden", "explosionoptimization") / 100F) : orig;
	}
	
}
