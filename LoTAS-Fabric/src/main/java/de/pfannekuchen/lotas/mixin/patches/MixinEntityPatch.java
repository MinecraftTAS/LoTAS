package de.pfannekuchen.lotas.mixin.patches;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;

@Mixin(LivingEntity.class)
public class MixinEntityPatch {

	@Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
	public void redodrop(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
			if (!man.enabled.isChecked())
				continue;
			List<ItemStack> list = man.redirectDrops((LivingEntity) (Object) this);
			if (!list.isEmpty()) {
				for (ItemStack itemStack : list) {
					((LivingEntity) (Object) this).dropStack(itemStack);
				}
				ci.cancel();
			}
		}
	}

}
