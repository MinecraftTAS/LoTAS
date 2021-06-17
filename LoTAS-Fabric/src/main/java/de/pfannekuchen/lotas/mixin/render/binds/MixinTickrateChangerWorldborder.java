package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public class MixinTickrateChangerWorldborder {

	@ModifyVariable(method = "renderWorldBorder", at = @At(value = "STORE"), index = 20, ordinal = 4)
	public float injectf3(float f) {
		return (TickrateChangerMod.getMilliseconds() % 3000L) / 3000.0F;
	}

}
