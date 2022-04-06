package de.pfannkuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.levelgen.structure.StructureCheck;

/**
 * This mixin accessor makes a few chunk sources available
 * @author Pancake
 */
@Mixin(ServerLevel.class)
public interface AccessorServerLevel {

	/**
	 * This Accessor opens the private field containing the chunk source for the world
	 * @return Chunk Source
	 */
	@Accessor
	public ServerChunkCache getChunkSource();
	@Accessor @Mutable
	public void setChunkSource(ServerChunkCache c);
	
	/**
	 * This Accessor opens the private field containing the feature manager for the world
	 * @return Structure Feature Manager
	 */
	@Accessor
	public StructureFeatureManager getStructureFeatureManager();
	
	/**
	 * This Accessor opens the private field containing the structure checker for the world
	 * @return Structure Check
	 */
	@Accessor
	public StructureCheck getStructureCheck();
	
	/**
	 * This Accessor opens the private field containing the entity manager for the world
	 * @return Entity Manager
	 */
	@Accessor
	public PersistentEntitySectionManager<Entity> getEntityManager();
	
}