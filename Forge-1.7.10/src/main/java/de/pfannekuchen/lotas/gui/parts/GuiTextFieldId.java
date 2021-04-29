package de.pfannekuchen.lotas.gui.parts;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldId extends GuiTextField {

	int id;
	
	public GuiTextFieldId(int id, FontRenderer par1fontRendererObj, int par2, int par3, int par4, int par5) {
		super(par1fontRendererObj, par2, par3, par4, par5);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
