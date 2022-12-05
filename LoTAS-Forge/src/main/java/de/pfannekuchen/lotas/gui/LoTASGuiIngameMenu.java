package de.pfannekuchen.lotas.gui;

import org.lwjgl.input.Keyboard;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class LoTASGuiIngameMenu {

	private GuiTextField savestateName;
	private GuiTextField tickrateField;
	
	// =============== BUTTONS
	private Button savestateButton;
	private Button loadstateButton;

	private Button tickrateIncreaseButton;
	private Button tickrateDecreaseButton;

	private Button dropButton;
	private Button dragonButton;
	private Button spawningButton;
	private Button aiButton;

	private Button saveItemsButton;
	private Button loadItemsButton;

	private Button jumpTicksButton;
	private Button tickDisplayButton;

	private GuiCheckBox avoidDamageCheckbox;

	private GuiCheckBox dropAwayCheckbox;
	private GuiCheckBox dropTowardsMeCheckbox;

	private GuiCheckBox optimizeExplosionsCheckbox;

	private GuiCheckBox rightAutoClickerCheckbox;

	private Button timerButton;
	
	// =============== STUFF
	
	GuiScreen parentScreen;
	
	private FontRenderer fontRenderer;
	
	/**
	 * Whether a small tutorial should be displayed on how to use the tickJump
	 */
	private static boolean tickjumpText = true;

	private boolean tickrateFail = false;
	
	/**
	 * Constructs new buttons for LoTAS' gui
	 * @param screen The current screen
	 */
	public LoTASGuiIngameMenu(GuiScreen screen) {
		
		this.parentScreen = screen;
		
		Minecraft mc = Minecraft.getMinecraft();
		
		fontRenderer = mc.fontRenderer;
		
		int width = screen.width;
		int height = screen.height;
		
		// =============== SAVESTATE
		
		savestateButton = new Button(13, width / 2 - 100, height / 4 + 96 + -16, 98, 20, I18n.format("Savestate"), (btn->{
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				savestateName = new GuiTextField(93, MCVer.getFontRenderer(mc), width / 2 - 100, height / 4 + 96 + -16, 98, 20);
				btn.enabled = false;
				savestateName.setFocused(true);
			} else SavestateMod.savestate(null);
		}));
	}
	
	
	public class Button extends GuiButton{

		private OnPress press;
		
		public Button(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, OnPress onPress) {
			super(buttonId, x, y, widthIn, heightIn, I18n.format(buttonText));
			this.press = onPress;
		}

		public void press() {
			press.call(this);
		}
	}
	
	@FunctionalInterface
	public interface OnPress{
		public void call(Button button);
	}
}
