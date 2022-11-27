package de.pfannkuchen.lotas.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

/**
 * Button widget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class ButtonLoWidget extends LoScreen {

	// Background color
	private static final int COLOR = 0xff1b1c21;
	// Background color but focused
	private static final int FOCUS_COLOR = 0xff108950;

	// Position of the button
	public double x;
	public double y;
	// Length of the button
	public double length;

	// Whether the button should be shown
	public boolean active;
	// Move handler
	public Runnable onClick;
	// Value of the button
	public Component value;
	// Hover animation
	public float animationProgress;

	/**
	 * Initializes a new button
	 * @param active Whether the button is active by default
	 * @param x position
	 * @param y position
	 * @param length Length of the button (in screen width percentage)
	 * @param Callback after button is clicked
	 */
	public ButtonLoWidget(boolean active, double x, double y, double length, Runnable onClick, Component value) {
		this.active = active;
		this.x = x;
		this.y = y;
		this.length = length;
		this.onClick = onClick;
		this.value = value;
	}

	@Override
	protected void click(double curX, double curY, int button) {
		if (!this.active)
			return;
		// Trigger click event when mouse is over the button
		if (curX > this.x && curY > this.y && curX < this.x + this.length && curY < this.y + .05) {
			this.onClick.run();
			this.animationProgress -= 2;
		}
		super.click(curX, curY, button);
	}

	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		if (!this.active)
			return;
		boolean mouseOver = curX > this.x && curY > this.y && curX < this.x + this.length && curY < this.y + .05;
		double size = this.mc.font.width(this.value) / 1920.0;
		// Render background
		this.fill(stack, this.x, this.y, this.x + this.length, this.y + .04, ButtonLoWidget.COLOR);
		// Animation
		if (mouseOver)
			this.animationProgress = Math.min(6, this.animationProgress + ClientLoTAS.internaltimer.tickDelta);
		else
			this.animationProgress = Math.max(0, this.animationProgress - ClientLoTAS.internaltimer.tickDelta);
		// Render animation
		byte alpha = (byte) (this.ease(this.animationProgress, 0, 1, 6) * 255);
		this.fill(stack, this.x, this.y, this.x + this.length, this.y + .04, ButtonLoWidget.FOCUS_COLOR - 0xFF000000 + (alpha << 24));
		// Render centered Text
		if (this.animationProgress > 3)
			this.draw(stack, this.value, this.x - size + this.length / 2, this.y + 0.012, 20, ButtonLoWidget.COLOR - 0xFF000000 + (alpha << 24), false);
		else
			this.draw(stack, this.value, this.x - size + this.length / 2, this.y + 0.012, 20, ButtonLoWidget.FOCUS_COLOR, false);
		super.render(stack, curX, curY);
	}

}
