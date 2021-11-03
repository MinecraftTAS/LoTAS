package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.dropmanipulation.drops.entitydrops.PassiveDropManipulation;
import net.minecraft.world.entity.animal.Sheep;

@Mixin(Sheep.class)
public class MixinSheepPatch {
	@ModifyVariable(method = "shear", at = @At(value = "STORE"), index = 1, ordinal = 0)
	public int redirectWoolCount(int i) {
		return PassiveDropManipulation.optimizeSheep.isChecked() ? 3 : i;
	}
}
