package de.pfannkuchen.lotas.gui.widgets;

import java.util.function.Consumer;

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
	private static int index = 6;
	// Tickrate Slider
	private static SliderLoWidget slider;
	// Update Event
	private static Consumer<Double> update;

	/**
	 * Initializes a Tickrate Changer Widget
	 */
	public TickrateChangerLoWidget(Consumer<Double> update) {
		super("tickratechangerwidget", new TextComponent("Tickrate Changer"), .15, .135);
		TickrateChangerLoWidget.update = update;
	}

	@Override
	protected void init() {
		this.addWidget(TickrateChangerLoWidget.slider = new SliderLoWidget(true, 0.015, 0.095, .12, TickrateChangerLoWidget.index / (double) TickrateChangerLoWidget.TICKRATES.length, c -> new TextComponent("Tickrate: " + TickrateChangerLoWidget.updateTickrate((int) (c*TickrateChangerLoWidget.TICKRATES.length), true, true)), new TextComponent("Tickrate: " + TickrateChangerLoWidget.TICKRATES[TickrateChangerLoWidget.index])));
		this.addWidget(new ButtonLoWidget(true, 0.005, 0.035, .065, () -> {
			TickrateChangerLoWidget.updateTickrate(TickrateChangerLoWidget.index+1, false, true);
		}, new TextComponent("+")));
		this.addWidget(new ButtonLoWidget(true, 0.08, 0.035, .065, () -> {
			TickrateChangerLoWidget.updateTickrate(TickrateChangerLoWidget.index-1, false, true);
		}, new TextComponent("-")));
		super.init();
	}

	/**
	 * Updates the selected index as the tickrate
	 * @param index tickrate index
	 * @return new tickrate
	 */
	private static double updateTickrate(int index, boolean isSlider, boolean shouldTrigger) {
		// Clamp and update tickrate
		index = Math.min(Math.max(0, index), TickrateChangerLoWidget.TICKRATES.length-1);
		if (index == TickrateChangerLoWidget.index) return TickrateChangerLoWidget.TICKRATES[index];
		TickrateChangerLoWidget.index = index;
		// Update Slider
		if (!isSlider && TickrateChangerLoWidget.slider != null) {
			TickrateChangerLoWidget.slider.value = new TextComponent("Tickrate: " + TickrateChangerLoWidget.TICKRATES[index]);
			TickrateChangerLoWidget.slider.progress = index / (double) TickrateChangerLoWidget.TICKRATES.length;
		}
		// Trigger Event
		if (TickrateChangerLoWidget.update != null && shouldTrigger) TickrateChangerLoWidget.update.accept(TickrateChangerLoWidget.TICKRATES[index]);
		// Return new tickrate
		return TickrateChangerLoWidget.TICKRATES[index];
	}

	/**
	 * Updates the tickrate and finds the nearest index
	 * WARNING: Widget needs to be initialized first
	 * @param tickrate tickrate
	 */
	public static void updateTickrate(double tickrate) {
		// Find closest index
		int bestIndex = -1;
		double closestMatch = Double.MAX_VALUE;
		for (int i = 0; i < TickrateChangerLoWidget.TICKRATES.length; i++) {
			double diff = Math.abs(TickrateChangerLoWidget.TICKRATES[i] - tickrate);
			if (closestMatch >= diff) {
				bestIndex = i;
				closestMatch = diff;
			}
		}
		// Update Tickrate
		TickrateChangerLoWidget.updateTickrate(bestIndex, false, false);
	}

}
