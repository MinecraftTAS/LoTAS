package de.pfannkuchen.lotas.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.TextComponent;

/**
 * Tickrate Changer Widget
 * @author Pancake
 */
public class TickrateChangerLoWidget extends WindowLoWidget {

	// Tickrates to clamp to
	private static final double[] TICKRATES = {
			0.5, 1, 2, 4, 5, 10, 20
	};
	
	/**
	 * Initializes a Tickrate Changer Widget
	 */
	public TickrateChangerLoWidget() {
		super(new TextComponent("Tickrate Changer"), .15, .135);
		this.active = true;
	}

	@Override
	protected void init() {
		final SliderLoWidget slider;
		addWidget(slider = new SliderLoWidget(true, 0.015, 0.095, .12, 1, c -> {
			return new TextComponent("Tickrate: " + ((c*19)+1+"").split("\\.")[0]);
		}, new TextComponent("Tickrate: 20")));
		addWidget( new ButtonLoWidget(true, 0.005, 0.035, .065, () -> {
			
		}, new TextComponent("+")));
		addWidget(new ButtonLoWidget(true, 0.08, 0.035, .065, () -> {
			
		}, new TextComponent("-")));
		super.init();
	}
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {

		super.render(stack, curX, curY);
	}
	
}
