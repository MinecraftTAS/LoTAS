package de.pfannekuchen.lotas.util;

import java.util.List;
import java.util.UUID;

import de.pfannekuchen.lotas.duck.ChunkProviderDuck;
import de.pfannekuchen.lotas.mixin.savestates.AccessorSaveHandler;
import de.pfannekuchen.lotas.mixin.savestates.AccessorWorld;
import de.pfannekuchen.lotas.mixin.savestates.MixinChunkProviderClient;
import de.pfannekuchen.lotas.mixin.savestates.MixinChunkProviderServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**
 * Various methods to unload/reload chunks and make loadless savestates possible
 * @author ScribbleLP
 *
 */
public class ChunkUtil {
	/**
	 * Unloads all chunks and reloads the renderer so no chunks will be visible throughout the unloading progress<br>
	 * <br>
	 * Side: Client
	 * @see MixinChunkProviderClient#unloadAllChunks()
	 */
	@SideOnly(Side.CLIENT)
	public static void unloadAllClientChunks() {
		Minecraft mc = Minecraft.getMinecraft();
		
		ChunkProviderClient chunkProvider=mc.world.getChunkProvider();
		
		((ChunkProviderDuck)chunkProvider).unloadAllChunks();
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	/**
	 * Unloads all chunks on the server<br>
	 * <br>
	 * Side: Server
	 * @see MixinChunkProviderServer#unloadAllChunks()
	 */
	public static void unloadAllServerChunks() {
		//Forge
		WorldServer[] worlds=DimensionManager.getWorlds();
		//Vanilla
		//WorldServer[] worlds=FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
		
		for (WorldServer world:worlds) {
			ChunkProviderServer chunkProvider=world.getChunkProvider();
			
			((ChunkProviderDuck)chunkProvider).unloadAllChunks();
		}
		
	}
	/**
	 * The player chunk map keeps track of which chunks need to be sent to the client. <br>
	 * Removing the player stops the server from sending chunks to the client.<br>
	 * <br>
	 * Side: Server
	 * @see #addPlayersToChunkMap()
	 */
	public static void disconnectPlayersFromChunkMap() {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		List<EntityPlayerMP> players=server.getPlayerList().getPlayers();
		//Forge
		WorldServer[] worlds=DimensionManager.getWorlds();
		//Vanilla
		//WorldServer[] worlds=server.worlds;
		for (WorldServer world:worlds) {
			for (EntityPlayerMP player : players) {
				world.getPlayerChunkMap().removePlayer(player);
			}
		}
	}
	/**
	 * The player chunk map keeps track of which chunks need to be sent to the client. <br>
	 * This adds the player to the chunk map so the server knows it can send the information to the client<br>
	 * <br>
	 * Side: Server
	 * @see #disconnectPlayersFromChunkMap()
	 */
	public static void addPlayersToChunkMap() {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		List<EntityPlayerMP> players=server.getPlayerList().getPlayers();
		//Forge
		for (EntityPlayerMP player : players) {
			WorldServer world=DimensionManager.getWorld(player.dimension);
			world.getPlayerChunkMap().addPlayer(player);
			world.getChunkProvider().provideChunk((int)player.posX >> 4, (int)player.posZ >> 4);
		}
	}
	/**
	 * Tells the save handler to save all changes to disk and remove all references to the region files, making them editable on disc<br>
	 * <br>
	 * Side: Server
	 */
	public static void flushSaveHandler() {
		//Forge
		WorldServer[] worlds=DimensionManager.getWorlds();
		//Vanilla
		//MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		//WorldServer[] worlds=server.worlds;
		for(WorldServer world : worlds) {
			world.getSaveHandler().flush();
		}
	}
	/**
	 * Updates the session lock to allow for vanilla saving again<br>
	 * <br>
	 * Side: Server
	 */
	public static void updateSessionLock() {
		//Forge
		WorldServer[] worlds=DimensionManager.getWorlds();
		//Vanilla
		//MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		//WorldServer[] worlds=server.worlds;
		for(WorldServer world : worlds) {
			((AccessorSaveHandler)world.getSaveHandler()).accessSetSessionLock();;
		}
	}
	/**
	 * Makes sure that the player is not removed from the loaded entity list<br>
	 * <br>
	 * Side: Client
	 */
	@SideOnly(Side.CLIENT)
	public static void keepPlayerInLoadedEntityList(EntityPlayer player) {
		((AccessorWorld)Minecraft.getMinecraft().world).getUnloadedEntityList().remove(player);
	}
	
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
				list.transferPlayerToDimension(player, dimensionNow, new ITeleporter() {
					
					@Override
					public boolean isVanilla() {
						return false;
					}
					
					@Override
					public void placeEntity(World world, Entity entity, float yaw) {
						
					}
				});
			}else {
				((AccessorWorld)player.getServerWorld()).getUnloadedEntityList().remove(player);
			}
			
			player.readFromNBT(nbttagcompound);
			
			Minecraft.getMinecraft().player.readFromNBT(nbttagcompound);
			ChunkUtil.keepPlayerInLoadedEntityList(player);
		}
	}
	
	/**
	 * Tries to reattach the player to an entity, if the player was riding it it while savestating.
	 * 
	 * Side: Server
	 * @param nbttagcompound where the ridden entity is saved
	 * @param world that needs to spawn the entity
	 * @param playerIn that needs to ride the entity
	 */
	public static void reattachEntityToPlayer(NBTTagCompound nbttagcompound, World world, Entity playerIn) {
		if (nbttagcompound != null && nbttagcompound.hasKey("RootVehicle", 10))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("RootVehicle");
            Entity entity1 = AnvilChunkLoader.readWorldEntity(nbttagcompound1.getCompoundTag("Entity"), world, true);
            
            
            if(entity1==null) {
            	for (Entity entity : world.loadedEntityList) {
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
                    world.removeEntityDangerously(entity1);

                    for (Entity entity2 : entity1.getRecursivePassengers())
                    {
                        world.removeEntityDangerously(entity2);
                    }
                }
            }
        }
	}
	
}
