package de.pfannekuchen.lotas.mixin.patches;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.mixin.accessors.AccessorEntityPlayerMP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Mixin(PlayerControllerMP.class)
public class MixinBlockHitDelayPatch {
	@Shadow
	int blockHitDelay;

	@Redirect(method = "onPlayerDamageBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;blockHitDelay:I", opcode = Opcodes.GETFIELD, ordinal = 0))
	public int inject_continueDestroyBlock(PlayerControllerMP mode) {
		if (ConfigUtils.getBoolean("tools", "blockHitDelayOptimizer")) {
			if (blockHitDelay > 0) {
				//#if MC<=11002
				//#if MC<=10809
//$$ 				FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerList().forEach(player -> {
				//#else
//$$ 				FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList().forEach(player -> {
				//#endif
				//#else
				FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player -> {
				//#endif
					((AccessorEntityPlayerMP) player).respawnInvulnerabilityTicks(60);
				});
				try {
					EntityPlayerSP player = MCVer.player(Minecraft.getMinecraft());
					player.motionX = 0;
					player.motionY = 0;
					player.motionZ = 0;
					player.prevPosX = player.posX;
					player.prevPosY = player.posY;
					player.prevPosZ = player.posZ;
				} catch (Exception e) {

				}
			}
			blockHitDelay=0;
		}
		return blockHitDelay;
	}
}
