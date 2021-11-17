package de.pfannkuchen.lotas.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TextComponent;

/**
 * This is the Main LoWidget that shows a list of items on the right.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class MainLoWidget extends LoScreen {

	// Width of the right panel
	private double widgetWidthRaw;
	private int widgetWidth;
	// Width of the left border
	private double borderWidthRaw;
	private int borderWidth;
	// Height of title text
	private double titleHeightRaw;
	private int titleHeight;
	// Total screen sizes
	private int screenWidth;
	private int screenHeight;
	// Colors
	private int backgroundColor;
	private int borderColor;
	private int focusedBorderColor;
	private int titleColor;
	// Text
	private TextComponent titleText;
	
	/**
	 * Initializes the right list with some parameters
	 * @param widgetWidth Width of the Widget in .percentage
	 * @param borderWidth Width of the left border in .percentage
	 * @param titleHeight Height of the title in .percentage
	 * @param backgroundColor Color of the background
	 * @param borderColor Color of the border
	 * @param focusedBorderColor Color of the border when focused
	 * @param titleColor Color of the title text
	 * @param titleText Title Text
	 */
	public MainLoWidget(double widgetWidth, double borderWidth, double titleHeight, int backgroundColor, int borderColor, int focusedBorderColor, int titleColor, String titleText) {
		this.widgetWidthRaw = widgetWidth;
		this.borderWidthRaw = borderWidth;
		this.titleHeightRaw = titleHeight;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.focusedBorderColor = focusedBorderColor;
		this.titleColor = titleColor;
		this.titleText = new TextComponent(titleText);
	}
	
	@Override
	public void update(double width, double height) {
		// Update widget specific values
		this.widgetWidth = (int) (width * this.widgetWidthRaw);
		this.borderWidth = (int) (width * this.borderWidthRaw);
		this.titleHeight = (int) (height * this.titleHeightRaw);
		// Update screen sizes
		this.screenWidth = (int) width;
		this.screenHeight = (int) height;
	}
	
	@Override
	public void render(PoseStack stack, double curX, double curY) {
		final boolean isMouseOver = curX > (this.screenWidth - this.widgetWidth);
		// Render Background
		GuiComponent.fill(stack, this.screenWidth - this.widgetWidth, 0, this.screenWidth, this.screenHeight, this.backgroundColor);
		GuiComponent.fill(stack, this.screenWidth - this.widgetWidth - this.borderWidth, 0, this.screenWidth - this.widgetWidth, this.screenHeight, isMouseOver ? this.focusedBorderColor : this.borderColor);
		// Render Text
		GuiComponent.drawCenteredString(stack, this.mc.font, this.titleText, this.screenWidth - (this.widgetWidth / 2), this.titleHeight, this.titleColor);
	}

	@Override
	public void init() {
		
	}
	
}
