package de.pfannekuchen.lotas.gui;

import java.awt.Color;
import java.time.Duration;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicates;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorGuiScreen;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.fml.client.config.GuiCheckBox;

/**
 * The LoTAS pause screen
 * 
 * @author Scribble
 * @since 2.0.5
 */
public class LoTASGuiIngameMenu {

	private GuiTextField savestateNameField;
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

	private CheckBox avoidDamageCheckbox;

	private CheckBox dropAwayCheckbox;
	private CheckBox dropTowardsMeCheckbox;

	private CheckBox optimizeExplosionsCheckbox;

	private CheckBox rightAutoClickerCheckbox;

	private Button timerButton;
	
	// =============== STUFF
	
	GuiScreen parentScreen;
	
	FontRenderer fontRenderer;
	
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
		
		this.fontRenderer = MCVer.getFontRenderer(mc);
		
		int width = screen.width;
		int height = screen.height;
		
		// =============== SAVESTATE
		
		savestateButton = new Button(-1, width / 2 - 100, height / 4 + 96 + -16, 98, 20, "Savestate", btn->{
			if (GuiScreen.isShiftKeyDown()) {
				activateSavestateField(true);
			} else SavestateMod.savestate(null);
		});
		
		loadstateButton = new Button(-2, width / 2 + 2, height / 4 + 96 + -16, 98, 20, "Loadstate", btn ->{
			if (GuiScreen.isShiftKeyDown()) mc.displayGuiScreen(new GuiLoadstateMenu());
			else SavestateMod.loadstate(-1);
		});
		
		// =============== TICKRATE
		
