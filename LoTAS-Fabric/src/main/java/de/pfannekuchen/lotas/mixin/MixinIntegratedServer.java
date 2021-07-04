package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.server.integrated.IntegratedServer;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer {
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/integrated/IntegratedServer;save(ZZZ)Z"))
	public boolean redirectSave(IntegratedServer server, boolean b1, boolean b2, boolean b3) {
		if(!SavestateMod.showSavestateDone) {
			if (KeybindsUtils.shouldSavestate) {
				KeybindsUtils.shouldSavestate = false;
				SavestateMod.savestate(null);
			}
		} else {
			server.save(false, false, false);
		}
		return true;
	}
}
