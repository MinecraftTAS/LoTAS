package de.pfannkuchen.lotas.gui.api;

import java.util.LinkedList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * LoScreen is an abstract class representing a second screen with a custom api rendered after the normal gui (Short: interactable overlay). 
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public abstract class LoScreen {
	
	/**
	 * Accessable Minecraft Instance.
	 */
	public Minecraft mc;
	
	/**
	 * List of Widgets managed by the LoScreen itself.
	 */
	public List<LoScreen> widgets = new LinkedList<>();
	
	/**
	 * Adds a Widget and updates its minecraft variable
	 * @param widget Widget to add
	 */
	public final void addWidget(LoScreen widget) {
		this.widgets.add(widget);
		widget.mc = this.mc;
		widget.init();
	}
	
	/**
	 * Initializes the Gui and it's widgets.
	 */
	public abstract void init();
	
	/**
	 * Updates the Gui and it's widgets.
	 * @param width Scaled Width of the Minecraft screen
	 * @param height Scaled Height of the Minecraft screen
	 */
	public void update(double width, double height) {
		widgets.forEach(w -> w.update(width, height));
	}
	
	/**
	 * Renders the Gui and it's widgets.
	 * @param stack Pose Stack for rendering
	 * @param curX Current cursor x position
	 * @param curY Current cursor y position
	 */
	public void render(PoseStack stack, double curX, double curY) {
		widgets.forEach(w -> w.render(stack, curX, curY));
	}
	
	/**
	 * Event for the cursor once it drags. Updates the Gui and it's widgets.
	 * @param prevCurX Previous cursor x position
	 * @param prevCurY Previous cursor y position
	 * @param curX Current cursor x position
	 * @param curY Current cursor y position
	 */
	public void drag(double prevCurX, double prevCurY, double curX, double curY) {
		widgets.forEach(w -> w.drag(prevCurX, prevCurY, curX, curY));
	}
	
	/**
	 * Event for the cursor once it clicks. Updates the Gui and it's widgets.
	 * @param curX Current cursor x position
	 * @param curY Current cursor y position
	 * @param button Button clicked with
	 */
	public void click(double curX, double curY, int button) {
		widgets.forEach(w -> w.click(curX, curY, button));
	}
	
}
