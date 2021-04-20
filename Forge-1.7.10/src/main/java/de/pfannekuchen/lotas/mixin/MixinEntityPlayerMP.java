package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import de.pfannekuchen.lotas.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer {

	/*
	 * This Class makes avoid taking damage realistic
	 */
	
	@Shadow
	public int field_147101_bU;
	@Shadow
	public MinecraftServer mcServer;

	public boolean flag2 = false;
	
	
	public MixinEntityPlayerMP(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
	}

	@Inject(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isDedicatedServer()Z", shift = At.Shift.AFTER))
	public void injectAfterFlag(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
		boolean flag = this.mcServer.isDedicatedServer() && "fall".equals(source.damageType);
		
		if (!ConfigManager.getBoolean("tools", "takeDamage")) Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().playerEntityList.forEach(p -> {
			if (((EntityPlayerMP) p).field_147101_bU <= 1) {
				((EntityPlayerMP) p).field_147101_bU = 60;
				flag2 = true;
			}
		});
		if (!flag && field_147101_bU > 0 && source != DamageSource.outOfWorld) {
			try {
				if (flag2) {
					flag2 = false;
					Minecraft.getMinecraft().thePlayer.motionX = 0;
					Minecraft.getMinecraft().thePlayer.motionY = 0;
					Minecraft.getMinecraft().thePlayer.motionZ = 0;
					Minecraft.getMinecraft().thePlayer.prevPosX = Minecraft.getMinecraft().thePlayer.posX;
					Minecraft.getMinecraft().thePlayer.prevPosY = Minecraft.getMinecraft().thePlayer.posY;
					Minecraft.getMinecraft().thePlayer.prevPosZ = Minecraft.getMinecraft().thePlayer.posZ; 
				}
			} catch (Exception e) {
				
			}
		}
	}

}
