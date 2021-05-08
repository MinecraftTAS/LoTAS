package de.pfannekuchen.lotas.gui.widgets;

//#if MC<=11102
//$$ import net.minecraft.client.Minecraft;
//#endif
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class ModifiedCheckBoxWidget extends GuiCheckBox {

	public ModifiedCheckBoxWidget(int id, int xPos, int yPos, String displayString, boolean isChecked) {
		super(id, xPos, yPos, displayString, isChecked);
	}

	//#if MC<=11102
	//$$ public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
	//$$ 	super.drawButton(mc, mouseX, mouseY);
	//$$ }
	//#endif
	
}
