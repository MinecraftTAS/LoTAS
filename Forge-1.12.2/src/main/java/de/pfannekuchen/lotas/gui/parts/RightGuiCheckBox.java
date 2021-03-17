package de.pfannekuchen.lotas.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiUtils;

public class RightGuiCheckBox extends GuiCheckBox {

	public RightGuiCheckBox(int id, int xPos, int yPos, String displayString, boolean isChecked) {
		super(id, xPos, yPos, displayString, isChecked);
	}

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled && this.visible && mouseX >= this.x + mc.fontRenderer.getStringWidth(displayString) + 13 && mouseY >= this.y && mouseX < this.x + mc.fontRenderer.getStringWidth(displayString) + 13 + this.width && mouseY < this.y + this.height) {
            this.setIsChecked(!this.isChecked());
            return true;
        }

        return false;
    }
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		if (this.visible) {
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + 11 && mouseY < this.y + this.height;
			GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.x + mc.fontRenderer.getStringWidth(displayString) + 13, this.y, 0, 46, 11, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
			this.mouseDragged(mc, mouseX, mouseY);
			int color = 14737632;

			if (packedFGColour != 0) {
				color = packedFGColour;
			} else if (!this.enabled) {
				color = 10526880;
			}

			if (this.isChecked())
				this.drawCenteredString(mc.fontRenderer, "x", this.x + mc.fontRenderer.getStringWidth(displayString) + 19, this.y + 1, 14737632);

			this.drawString(mc.fontRenderer, displayString, this.x + 11, this.y + 2, color);
		}
	}

}
