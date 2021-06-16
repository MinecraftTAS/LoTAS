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
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntityPatch extends PlayerEntity {

	//#if MC>=11601
//$$ 	public MixinServerPlayerEntityPatch(World world, BlockPos blockPos, GameProfile gameProfile) { super(world, blockPos, gameProfile); }
	//#else
	public MixinServerPlayerEntityPatch(World world, GameProfile profile) {
		super(world, profile);
	}
	//#endif

	@Final
	@Shadow
	public MinecraftServer server;

	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> returnable) {
		if (ConfigUtils.getBoolean("tools", "takeDamage"))
			return;
		boolean bl = this.server.isDedicated() && this.server.isPvpEnabled() && "fall".equals(source.name);
		AtomicBoolean flag = new AtomicBoolean(false);
		server.getPlayerManager().getPlayerList().forEach(player -> {
			if (((AccessorServerPlayerEntity) player).getField_13998() <= 0) {
				((AccessorServerPlayerEntity) player).setField_13998(60);
				flag.set(true);
			}
		});
		if (!bl && source != DamageSource.OUT_OF_WORLD) {
			if (flag.get()) {
				MinecraftClient.getInstance().player.setVelocity(0, 0, 0);
				MinecraftClient.getInstance().player.velocityModified = true;
				//#if MC>=11601
//$$ 				MinecraftClient.getInstance().player.prevX = this.getX();
//$$ 				MinecraftClient.getInstance().player.prevY = this.getY();
//$$ 				MinecraftClient.getInstance().player.prevZ = this.getZ();
				//#else
				MinecraftClient.getInstance().player.prevX = this.x;
				MinecraftClient.getInstance().player.prevY = this.y;
				MinecraftClient.getInstance().player.prevZ = this.z;
				//#endif
			}
			returnable.setReturnValue(false);
			returnable.cancel();
		}
	}

}
