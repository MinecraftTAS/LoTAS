package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.PassiveDropManipulation;
import net.minecraft.entity.passive.EntitySheep;

@Mixin(EntitySheep.class)
public class MixinEntitySheepPatch {
	@ModifyVariable(method = "onSheared", at = @At(value = "STORE"), index = 5, ordinal = 1, remap = false)
	public int redirectWoolCount(int i) {
		return PassiveDropManipulation.optimizeSheep.isChecked()? 3 : i;
	}
}
