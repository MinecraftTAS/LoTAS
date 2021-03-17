package de.pfannekuchen.lotas.gui.parts;

import java.util.HashMap;

import de.pfannekuchen.lotas.gui.GuiEntitySpawner;
import net.minecraftforge.fml.client.config.GuiSlider;

public class CustomEntitySlider extends GuiSlider {

	public HashMap<Integer, String> entities;
	
	public CustomEntitySlider(int id, int xPos, int yPos, HashMap<Integer, String> ent, int width, int height) {
		super(id, xPos, yPos, width, height, "Entity: ", "", 0, ent.size() - 1, 0, true, true, null);
		showDecimal = false;
		this.displayString = dispString + GuiEntitySpawner.entities.get(0);
		this.entities = ent;
	}

	public void updateSlider() {
		if (this.sliderValue < 0.0F) {
			this.sliderValue = 0.0F;
		}

		if (this.sliderValue > 1.0F) {
			this.sliderValue = 1.0F;
		}

		displayString = dispString + entities.get((int) Math.round(sliderValue * (maxValue - minValue) + minValue)) + suffix;
	}

}
