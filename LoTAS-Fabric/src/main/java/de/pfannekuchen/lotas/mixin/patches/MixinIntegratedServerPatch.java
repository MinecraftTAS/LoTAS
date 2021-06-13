package de.pfannekuchen.lotas.mixin.patches;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.taschallenges.ChallengeLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServerPatch {

	@ModifyArg(method = "<init>", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;<init>(Ljava/io/File;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/server/command/CommandManager;Lcom/mojang/authlib/yggdrasil/YggdrasilAuthenticationService;Lcom/mojang/authlib/minecraft/MinecraftSessionService;Lcom/mojang/authlib/GameProfileRepository;Lnet/minecraft/util/UserCache;Lnet/minecraft/server/WorldGenerationProgressListenerFactory;Ljava/lang/String;)V"))
	private static File hackAnvilFile(File original) {
		return ChallengeLoader.map == null ? original : new File(MinecraftClient.getInstance().runDirectory, "challenges");
	}

}