package de.pfannkuchen.lotas.gui.api;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;

/*
 * WARNING: EXAMPLE CODE
 */

/**
 * This is the Main LoScreen that opens on the LoTAS Edit button. Currently it is only an example though
 * @author Pancake
 */
public class MainLoScreen extends LoScreen {

	@Override
	public void update(double width, double height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(double curX, double curY) {
		Screen.drawString(new PoseStack(), mc.font, "Hell yeah this kinda works now!", (int) curX / 4, (int) curY / 4, 0xFFFFFF);
	}

	@Override
	public void drag(double prevCurX, double prevCurY, double curX, double curY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void click(double curX, double curY, int button) {
		// TODO Auto-generated method stub
		
	}
	
	
}
