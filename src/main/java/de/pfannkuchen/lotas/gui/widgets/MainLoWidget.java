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
	
	// Render Counter
	private float animationProgress;
	private float animationProgressInterpolated;
	
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
	
	/**
	 * Ease Interpolation
	 * @param t Progress
	 * @param b Offset
	 * @param c Goal
	 * @param d Dividor for Progress
	 * @return Ease-out-quad variable
	 */
	private float ease(float t, float b, float c, float d) {
		return -c *(t/=d)*(t-2) + b;
	}
	
	@Override
	public void render(PoseStack stack, double curX, double curY) {
		animationProgressInterpolated = Math.min(3, animationProgressInterpolated + mc.getDeltaFrameTime() / 4);
		animationProgress = ease(animationProgressInterpolated, 0, 6, 3);
		final boolean isMouseOver = curX > (this.screenWidth - this.widgetWidth);
		// Render Background
		GuiComponent.fill(stack, (int) (this.screenWidth - (this.widgetWidth * (animationProgress / 6))), 0, this.screenWidth, this.screenHeight, this.backgroundColor);
		GuiComponent.fill(stack, (int) (this.screenWidth - ((this.widgetWidth - this.borderWidth) * (animationProgress / 6))) - this.borderWidth, 0, (int) (this.screenWidth - ((this.widgetWidth - this.borderWidth) * (animationProgress / 6))), this.screenHeight, isMouseOver ? this.focusedBorderColor : this.borderColor);
		// Render Text
		stack.pushPose();
		stack.scale(2f, 2f, 2f);
		GuiComponent.drawCenteredString(stack, this.mc.font, this.titleText, (int) ((this.screenWidth - ((this.widgetWidth / 2) * (animationProgress / 6))) /2), this.titleHeight/2, this.titleColor);
		stack.popPose();
	}

	@Override
	public void init() {
		
	}
	
}
