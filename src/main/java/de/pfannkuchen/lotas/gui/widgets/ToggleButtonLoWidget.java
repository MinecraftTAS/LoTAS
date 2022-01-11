package de.pfannkuchen.lotas.gui.widgets;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * Button Widget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class ToggleButtonLoWidget extends LoScreen {

	// Background Color
	private static final int COLOR = 0xff1b1c21;
	// Background Color but focused
	private static final int FOCUS_COLOR = 0xff108950;
	// Border Color but focused
	private static final int MOUSEOVER_FOCUS_COLOR = 0xff19b36a;

	// Position of the Button
	double x;
	double y;
	// Length of the Button
	double length;

	// Whether the Button should be shown
	boolean active;
	// Move Handler
	Consumer<Boolean> onClick;
	// Value of the button
	TextComponent value;
	// Hover animation
	float animationProgress;
	// Is the button pressed
	boolean down;

	/**
	 * Initializes a new Button
	 * @param active Whether the Button is active by default
	 * @param x Position
	 * @param y Position
	 * @param length Length of the button (in screen width percentage)
	 * @param Callback after button is clicked
	 */
	public ToggleButtonLoWidget(boolean active, double x, double y, double length, Consumer<Boolean> onClick, TextComponent value) {
		this.active = active;
		this.x = x;
		this.y = y;
		this.length = length;
		this.onClick = onClick;
		this.value = value;
	}

	@Override
	protected void click(double curX, double curY, int button) {
		if (!this.active) return;
		// Trigger click event when mouse is over the button
		if (curX > this.x && curY > this.y && curX < this.x+this.length && curY < this.y+.05) {
			this.down = !this.down;
			this.onClick.accept(this.down);
		}
		super.click(curX, curY, button);
	}

	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		if (!this.active) return;
		boolean isMouseOver = curX > this.x && curY > this.y && curX < this.x+this.length && curY < this.y+.05;
		double size	= this.mc.font.width(this.value)/1920.0;
		// Render Background
		this.fill(stack, this.x, this.y, this.x+this.length, this.y+.04, ToggleButtonLoWidget.COLOR);
		// Animation
		if (this.down) this.animationProgress = Math.min(6, this.animationProgress + ClientLoTAS.internaltimer.tickDelta);
		else this.animationProgress = Math.max(0, this.animationProgress - ClientLoTAS.internaltimer.tickDelta);
		// Render Animation
		byte alpha = (byte) (this.ease(this.animationProgress, 0, 1, 6)*255);
		this.fill(stack, this.x, this.y, this.x+this.length, this.y+.04, (isMouseOver ? ToggleButtonLoWidget.MOUSEOVER_FOCUS_COLOR : ToggleButtonLoWidget.FOCUS_COLOR) - 0xFF000000 + (alpha << 24));
		// Render Centered Text
		if (this.animationProgress > 3) this.draw(stack, this.value, this.x-size+this.length/2, this.y+0.012, 20, ToggleButtonLoWidget.COLOR - 0xFF000000 + (alpha << 24), false);
		else this.draw(stack, this.value, this.x-size+this.length/2, this.y+0.012, 20, isMouseOver ? ToggleButtonLoWidget.MOUSEOVER_FOCUS_COLOR : ToggleButtonLoWidget.FOCUS_COLOR, false);
		super.render(stack, curX, curY);
	}


}
