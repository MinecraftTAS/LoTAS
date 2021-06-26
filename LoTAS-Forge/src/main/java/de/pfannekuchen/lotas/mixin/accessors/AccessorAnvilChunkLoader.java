package de.pfannekuchen.lotas.mixin.accessors;

import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.chunk.storage.AnvilChunkLoader;

@Mixin(AnvilChunkLoader.class)
public interface AccessorAnvilChunkLoader {

	//#if MC>=11200
	@Accessor("chunksToSave")
	public Map<net.minecraft.util.math.ChunkPos, net.minecraft.nbt.NBTTagCompound> chunksToSave();
	//#else
	//#if MC>=10900
//$$ 	@Accessor("pendingAnvilChunksCoordinates")
//$$ 	public java.util.Set<net.minecraft.util.math.ChunkPos> chunksToSave();
	//#else
//$$ 	@Accessor("pendingAnvilChunksCoordinates")
//$$ 	public java.util.Set<net.minecraft.world.ChunkCoordIntPair> chunksToSave();
	//#endif
	//#endif
	
}
