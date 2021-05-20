package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import net.minecraft.server.MinecraftServer;
//#if MC>=10900
import net.minecraft.util.math.BlockPos;
//#else
//$$ import net.minecraft.util.BlockPos;
//#endif
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;

@Mixin(WorldProvider.class)
public class MixinWorldProviderPatch {
	
	//#if MC>=10900
	@Redirect(method = "getRandomizedSpawnPoint", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldType;getSpawnFuzz(Lnet/minecraft/world/WorldServer;Lnet/minecraft/server/MinecraftServer;)I"))
	public int redirectRandomizedSpawnPoint(WorldType source, WorldServer world, MinecraftServer server) {
	//#else
	//$$ @Redirect(method = "getRandomizedSpawnPoint", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldType;getSpawnFuzz()I"))
		//$$ public int redirectRandomizedSpawnPoint(WorldType source) {
	//#endif
		return 2;
	}
	
	//#if MC>=10900
	@Redirect(method = "getRandomizedSpawnPoint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTopSolidOrLiquidBlock(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;"))
	//#else
	//$$ @Redirect(method = "getRandomizedSpawnPoint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTopSolidOrLiquidBlock(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/util/BlockPos;"))
	//#endif
	public BlockPos redirectBlockPos(World source, BlockPos blockPos) {
		return source.getTopSolidOrLiquidBlock(blockPos.add(-LoTASModContainer.offsetX, 0, -LoTASModContainer.offsetZ));
	}
	
	
}
