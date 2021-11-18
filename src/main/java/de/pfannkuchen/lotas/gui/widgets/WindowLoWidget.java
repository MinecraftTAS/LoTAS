package de.pfannkuchen.lotas.gui.widgets;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.minecraft.network.chat.TextComponent;

/**
 * Moveable Window Widget with Title
 * @author Pancake
 */
public class WindowLoWidget extends LoScreen {

	// Height of the Title Bar
	private static final double MENU_HEIGHT = 0.030;
	// Width and Height of the border around the Menu
	private static final double BORDER_WIDTH = 0.003125;
	private static final double BORDER_HEIGHT = 0.00555;
	// Background Color
	private static final int BACKGROUND_COLOR = 0xff1c1a1e;
	// Border Color
	private static final int BORDER_COLOR = 0xff100e11;
	// Title Color
	private static final int TITLE_COLOR = 0xff035257;
	// x
	private static final TextComponent X_TEXT = new TextComponent("x");
	// Random
	private static final Random rng = new Random();
	
	// Window Sizes
	private TextComponent title;
	private double width;
	private double height;
	private double x = 0.2;
	private double y = 0.2;
	// Whether the close buttons should have a shadow
	private boolean showXShadow = false;
	// Animation Progress
	private double animationProgress;
	private boolean leftORight = rng.nextBoolean();
	private boolean topOBottom = rng.nextBoolean();
	private boolean horizontal = rng.nextBoolean();
	private boolean vertical = rng.nextBoolean();
	
	// Editable Properties
	public boolean active = false;
	
	/**
	 * Initializes the Window with a Title and Size
	 */
	public WindowLoWidget(TextComponent title, double width, double height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}

	@Override 
	protected void init() {
		if (!vertical && !horizontal) horizontal = true;
	}
	
	@Override
	protected void click(double curX, double curY, int button) {
		if (!this.active) return;
		if (curX > this.x+this.width-BORDER_WIDTH*5 && curX < this.x+width+BORDER_WIDTH*2 && curY > this.y && curY < this.y+0.03) 
			this.active = false;
		this.isDragging = false;
	}
	
	boolean isDragging;
	double curXStored;
	double curYStored;
	double draggingOffsetX;
	double draggingOffsetY;
	
	@Override
	protected void drag(double prevCurX, double prevCurY, double curX, double curY) {
		if (!this.active) return;
		if (curX > this.x && curX < this.x+this.width && curY > this.y && curY < this.y+MENU_HEIGHT && !this.isDragging) {
			this.isDragging = true;
			this.curXStored = curX;
			this.curYStored = curY;
			this.draggingOffsetX = curX - this.x;
			this.draggingOffsetY = curY - this.y;
		}
		if (isDragging) {
			this.x = curX - this.draggingOffsetX;
			this.y = curY - this.draggingOffsetY;
		}
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
		this.fill(stack, this.x+0.01, this.y+0.01, this.x+this.width+BORDER_WIDTH*2+0.01, this.y+this.height+MENU_HEIGHT+BORDER_HEIGHT+0.01, 0xAA000000);
		this.fill(stack, this.x, this.y, this.x+this.width+BORDER_WIDTH*2, this.y+this.height+MENU_HEIGHT+BORDER_HEIGHT, BORDER_COLOR);
		this.fill(stack, this.x+BORDER_WIDTH, this.y+MENU_HEIGHT, this.x+this.width+BORDER_WIDTH, this.y+this.height+MENU_HEIGHT, BACKGROUND_COLOR);
		// X Hover
		if (curX > this.x+this.width-BORDER_WIDTH*5 && curX < this.x+this.width+BORDER_WIDTH*2 && curY > this.y && curY < this.y+0.03) {
			this.fill(stack, this.x+this.width-0.0135, this.y+.005, this.x+this.width, y+0.0275, BACKGROUND_COLOR);
			showXShadow = true;
		} else {
			showXShadow = false;
		}
		// Render Title and X
		this.draw(stack, this.title, this.x+0.006, this.y+0.007, 20, TITLE_COLOR, false);
		this.draw(stack, X_TEXT, this.x+width-BORDER_WIDTH*3.5, this.y+0.0065, 20, TITLE_COLOR, showXShadow);
		super.render(stack, curX, curY);
	}
	
}
