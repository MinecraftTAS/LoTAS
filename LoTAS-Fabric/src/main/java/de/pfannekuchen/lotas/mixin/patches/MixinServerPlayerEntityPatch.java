package de.pfannekuchen.lotas.mixin.patches;

import java.util.concurrent.atomic.AtomicBoolean;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.mixin.accessors.AccessorServerPlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayerEntityPatch extends Player {

	public MixinServerPlayerEntityPatch(Level world, GameProfile profile) {
		super(world, profile);
	}

	@Final
	@Shadow
	public MinecraftServer server;

	@Inject(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> returnable) {
		if (ConfigUtils.getBoolean("tools", "takeDamage"))
			return;
		boolean bl = this.server.isDedicatedServer() && this.server.isPvpAllowed() && "fall".equals(source.msgId);
		AtomicBoolean flag = new AtomicBoolean(false);
		server.getPlayerList().getPlayers().forEach(player -> {
			if (((AccessorServerPlayerEntity) player).getSpawnInvulnerableTime() <= 0) {
				((AccessorServerPlayerEntity) player).setSpawnInvulnerableTime(60);
				flag.set(true);
			}
		});
		if (!bl && source != DamageSource.OUT_OF_WORLD) {
			if (flag.get()) {
				Minecraft.getInstance().player.setDeltaMovement(0, 0, 0);
				Minecraft.getInstance().player.hurtMarked = true;
				Minecraft.getInstance().player.xo = this.x;
				Minecraft.getInstance().player.yo = this.y;
				Minecraft.getInstance().player.zo = this.z;
			}
			returnable.setReturnValue(false);
			returnable.cancel();
		}
	}

}
