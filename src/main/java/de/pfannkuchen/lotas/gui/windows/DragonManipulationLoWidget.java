package de.pfannkuchen.lotas.gui.windows;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.gui.widgets.ToggleButtonLoWidget;
import de.pfannkuchen.lotas.gui.widgets.WindowLoWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * Dragon manipulation window lowidget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class DragonManipulationLoWidget extends WindowLoWidget {
	
	// Buttons that toggle each other
	ToggleButtonLoWidget optimalpath;
	ToggleButtonLoWidget ccwtoggle;
	ToggleButtonLoWidget landingapproach;
	ToggleButtonLoWidget playerstrafing;
	boolean hasToggled = false; // boolean to prevent deadlock
	
	/**
	 * Initializes a dragon manipulation widget
	 */
	public DragonManipulationLoWidget() {
		super("dragonmanipulationwidget", new TextComponent("Dragon Manipulation"), .15, .235);
	}

	@Override
	protected void init() {
		this.addWidget(this.optimalpath = new ToggleButtonLoWidget(true, 0.005, 0.035, .14, LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forceOptimalPath"), b -> {
			LoTAS.dragonmanipulationmod.requestState("forceOptimalPath", b);
		}, new TextComponent("Optimal dragon path")));
		this.addWidget(this.ccwtoggle = new ToggleButtonLoWidget(true, 0.005, 0.085, .14, LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forceCCWToggle"), b -> {
			LoTAS.dragonmanipulationmod.requestState("forceCCWToggle", b);
		}, new TextComponent("Force cc/ccw toggle")));
		this.addWidget(this.landingapproach = new ToggleButtonLoWidget(true, 0.005, 0.185, .14, LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forceLandingApproach"), b -> {
			LoTAS.dragonmanipulationmod.requestState("forceLandingApproach", b);
			if (b) LoTAS.dragonmanipulationmod.requestState("forcePlayerStrafing", !b);
		}, new TextComponent("Force landing approach")));
		this.addWidget(this.playerstrafing = new ToggleButtonLoWidget(true, 0.005, 0.135, .14, LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forcePlayerStrafing"), b -> {
			LoTAS.dragonmanipulationmod.requestState("forcePlayerStrafing", b);
			if (b) LoTAS.dragonmanipulationmod.requestState("forceLandingApproach", !b);
		}, new TextComponent("Force player strafing")));
		super.init();
	}

	private static boolean shouldUpdate;
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		if (DragonManipulationLoWidget.shouldUpdate) {
			DragonManipulationLoWidget.shouldUpdate = false;
			this.optimalpath.down = LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forceOptimalPath");
			this.ccwtoggle.down = LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forceCCWToggle");
			this.landingapproach.down = LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forceLandingApproach");
			this.playerstrafing.down = LoTAS.configmanager.getBoolean("dragonmanipulationwidget", "forcePlayerStrafing");
		}
		super.render(stack, curX, curY);
	}
	
	/**
	 * Forces the reinitialization of the widget
	 */
	public static void forceUpdate() {
		DragonManipulationLoWidget.shouldUpdate = true;
	}

}
