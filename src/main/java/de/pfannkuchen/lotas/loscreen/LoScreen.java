package de.pfannkuchen.lotas.loscreen;

import java.util.LinkedList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TextComponent;

/**
 * LoScreen is an abstract class representing a second screen with a custom api rendered after the normal gui (Short: interactable overlay). 
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public abstract class LoScreen {
	
	/**
	 * Accessable Minecraft Instance.
	 */
	protected Minecraft mc;
	
	/**
	 * List of Widgets managed by the LoScreen itself.
	 */
	private List<LoScreen> widgets = new LinkedList<>();
	
	/**
	 * Screen Width
	 */
	protected int width;
	
	/**
	 * Screen Height
	 */
	protected int height;
	
	/**
	 * Gui Scale
	 */
	protected double guiscale;
	
	/**
	 * Adds a Widget and updates its minecraft variable
	 * @param widget Widget to add
	 */
	protected final void addWidget(LoScreen widget) {
		this.widgets.add(widget);
		widget.mc = this.mc;
		widget.init();
	}
	
	/**
	 * Initializes the Gui and it's widgets.
	 */
	protected void init() {
		this.widgets.forEach(w -> w.init());
	}
	
	/**
	 * Updates the Gui and it's widgets.
	 * @param width Scaled Width of the Minecraft screen
	 * @param height Scaled Height of the Minecraft screen
	 */
	protected void update(int width, int height) {
		this.width = width;
		this.height = height;
		this.guiscale = mc.getWindow().getGuiScale();
		this.widgets.clear();
		this.init();
		this.widgets.forEach(w -> w.update(width, height));
	}
	
	/**
	 * Renders the Gui and it's widgets.
	 * @param stack Pose Stack for rendering
	 * @param curX Current cursor x position
	 * @param curY Current cursor y position
	 */
	protected void render(PoseStack stack, double curX, double curY) {
		this.widgets.forEach(w -> w.render(stack, curX, curY));
	}
	
	/**
	 * Event for the cursor once it drags. Updates the Gui and it's widgets.
	 * @param prevCurX Previous cursor x position
	 * @param prevCurY Previous cursor y position
	 * @param curX Current cursor x position
	 * @param curY Current cursor y position
	 */
	protected void drag(double prevCurX, double prevCurY, double curX, double curY) {
		this.widgets.forEach(w -> w.drag(prevCurX, prevCurY, curX, curY));
	}
	
	/**
	 * Event for the cursor once it clicks. Updates the Gui and it's widgets.
	 * @param curX Current cursor x position
	 * @param curY Current cursor y position
	 * @param button Button clicked with
	 */
	protected void click(double curX, double curY, int button) {
		this.widgets.forEach(w -> w.click(curX, curY, button));
	}
	
	/**
	 * Fills a plane with given color
	 * @param stack Pose Stack ._.
	 * @param x1 Percentage X
	 * @param y1 Percentage Y
	 * @param x2 End Percentage X
	 * @param y2 End Percentage Y
	 * @param color Color to fill with
	 */
	protected final void fill(PoseStack stack, double x1, double y1, double x2, double y2, int color) {
		GuiComponent.fill(stack, (int) (x1*this.width/this.guiscale), (int) (y1*this.height/this.guiscale), (int) (x2*this.width/this.guiscale), (int) (y2*this.height/this.guiscale), color);
	}

	/**
	 * Draws a Text Component onto the screen
	 * @param stack Pose Stack ._.
	 * @param text Text to print
	 * @param x Percentage X
	 * @param y Percentage Y
	 * @param size Size in pt
	 * @param color Color
	 * @param shadow Shadow?
	 */
	protected final void draw(PoseStack stack, TextComponent text, double x, double y, int size, int color, boolean shadow) {
		stack.pushPose();
		// Move by gui scale first
		stack.scale((float) (1f/this.guiscale), (float) (1f/this.guiscale), 1.0f);
		stack.translate(x*this.width, y*this.height, 0);
		// Resize for pt size
		final float scaleX = size/192.0f*this.width/100f;
		final float scaleY = size/108.0f*(this.width/16*9)/100f;
		stack.scale(scaleX, scaleY, 1.0f);
		// Render text using font renderer
		if (shadow) this.mc.font.drawShadow(stack, text, 0, 0, color);
		else this.mc.font.draw(stack, text, 0, 0, color);
		stack.popPose();
	}
	
	
	/**
	 * Ease Interpolation
	 * @param t Progress
	 * @param b Offset
	 * @param c Goal
	 * @param d Dividor for Progress
	 * @return Ease-out-quad variable
	 */
	protected final double ease(double t, double b, double c, double d) {
		return -c *(t/=d)*(t-2) + b;
	}

	/**
	 * Resets all Widgets and recreates the Gui from scratch
	 * @param width
	 * @param height
	 */
	public void reset(int width, int height) {
		this.widgets.clear();
		this.init();
		this.update(width, height);
	}
	
}
