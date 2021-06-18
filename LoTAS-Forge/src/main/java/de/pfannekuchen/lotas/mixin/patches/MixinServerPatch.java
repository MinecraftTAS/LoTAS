package de.pfannekuchen.lotas.mixin.patches;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;

//#if MC>=10900
import net.minecraft.util.datafix.DataFixer;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;

/**
 * This Mixin modifies the directory where to load the world from, whenever a TAS Challenge is being played.
 * @since v1.2
 * @version v1.2
 * @author Pancake
 */
@Mixin(MinecraftServer.class)
public class MixinServerPatch {
	@Shadow @Mutable
	public File anvilFile;
	@Shadow @Mutable
	public ISaveFormat anvilConverterForAnvilFile;
	@Shadow @Mutable
	public DataFixer dataFixer;
	@Inject(at = @At("RETURN"), method = "<init>")
	public void hackCtr(CallbackInfo ci) {
		this.anvilFile = new File(Minecraft.getMinecraft().mcDataDir, ChallengeMap.currentMap == null ? "saves" : "challenges/"); // EPIC
		this.anvilConverterForAnvilFile = new AnvilSaveConverter(anvilFile, dataFixer);
	}
//#else
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//$$ import net.minecraft.server.integrated.IntegratedServer;
//$$ @Mixin(IntegratedServer.class)
//$$ public class MixinServerPatch {
//$$ 	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;<init>(Ljava/io/File;Ljava/net/Proxy;Ljava/io/File;)V"), index = 0, method = "Lnet/minecraft/server/integrated/IntegratedServer;<init>(Lnet/minecraft/client/Minecraft;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/world/WorldSettings;)V")
//$$ 	private static File hackCtr2(File original) {
//$$ 		return new File(Minecraft.getMinecraft().mcDataDir, ChallengeMap.currentMap == null ? "saves" : "challenges/"); // EPIC
//$$ 	}
//#endif
}
