package de.pfannekuchen.lotas.mixin;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinEndPortal {
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getForcedSpawnPoint()Lnet/minecraft/util/math/BlockPos;"), method = "changeDimension")
	public void endChallenge(CallbackInfoReturnable<Entity> cir) {
		((ServerPlayerEntity) (Object) this).setGameMode(GameMode.SPECTATOR);
		Timer.running = false;
		ChatHud chat = MinecraftClient.getInstance().inGameHud.getChatHud();
		chat.clear(true);
		chat.addMessage(new LiteralText("You have completed: \u00A76" + ChallengeLoader.map.displayName + "\u00A7f! Your Time is: " + Timer.getCurrentTimerFormatted()));
		chat.addMessage(new LiteralText("Please submit your \u00A7craw \u00A7fvideo to \u00A77#new-misc-things \u00A7f on the Minecraft TAS Discord Server."));
		ChallengeLoader.map = null;
        try {
        	Field h = MinecraftClient.class.getDeclaredField("levelStorage");
        	h.setAccessible(true);
        	h.set(MinecraftClient.getInstance(), new LevelStorage(MinecraftClient.getInstance().runDirectory.toPath().resolve("saves"), MinecraftClient.getInstance().runDirectory.toPath().resolve("backups"), MinecraftClient.getInstance().getDataFixer()));
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
