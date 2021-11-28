package de.pfannkuchen.lotas.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.loscreen.LoScreen;

/**
 * Locks the players in the savestate loscreen
 * @author Pancake
 */
public class SavestateLoScreen extends LoScreen {

	private static boolean allowUnlocking = false;
	
	@Override
	protected void init() {
		allowUnlocking = false; // Lock the screen
		super.init();
	}
	
	@Override
	protected void click(double curX, double curY, int button) {
		if (!allowUnlocking) return;
		ClientLoTAS.loscreenmanager.setScreen(null); // Close on click
		super.click(curX, curY, button);	
	}
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		// TODO: Temporary code
		fill(stack, 0, 0, 1, 1, 0xFFFFFFFF);
		super.render(stack, curX, curY);
	}
	
	/**
	 * Allows this screen to be closed with a mouse click
	 */
	public static void allowUnlocking() {
		SavestateLoScreen.allowUnlocking = true;
	}
	
}
