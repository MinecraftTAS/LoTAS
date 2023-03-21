package de.pfannekuchen.lotas.mixin.patches;

import java.io.File;
import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;

import com.mojang.authlib.GameProfile;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
//#if MC>=11000
import net.minecraft.world.GameType;
//#else
//$$ import net.minecraft.world.WorldSettings.GameType;
//#endif
import net.minecraft.world.chunk.storage.AnvilSaveConverter;

/**
 * This Mixin makes Players invulnerable if they want to be
 * @author Pancake
 * @since v1.1
 * @version v1.1
 */
@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMPPatch extends EntityPlayer {
	public MixinEntityPlayerMPPatch(World worldIn, GameProfile gameProfileIn) { super(worldIn, gameProfileIn); }

	
	@Shadow
	public int respawnInvulnerabilityTicks;
	@Shadow
	public MinecraftServer mcServer;
	
	public boolean takenDamage = false;
	
	@Shadow
	public abstract boolean canPlayersAttack();

	@Inject(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isDedicatedServer()Z", shift = At.Shift.AFTER))
	public void injectAfterFlag(DamageSource source, float amount, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> ci) {
		boolean flag = this.mcServer.isDedicatedServer() && this.canPlayersAttack() && "fall".equals(source.damageType);
		
		if (!ConfigUtils.getBoolean("tools", "takeDamage"))
			if (respawnInvulnerabilityTicks <= 1 && !isEnderdragonLoaded()) {
				respawnInvulnerabilityTicks = 60;
				takenDamage = true;
			}
		//#if MC>=11100
		if (!flag && this.respawnInvulnerabilityTicks > 0 && source != DamageSource.OUT_OF_WORLD) {
		//#else
//$$ 		if (!flag && this.respawnInvulnerabilityTicks > 0 && source != DamageSource.outOfWorld) {	
		//#endif
			try {
				if (takenDamage) {
					takenDamage = false;
					EntityPlayerSP player = MCVer.player(Minecraft.getMinecraft());
					player.motionX = 0;
					player.motionY = 0;
					player.motionZ = 0;
					player.prevPosX = player.posX;
					player.prevPosY = player.posY;
					player.prevPosZ = player.posZ;
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	private boolean isEnderdragonLoaded() {
		if(dimension == 1) {
			for (Entity entity : getEntityWorld().loadedEntityList){
				if(entity instanceof EntityDragon) {
					return true;
				}
			}
		}
		return false;
	}
	
}
