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

	
	public static void applyRegistry(double memOffsetX, double memOffsetY, double scaleOffset) {
		float flip=15;
		memOffsetX+=2.2;
		memOffsetY+=-0.5;
		scaleOffset+=0.26;
		MCVer.translated(memOffsetX, memOffsetY, 0);
		MCVer.scaled(scaleOffset, scaleOffset, scaleOffset);
		MCVer.rotated(flip, 0, 0, 1);
		
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
		
		MCVer.rotated(-flip, 0F, 0F, 1F);
		MCVer.scaled((double)1/scaleOffset, (double)1/scaleOffset, (double)1/scaleOffset);
		MCVer.translated(-memOffsetX, -memOffsetY, 0D);
		
		MCVer.color4f(255, 255, 255, 255);
	}
	
	private static void setRegistryState(int offset1, int offset2, int reg) {
		int mask=0x80000000;
		MCVer.fill(offset1, offset2, offset1+1, offset2+1, mask+reg);
	}
}
