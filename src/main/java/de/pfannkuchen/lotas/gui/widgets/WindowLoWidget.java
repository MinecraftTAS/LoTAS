package de.pfannkuchen.lotas.gui.widgets;

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
	
	// Window Sizes
	private TextComponent title;
	private double width;
	private double height;
	private double x = 0.2;
	private double y = 0.2;
	// Whether the close buttons should have a shadow
	private boolean showXShadow = false;
	
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
	protected void init() {}
	
	@Override
	protected void click(double curX, double curY, int button) {
		if (!active) return;
		if (curX > x+width-BORDER_WIDTH*5 && curX < x+width+BORDER_WIDTH*2 && curY > y && curY < y+0.03) {
			active = false;
		}
		isDragging = false;
	}
	
	boolean isDragging;
	double curXStored;
	double curYStored;
	double draggingOffsetX;
	double draggingOffsetY;
	
	@Override
	protected void drag(double prevCurX, double prevCurY, double curX, double curY) {
		if (!active) return;
		if (curX > x && curX < x+width && curY > y && curY < y+MENU_HEIGHT && !isDragging) {
			this.isDragging = true;
			this.curXStored = curX;
			this.curYStored = curY;
			this.draggingOffsetX = curX - x;
			this.draggingOffsetY = curY - y;
		}
		if (isDragging) {
			this.x = curX - this.draggingOffsetX;
			this.y = curY - this.draggingOffsetY;
		}
	}
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		if (!active) return;
		// Render Background and Border
		this.fill(stack, x+0.01, y+0.01, x+width+BORDER_WIDTH*2+0.01, y+height+MENU_HEIGHT+BORDER_HEIGHT+0.01, 0xAA000000);
		this.fill(stack, x, y, x+width+BORDER_WIDTH*2, y+height+MENU_HEIGHT+BORDER_HEIGHT, BORDER_COLOR);
		this.fill(stack, x+BORDER_WIDTH, y+MENU_HEIGHT, x+width+BORDER_WIDTH, y+height+MENU_HEIGHT, BACKGROUND_COLOR);
		// X Hover
		if (curX > x+width-BORDER_WIDTH*5 && curX < x+width+BORDER_WIDTH*2 && curY > y && curY < y+0.03) {
			this.fill(stack, x+width-0.0135, y+.005, x+width, y+0.0275, BACKGROUND_COLOR);
			showXShadow = true;
		} else {
			showXShadow = false;
		}
		// Render Title and X
		this.draw(stack, title, x+0.006, y+0.007, 20, TITLE_COLOR, false);
		this.draw(stack, X_TEXT, x+width-BORDER_WIDTH*3.5, y+0.0065, 20, TITLE_COLOR, showXShadow);
		super.render(stack, curX, curY);
	}
	
}
