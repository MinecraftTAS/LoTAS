package de.pfannkuchen.lotas.gui.widgets;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.mods.ConfigManager;
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
		super(new TextComponent("Tickrate Changer"), .15, .135);
		TickrateChangerLoWidget.update = update;
	}

	@Override
	protected void init() {
		addWidget(slider = new SliderLoWidget(true, 0.015, 0.095, .12, index / ((double) TICKRATES.length), c -> {
			return new TextComponent("Tickrate: " + updateTickrate((int) (c*TICKRATES.length), true));
		}, new TextComponent("Tickrate: " + TICKRATES[index])));
		addWidget( new ButtonLoWidget(true, 0.005, 0.035, .065, () -> {
			updateTickrate(TickrateChangerLoWidget.index+1, false);
		}, new TextComponent("+")));
		addWidget(new ButtonLoWidget(true, 0.08, 0.035, .065, () -> {
			updateTickrate(TickrateChangerLoWidget.index-1, false);
		}, new TextComponent("-")));
		// Load elements from config
		ConfigManager config = LoTAS.configmanager;
		this.x = config.getDouble("tickratechangerwidget", "x");
		this.y = config.getDouble("tickratechangerwidget", "y");
		this.active = config.getBoolean("tickratechangerwidget", "active");
		super.init();
	}
	
	/**
	 * Updates the selected index as the tickrate
	 * @param index tickrate index
	 * @return new tickrate
	 */
	private static double updateTickrate(int index, boolean isSlider) {
		// Clamp and update tickrate
		index = Math.min(Math.max(0, index), TICKRATES.length-1);
		if (index == TickrateChangerLoWidget.index) return TICKRATES[index];
		TickrateChangerLoWidget.index = index;
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
	public static void updateTickrate(double tickrate) {
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
	
	/**
	 * Save Configuration after releasing the mouse
	 */
	@Override
	protected void click(double curX, double curY, int button) {
		super.click(curX, curY, button);
		// Save Config AFTER super method
		ConfigManager config = LoTAS.configmanager;
		config.setDouble("tickratechangerwidget", "x", this.x);
		config.setDouble("tickratechangerwidget", "y", this.y);
		config.setBoolean("tickratechangerwidget", "active", this.active);
		config.save();
	}
		
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		stack.pushPose();
		super.render(stack, curX, curY);
		stack.popPose();
	}

	/**
	 * Enables or Disables a widget and stores it into the config
	 * @param enable Whether the widget is to enable or disable
	 */
	public void changeVisibility(boolean enable) {
		this.active = enable;
		ConfigManager config = LoTAS.configmanager;
		config.setBoolean("tickratechangerwidget", "active", this.active);
		config.save();
	}
	
}
