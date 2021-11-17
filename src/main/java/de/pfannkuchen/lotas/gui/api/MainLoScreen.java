package de.pfannkuchen.lotas.gui.api;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;

/*
 * WARNING: EXAMPLE CODE
 */

/**
 * This is the Main LoScreen that opens on the LoTAS Edit button. Currently it is only an example though
 * @author Pancake
 */
public class MainLoScreen extends LoScreen {

	int width;
	int height;
	
	@Override
	public void update(double width, double height) {
		this.width = (int) width;
		this.height = (int) height;
	}

	@Override
	public void render(PoseStack stack, double curX, double curY) {
		final int rightWidgetWidth = width / 4;
		GuiComponent.fill(stack, width - rightWidgetWidth, 0, width, height, 0xFF1c1a1e);
	}

	@Override
	public void drag(double prevCurX, double prevCurY, double curX, double curY) {
		
	}

	@Override
	public void click(double curX, double curY, int button) {
		
	}
	
	
}
