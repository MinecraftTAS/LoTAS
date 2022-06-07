package de.pfannkuchen.lotas.gui.widgets;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

/**
 * Slider lowidget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class SliderLoWidget extends LoScreen {

	// Background color
	private static final int BACKGROUND_COLOR = 0xff2a2a2a;
	// Border color
	private static final int BORDER_COLOR = 0xff108950;
	// Border color but focused
	private static final int BORDER_FOCUS_COLOR = 0xff19b36a;
	// Text color
	private static final int TEXT_COLOR = 0xff108950;

	// Position of the slider
	public double x;
	public double y;
	// Length of the slider
	public double length;
	// Progress of the slider
	public double progress;

	// Whether the slider should be shown
	public boolean active;
	// Whether it's currently being dragged
	public boolean isDragging = false;
	// Move handler
	public Function<Double, Component> onMove;
	// Value of the progress
	public Component value;

	/**
	 * Initializes a new slider
	 * @param active Whether the slider is active by default
	 * @param x position
	 * @param y position
	 * @param length Length of the slider (in screen width percentage)
	 * @param Callback after slider is moved
	 */
	public SliderLoWidget(boolean active, double x, double y, double length, double progress, Function<Double, Component> onMove, Component value) {
		this.active = active;
		this.x = x;
		this.y = y;
		this.length = length;
		this.progress = progress;
		this.onMove = onMove;
		this.value = value;
	}

	@Override
	protected void click(double curX, double curY, int button) {
		if (!this.active) return;
		this.isDragging = false; // Reset dragging
		super.click(curX, curY, button);
	}

	@Override
	protected void drag(double prevCurX, double prevCurY, double curX, double curY) {
		if (!this.active) return;
		// Check whether the drag has started
		if (curX > this.x-0.005+this.length*this.progress && curX < this.x+0.005+this.length*this.progress && curY > this.y-0.005/9.0f*16.0f && curY < this.y+0.005/9.0f*16.0f && !this.isDragging) {
			this.isDragging = true;
		}
		// Move the slider while dragged
		if (this.isDragging) {
			double p = curX-this.x;
			if (p < 0) p = 0;
			if (p > this.length) p = this.length;
			this.progress = p/this.length;
			// Update the Label
			this.value = this.onMove.apply(this.progress);
		}
		super.drag(prevCurX, prevCurY, curX, curY);
	}

	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		if (!this.active) return;
		this.fill(stack, this.x, this.y-.0015, this.x+this.length, this.y+.0015, SliderLoWidget.BACKGROUND_COLOR); // Render Background
		// Render dot with different color when hovered
		if (curX > this.x-0.005+this.length*this.progress && curX < this.x+0.005+this.length*this.progress && curY > this.y-0.005/9.0f*16.0f && curY < this.y+0.005/9.0f*16.0f && !this.isDragging)
			this.fill(stack, this.x-0.005+this.length*this.progress, this.y-0.005/9.0f*16.0f, this.x+0.005+this.length*this.progress, this.y+0.005/9.0f*16.0f, SliderLoWidget.BORDER_FOCUS_COLOR);
		else
			this.fill(stack, this.x-0.005+this.length*this.progress, this.y-0.005/9.0f*16.0f, this.x+0.005+this.length*this.progress, this.y+0.005/9.0f*16.0f, SliderLoWidget.BORDER_COLOR);
		// Render value
		this.draw(stack, this.value, this.x, this.y+0.0165, 20, SliderLoWidget.TEXT_COLOR, false);
		super.render(stack, curX, curY);
	}


}
