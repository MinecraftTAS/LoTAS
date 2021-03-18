package de.pfannekuchen.lotas.savestates.playerloading;

import java.util.List;
import java.util.UUID;

import de.pfannekuchen.lotas.LoTASModContainer;
import de.pfannekuchen.lotas.mixin.savestates.AccessorWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SavestatePlayerLoading {
	
	/**
	 * Loads all worlds and players from the disk. Also sends the playerdata to the client in {@linkplain SavestatePlayerLoadingPacketHandler}
	 * 
	 * Side: Server
	 */
	public static void loadAndSendMotionToPlayer() {
		
		MinecraftServer server=FMLCommonHandler.instance().getMinecraftServerInstance();
		List<EntityPlayerMP> players=server.getPlayerList().getPlayers();
		PlayerList list=server.getPlayerList();
		
		WorldServer[] worlds=DimensionManager.getWorlds();
		for (WorldServer world : worlds) {
			WorldInfo info=world.getSaveHandler().loadWorldInfo();
			((AccessorWorld)world).setWorldInfo(info);
		}
		for(EntityPlayerMP player : players) {
			
			int dimensionPrev=player.dimension;
			
			NBTTagCompound nbttagcompound = server.getPlayerList().getPlayerNBT(player);
			
			int dimensionNow=0;
			if (nbttagcompound.hasKey("Dimension"))
            {
                dimensionNow = nbttagcompound.getInteger("Dimension");
            }
			
			if(dimensionNow!=dimensionPrev) {
				list.transferPlayerToDimension(player, dimensionNow, new NoPortalTeleporter());
			}else {
				((AccessorWorld)player.getServerWorld()).getUnloadedEntityList().remove(player);
			}
			
			player.readFromNBT(nbttagcompound);
			
			LoTASModContainer.NETWORK.sendTo(new SavestatePlayerLoadingPacket(nbttagcompound), player);
		}
	}
	
	/**
	 * Tries to reattach the player to an entity, if the player was riding it it while savestating.
	 * 
	 * Side: Server
	 * @param nbttagcompound where the ridden entity is saved
	 * @param worldserver that needs to spawn the entity
	 * @param playerIn that needs to ride the entity
	 */
	public static void reattachEntityToPlayer(NBTTagCompound nbttagcompound, World worldserver, Entity playerIn) {
		if (nbttagcompound != null && nbttagcompound.hasKey("RootVehicle", 10))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("RootVehicle");
            Entity entity1 = AnvilChunkLoader.readWorldEntity(nbttagcompound1.getCompoundTag("Entity"), worldserver, true);
            
            
            if(entity1==null) {
            	for (Entity entity : worldserver.loadedEntityList) {
            		if(entity.getUniqueID().equals(nbttagcompound1.getUniqueId("Attach"))) entity1=entity;
            	}
            }
            
            if (entity1 != null)
            {
                UUID uuid = nbttagcompound1.getUniqueId("Attach");

                if (entity1.getUniqueID().equals(uuid))
                {
                    playerIn.startRiding(entity1, true);
                }
                else
                {
                    for (Entity entity : entity1.getRecursivePassengers())
                    {
                        if (entity.getUniqueID().equals(uuid))
                        {
                            playerIn.startRiding(entity, true);
                            break;
                        }
                    }
                }

                if (!playerIn.isRiding())
                {
                    worldserver.removeEntityDangerously(entity1);

                    for (Entity entity2 : entity1.getRecursivePassengers())
                    {
                        worldserver.removeEntityDangerously(entity2);
                    }
                }
            }
        }
	}
	
}
