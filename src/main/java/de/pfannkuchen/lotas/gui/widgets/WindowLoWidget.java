package de.pfannkuchen.lotas.gui.widgets;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

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
	// x
	private static final TextComponent X_TEXT = new TextComponent("x");
	// Random
	private static final Random rng = new Random();
	
	// Window Sizes
	private TextComponent title;
	protected double windowWidth;
	protected double windowHeight;
	public double x = 0.2;
	public double y = 0.2;
	// Whether the close buttons should have a shadow
	private boolean showXShadow = false;
	// Animation Progress
	private double animationProgress;
	private boolean leftORight = rng.nextBoolean();
	private boolean topOBottom = rng.nextBoolean();
	private boolean horizontal = rng.nextBoolean();
	private boolean vertical = rng.nextBoolean();
	// Movement stuff
	private boolean isDragging;
	private double draggingOffsetX;
	private double draggingOffsetY;
	
	// Editable Properties
	public boolean active = false;
	
	/**
	 * Initializes the Window with a Title and Size
	 */
	public WindowLoWidget(TextComponent title, double windowWidth, double windowHeight) {
		this.title = title;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}

	@Override 
	protected void init() {
		// Force horizontal animation if none given
		if (!vertical && !horizontal) horizontal = true;
	}
	
	@Override
	protected void click(double curX, double curY, int button) {
		if (!this.active) return;
		// Close Window in X press
		if (curX > this.x+this.windowWidth-0.015 && curX < this.x+this.windowWidth && curY > this.y && curY < this.y+(0.015/9*16))
			this.active = false;
		this.isDragging = false; // Reset dragging
		super.click(curX-this.x, curY-this.y, button);
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
			this.x = curX - this.draggingOffsetX;
			this.y = curY - this.draggingOffsetY;
		}
		super.drag(prevCurX-this.x, prevCurY-this.y, curX-this.x, curY-this.y);
	}
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		if (!this.active) return;
		this.animationProgress = Math.min(6, this.animationProgress + mc.getDeltaFrameTime()); // Move the animation
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
		// X Hover
		if (curX > this.x+this.windowWidth-0.015 && curX < this.x+this.windowWidth && curY > this.y && curY < this.y+(0.015/9*16)) {
			this.fill(stack, this.x+this.windowWidth-0.015, this.y, this.x+this.windowWidth, this.y+(0.015/9*16), BORDER_COLOR);
			showXShadow = true;
		} else {
			showXShadow = false;
		}
		// Render Title and X
		this.draw(stack, this.title, this.x+0.006, this.y+0.007, 20, TITLE_COLOR, false);
		this.draw(stack, X_TEXT, this.x+windowWidth-0.0096, this.y+0.0045, 20, TITLE_COLOR, showXShadow);
		stack.translate(this.x*this.width, this.y*this.height, 0);
		super.render(stack, curX-this.x, curY-this.y);
	}
	
}
