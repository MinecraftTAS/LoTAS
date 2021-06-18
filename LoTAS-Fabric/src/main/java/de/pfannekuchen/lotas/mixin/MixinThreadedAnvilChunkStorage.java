package de.pfannekuchen.lotas.mixin;

import java.util.Collection;
import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Iterables;

import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;

@Mixin(ThreadedAnvilChunkStorage.class)
public class MixinThreadedAnvilChunkStorage {

	private static int count;

	@Shadow
	private ServerWorld world;

	@Inject(method = "save", at = @At(value = "HEAD"))
	public void checkWhenStart(CallbackInfo ci) {
		if (!SavestateMod.stillSaving) {
			SavestateMod.stillSaving = true;
			count = 0;
		}
		count++;
	}

	@Inject(method = "save", at = @At(value = "RETURN"))
	public void checkWhenDone(CallbackInfo ci) {
		MinecraftServer server = world.getServer();
		if (sizeOfIterable(server.getWorlds()) == count && SavestateMod.stillSaving) {
			SavestateMod.stillSaving = false;
		}
	}

	private int sizeOfIterable(Iterable<ServerWorld> worlds) {
		return Iterables.size(worlds);
	}
}
