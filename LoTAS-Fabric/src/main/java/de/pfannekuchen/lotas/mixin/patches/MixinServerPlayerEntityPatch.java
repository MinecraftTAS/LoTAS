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

/**
 * Makes the Player Invulnerable by abusing 3 seconds spawn invulnerability
 * @author Pancake
 */
@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayerEntityPatch extends Player {

	//#if MC>=11900
//$$ 	public MixinServerPlayerEntityPatch(Level level, net.minecraft.core.BlockPos blockPos, float f, GameProfile gameProfile, net.minecraft.world.entity.player.ProfilePublicKey profilePublicKey) { super(level, blockPos, f, gameProfile, profilePublicKey); }
	//#else
	//#if MC>=11601
	//#if MC>=11605
//$$ 	public MixinServerPlayerEntityPatch(Level level, net.minecraft.core.BlockPos blockPos, float f, GameProfile gameProfile) { super(level, blockPos, f, gameProfile); }
	//#else
//$$ 	public MixinServerPlayerEntityPatch(Level level, net.minecraft.core.BlockPos blockPos, GameProfile gameProfile) { super(level, blockPos, gameProfile); }
	//#endif
	//#else
	public MixinServerPlayerEntityPatch(Level world, GameProfile profile) { super(world, profile); }
	//#endif
	//#endif

	@Final
	@Shadow
	public MinecraftServer server;

	/**
	 * Disable Damage
	 */
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
			}
			returnable.setReturnValue(false);
			returnable.cancel();
		}
	}

}
