package de.pfannkuchen.lotas.gui.widgets;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * Tickrate Changer Widget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class TickrateChangerLoWidget extends WindowLoWidget {

	// Tickrates to clamp to
	private static final double[] TICKRATES = {
			0.5, 1, 2, 4, 5, 10, 20
	};
	// Tickrate index selected
	private int index = 6;
	// Tickrate Slider
	SliderLoWidget slider;
	// Update Event
	Consumer<Double> update;
	
	/**
	 * Initializes a Tickrate Changer Widget
	 */
	public TickrateChangerLoWidget(Consumer<Double> update) {
		super(new TextComponent("Tickrate Changer"), .15, .135);
		this.active = true;
		this.update = update;
	}

	@Override
	protected void init() {
		addWidget(slider = new SliderLoWidget(true, 0.015, 0.095, .12, 1, c -> {
			return new TextComponent("Tickrate: " + updateTickrate((int) (c*TICKRATES.length), true));
		}, new TextComponent("Tickrate: 20.0")));
		addWidget( new ButtonLoWidget(true, 0.005, 0.035, .065, () -> {
			updateTickrate(this.index+1, false);
		}, new TextComponent("+")));
		addWidget(new ButtonLoWidget(true, 0.08, 0.035, .065, () -> {
			updateTickrate(this.index-1, false);
		}, new TextComponent("-")));
		super.init();
	}
	
	/**
	 * Updates the selected index as the tickrate
	 * @param index tickrate index
	 * @return new tickrate
	 */
	private double updateTickrate(int index, boolean isSlider) {
		// Clamp and update tickrate
		index = Math.min(Math.max(0, index), TICKRATES.length-1);
		if (index == this.index) return TICKRATES[index];
		this.index = index;
		// Update Slider
		if (!isSlider) {
			slider.value = new TextComponent("Tickrate: " + TICKRATES[index]);
			slider.progress = index / ((double) TICKRATES.length);
		}
		// Trigger Event
		update.accept(TICKRATES[index]);
		// Return new tickrate
		return TICKRATES[index];
	}
	
	/**
	 * Updates the tickrate and finds the nearest index
	 * WARNING: Widget needs to be initialized first
	 * @param tickrate tickrate
	 */
	public void updateTickrate(double tickrate) {
		// Find closest index
		int bestIndex = -1;
		double closestMatch = Double.MAX_VALUE;
		for (int i = 0; i < TICKRATES.length; i++) {
			double diff = Math.abs(TICKRATES[i] - tickrate);
			if (closestMatch >= diff) {
				bestIndex = i;
				closestMatch = diff;
			}
		}
		// Update Tickrate
		updateTickrate(bestIndex, false);
	}
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		stack.pushPose();
		super.render(stack, curX, curY);
		stack.popPose();
	}
	
}
