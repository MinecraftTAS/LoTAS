package de.pfannkuchen.lotas.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * This is the Main LoWidget that shows a list of items on the right.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class MainLoWidget extends LoScreen {

	// Background Width from the right
	private static final double BACKGROUND_WIDTH = 0.215;
	// Border Width
	private static final double BORDER_WIDTH = 0.0045;
	
	// Background Color
	private static final int BACKGROUND_COLOR = 0xff1c1a1e;
	// Border Color
	private static final int BORDER_COLOR = 0xff025a5f;
	// Border Color when focused
	private static final int BORDER_FOCUS_COLOR = 0xff038386;
	
	// Title Position. X-Offset by BACKGROUND_WIDTH
	private static final double TITLE_X = 0.045;
	private static final double TITLE_Y = 0.05;
	// Title Size
	private static final int TITLE_SIZE = 40;
	// Title Color
	private static final int TITLE_COLOR = 0xFFFFFFFF;
	// Title Text
	private static final TextComponent TITLE = new TextComponent("LoTAS Menu");
	
	// Category Position. X-Offset by BACKGROUND_WIDTH
	private static final double CATEGORY_X = 0.02;
	private static final double CATEGORY_Y = 0.202;
	private static final double CATEGORY_GAP_Y = 0.055;
	// Category Size
	private static final int CATEGORY_SIZE = 30;
	// Category Color
	private static final int CATEGORY_COLOR = 0xff025a5f;
	// Category Texts
	private static final TextComponent[] CATEGORIES = new TextComponent[] {
		new TextComponent("Tickrate Changing"),
		new TextComponent("Duping"),
		new TextComponent("Savestating"),
		new TextComponent("Dragon Manipulation"),
		new TextComponent("Drop Manipulation"),
		new TextComponent("Entity AI Manipulation"),
		new TextComponent("Spawn Manipulation"),
		new TextComponent("Misc Manipulation"),
		new TextComponent("Configuration")
	};
	
	// Version Position. X-Offset by BACKGROUND_WIDTH
	private static final double VERSION_X = 0.013;
	private static final double VERSION_Y = 0.923;
	// Version Size
	private static final int VERSION_SIZE = 30;
	// Version Color
	private static final int VERSION_COLOR = 0xff989898;
	// Version Text
	private static final TextComponent VERSION1 = new TextComponent("LoTAS 3.0.0-SNAPSHOT");
	private static final TextComponent VERSION2 = new TextComponent("Development Build");
	
	// Ease-In Animation Progress
	private double animationProgress;
	
	/**
	 * Ease Interpolation
	 * @param t Progress
	 * @param b Offset
	 * @param c Goal
	 * @param d Dividor for Progress
	 * @return Ease-out-quad variable
	 */
	private double ease(double t, double b, double c, double d) {
		return -c *(t/=d)*(t-2) + b;
	}
	
	@Override
	public void render(PoseStack stack, double curX, double curY) {
		this.animationProgress = Math.min(6, this.animationProgress + mc.getDeltaFrameTime()); // Move the animation
		final boolean isMouseOver = curX > 1 - BACKGROUND_WIDTH; // Check whether the mouse is over the widget
		// Render the background and the border
		this.fill(stack, 1 - BORDER_WIDTH - BACKGROUND_WIDTH * ease(this.animationProgress, 0, 1, 6), 0, 1, 1, isMouseOver ? BORDER_FOCUS_COLOR : BORDER_COLOR); // Border
		this.fill(stack, 1 - BACKGROUND_WIDTH * ease(this.animationProgress, 0, 1, 6), 0, 1, 1, BACKGROUND_COLOR); // Background
		// Render Title
		this.draw(stack, TITLE, 1 - BACKGROUND_WIDTH * ease(this.animationProgress, 0, 1, 6) + TITLE_X, TITLE_Y, TITLE_SIZE, TITLE_COLOR, false);
		// Render Categories
		double categoryY = CATEGORY_Y;
		for (int i = 0; i < CATEGORIES.length; i++) {
			this.draw(stack, CATEGORIES[i], 1 - BACKGROUND_WIDTH * ease(this.animationProgress, 0, 1, 6) + CATEGORY_X, categoryY, CATEGORY_SIZE, CATEGORY_COLOR, false);
			categoryY += CATEGORY_GAP_Y;
		}
		// Render Version String
		this.draw(stack, VERSION1, 1 - BACKGROUND_WIDTH * ease(this.animationProgress, 0, 1, 6) + VERSION_X, VERSION_Y, VERSION_SIZE, VERSION_COLOR, false);
		this.draw(stack, VERSION2, 1 - BACKGROUND_WIDTH * ease(this.animationProgress, 0, 1, 6) + VERSION_X, VERSION_Y+0.03, VERSION_SIZE, VERSION_COLOR, false);
	}

	@Override
	public void init() {}
	
}
