package de.pfannekuchen.lotas.mixin.patches;

import java.util.List;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;

@Mixin(LivingEntity.class)
public class MixinEntityPatch {

	@Inject(method = "dropFromLootTable", at = @At("HEAD"), cancellable = true)
	public void redodrop(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
			if (!man.enabled.selected())
				continue;
			List<ItemStack> list = man.redirectDrops((LivingEntity) (Object) this);
			if (!list.isEmpty()) {
				for (ItemStack itemStack : list) {
					((LivingEntity) (Object) this).spawnAtLocation(itemStack);
				}
				ci.cancel();
			}
		}
	}

}
