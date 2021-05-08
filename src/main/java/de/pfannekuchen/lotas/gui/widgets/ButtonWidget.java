package de.pfannekuchen.lotas.gui.widgets;

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
		playPressSound(mc.getSoundHandler());
		//#if MC>=11200
		if (this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
		//#else
		//$$ if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
		//#endif
			p.trigger(this);
			return true;
		} else {
			return false;
		}
	}

	public void render(int mouseX, int mouseY, float delta) {
		//#if MC>=11200
		super.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, delta);
		//#else
		//$$ super.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		//#endif
	}

}
