package de.pfannkuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.LoTAS;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

/**
 * This mixin is purely responsible for the hooking up the events in {@link LoTAS}.
 * @author Pancake
 */
@Mixin(PlayerList.class)
public class MixinPlayerList {

	/**
	 * Triggers an Event in {@link LoTAS#onClientConnect(ServerPlayer)} if the player connects
	 * @param connection Connection to the client
	 * @param serverPlayer Player associated with this connection
	 * @param ci Callback Info
	 */
	@Inject(method = "placeNewPlayer", at = @At("RETURN"))
	public void hookConnectEvent(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
		LoTAS.instance.onClientConnect(serverPlayer);
	}

}
