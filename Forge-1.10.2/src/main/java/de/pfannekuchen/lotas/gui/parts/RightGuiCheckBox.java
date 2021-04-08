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
        if (this.enabled && this.visible && mouseX >= this.xPosition + mc.fontRendererObj.getStringWidth(displayString) + 13 && mouseY >= this.yPosition && mouseX < this.xPosition + mc.fontRendererObj.getStringWidth(displayString) + 13 + this.width && mouseY < this.yPosition + this.height) {
            this.setIsChecked(!this.isChecked());
            return true;
        }

        return false;
    }
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + 11 && mouseY < this.yPosition + this.height;
			GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.xPosition + mc.fontRendererObj.getStringWidth(displayString) + 13, this.yPosition, 0, 46, 11, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
			this.mouseDragged(mc, mouseX, mouseY);
			int color = 14737632;

			if (packedFGColour != 0) {
				color = packedFGColour;
			} else if (!this.enabled) {
				color = 10526880;
			}

			if (this.isChecked())
				this.drawCenteredString(mc.fontRendererObj, "x", this.xPosition + mc.fontRendererObj.getStringWidth(displayString) + 19, this.yPosition + 1, 14737632);

			this.drawString(mc.fontRendererObj, displayString, this.xPosition + 11, this.yPosition + 2, color);
		}
	}

}
