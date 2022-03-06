package de.pfannekuchen.lotas.mixin.patches;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * This Mixin alters the behaviour when entities get killed
 * @author Pancake
 */
@Mixin(LivingEntity.class)
public class MixinEntityPatch {

	/**
	 * Redirects the drops for Entities
	 */
	@Inject(method = "dropFromLootTable", at = @At("HEAD"), cancellable = true)
	public void redodrop(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
			if (!man.enabled.selected())
				continue;
			int lootingBonus=getLootingValue(source);
			List<ItemStack> list = man.redirectDrops((LivingEntity) (Object) this, lootingBonus);
			if (!list.isEmpty()) {
				for (ItemStack itemStack : list) {
					((LivingEntity) (Object) this).spawnAtLocation(itemStack);
				}
				ci.cancel();
			}
		}
	}

	private int getLootingValue(DamageSource source) {
		if (source.getEntity() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) source.getEntity();
			for (Tag tag : player.getMainHandItem().getEnchantmentTags()) {
				CompoundTag compoundTag = (CompoundTag) tag;
				if (compoundTag.getString("id").equals("minecraft:looting")) {
					return compoundTag.getInt("lvl");
				}
			}
		}
		return 0;
	}

}
