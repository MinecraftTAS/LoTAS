package de.pfannkuchen.lotas.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.loscreen.LoScreen;

/**
 * Locks the players in the savestate loscreen
 * @author Pancake
 */
public class StateLoScreen extends LoScreen {

	// Background Color
	private static final int BACKGROUND_COLOR = 0xff161618;
	// Whether the screen is able to be removed
	private static boolean allowUnlocking = false;
	// Hover animation
	private double animationProgress;
	
	@Override
	protected void init() {
		StateLoScreen.allowUnlocking = false; // Lock the screen
		super.init();
	}
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		this.animationProgress = Math.min(6, this.animationProgress + mc.getDeltaFrameTime()/LoTAS.tickratechanger.getGamespeed()); // Move the animation
		// Animate and close once animatino is done
		if (StateLoScreen.allowUnlocking) {
			animationProgress -= mc.getDeltaFrameTime()/LoTAS.tickratechanger.getGamespeed()*2;
			if (animationProgress < 0) {
				ClientLoTAS.loscreenmanager.setScreen(null);
				return;
			}
		}
		// Render 16*9 squares with the animation affecting them
		double widthOfBox = (1.0 / 16.0) * this.ease(this.animationProgress, 0, 1, 6);
		double heightOfBox = (1.0 / 9.0) * this.ease(this.animationProgress, 0, 1, 6);
		double widthPerBox = (1.0 / 16.0);
		double heightPerBox = (1.0 / 9.0);
		for (int x = 0; x < 16; x++) 
			for (int y = 0; y < 9; y++) 
				this.fill(stack, widthPerBox*x, heightPerBox*y, widthPerBox*x+widthOfBox, heightPerBox*y+heightOfBox, BACKGROUND_COLOR);
		super.render(stack, curX, curY);
	}
	
	/**
	 * Allows this screen to be closed after the animation.
	 */
	public static void allowUnlocking() {
		StateLoScreen.allowUnlocking = true;
	}
	
}
