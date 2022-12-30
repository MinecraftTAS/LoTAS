package com.minecrafttas.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;

/**
 * This mixin accessor makes the data and identifier accessible for every version. Two getters were added in 1.17
 * @author Pancake
 */
@Mixin(ServerboundCustomPayloadPacket.class)
@Environment(EnvType.CLIENT)
public interface AccessorServerboundCustomPayloadPacket {

	/**
	 * This Accessor opens the private field containing the identifier for the packet
	 * @return Identifier
	 */
	@Accessor
	public ResourceLocation getIdentifier();

	/**
	 * This Accessor opens the private field containing the data for the packet
	 * @return Data
	 */
	@Accessor
	public FriendlyByteBuf getData();
	
}
