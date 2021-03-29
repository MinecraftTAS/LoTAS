package de.pfannekuchen.lotas.renderer;

/**
 * Draws a potion in the gui hud that is moving with the camera
 * 
 * @see MixinPotionEntityRenderer
 * @author Pancake 
 *
 */
public class PotionRenderer {

	public static float lerpX = 0f;
	public static float lerpY = 0f;
	
	public static float lerp(float point1, float point2, float alpha){ return point1 + alpha * (point2 - point1);}
}
