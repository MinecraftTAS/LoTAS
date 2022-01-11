package de.pfannkuchen.lotas.gui.widgets;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.loscreen.LoScreen;
import de.pfannkuchen.lotas.mods.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;

/**
 * window lowidget with title
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class WindowLoWidget extends LoScreen {

	// Width and height of the border around the window
	private static final double BORDER_WIDTH = 0.0015625;
	private static final double BORDER_HEIGHT = 0.00277;
	// Background color
	private static final int BACKGROUND_COLOR = 0xff0a0a0b;
	// Border color
	private static final int BORDER_COLOR = 0xff1b1c21;
	// Title color
	private static final int TITLE_COLOR = 0xff149b5b;
	// Random
	private static final Random rng = new Random();

	// Window sizes
	private TextComponent title;
	private String id;
	protected double windowWidth;
	protected double windowHeight;
	public double x = 0.2;
	public double y = 0.2;
	// Animation progress
	protected double animationProgress = 6;
	protected boolean leftORight = WindowLoWidget.rng.nextBoolean();
	protected boolean topOBottom = WindowLoWidget.rng.nextBoolean();
	protected boolean horizontal = WindowLoWidget.rng.nextBoolean();
	protected boolean vertical = WindowLoWidget.rng.nextBoolean();
	// Movement stuff
	private boolean isDragging;
	private double draggingOffsetX;
	private double draggingOffsetY;

	// Editable properties
	protected boolean active = false;

	/**
	 * Initializes the window with a title and size
	 */
	public WindowLoWidget(String id, TextComponent title, double windowWidth, double windowHeight) {
		this.id = id;
		this.title = title;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}

	@Override
	protected void init() {
		// Load elements from configuration
		ConfigManager config = LoTAS.configmanager;
		this.x = config.getDouble(this.id, "x");
		this.y = config.getDouble(this.id, "y");
		this.active = config.getBoolean(this.id, "active");
		// Update configuration
		Random rng = new Random();
		if (this.x == -1)
			this.x = rng.nextDouble()*0.7;
		if (this.y == -1)
			this.y = rng.nextDouble()*0.7;
		// Force horizontal animation if none given
		if (!this.vertical && !this.horizontal) this.horizontal = true;
		super.init();
	}

	@Override
	protected void click(double curX, double curY, int button) {
		if (!this.active) return;
		this.isDragging = false; // Reset dragging
		super.click(curX-this.x, curY-this.y, button);
		// Save configuration AFTER super method
		ConfigManager config = LoTAS.configmanager;
		config.setDouble(this.id, "x", this.x);
		config.setDouble(this.id, "y", this.y);
		config.setBoolean(this.id, "active", this.active);
		config.save();
	}

	@Override
	protected void drag(double prevCurX, double prevCurY, double curX, double curY) {
		if (!this.active) return;
		// Check whether the drag has started
		if (curX > this.x && curX < this.x+this.windowWidth && curY > this.y && curY < this.y+0.015/9*16 && !this.isDragging) {
			this.isDragging = true;
			// Store offset of cursor on the title bar
			this.draggingOffsetX = curX - this.x;
			this.draggingOffsetY = curY - this.y;
		}
		// Move the window while dragged
		if (this.isDragging) {
			this.x = Mth.clamp(curX - this.draggingOffsetX, 0, 0.76);
			this.y = Mth.clamp(curY - this.draggingOffsetY, 0, 0.95);
		}
		super.drag(prevCurX-this.x, prevCurY-this.y, curX-this.x, curY-this.y);
	}

	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		if (!this.active) return;
		stack.pushPose();
		this.animationProgress = Math.min(6, this.animationProgress + ClientLoTAS.internaltimer.tickDelta); // Move the animation
		if (this.animationProgress != 6) {
			stack.translate(
					this.vertical ? (this.leftORight ? -1000 : 1000) + this.ease(this.animationProgress, 0, 1, 6)*(this.leftORight ? +1000 : -1000) : 0,
							this.horizontal ? (this.topOBottom ? -1000 : 1000) + this.ease(this.animationProgress, 0, 1, 6)*(this.topOBottom ? +1000 : -1000) : 0,
									0);
		}
		// Render background and border
		this.fill(stack, this.x-WindowLoWidget.BORDER_WIDTH+0.005, this.y-WindowLoWidget.BORDER_HEIGHT+0.01, this.x+this.windowWidth+WindowLoWidget.BORDER_WIDTH*1.25+0.005, this.y+this.windowHeight+WindowLoWidget.BORDER_HEIGHT*1.25+0.01, 0xAA000000);
		this.fill(stack, this.x-WindowLoWidget.BORDER_WIDTH, this.y-WindowLoWidget.BORDER_HEIGHT, this.x+this.windowWidth+WindowLoWidget.BORDER_WIDTH*1.25, this.y+this.windowHeight+WindowLoWidget.BORDER_HEIGHT*1.25, WindowLoWidget.BORDER_COLOR);
		this.fill(stack, this.x, this.y, this.x+this.windowWidth, this.y+this.windowHeight, WindowLoWidget.BACKGROUND_COLOR);
		// Render title
		this.draw(stack, this.title, this.x+0.006, this.y+0.007, 20, WindowLoWidget.TITLE_COLOR, false);
		stack.translate(this.x*this.width/this.guiscale, this.y*this.height/this.guiscale, 0);
		super.render(stack, curX-this.x, curY-this.y);
		stack.popPose();
	}

	/**
	 * Enables or disables a widget and stores it into the configuration
	 * @param enable Whether the widget should be enabled or disabled
	 */
	public void changeVisibility(boolean enable) {
		this.active = enable;
		if (enable) this.animationProgress = 0;
		ConfigManager config = LoTAS.configmanager;
		config.setBoolean(this.id, "active", this.active);
		config.save();
	}

}
