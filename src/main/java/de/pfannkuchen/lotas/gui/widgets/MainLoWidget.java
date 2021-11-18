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
	// Height of category text
	private double textHeightRaw;
	private int textHeight;
	// Height of gap inbetween category text
	private double textGapRaw;
	private int textGap;
	// Height of bottom text
	private double bottomHeightRaw;
	private int bottomHeight;
	// Total screen sizes
	private int screenWidth;
	private int screenHeight;
	// Colors
	private int backgroundColor;
	private int borderColor;
	private int focusedBorderColor;
	private int titleColor;
	private int categoryColor;
	private int bottomColor;
	// Text
	private TextComponent titleText;
	private TextComponent[] categoryTexts;
	private TextComponent versionText1;
	private TextComponent versionText2;
	
	// Render Counter
	private float animationProgress;
	private float animationProgressInterpolated;
	
	/**
	 * Initializes the right list with some parameters
	 * @param widgetWidth Width of the Widget in .percentage
	 * @param borderWidth Width of the left border in .percentage
	 * @param titleHeight Height of the title in .percentage
	 * @param textHeight Height of the first Category text
	 * @param textGap Gap between the category texts
	 * @param bottomHeight Height for the bottom Text
	 * @param backgroundColor Color of the background
	 * @param borderColor Color of the border
	 * @param focusedBorderColor Color of the border when focused
	 * @param categoryColor Color of the category text
	 * @param bottomColor Color of the version string
	 * @param titleColor Color of the title text
	 * @param titleText Title Text
	 * @param versionText1 Higher Version Text
	 * @param versionText2 Lower Version Text
	 * @param categoryTexts Clickable texts
	 */
	public MainLoWidget(double widgetWidth, double borderWidth, double titleHeight, double textHeight, double textGap, double bottomHeight, int backgroundColor, int borderColor, int focusedBorderColor, int titleColor, int categoryColor, int bottomColor, String titleText, String versionText1, String versionText2, String... categoryTexts) {
		this.widgetWidthRaw = widgetWidth;
		this.borderWidthRaw = borderWidth;
		this.titleHeightRaw = titleHeight;
		this.textHeightRaw = textHeight;
		this.textGapRaw = textGap;
		this.bottomHeightRaw = bottomHeight;
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.focusedBorderColor = focusedBorderColor;
		this.categoryColor = categoryColor;
		this.bottomColor = bottomColor;
		this.titleColor = titleColor;
		this.titleText = new TextComponent(titleText);
		this.categoryTexts = new TextComponent[categoryTexts.length];
		this.versionText1 = new TextComponent(versionText1);
		this.versionText2 = new TextComponent(versionText2);
		for (int i = 0; i < categoryTexts.length; i++) this.categoryTexts[i] = new TextComponent(categoryTexts[i]);
	}
	
	@Override
	public void update(double width, double height) {
		// Update widget specific values
		this.widgetWidth = (int) (width * this.widgetWidthRaw);
		this.borderWidth = (int) (width * this.borderWidthRaw);
		this.titleHeight = (int) (height * this.titleHeightRaw);
		this.textHeight = (int) (height * this.textHeightRaw);
		this.textGap = (int) (height * this.textGapRaw);
		this.bottomHeight = (int) (height * this.bottomHeightRaw);
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
		animationProgressInterpolated = Math.min(3, animationProgressInterpolated + mc.getDeltaFrameTime() / 2);
		animationProgress = ease(animationProgressInterpolated, 0, 6, 3);
		final boolean isMouseOver = curX > (this.screenWidth - this.widgetWidth);
		// Render Background
		GuiComponent.fill(stack, (int) (this.screenWidth - (this.widgetWidth * (animationProgress / 6))), 0, this.screenWidth, this.screenHeight, this.backgroundColor);
		GuiComponent.fill(stack, (int) (this.screenWidth - ((this.widgetWidth - this.borderWidth) * (animationProgress / 6))) - this.borderWidth, 0, (int) (this.screenWidth - ((this.widgetWidth - this.borderWidth) * (animationProgress / 6))), this.screenHeight, isMouseOver ? this.focusedBorderColor : this.borderColor);
		// Render Text
		stack.pushPose();
		float screenWidthFloat = Integer.valueOf(this.mc.getWindow().getGuiScaledWidth()).floatValue();
		float scaleFactor = screenWidthFloat / 475.0f;
		stack.translate(this.screenWidth - this.widgetWidth / 2 - this.mc.font.width(this.titleText)*scaleFactor/2, this.titleHeight, 0);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		this.mc.font.draw(stack, this.titleText, 0, 0, this.titleColor);
		stack.popPose();
		// Render Categories
		int categoryY = this.textHeight;
		for (int i = 0; i < this.categoryTexts.length; i++) {
			this.mc.font.draw(stack, this.categoryTexts[i], this.screenWidth - (this.widgetWidth * (animationProgress / 6)) + this.borderWidth*6, categoryY, this.categoryColor);
			categoryY += this.textGap;
		}
		// Render Version String
		this.mc.font.draw(stack, this.versionText1, this.screenWidth - (this.widgetWidth * (animationProgress / 6)) + this.borderWidth*3, bottomHeight, this.bottomColor);
		this.mc.font.draw(stack, this.versionText2, this.screenWidth - (this.widgetWidth * (animationProgress / 6)) + this.borderWidth*3, (float) (bottomHeight + this.mc.font.lineHeight*1.3), this.bottomColor);
	}

	@Override
	public void init() {
		
	}
	
}
