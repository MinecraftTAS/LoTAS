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
 * Moveable Window Widget with Title
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class WindowLoWidget extends LoScreen {

	// Width and Height of the border around the Menu
	private static final double BORDER_WIDTH = 0.0015625;
	private static final double BORDER_HEIGHT = 0.00277;
	// Background Color
	private static final int BACKGROUND_COLOR = 0xff0a0a0b;
	// Border Color
	private static final int BORDER_COLOR = 0xff1b1c21;
	// Title Color
	private static final int TITLE_COLOR = 0xff149b5b;
	// Random
	private static final Random rng = new Random();
	
	// Window Sizes
	private TextComponent title;
	private String id;
	protected double windowWidth;
	protected double windowHeight;
	public double x = 0.2;
	public double y = 0.2;
	// Animation Progress
	private double animationProgress = 6;
	private boolean leftORight = rng.nextBoolean();
	private boolean topOBottom = rng.nextBoolean();
	private boolean horizontal = rng.nextBoolean();
	private boolean vertical = rng.nextBoolean();
	// Movement stuff
	private boolean isDragging;
	private double draggingOffsetX;
	private double draggingOffsetY;
	
	// Editable Properties
	protected boolean active = false;
	
	/**
	 * Initializes the Window with a Title and Size
	 */
	public WindowLoWidget(String id, TextComponent title, double windowWidth, double windowHeight) {
		this.id = id;
		this.title = title;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}

	@Override 
	protected void init() {
		// Load elements from config
		ConfigManager config = LoTAS.configmanager;
		this.x = config.getDouble(id, "x");
		this.y = config.getDouble(id, "y");
		this.active = config.getBoolean(id, "active");
		// Update Config
		Random rng = new Random();
		if (this.x == -1)
			this.x = rng.nextDouble()*0.7;
		if (this.y == -1)
			this.y = rng.nextDouble()*0.7;
		// Force horizontal animation if none given
		if (!vertical && !horizontal) horizontal = true;
		super.init();
	}
	
	@Override
	protected void click(double curX, double curY, int button) {
		if (!this.active) return;
		this.isDragging = false; // Reset dragging
		super.click(curX-this.x, curY-this.y, button);
		// Save Config AFTER super method
		ConfigManager config = LoTAS.configmanager;
		config.setDouble(id, "x", this.x);
		config.setDouble(id, "y", this.y);
		config.setBoolean(id, "active", this.active);
		config.save();
	}
	
	@Override
	protected void drag(double prevCurX, double prevCurY, double curX, double curY) {
		if (!this.active) return;
		// Check whether the drag has started
		if (curX > this.x && curX < this.x+this.windowWidth && curY > this.y && curY < this.y+(0.015/9*16) && !this.isDragging) {
			this.isDragging = true;
			// Store offset of cursor on the title bar
			this.draggingOffsetX = curX - this.x;
			this.draggingOffsetY = curY - this.y;
		}
		// Move the window while dragged
		if (isDragging) {
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
					vertical ? ((leftORight ? -1000 : 1000) + ease(this.animationProgress, 0, 1, 6)*(leftORight ? +1000 : -1000)) : 0,
					horizontal ? ((topOBottom ? -1000 : 1000) + ease(this.animationProgress, 0, 1, 6)*(topOBottom ? +1000 : -1000)) : 0,
					0);
		}
		// Render Background and Border
		this.fill(stack, this.x-BORDER_WIDTH+0.005, this.y-BORDER_HEIGHT+0.01, this.x+this.windowWidth+BORDER_WIDTH*1.25+0.005, this.y+this.windowHeight+BORDER_HEIGHT*1.25+0.01, 0xAA000000);
		this.fill(stack, this.x-BORDER_WIDTH, this.y-BORDER_HEIGHT, this.x+this.windowWidth+BORDER_WIDTH*1.25, this.y+this.windowHeight+BORDER_HEIGHT*1.25, BORDER_COLOR);
		this.fill(stack, this.x, this.y, this.x+this.windowWidth, this.y+this.windowHeight, BACKGROUND_COLOR);
		// Render Title
		this.draw(stack, this.title, this.x+0.006, this.y+0.007, 20, TITLE_COLOR, false);
		stack.translate(this.x*this.width/this.guiscale, this.y*this.height/this.guiscale, 0);
		super.render(stack, curX-this.x, curY-this.y);
		stack.popPose();
	}
	
	/**
	 * Enables or Disables a widget and stores it into the config
	 * @param enable Whether the widget is to enable or disable
	 */
	public void changeVisibility(boolean enable) {
		this.active = enable;
		if (enable) animationProgress = 0;
		ConfigManager config = LoTAS.configmanager;
		config.setBoolean(id, "active", this.active);
		config.save();
	}
	
}
