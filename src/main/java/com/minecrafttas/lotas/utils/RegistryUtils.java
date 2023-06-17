package com.minecrafttas.lotas.utils;

/**
 * Adds debug values to the Minecraft registry
 * 
 * @author Scribble
 * @since v1.0
 * @version v1.3
 */
public class RegistryUtils {

	//#1.16.1
	//#def
	@SuppressWarnings("unused")
	//#end
	private static Object memberStack; //@PoseStack;
	
	public static void applyRegistry(Object memStack, double memOffsetX, double memOffsetY, double flipOffset) { //@PoseStack;
		memberStack = memStack;
		double flip=1;
		flipOffset-=0.05;
		memOffsetX+=3;
		memOffsetY+=-0.5;
		flip+=0.26;
		RegistryShifter.shiftBy(memStack, memOffsetX, memOffsetY, 0);
		RegistryShifter.explode(memStack, flip, flip, flip);
		RegistryShifter.flip(memStack, flipOffset, 0, 0);
		
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
		
		RegistryShifter.flip(memStack, -flipOffset, 0, 0);
		RegistryShifter.explode(memStack, (double)1/flip, (double)1/flip, (double)1/flip);
		RegistryShifter.shiftBy(memStack, -memOffsetX, -memOffsetY, 0);
		
		//# 1.16.1
		//#def
		RegistryShifter.tagColor(255, 255, 255, 255);
		//#end
		memberStack = null;
	}
	
	private static void setRegistryState(int x, int y, int color) {
		int alpha=0x80000000;
		//# 1.20.1
//$$		memberStack.fill(x, y, x+1,y+1, alpha+color);
		//# 1.16.1
//$$		net.minecraft.client.gui.Gui.fill(memberStack, x, y, x+1,y+1, alpha+color);
		//# def
		net.minecraft.client.gui.Gui.fill(x, y, x+1,y+1, alpha+color);
		//# end
	}
	
	private static class RegistryShifter {
		private static void shiftBy(Object stack, double x, double y, double z) { //@PoseStack;
			RenderUtils.translate(stack, x, y, z);
		}

		public static void explode(Object memStack, double flip, double flip2, double flip3) { //@PoseStack;
			RenderUtils.scale(memStack, flip, flip2, flip3);
		}
		
		private static void flip(Object memStack, double flipDirection, double flipRotation, double flipVelocity) {	//@PoseStack;
			RenderUtils.rotate(memStack, flipDirection, flipRotation, flipVelocity);
		}
		
		//# 1.16.1
		//#def
		private static void tagColor(float r, float g, float b, float a) {
			RenderUtils.color4f(r, g, b, a);
		}
		//#end
	}
}
