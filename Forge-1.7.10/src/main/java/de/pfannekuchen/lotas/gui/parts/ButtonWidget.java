package de.pfannekuchen.lotas.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

/*
 * Backport of the new button from Fabric, does not require the use of an id.
 */

public class ButtonWidget extends GuiButton {

	PressAction p;
	
	public static interface PressAction {
		public void trigger(ButtonWidget b);
	}
	
	public ButtonWidget(int x, int y, int widthIn, int heightIn, String buttonText, PressAction p) {
		super(9999, x, y, widthIn, heightIn, buttonText);
		this.p = p;
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
			p.trigger(this);
			return true;
		} else {
			return false;
		}
	}

	public void render(int mouseX, int mouseY, float delta) {
		super.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
	}

}
