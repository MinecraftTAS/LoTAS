package de.pfannkuchen.lotas.gui.widgets;

import java.util.function.BiFunction;

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
	// Background Color when focused
	private static final int BACKGROUND_FOCUS_COLOR = 0xff100e11;
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
	// Category Color when focused
	private static final int CATEGORY_FOCUS_COLOR = 0xff27b6bc;
	// Category Texts
	private static final TextComponent[] CATEGORIES = new TextComponent[] {
		new TextComponent("Tickrate Changing"),
		new TextComponent("Duping"),
		new TextComponent("Savestating"),
		new TextComponent("Dragon Manipulation"),
		new TextComponent("Drop Manipulation"),
		new TextComponent("AI Manipulation"),
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
	// On Click on Text Component
	private BiFunction<TextComponent, Integer, TextComponent> onClick;
	
	/**
	 * Initializes the LoWidget with callbacks
	 * @param onClick Onclick for manipulating the Text
	 */
	public MainLoWidget(BiFunction<TextComponent, Integer, TextComponent> onClick) {
		this.onClick = onClick;
	}
	
	@Override
	protected void click(double curX, double curY, int button) {
		double categoryY = CATEGORY_Y;
		double categoryX = 1 - BACKGROUND_WIDTH + CATEGORY_X;
		for (int i = 0; i < CATEGORIES.length; i++) {
			if (curX > categoryX && curY + CATEGORY_GAP_Y/4 > categoryY && curY + CATEGORY_GAP_Y/4 < categoryY + CATEGORY_GAP_Y) {
				CATEGORIES[i] = onClick.apply(CATEGORIES[i], i);
			}
			categoryY += CATEGORY_GAP_Y;
		}
		super.click(curX, curY, button);
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
		double categoryX = 1 - BACKGROUND_WIDTH * ease(this.animationProgress, 0, 1, 6) + CATEGORY_X;
		for (int i = 0; i < CATEGORIES.length; i++) {
			boolean shadow = false;
			if (curX > categoryX && curY + CATEGORY_GAP_Y/4 > categoryY && curY + CATEGORY_GAP_Y/4 < categoryY + CATEGORY_GAP_Y) {
				this.fill(stack, categoryX - CATEGORY_GAP_Y/8, categoryY - CATEGORY_GAP_Y/4, 1 - CATEGORY_GAP_Y/8, categoryY + CATEGORY_GAP_Y/4 + 0.025, BACKGROUND_FOCUS_COLOR);
				shadow = true;
			}
			this.draw(stack, CATEGORIES[i], categoryX, categoryY, CATEGORY_SIZE, CATEGORIES[i].getString().startsWith("\u00A7\u00A7") ? CATEGORY_FOCUS_COLOR : CATEGORY_COLOR, shadow);
			categoryY += CATEGORY_GAP_Y;
		}
		// Render Version String
		this.draw(stack, VERSION1, 1 - BACKGROUND_WIDTH * ease(this.animationProgress, 0, 1, 6) + VERSION_X, VERSION_Y, VERSION_SIZE, VERSION_COLOR, false);
		this.draw(stack, VERSION2, 1 - BACKGROUND_WIDTH * ease(this.animationProgress, 0, 1, 6) + VERSION_X, VERSION_Y+0.03, VERSION_SIZE, VERSION_COLOR, false);
		super.render(stack, curX, curY);
	}

	@Override
	public void init() {}
	
}