		tickrateIncreaseButton = new Button(-3, 5, 15, 48, 20, "+", btn ->{
			if (GuiScreen.isShiftKeyDown()) {
				activateTickrateField(true);
			} else {
				TickrateChangerMod.index++;
				TickrateChangerMod.index = MCVer.clamp(TickrateChangerMod.index, 0, 11);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		});
		
		tickrateDecreaseButton = new Button(-4, 55, 15, 48, 20, "-", btn ->{
			if (GuiScreen.isShiftKeyDown()) {
				activateTickrateField(true);
			} else {
				TickrateChangerMod.index--;
				TickrateChangerMod.index = MCVer.clamp(TickrateChangerMod.index, 0, 11);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		});
		
		// =============== MANIPULATION
		
		dropButton = new Button(-5, (width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, I18n.format("menu.lotas.dropmanip"), btn->{ // "Manipulate Drops"
			Minecraft.getMinecraft().displayGuiScreen(new GuiDropChanceManipulation((GuiIngameMenu) parentScreen));
		});
		
		dragonButton = new Button(-6, (width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, I18n.format("menu.lotas.dragonmanip"), btn->{ // "Manipulate Dragon"
			//#if MC>=10900
			Minecraft.getMinecraft().displayGuiScreen(new GuiDragonManipulation((GuiIngameMenu) parentScreen));
			//#else
//$$ 			btn.enabled=false;
//$$ 			GuiDragonManipulation.chargePlayer();
			//#endif
		});
		
		spawningButton = new Button(-7, (width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, "Manipulate Spawning", btn->{
			Minecraft.getMinecraft().displayGuiScreen(new GuiEntitySpawnManipulation());
		});
		
		aiButton = new Button(-8, (width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, "Manipulate AI", btn ->{
			Minecraft.getMinecraft().displayGuiScreen(new GuiAiManipulation());
		});
		
		// =============== DUPEMOD
		
		saveItemsButton = new Button(-9, 5, 55, 98, 20, "Save Items", btn->{
			DupeMod.saveItems();
			DupeMod.saveChests();
			btn.enabled = false;
		});
		
		loadItemsButton = new Button(-10, 5, 77, 98, 20, "Load Items", btn->{
			DupeMod.loadItems();
			DupeMod.loadChests();
			btn.enabled = false;
		});
		
		// =============== JUMP TICKS
		
		jumpTicksButton = new Button(-11, 37, 115, 66, 20, "Jump ticks", btn->{
			TickrateChangerMod.ticksToJump = (int) TickrateChangerMod.ticks[TickrateChangerMod.ji];
			btn.enabled = false;
			btn.displayString = "Jumping...";
		});
		
		tickDisplayButton = new Button(-12, 5, 115, 30, 20, ((int) TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t"), btn ->{
			if (GuiIngameMenu.isShiftKeyDown()) {
				TickrateChangerMod.ji--;
			} else {
				TickrateChangerMod.ji++;
			}

			int upper = 9;
			int lower = 2;

			if (TickrateChangerMod.ji > upper)
				TickrateChangerMod.ji = lower;

			else if (TickrateChangerMod.ji < lower)
				TickrateChangerMod.ji = upper;
		});
		
		// =============== CHECKBOXES
		
		avoidDamageCheckbox = new CheckBox(22, 2, height - 20 - 15, "Avoid taking damage", !ConfigUtils.getBoolean("tools", "takeDamage"), btn->{
			ConfigUtils.setBoolean("tools", "takeDamage", !((GuiCheckBox) btn).isChecked());
			ConfigUtils.save();
		});
		
		dropTowardsMeCheckbox = new CheckBox(26, 2, height - 32 - 15, "Drop towards me", ConfigUtils.getBoolean("tools", "manipulateVelocityTowards"), btn ->{
			if (btn.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityAway", false);
				ConfigUtils.save();
				dropAwayCheckbox.setIsChecked(false);
			}
			ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", ((GuiCheckBox) btn).isChecked());
			ConfigUtils.save();
		});
		
		dropAwayCheckbox = new CheckBox(27, 2, height - 44 - 15, "Drop away from me", ConfigUtils.getBoolean("tools", "manipulateVelocityAway"), btn ->{
			if (btn.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", false);
				ConfigUtils.save();
				dropTowardsMeCheckbox.setIsChecked(false);
			}
			ConfigUtils.setBoolean("tools", "manipulateVelocityAway", btn.isChecked());
			ConfigUtils.save();
		});
		
		optimizeExplosionsCheckbox = new CheckBox(28, 2, height - 56 - 15, "Optimize Explosions", ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance"), btn ->{
			ConfigUtils.setBoolean("tools", "manipulateExplosionDropChance", btn.isChecked());
			ConfigUtils.save();
		});
		
		rightAutoClickerCheckbox = new CheckBox(30, 2, height - 68 - 15, "Toggle R Auto Clicker", ConfigUtils.getBoolean("tools", "lAutoClicker"), btn ->{
			ConfigUtils.setBoolean("tools", "lAutoClicker", btn.isChecked());
			ConfigUtils.save();
		});
		
		// =============== TIMER
		
		timerButton = new Button(-12, width / 2 - 100, height / 4 + 144 + -16, "Reset Timer", btn ->{
			Timer.ticks = -1;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
		});
	}
	
	public void addButtons() {
		
		Minecraft mc  = Minecraft.getMinecraft();
		
		dragonButton.enabled = MCVer.world(mc.getIntegratedServer(), MCVer.player(mc).dimension).getEntities(EntityDragon.class, Predicates.alwaysTrue()).size() >= 1;
		
		aiButton.enabled = AIManipMod.isEntityInRange();
		
		loadstateButton.enabled = SavestateMod.hasSavestate();
		
		List<GuiButton> buttonList = ((AccessorGuiScreen)parentScreen).getButtonList();
		
		buttonList.add(savestateButton);
		buttonList.add(loadstateButton);
		
		buttonList.add(tickrateIncreaseButton);
		buttonList.add(tickrateDecreaseButton);
		 
		buttonList.add(dropButton);
		buttonList.add(dragonButton);
		buttonList.add(spawningButton);
		buttonList.add(aiButton);
		
		buttonList.add(saveItemsButton);
		buttonList.add(loadItemsButton);
		
		buttonList.add(jumpTicksButton);
		buttonList.add(tickDisplayButton);
		
		buttonList.add(avoidDamageCheckbox);
		buttonList.add(dropTowardsMeCheckbox);
		buttonList.add(dropAwayCheckbox);
		buttonList.add(optimizeExplosionsCheckbox);
		buttonList.add(rightAutoClickerCheckbox);
		
		buttonList.add(timerButton);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		Minecraft mc = Minecraft.getMinecraft();
		
		FontRenderer fontRenderer = MCVer.getFontRenderer(mc);
		
		// Tickratechanger text
		fontRenderer.drawString("Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);
		
		// Duping text
		fontRenderer.drawStringWithShadow("Duping", 10, 45, 0xFFFFFF);
		
		// Tickjump stuff
		fontRenderer.drawStringWithShadow("Tickjump", 10, 105, 0xFFFFFF);
		if(jumpTicksButton.enabled==false) {
			fontRenderer.drawStringWithShadow("Tickjump is ready,", 8, 137, 0xFFFFFF);
			fontRenderer.drawStringWithShadow("press ESC to continue", 8, 147, 0xFFFFFF);
		}
		
		boolean isShiftDown = GuiScreen.isShiftKeyDown();

		// Show Tickjump number
		String color = isShiftDown ? "\u00A76" : "";

		tickDisplayButton.displayString = color + TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t";
		
		if (isShiftDown && tickjumpText) {
			parentScreen.drawCenteredString(fontRenderer, "\u00A7a^^^^^^^^", 70, 139, 0xFFFFFF);
			parentScreen.drawCenteredString(fontRenderer, String.format("\u00A7aReopens the Game Menu"), 70, 145, 0xFFFFFF);
			parentScreen.drawCenteredString(fontRenderer, String.format("\u00A7aafter %s ticks", TickrateChangerMod.ticks[TickrateChangerMod.ji]), 70, 155, 0xFFFFFF);
		}
		
		// Draw shift tooltip
		if (isShiftDown) {
			savestateButton.displayString = "\u00A76Name Savestate";
			loadstateButton.displayString = "\u00A76Choose State";
			tickrateIncreaseButton.displayString = "\u00A76Custom";
			tickrateDecreaseButton.displayString = "\u00A76Tickrate";
		} else {
			savestateButton.displayString = "Savestate";
			loadstateButton.displayString = "Loadstate";
			tickrateIncreaseButton.displayString = "+";
			tickrateDecreaseButton.displayString = "-";
		}
		
		int width = parentScreen.width;
		int height = parentScreen.height;

		// Render Hint
		parentScreen.drawCenteredString(fontRenderer, "Hold Shift to access more features", width / 2, height / 4 + 150, 0xFFFFFF);
		
		// Render Edit boxes
		if (savestateNameField != null) {
			savestateNameField.drawTextBox();
			if (savestateNameField.getText().isEmpty()) {
				parentScreen.drawCenteredString(fontRenderer, "Press \u2936 to apply", x(savestateNameField) + 47, y(savestateNameField) + 4, 0x777777);
			}
		}
		if (tickrateField != null) {
			tickrateField.drawTextBox();
			if (tickrateField.getText().isEmpty()) {
				parentScreen.drawCenteredString(fontRenderer, "Press \u2936 to apply", x(tickrateField) + 47, y(tickrateField) + 4, 0x777777);
			}
		}
		
		// Render Savestate text
		if (SavestateMod.showSavestateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showSavestateDone = false;
				loadstateButton.enabled=SavestateMod.hasSavestate();
				return;
			}
			parentScreen.drawCenteredString(fontRenderer, "\u00A76Savestate successful...", width / 2, 40, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		} else if (SavestateMod.showLoadstateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showLoadstateDone = false;
				return;
			}
			parentScreen.drawCenteredString(fontRenderer, "\u00A76Loadstate successful...", width / 2, 40, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		}
		
		// Render tickrate fail
		if (tickrateFail) {
			parentScreen.drawCenteredString(fontRenderer, "\u00A7cPlease enter a number!", 170, 22, 0xFFFFFF);
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (savestateNameField != null) {
			if (!isMouseOver(savestateNameField, mouseX, mouseY)) {
				activateSavestateField(false);
			} else {
				savestateNameField.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		if (tickrateField != null) {
			if(!isMouseOver(tickrateField, mouseY, mouseButton)) {
				activateTickrateField(false);
			}else {
				tickrateField.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}
	
	public void keyTyped(char typedChar, int keyCode) {
		
		if (savestateNameField != null) {
			boolean focused = savestateNameField.isFocused();
			savestateNameField.textboxKeyTyped(typedChar, keyCode);
			
			if (keyCode == Keyboard.KEY_RETURN && focused) {
				if (savestateNameField.getText().isEmpty()) {
					SavestateMod.savestate(null);
				} else {
					SavestateMod.savestate(savestateNameField.getText());
				}
				
				activateSavestateField(false);
			}
		}

		if (tickrateField != null) {
			boolean focused = tickrateField.isFocused();
			tickrateField.textboxKeyTyped(typedChar, keyCode);
			if (keyCode == Keyboard.KEY_RETURN && focused) {
				if (!tickrateField.getText().isEmpty()) {
					try {
						TickrateChangerMod.updateTickrate(Float.parseFloat(tickrateField.getText()));
						activateTickrateField(false);
					} catch (NumberFormatException e) {
						tickrateFail = true;
					}
				}
			}
		}
		
	}
	
	public void actionPerformed(GuiButton button) {
		List<GuiButton> buttonList = ((AccessorGuiScreen)parentScreen).getButtonList();
		buttonList.forEach(clickables ->{
			if(clickables.equals(button)) {
				if(clickables instanceof CustomClickable) {
					((CustomClickable)clickables).press();
				}
			}
		});
	}

	private void activateTickrateField(boolean activate) {
		
		if (activate) {
			tickrateField = new GuiTextField(-30, fontRenderer, 7, 17, 94, 16);
			tickrateIncreaseButton.enabled = false;
			tickrateDecreaseButton.enabled = false;
			tickrateIncreaseButton.visible = false;
			tickrateDecreaseButton.visible = false;
			tickrateField.setFocused(true);
		} else {
			tickrateField = null;
			tickrateFail = false;
			tickrateIncreaseButton.enabled = true;
			tickrateDecreaseButton.enabled = true;
			tickrateIncreaseButton.visible = true;
			tickrateDecreaseButton.visible = true;
		}
	}

	private void activateSavestateField(boolean activate) {
		
		if (activate) {
			savestateNameField =  new GuiTextField(-31, fontRenderer, parentScreen.width / 2 - 98, parentScreen.height / 4 + 82, 94, 16);
			savestateButton.enabled = false;
			savestateButton.visible = false;
			savestateNameField.setFocused(true);
		} else {
			savestateNameField = null;
			savestateButton.enabled = true;
			savestateButton.visible = true;
		}
	}
	
	private boolean isMouseOver(GuiTextField button, double d, double e) {
		return button.getVisible() && 
				d >= (double) x(button) && 
				d < (double) (x(button) + button.width) && 
				e >= (double) y(button) && 
				e < (double) (y(button) + button.height);
	}
	
	private int x(GuiTextField field) {
		//#if MC>=11202
		return field.x;
		//#else
//$$ 		return field.xPosition;
		//#endif
	}
	
	private int y(GuiTextField field) {
		//#if MC>=11202
		return field.y;
		//#else
//$$ 		return field.yPosition;
		//#endif
	}
	
	public class Button extends GuiButton implements CustomClickable{

		private OnPress press;
		
		public Button(int buttonId, int x, int y, String buttonText, OnPress onPress) {
			this(buttonId, x, y, 200, 20, buttonText, onPress);
		}
		
		public Button(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, OnPress onPress) {
			super(buttonId, x, y, widthIn, heightIn, I18n.format(buttonText));
			this.press = onPress;
		}

		@Override
		public void press() {
			press.call(this);
		}
	}

	public class CheckBox extends GuiCheckBox implements CustomClickable {

		private OnPress2 press;
		
		public CheckBox(int id, int xPos, int yPos, String displayString, boolean isChecked, OnPress2 press) {
			super(id, xPos, yPos,  I18n.format(displayString), isChecked);
			
			this.press = press;
		}
		
		@Override
		public void press() {
			press.call(this);
		}
	}
	
	@FunctionalInterface
	public interface OnPress{
		public void call(Button button);
	}
	
	@FunctionalInterface
	private interface OnPress2{
		public void call(CheckBox button);
	}
	
	private interface CustomClickable{
		public void press();
	}
}
