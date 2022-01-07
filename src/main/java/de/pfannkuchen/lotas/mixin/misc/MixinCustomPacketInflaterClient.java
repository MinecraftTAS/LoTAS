package de.pfannkuchen.lotas.mixin.misc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;

/**
 * Minecraft is limited to 32k bytes packet sizes, which is not enough to copy our image. 
 * This Mixin increases that packet size
 * @author Pancake
 */
@Mixin(ServerboundCustomPayloadPacket.class)
public class MixinCustomPacketInflaterClient {

	@ModifyConstant(method = "Lnet/minecraft/network/protocol/game/ServerboundCustomPayloadPacket;<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", constant = @Constant(intValue = 32767))
	public int redirect_writerIndex(int original) {
		return Integer.MAX_VALUE;
	}
	
}
