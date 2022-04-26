package de.pfannkuchen.lotas.gui.widgets;

import java.util.function.BiFunction;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * This is the main lowidget that shows a list of items on the right.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class MainLoWidget extends LoScreen {

	// Background width from the right
	private static final double BACKGROUND_WIDTH = 0.215;
	// Border width
	private static final double BORDER_WIDTH = 0.0045;

	// Background color
	private static final int BACKGROUND_COLOR = 0xff161618;
	// Background color when focused
	private static final int BACKGROUND_FOCUS_COLOR = 0xff1b1c21;
	// Border color
	private static final int BORDER_COLOR = 0xff108950;
	// Border color when focused
	private static final int BORDER_FOCUS_COLOR = 0xff19b36a;

	// Title position. x-offset by BACKGROUND_WIDTH
	private static final double TITLE_X = 0.045;
	private static final double TITLE_Y = 0.05;
	// Title size
	private static final int TITLE_SIZE = 40;
	// Title color
	private static final int TITLE_COLOR = 0xFFFFFFFF;
	// Title text
	private static final TextComponent TITLE = new TextComponent("LoTAS Menu");

	// Category position. x-offset by BACKGROUND_WIDTH
	private static final double CATEGORY_X = 0.02;
	private static final double CATEGORY_Y = 0.202;
	private static final double CATEGORY_GAP_Y = 0.055;
	// Category size
	private static final int CATEGORY_SIZE = 30;
	// Category color
	private static final int CATEGORY_COLOR = 0xff149b5b;
	// Category color when focused
	private static final int CATEGORY_FOCUS_COLOR = 0xff22e187;
	// Category texts
	private static TextComponent[] CATEGORIES;

	// Version position. x-offset by BACKGROUND_WIDTH
	private static final double VERSION_X = 0.013;
	private static final double VERSION_Y = 0.923;
	// Version size
	private static final int VERSION_SIZE = 30;
	// Version color
	private static final int VERSION_COLOR = 0xff8f8f8f;
	// Version text
	private static final TextComponent VERSION1 = new TextComponent("LoTAS 3.0.0-SNAPSHOT");
	private static final TextComponent VERSION2 = new TextComponent("Development Build");

	// Ease-in animation progress
	private double animationProgress;
	// On click on text component
	private BiFunction<TextComponent, Integer, TextComponent> onClick;

	/**
	 * Initializes the lowidget with callbacks
	 * @param onClick Onclick for manipulating the text
	 */
	public MainLoWidget(BiFunction<TextComponent, Integer, TextComponent> onClick) {
		this.onClick = onClick;
		// Update messages to fit with already opened windows
		MainLoWidget.CATEGORIES = new TextComponent[] {
				LoTAS.configmanager.getBoolean("tickratechangerwidget", "active") ? new TextComponent("\u00A7\u00A7\u00A7lTickrate Changing") : new TextComponent("Tickrate Changing"),
				LoTAS.configmanager.getBoolean("dupemodwidget", "active") ? new TextComponent("\u00A7\u00A7\u00A7lDuping") : new TextComponent("Duping"),
				LoTAS.configmanager.getBoolean("savestatewidget", "active") ? new TextComponent("\u00A7\u00A7\u00A7lSavestating") : new TextComponent("Savestating"),
				LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "active") ? new TextComponent("\u00A7\u00A7\u00A7lDragon Manipulation") : new TextComponent("Dragon Manipulation"),
					new TextComponent("Drop Manipulation"),
					new TextComponent("AI Manipulation"),
					new TextComponent("Spawn Manipulation"),
					new TextComponent("Misc Manipulation"),
					new TextComponent("Configuration")
		};
	}

	@Override
	protected void click(double curX, double curY, int button) {
		double categoryY = MainLoWidget.CATEGORY_Y;
		double categoryX = 1 - MainLoWidget.BACKGROUND_WIDTH + MainLoWidget.CATEGORY_X;
		for (int i = 0; i < MainLoWidget.CATEGORIES.length; i++) {
			if (curX > categoryX && curY + MainLoWidget.CATEGORY_GAP_Y/4 > categoryY && curY + MainLoWidget.CATEGORY_GAP_Y/4 < categoryY + MainLoWidget.CATEGORY_GAP_Y) {
				MainLoWidget.CATEGORIES[i] = this.onClick.apply(MainLoWidget.CATEGORIES[i], i);
			}
			categoryY += MainLoWidget.CATEGORY_GAP_Y;
		}
		super.click(curX, curY, button);
	}

	@Override
	public void render(PoseStack stack, double curX, double curY) {
		this.animationProgress = Math.min(6, this.animationProgress + ClientLoTAS.internaltimer.tickDelta); // Move the animation
		final boolean isMouseOver = curX > 1 - MainLoWidget.BACKGROUND_WIDTH; // Check whether the mouse is over the widget
		// Render the background and the border
		this.fill(stack, 1 - MainLoWidget.BORDER_WIDTH - MainLoWidget.BACKGROUND_WIDTH * this.ease(this.animationProgress, 0, 1, 6), 0, 1, 1, isMouseOver ? MainLoWidget.BORDER_FOCUS_COLOR : MainLoWidget.BORDER_COLOR); // Border
		this.fill(stack, 1 - MainLoWidget.BACKGROUND_WIDTH * this.ease(this.animationProgress, 0, 1, 6), 0, 1, 1, MainLoWidget.BACKGROUND_COLOR); // Background
		// Render Title
		this.draw(stack, MainLoWidget.TITLE, 1 - MainLoWidget.BACKGROUND_WIDTH * this.ease(this.animationProgress, 0, 1, 6) + MainLoWidget.TITLE_X, MainLoWidget.TITLE_Y, MainLoWidget.TITLE_SIZE, MainLoWidget.TITLE_COLOR, false);
		// Render Categories
		double categoryY = MainLoWidget.CATEGORY_Y;
		double categoryX = 1 - MainLoWidget.BACKGROUND_WIDTH * this.ease(this.animationProgress, 0, 1, 6) + MainLoWidget.CATEGORY_X;
		for (TextComponent element : MainLoWidget.CATEGORIES) {
			boolean shadow = false;
			if (curX > categoryX && curY + MainLoWidget.CATEGORY_GAP_Y/4 > categoryY && curY + MainLoWidget.CATEGORY_GAP_Y/4 < categoryY + MainLoWidget.CATEGORY_GAP_Y) {
				this.fill(stack, categoryX - MainLoWidget.CATEGORY_GAP_Y/8, categoryY - MainLoWidget.CATEGORY_GAP_Y/4, 1 - MainLoWidget.CATEGORY_GAP_Y/8, categoryY + MainLoWidget.CATEGORY_GAP_Y/4 + 0.025, MainLoWidget.BACKGROUND_FOCUS_COLOR);
				shadow = true;
			}
			this.draw(stack, element, categoryX, categoryY, MainLoWidget.CATEGORY_SIZE, element.getString().startsWith("\u00A7\u00A7") ? MainLoWidget.CATEGORY_FOCUS_COLOR : MainLoWidget.CATEGORY_COLOR, shadow);
			categoryY += MainLoWidget.CATEGORY_GAP_Y;
		}
		// Render Version String
		this.draw(stack, MainLoWidget.VERSION1, 1 - MainLoWidget.BACKGROUND_WIDTH * this.ease(this.animationProgress, 0, 1, 6) + MainLoWidget.VERSION_X, MainLoWidget.VERSION_Y, MainLoWidget.VERSION_SIZE, MainLoWidget.VERSION_COLOR, false);
		this.draw(stack, MainLoWidget.VERSION2, 1 - MainLoWidget.BACKGROUND_WIDTH * this.ease(this.animationProgress, 0, 1, 6) + MainLoWidget.VERSION_X, MainLoWidget.VERSION_Y+0.03, MainLoWidget.VERSION_SIZE, MainLoWidget.VERSION_COLOR, false);
		super.render(stack, curX, curY);
	}

}
