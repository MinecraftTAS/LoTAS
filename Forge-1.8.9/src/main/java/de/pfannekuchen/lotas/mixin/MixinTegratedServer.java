package de.pfannekuchen.lotas.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;

@Mixin(IntegratedServer.class)
public class MixinTegratedServer {
	
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;<init>(Ljava/io/File;Ljava/net/Proxy;Ljava/io/File;)V"), index = 0, method = "Lnet/minecraft/server/integrated/IntegratedServer;<init>(Lnet/minecraft/client/Minecraft;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/world/WorldSettings;)V")
	private static File hackCtr2(File original) {
		return new File(Minecraft.getMinecraft().mcDataDir, ChallengeLoader.map == null ? "saves" : "challenges/"); // EPIC
	}

}
