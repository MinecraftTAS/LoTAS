package de.pfannekuchen.lotas.mixin.patches;

import java.util.concurrent.atomic.AtomicBoolean;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.mixin.accessors.AccessorServerPlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.server.MinecraftServer;

@Mixin(MultiPlayerGameMode.class)
public class MixinBlockHitDelayPatch {
	@Shadow
	int destroyDelay;

	@Redirect(method = "continueDestroyBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyDelay:I", opcode = Opcodes.GETFIELD, ordinal = 0))
	public int inject_continueDestroyBlock(MultiPlayerGameMode mode) {
		if (ConfigUtils.getBoolean("tools", "blockHitDelayOptimizer")) {
			if(destroyDelay>0) {
				MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
				AtomicBoolean flag = new AtomicBoolean(false);
				
				server.getPlayerList().getPlayers().forEach(player -> {
					if (((AccessorServerPlayerEntity) player).getSpawnInvulnerableTime() <= 0) {
						((AccessorServerPlayerEntity) player).setSpawnInvulnerableTime(60);
						flag.set(true);
					}
				});
				Minecraft mc = Minecraft.getInstance();
				mc.player.setDeltaMovement(0, 0, 0);
				mc.player.hurtMarked = true;
			}
			destroyDelay=0;
		}
		return destroyDelay;
	}
}
