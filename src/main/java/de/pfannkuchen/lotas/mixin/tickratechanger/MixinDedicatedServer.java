package de.pfannkuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.server.dedicated.DedicatedServer;

/**
 * This mixin disabled the max tick length preventing a crash that occurs when in tickrate zero for too long.
 * @author Pancake
 */
@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {

	@Overwrite
	public long getMaxTickLength() {
		return Long.MAX_VALUE;
	}

}
