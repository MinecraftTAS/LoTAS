package de.pfannekuchen.lotas.mixin.savestates;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.pfannekuchen.lotas.duck.ChunkProviderDuck;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer implements ChunkProviderDuck{
	
	@Shadow @Final
	private Long2ObjectMap<Chunk> id2ChunkMap;

	@Override
	public void unloadAllChunks() {
    	ObjectIterator<Chunk> objectiterator = this.id2ChunkMap.values().iterator();

        while (objectiterator.hasNext()) {
            Chunk chunk = (Chunk)objectiterator.next();
            this.saveChunkData(chunk);
            this.saveChunkExtraData(chunk);
            chunk.onUnload();
        }
        id2ChunkMap.clear();
    }
	@Shadow
	abstract void saveChunkExtraData(Chunk chunk);
	@Shadow
	abstract void saveChunkData(Chunk chunk);

}
