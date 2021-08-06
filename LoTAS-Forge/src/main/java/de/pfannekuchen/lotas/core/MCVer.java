package de.pfannekuchen.lotas.core;

import com.mojang.authlib.GameProfile;

import de.pfannekuchen.lotas.mixin.accessors.AccessorEntityLiving;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeVersion;
//#if MC>=10900
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.network.login.client.CPacketLoginStart;
//#else
//$$ import net.minecraft.util.AxisAlignedBB;
//$$ import net.minecraft.network.login.client.C00PacketLoginStart;
//#endif

/**
 * Class that preprocesses a lot of stuff
 * @author Pancake
 * @since v1.2
 * @version v1.2
 */
public class MCVer {
	
	public static int clamp(int num, int min, int max) {
		if (num < min) {
			return min;
		} else {
			return num > max ? max : num;
		}
	}
	
    public static int ceil(float value) {
        int i = (int)value;
        return value > (float)i ? i + 1 : i;
    }
    
    public static float sin(float value) {
        return (float) Math.sin(value);
    }

    public static float cos(float value) {
        return (float) Math.cos(value);
    }

    public static double sqrt(double value)
    {
        return Math.sqrt(value);
    }
    
    public static float sqrt(float value)
    {
        return (float)Math.sqrt((double)value);
    }

	public static float clamp(float num, float min, float max) {
		if (num < min) {
			return min;
		} else {
			return num > max ? max : num;
		}
	}
	
    public static float abs(float a) {
        return (a <= 0.0F) ? 0.0F - a : a;
    }
	
	public static int x(GuiButton button) {
        //#if MC>=11200
        return button.x;
        //#else
        //$$ return button.xPosition;
        //#endif
    }

    public static int y(GuiButton button) {
        //#if MC>=11200
        return button.y;
        //#else
        //$$ return button.yPosition;
        //#endif
    }
	
    public static EntityPlayerSP player(Minecraft mc) {
        //#if MC>=11100
        return mc.player;
        //#else
        //$$ return mc.thePlayer;
        //#endif
    }

    public static WorldServer world(IntegratedServer mc, int dimension) {
        //#if MC>=11200
        return mc.getWorld(dimension);
        //#else
        //$$ return mc.worldServerForDimension(dimension);
        //#endif
    }
    
    public static WorldClient world(Minecraft mc) {
        //#if MC>=11100
        return mc.world;
        //#else
        //$$ return mc.theWorld;
        //#endif
    }
    
    public static FontRenderer getFontRenderer(Minecraft mc) {
        //#if MC>=11200
        return mc.fontRenderer;
        //#else
        //$$ return mc.fontRendererObj;
        //#endif
    }
    
    public static WorldServer[] getWorlds(MinecraftServer server) {
    	//#if MC>=11100
    	return server.worlds;
    	//#else
    //$$ 	return server.worldServers;
    	//#endif
    }

	public static World world(EntityLiving entity) {
        //#if MC>=11100
        return entity.getEntityWorld();
        //#else
        //$$ return entity.worldObj;
        //#endif
	}
    
	public static Item getItem(String itemId) {
		int mainVersion = Integer.parseInt(ForgeVersion.mcVersion.split("\\.")[1]);
		if (mainVersion <= 10) {
			return Item.getByNameOrId(itemId.toLowerCase());
		}
		return Item.getByNameOrId(itemId);
	}
	
	public static Block getBlock(String blockId) {
		int mainVersion = Integer.parseInt(ForgeVersion.mcVersion.split("\\.")[1]);
		if (mainVersion <= 8) {
			return Block.getBlockFromName(blockId.toLowerCase());
		}
		return Block.getBlockFromName(blockId);
	}

	public static AxisAlignedBB aabb(double d, double e, double f, double g, double h, double i) {
		return new AxisAlignedBB(d, e, f, g, h, i);
	}

	public static Packet<?> loginStart(GameProfile gameProfile) {
        //#if MC>=10900
        return (new CPacketLoginStart(gameProfile));
        //#else
        //$$ return (new C00PacketLoginStart(gameProfile));
        //#endif
	}

	public static Packet<?> handshake(String string, int i, EnumConnectionState login, boolean b) {
        //#if MC>=11202
        return new C00Handshake(string, i, login, b);
        //#else
        //$$ return new C00Handshake(316, string, i, login, b);
        //#endif
	}

	public static AxisAlignedBB expandBy64(AxisAlignedBB box) {
		return new AxisAlignedBB(box.minX - 32, box.minY - 32, box.minZ - 32, box.maxX + 32, box.maxY + 32, box.maxZ + 32);
	}
	
	public static void setItemToSlot(EntityLiving entity, int slot, ItemStack stack){
	//#if MC>=10900
		switch (slot) {
		case 0:
			entity.setItemStackToSlot(net.minecraft.inventory.EntityEquipmentSlot.MAINHAND, stack);
			break;
		case 1:
			entity.setItemStackToSlot(net.minecraft.inventory.EntityEquipmentSlot.FEET, stack);
			break;
		case 2:
			entity.setItemStackToSlot(net.minecraft.inventory.EntityEquipmentSlot.CHEST, stack);
			break;
		case 3:
			entity.setItemStackToSlot(net.minecraft.inventory.EntityEquipmentSlot.LEGS, stack);
			break;
		case 4:
			entity.setItemStackToSlot(net.minecraft.inventory.EntityEquipmentSlot.HEAD, stack);

			break;
		}
	//#else
//$$ 		entity.setCurrentItemOrArmor(slot, stack);
	//#endif
	}

	public static void spawnEntity(WorldServer world, EntityLiving entity) {
		//#if MC>=11100
			world.spawnEntity(entity);
		//#else
//$$ 		world.spawnEntityInWorld(entity);
		//#endif
	}

	public static void setArmorDropChances(EntityLiving entity, float[] fs) {
		//#if MC>=10900
		((AccessorEntityLiving) entity).inventoryArmorDropChances(fs);
		//#else
//$$ 		((AccessorEntityLiving) entity).equipmentDropChances(new float[] {1F,1F,1F,1F,1F});
		//#endif
	}

	public static void setHandDropChances(EntityLiving entity, float[] fs) {
		//#if MC>=10900
		((AccessorEntityLiving) entity).inventoryHandsDropChances(fs);
		//#endif
	}
	
}