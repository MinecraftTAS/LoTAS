package de.pfannekuchen.lotas.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLiving;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class MCVer {
	
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
    	//$$ return server.worldServers;
    	//#endif
    }

	public static World world(EntityLiving entity) {
        //#if MC>=11100
        return entity.getEntityWorld();
        //#else
        //$$ return entity.worldObj;
        //#endif
	}
    
}