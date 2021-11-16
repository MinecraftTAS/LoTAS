package de.pfannkuchen.lotas.gui.api;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TextComponent;

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
		GL11.glEnable(GL11.GL_BLEND);
     	GL11.glBlendFunc(770, 771);
		GuiComponent.drawString(stack, mc.font, new TextComponent("Test"), (width) - 80, 0, 0x1c1a1e);
		GuiComponent.fill(stack, width - (width / 4), 0, width / 4, height, 0xFF1c1a1e);
	}

	@Override
	public void drag(double prevCurX, double prevCurY, double curX, double curY) {
		
	}

	@Override
	public void click(double curX, double curY, int button) {
		
	}
	
	
}
