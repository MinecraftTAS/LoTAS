package de.pfannekuchen.lotas.core.utils;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Draws a potion in the gui hud that is moving with the camera
 * @author Pancake
 * @since v1.0
 * @version v1.3
 */
public class PotionRenderingUtils {

	
	public static void render(double xPos, double yPos, double scale) {
		float rotation=15;
		xPos+=2.2;
		yPos+=-0.5;
		scale+=0.26;
		MCVer.translated(xPos, yPos, 0);
		MCVer.scaled(scale, scale, scale);
		MCVer.rotated(rotation, 0, 0, 1);
		
		int orangeBright=0xE35720;
		int orange=0xC24218;
		int orangeDark=0x9A3212;
		
		int white=0xFFFFFF;
		int cyan=0x546980;
		
		int y=0;
		renderPixel(8, y, orangeBright);
		renderPixel(9, y, orangeBright);
		
		y=1;
		renderPixel(7, y, orange);
		renderPixel(8, y, orange);
		renderPixel(9, y, orangeBright);
		
		y=2;
		renderPixel(6, y, orange);
		renderPixel(7, y, orange);
		renderPixel(8, y, orange);
		renderPixel(9, y, orange);
		
		y=3;
		renderPixel(5, y, white);
		renderPixel(8, y, orangeDark);
		renderPixel(9, y, orange);
		
		y=4;
		renderPixel(4, y, white);
		renderPixel(7, y, white);
		renderPixel(8, y, orangeDark);
		renderPixel(9, y, orangeDark);
		
		y=5;
		renderPixel(3, y, white);
		renderPixel(7, y, white);
		
		y=6;
		renderPixel(2, y, white);
		renderPixel(3, y, cyan);
		renderPixel(4, y, white);
		renderPixel(5, y, cyan);
		renderPixel(6, y, cyan);
		renderPixel(7, y, cyan);
		renderPixel(8, y, white);
		
		y=7;
		renderPixel(1, y, white);
		renderPixel(2, y, cyan);
		renderPixel(3, y, white);
		renderPixel(4, y, cyan);
		renderPixel(5, y, cyan);
		renderPixel(6, y, cyan);
		renderPixel(7, y, cyan);
		renderPixel(8, y, cyan);
		renderPixel(9, y, white);
		
		y=8;
		renderPixel(1, y, white);
		renderPixel(2, y, cyan);
		renderPixel(3, y, white);
		renderPixel(4, y, cyan);
		renderPixel(5, y, cyan);
		renderPixel(6, y, cyan);
		renderPixel(7, y, cyan);
		renderPixel(8, y, cyan);
		renderPixel(9, y, white);
		
		y=9;
		renderPixel(1, y, white);
		renderPixel(2, y, cyan);
		renderPixel(3, y, cyan);
		renderPixel(4, y, cyan);
		renderPixel(5, y, cyan);
		renderPixel(6, y, cyan);
		renderPixel(7, y, white);
		renderPixel(8, y, cyan);
		renderPixel(9, y, white);
		
		y=10;
		renderPixel(1, y, white);
		renderPixel(2, y, cyan);
		renderPixel(3, y, cyan);
		renderPixel(4, y, cyan);
		renderPixel(5, y, cyan);
		renderPixel(6, y, cyan);
		renderPixel(7, y, white);
		renderPixel(8, y, cyan);
		renderPixel(9, y, white);
		
		y=11;
		renderPixel(1, y, white);
		renderPixel(2, y, white);
		renderPixel(3, y, cyan);
		renderPixel(4, y, cyan);
		renderPixel(5, y, cyan);
		renderPixel(6, y, white);
		renderPixel(7, y, cyan);
		renderPixel(8, y, white);
		renderPixel(9, y, white);
		
		y=12;
		renderPixel(3, y, white);
		renderPixel(4, y, white);
		renderPixel(5, y, white);
		renderPixel(6, y, white);
		renderPixel(7, y, white);
		
		MCVer.rotated(-rotation, 0F, 0F, 1F);
		MCVer.scaled((double)1/scale, (double)1/scale, (double)1/scale);
		MCVer.translated(-xPos, -yPos, 0D);
		
		MCVer.color4f(255, 255, 255, 255);
	}
	
	private static void renderPixel(int x, int y, int color) {
		int alpha=0x80000000;
		MCVer.fill(x, y, x+1,y+1, alpha+color);
	}
}
