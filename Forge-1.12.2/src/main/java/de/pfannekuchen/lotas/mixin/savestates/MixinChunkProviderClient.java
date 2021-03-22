package de.pfannekuchen.lotas.mixin.savestates;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.pfannekuchen.lotas.duck.ChunkProviderDuck;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.world.chunk.Chunk;

@Mixin(ChunkProviderClient.class)
public class MixinChunkProviderClient implements ChunkProviderDuck {
	@Shadow @Final
	private Long2ObjectMap<Chunk> chunkMapping;

	@Override
	public void unloadAllChunks() {
    	ObjectIterator<Chunk> objectiterator = this.chunkMapping.values().iterator();

        while (objectiterator.hasNext()) {
            Chunk chunk = (Chunk)objectiterator.next();
            chunk.onUnload();
        }
        chunkMapping.clear();
    }

}
