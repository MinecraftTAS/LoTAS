package com.minecrafttas.lotas.utils;

public class RenderUtils {
	
	public static void translate(Object stack, double x, double y, double z) { //@PoseStack;
		//#1.20.1
//$$		stack.pose().translate(x, y, z);
		//#1.16.1
//$$		stack.translate(x, y, z);
		//#1.15.2
//$$		com.mojang.blaze3d.systems.RenderSystem.translated(x, y, z);
		//#def
		com.mojang.blaze3d.platform.GlStateManager.translated(x, y, z);
		//#end
	}

	public static void scale(Object stack, double x, double y, double z) { //@PoseStack;
		//#1.20.1
//$$		stack.pose().scale((float)x, (float)y, (float)z);
		//#1.16.1
//$$		stack.scale((float)x, (float)y, (float)z);
		//#1.15.2
//$$		com.mojang.blaze3d.systems.RenderSystem.scaled(x, y, z);
		//#def
		com.mojang.blaze3d.platform.GlStateManager.scaled(x, y, z);
		//#end
	}

	public static void rotate(Object stack, double x, double y, double z) { //@PoseStack;
		//#1.20.1
//$$		stack.pose().mulPose(getQuaternionFromXYZ((float) z, (float) y, (float) x));
		//#1.16.1
//$$		stack.mulPose(getQuaternionFromXYZ((float) z,(float) y,(float) x));
		//#1.15.2
//$$		com.mojang.blaze3d.systems.RenderSystem.rotatef((float) x,(float) y,(float) z, 1f);
		//#def
		com.mojang.blaze3d.platform.GlStateManager.rotated(x, y, z, 1);
		//#end
	}

	public static void color4f(float r, float g, float b, float a) {
		//#1.17.1
//$$		com.mojang.blaze3d.systems.RenderSystem.setShaderColor(r, g, b, a);
		//#1.15.2
//$$		com.mojang.blaze3d.systems.RenderSystem.color4f(r, g, b, a);
		//#def
		com.mojang.blaze3d.platform.GlStateManager.color4f(r, g, b, a);
		//#end
	}
	
	//#1.16.1
//$$	private static Object getQuaternionFromXYZ(float x, float y, float z){	//@Quaternion;
//$$		
		//##1.19.4
//$$    	org.joml.Quaternionf quaternion = new org.joml.Quaternionf(0.0f, 0.0f, 0.0f, 1.0f);
//$$        quaternion.mul(new org.joml.Quaternionf(0.0f, (float)Math.sin(x / 2.0f), 0.0f, (float)Math.cos(x / 2.0f)));
//$$        quaternion.mul(new org.joml.Quaternionf((float)Math.sin(y / 2.0f), 0.0f, 0.0f, (float)Math.cos(y / 2.0f)));
//$$        quaternion.mul(new org.joml.Quaternionf(0.0f, 0.0f, (float)Math.sin(z / 2.0f), (float)Math.cos(z / 2.0f)));
//$$        return quaternion;
        //##def
//$$		com.mojang.math.Quaternion quaternion = new com.mojang.math.Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
//$$        quaternion.mul(new com.mojang.math.Quaternion(0.0f, (float)Math.sin(y / 2.0f), 0.0f, (float)Math.cos(y / 2.0f)));
//$$        quaternion.mul(new com.mojang.math.Quaternion((float)Math.sin(x / 2.0f), 0.0f, 0.0f, (float)Math.cos(x / 2.0f)));
//$$        quaternion.mul(new com.mojang.math.Quaternion(0.0f, 0.0f, (float)Math.sin(z / 2.0f), (float)Math.cos(z / 2.0f)));
//$$        return quaternion;
        //##end
//$$	}
	//#def
	//#end
}
