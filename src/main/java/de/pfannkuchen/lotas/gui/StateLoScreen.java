package de.pfannkuchen.lotas.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * Locks the players in the savestate loscreen
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class StateLoScreen extends LoScreen {
	// Background Color
	private static final int BACKGROUND_COLOR = 0xff161618;
	// Border Color
	private static final int BORDER_COLOR = 0xff108950;
	// Title Color
	private static final int TITLE_COLOR = 0xFFFFFFFF;
	// Whether the screen is able to be removed
	private static boolean allowUnlocking = false;
	// Animation
	private double animationProgress;
	private double animationProgress2;
	
	@Override
	protected void init() {
		StateLoScreen.allowUnlocking = false;
		super.init();
	}
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		// Animate
		if (StateLoScreen.allowUnlocking && this.animationProgress2 >= 12) {
			this.animationProgress = Math.min(26, this.animationProgress + ClientLoTAS.internaltimer.tickDelta);
		} else {
			this.animationProgress = Math.min(12, this.animationProgress + ClientLoTAS.internaltimer.tickDelta);
		}
		// Animate the second animation
		if (this.animationProgress >= 12 && !StateLoScreen.allowUnlocking) {
			this.animationProgress2 = Math.min(6, this.animationProgress2 + ClientLoTAS.internaltimer.tickDelta);
		}
		if (this.animationProgress >= 12 && StateLoScreen.allowUnlocking) {
			this.animationProgress2 = Math.min(12, this.animationProgress2 + ClientLoTAS.internaltimer.tickDelta);
		}
		// Close once animation is done
		if (this.animationProgress >= 26) {
			ClientLoTAS.loscreenmanager.setScreen(new MainLoScreen());
			StateLoScreen.allowUnlocking = false;
			return;
		}
		double ease = this.ease(this.animationProgress, 0, 1, 12);
		double ease2 = this.ease(this.animationProgress2, 0, 1, 12);
		this.fill(stack, 1 - 0.0045 - 0.215 - (0.785*ease), 0, 1, 1, BORDER_COLOR); // Border
		this.fill(stack, 1 - 0.215 - (0.785*ease), 0, 1, 1, BACKGROUND_COLOR); // Background
		this.draw(stack, new TextComponent("Performing State Action..."), 1 + .05 - (0.785*ease), 0.25, 70, TITLE_COLOR, false); // Title
		// Render Progress Bar
		this.fill(stack, 0.19, 0.72*(1*ease), 0.81, 0.76*(1*ease), BORDER_COLOR); 
		this.fill(stack, 0.19215, 0.723*(1*ease), 0.8079, 0.757*(1*ease), BACKGROUND_COLOR); 
		this.fill(stack, 0.193, 0.726*(1*ease), 0.193+(0.614*ease2), 0.754*(1*ease), BORDER_COLOR); 

		super.render(stack, curX, curY);
	}
	
	/**
	 * Allows this screen to be closed after the animation.
	 */
	public static void allowUnlocking() {
		StateLoScreen.allowUnlocking = true;
	}
	
}
