package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.client.server.IntegratedServer;

/**
 * Savestates whenever it was requested from another Thread
 * @author ScribbleLP
 */
@Mixin(IntegratedServer.class)
public class MixinIntegratedServer {
	@Redirect(method = "tickServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/server/IntegratedServer;saveAllChunks(ZZZ)Z"))
	public boolean redirectSave(IntegratedServer server, boolean b1, boolean b2, boolean b3) {
		if(!SavestateMod.showSavestateDone) {
			if (KeybindsUtils.shouldSavestate) {
				KeybindsUtils.shouldSavestate = false;
				SavestateMod.savestate(null);
			}
		} else {
			server.saveAllChunks(false, false, false);
		}
		return true;
	}
}
