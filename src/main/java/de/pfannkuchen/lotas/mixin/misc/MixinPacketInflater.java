package de.pfannkuchen.lotas.mixin.misc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.CompressionDecoder;

/**
 * Minecraft is limited to 32k bytes packet sizes, which is not enough to copy our image. 
 * This Mixin increases that packet size
 * @author Pancake
 */
@Mixin(CompressionDecoder.class)
public class MixinPacketInflater {

	@ModifyConstant(method = "decode", constant = @Constant(intValue = 0x800000))
	public int redirect_writerIndex(int original) {
		return Integer.MAX_VALUE;
	}

}
