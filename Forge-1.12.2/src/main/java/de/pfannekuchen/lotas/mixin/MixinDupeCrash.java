package de.pfannekuchen.lotas.mixin;

import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

@Mixin(EntityTracker.class)
public class MixinDupeCrash {

	@Shadow @Final
    private WorldServer world;
    @Shadow @Final
    private Set<EntityTrackerEntry> entries = Sets.<EntityTrackerEntry>newHashSet();
	
	@Overwrite
    public void tick() {
        try {
			List<EntityPlayerMP> list = Lists.<EntityPlayerMP>newArrayList();

			for (EntityTrackerEntry entitytrackerentry : this.entries)
			{
			    entitytrackerentry.updatePlayerList(this.world.playerEntities);

			    if (entitytrackerentry.playerEntitiesUpdated)
			    {
			        Entity entity = entitytrackerentry.getTrackedEntity();

			        if (entity instanceof EntityPlayerMP)
			        {
			            list.add((EntityPlayerMP)entity);
			        }
			    }
			}

			for (int i = 0; i < list.size(); ++i)
			{
			    EntityPlayerMP entityplayermp = list.get(i);

			    for (EntityTrackerEntry entitytrackerentry1 : this.entries)
			    {
			        if (entitytrackerentry1.getTrackedEntity() != entityplayermp)
			        {
			            entitytrackerentry1.updatePlayerEntity(entityplayermp);
			        }
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
}
