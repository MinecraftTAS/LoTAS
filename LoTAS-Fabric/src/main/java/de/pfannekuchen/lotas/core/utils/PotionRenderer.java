package de.pfannekuchen.lotas.core.utils;

import de.pfannekuchen.lotas.core.MCVer;

/**
 * Draws a potion in the gui hud pixel by pixel
 * 
 * @author ScribbleLP
 * @since v1.0
 * @version v1.3
 */
public class PotionRenderer {
	
	public static void render(Object poseStack, double xPos, double yPos, double rotation) {
		double scale=1;
		xPos+=2.2;
		yPos+=-0.5;
		scale+=0.26;
		MCVer.translated(poseStack, xPos, yPos, 0);
		MCVer.scaled(poseStack, scale, scale, scale);
		
		//#if MC>=11700
//$$ 		MCVer.stack.mulPose(com.mojang.math.Quaternion.fromXYZ(0, 0, (float) rotation));
		//#else
		MCVer.rotated(poseStack, rotation, 0, 0, 1);
		//#endif
		
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
		
		//#if MC>=11700
//$$ 		MCVer.stack.mulPose(com.mojang.math.Quaternion.fromXYZ(0, 0, (float) -rotation));
		//#else
		MCVer.rotated(poseStack, -rotation, 0, 0, 1);
		//#endif
		MCVer.scaled(poseStack, (double)1/scale, (double)1/scale, (double)1/scale);
		MCVer.translated(poseStack, -xPos, -yPos, 0);
		
		//#if MC<11600
		MCVer.color4f(255, 255, 255, 255);
		//#endif
	}
	
	private static void renderPixel(int x, int y, int color) {
		int alpha=0x80000000;
		MCVer.fill(x, y, x+1,y+1, alpha+color);
	}
}