package de.pfannekuchen.lotas.gui.widgets;

import net.minecraftforge.fml.client.config.GuiCheckBox;

public class ModifiedCheckBoxWidget extends GuiCheckBox {

	public ModifiedCheckBoxWidget(int id, int xPos, int yPos, String displayString, boolean isChecked) {
		super(id, xPos, yPos, displayString, isChecked);
	}

	//#if MC<=11102
//$$ 	public void drawButton(net.minecraft.client.Minecraft mc, int mouseX, int mouseY, float partial) {
//$$ 		super.drawButton(mc, mouseX, mouseY);
//$$ 	}
	//#endif
	
}
