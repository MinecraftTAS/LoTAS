package de.pfannekuchen.lotas.mixin.accessors;

//#if MC>=10900
import net.minecraft.util.math.ChunkPos;
//#else
//$$ import net.minecraft.world.ChunkCoordIntPair;
//#endif

import java.util.Map;
import java.util.Set;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraft.world.chunk.storage.AnvilChunkLoader;

@Mixin(AnvilChunkLoader.class)
public interface AccessorAnvilChunkLoader {

	//#if MC>=11200
	@Accessor("chunksToSave")
	public Map<ChunkPos, NBTTagCompound> chunksToSave();
	//#else
	//#if MC>=10900
//$$ 	@Accessor("pendingAnvilChunksCoordinates")
//$$ 	public Set<ChunkPos> chunksToSave();
	//#else
//$$ 	@Accessor("pendingAnvilChunksCoordinates")
//$$ 	public Set<ChunkCoordIntPair> chunksToSave();
	//#endif
	//#endif
	
}
