package de.pfannkuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import de.pfannkuchen.lotas.LoTAS;
import net.minecraft.server.MinecraftServer;

/**
 * Slows down the Minecraft Server
 * @author ScribbleLP
 */
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

	/**
	 * Replaces all 50L values in MinecraftServer.run() to the desired milliseconds per tick which is obtained by
	 * @param ignored the value that was originally used, in this case 50L
	 * @return Milliseconds per tick
	 */
 	@ModifyConstant(method = "runServer", constant = @Constant(longValue = 50L))
	private long serverTickWaitTime(long ignored) {
		return (long) LoTAS.tickratechanger.getMsPerTick();
	}
	
}