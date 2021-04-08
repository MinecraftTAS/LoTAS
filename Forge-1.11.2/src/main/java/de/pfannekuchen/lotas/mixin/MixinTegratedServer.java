package de.pfannekuchen.lotas.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;

@Mixin(MinecraftServer.class)
public class MixinTegratedServer {

	@Shadow @Final
	public File anvilFile;
	@Shadow @Final
	public ISaveFormat anvilConverterForAnvilFile;
	@Shadow @Final
	public DataFixer dataFixer;
	
	@Inject(at = @At("RETURN"), method = "<init>")
	public void hackCtr(CallbackInfo ci) {
		this.anvilFile = new File(Minecraft.getMinecraft().mcDataDir, ChallengeLoader.map == null ? "saves" : "challenges/"); // EPIC
		this.anvilConverterForAnvilFile = new AnvilSaveConverter(anvilFile, dataFixer);
	}

}
