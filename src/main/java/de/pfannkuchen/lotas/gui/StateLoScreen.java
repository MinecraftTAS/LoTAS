package de.pfannkuchen.lotas.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Locks the players in the savestate loscreen
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class StateLoScreen extends LoScreen {

	// Background Color
	private static final int BACKGROUND_COLOR = 0xff161618;
	// Whether the screen is able to be removed
	private static boolean allowUnlocking = false;
	// Hover animation
	private double animationProgress;
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		// Animate
		if (StateLoScreen.allowUnlocking) {
			this.animationProgress = Math.min(6, this.animationProgress + 0.015);
		} else {
			this.animationProgress = Math.min(3, this.animationProgress + 0.015);
		}
		// Close once animation is done
		if (this.animationProgress >= 6) {
			ClientLoTAS.loscreenmanager.setScreen(null);
			StateLoScreen.allowUnlocking = false;
			return;
		}
		// Render 16*9 squares with the animation affecting them
		double endX = (this.ease(this.animationProgress, 0, 1, 3)*1.1)/16;
		for (int i = 0; i < 16; i++) {
			this.fill(stack, (1/16.0)*i, 0, (1/16.0)*i+endX, 1, BACKGROUND_COLOR);
		}
		super.render(stack, curX, curY);
	}
	
	/**
	 * Allows this screen to be closed after the animation.
	 */
	public static void allowUnlocking() {
		StateLoScreen.allowUnlocking = true;
	}
	
}
