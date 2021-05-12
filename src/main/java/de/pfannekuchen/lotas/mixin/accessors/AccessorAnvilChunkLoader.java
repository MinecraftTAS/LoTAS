package de.pfannekuchen.lotas.mixin.accessors;

//#if MC>=11200
import java.util.Map;
//#else
//$$ import java.util.Set;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

@Mixin(AnvilChunkLoader.class)
public interface AccessorAnvilChunkLoader {

	//#if MC>=11200
	@Accessor("chunksToSave")
	public Map<ChunkPos, NBTTagCompound> chunksToSave();
	//#else
//$$ 	@Accessor("pendingAnvilChunksCoordinates")
//$$ 	public Set<ChunkPos> chunksToSave();
	//#endif
	
}
