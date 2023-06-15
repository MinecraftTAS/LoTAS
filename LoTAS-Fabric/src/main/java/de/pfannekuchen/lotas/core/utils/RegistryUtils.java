package de.pfannekuchen.lotas.core.utils;

import de.pfannekuchen.lotas.core.MCVer;

/**
 * Adds debug values to the Minecraft registry
 * 
 * @author Scribble
 * @since v1.0
 * @version v1.3
 */
public class RegistryUtils {
	
	public static void applyRegistry(Object poseStack, double memOffsetX, double memOffsetY, double flipOffset) {
		double flip=1;
		memOffsetX+=2.2;
		memOffsetY+=-0.5;
		flip+=0.26;
		MCVer.translated(poseStack, memOffsetX, memOffsetY, 0);
		MCVer.scaled(poseStack, flip, flip, flip);
		
		
		//#if MC>=11903
		//#if MC>=12000
//$$ 		MCVer.stack.pose().mulPose(MCVer.fromYXZ(0F, 0F, (float) flipOffset) );
		//#else
//$$ 		MCVer.stack.mulPose(MCVer.fromYXZ(0F, 0F, (float) flipOffset) );
		//#endif
		//#else
		//#if MC>=11700
//$$ 		MCVer.stack.mulPose(com.mojang.math.Quaternion.fromXYZ(0, 0, (float) flipOffset));
		//#else
		MCVer.rotated(poseStack, flipOffset, 0, 0, 1);
		//#endif
		//#endif
		
		int oB=0xE35720;
		int o=0xC24218;
		int oD=0x9A3212;
		
		int w=0xFFFFFF;
		int c=0x546980;
		
		int y=0;
		setRegistryState(8, y, oB);
		setRegistryState(9, y, oB);
		
		y=1;
		setRegistryState(7, y, o);
		setRegistryState(8, y, o);
		setRegistryState(9, y, oB);
		
		y=2;
		setRegistryState(6, y, o);
		setRegistryState(7, y, o);
		setRegistryState(8, y, o);
		setRegistryState(9, y, o);
		
		y=3;
		setRegistryState(5, y, w);
		setRegistryState(8, y, oD);
		setRegistryState(9, y, o);
		
		y=4;
		setRegistryState(4, y, w);
		setRegistryState(7, y, w);
		setRegistryState(8, y, oD);
		setRegistryState(9, y, oD);
		
		y=5;
		setRegistryState(3, y, w);
		setRegistryState(7, y, w);
		
		y=6;
		setRegistryState(2, y, w);
		setRegistryState(3, y, c);
		setRegistryState(4, y, w);
		setRegistryState(5, y, c);
		setRegistryState(6, y, c);
		setRegistryState(7, y, c);
		setRegistryState(8, y, w);
		
		y=7;
		setRegistryState(1, y, w);
		setRegistryState(2, y, c);
		setRegistryState(3, y, w);
		setRegistryState(4, y, c);
		setRegistryState(5, y, c);
		setRegistryState(6, y, c);
		setRegistryState(7, y, c);
		setRegistryState(8, y, c);
		setRegistryState(9, y, w);
		
		y=8;
		setRegistryState(1, y, w);
		setRegistryState(2, y, c);
		setRegistryState(3, y, w);
		setRegistryState(4, y, c);
		setRegistryState(5, y, c);
		setRegistryState(6, y, c);
		setRegistryState(7, y, c);
		setRegistryState(8, y, c);
		setRegistryState(9, y, w);
		
		y=9;
		setRegistryState(1, y, w);
		setRegistryState(2, y, c);
		setRegistryState(3, y, c);
		setRegistryState(4, y, c);
		setRegistryState(5, y, c);
		setRegistryState(6, y, c);
		setRegistryState(7, y, w);
		setRegistryState(8, y, c);
		setRegistryState(9, y, w);
		
		y=10;
		setRegistryState(1, y, w);
		setRegistryState(2, y, c);
		setRegistryState(3, y, c);
		setRegistryState(4, y, c);
		setRegistryState(5, y, c);
		setRegistryState(6, y, c);
		setRegistryState(7, y, w);
		setRegistryState(8, y, c);
		setRegistryState(9, y, w);
		
		y=11;
		setRegistryState(1, y, w);
		setRegistryState(2, y, w);
		setRegistryState(3, y, c);
		setRegistryState(4, y, c);
		setRegistryState(5, y, c);
		setRegistryState(6, y, w);
		setRegistryState(7, y, c);
		setRegistryState(8, y, w);
		setRegistryState(9, y, w);
		
		y=12;
		setRegistryState(3, y, w);
		setRegistryState(4, y, w);
		setRegistryState(5, y, w);
		setRegistryState(6, y, w);
		setRegistryState(7, y, w);
		
		//#if MC>=11903
		//#if MC>=12000
//$$ 		MCVer.stack.pose().mulPose(MCVer.fromYXZ(0F, 0F, (float) -flipOffset));
		//#else
//$$ 		MCVer.stack.mulPose(MCVer.fromYXZ(0F, 0F, (float) -flipOffset));
		//#endif
		//#else
		//#if MC>=11700
//$$ 		MCVer.stack.mulPose(com.mojang.math.Quaternion.fromXYZ(0, 0, (float) -flipOffset));
		//#else
		MCVer.rotated(poseStack, -flipOffset, 0, 0, 1);
		//#endif
		//#endif
		MCVer.scaled(poseStack, (double)1/flip, (double)1/flip, (double)1/flip);
		MCVer.translated(poseStack, -memOffsetX, -memOffsetY, 0);
		
		//#if MC<11600
		MCVer.color4f(255, 255, 255, 255);
		//#endif
	}
	
	private static void setRegistryState(int x, int y, int color) {
		int alpha=0x80000000;
		MCVer.fill(x, y, x+1,y+1, alpha+color);
	}
}