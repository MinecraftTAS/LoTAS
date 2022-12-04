package de.pfannekuchen.lotas.gui;

import java.awt.Color;
import java.time.Duration;

import org.lwjgl.glfw.GLFW;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.Keyboard;
import de.pfannekuchen.lotas.core.utils.Timer;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

/**
 * The LoTAS pause screen
 * 
 * @author Scribble
 * @since 2.0.5
 */
public class LoTASIngameGui {

	private EditBox savestateNameField;
	private EditBox tickrateField;

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

	private SmallCheckboxWidget avoidDamageCheckbox;

	private SmallCheckboxWidget dropAwayCheckbox;
	private SmallCheckboxWidget dropTowardsMeCheckbox;

	private SmallCheckboxWidget optimizeExplosionsCheckbox;

	private SmallCheckboxWidget rightAutoClickerCheckbox;

	private Button timerButton;

	// =============== STUFF

	private Screen parentScreen;

	private Font fontRenderer;

	/**
	 * Whether a small tutorial should be displayed on how to use the tickJump
	 */
	private static boolean tickjumpText = true;

	private boolean tickrateFail = false;

	/**
	 * Constructs new buttons for LoTAS' gui
	 * @param screen The current screen
	 */
	public LoTASIngameGui(Screen screen) {

		this.parentScreen = screen;

		Minecraft mc = Minecraft.getInstance();

		fontRenderer = mc.font;

		int width = screen.width;
		int height = screen.height;

		// =============== SAVESTATE

		savestateButton = constructSavestateButton();

		loadstateButton = constructLoadstateButton();

		// =============== TICKRATE

		tickrateIncreaseButton = MCVer.Button(5, 15, 48, 20, "+", btn -> {
			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				activateTickrateField(true);
			} else {
				TickrateChangerMod.index++;
				TickrateChangerMod.index = Mth.clamp(TickrateChangerMod.index, 1, 10);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		});

		tickrateDecreaseButton = MCVer.Button(55, 15, 48, 20, "-", btn -> {
			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				activateTickrateField(true);
			} else {
				TickrateChangerMod.index--;
				TickrateChangerMod.index = Mth.clamp(TickrateChangerMod.index, 1, 10);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		});

		// =============== MANIPULATION

		dropButton = MCVer.Button((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, "Manipulate Drops", btn -> {
			mc.setScreen(new DropManipulationScreen((PauseScreen) parentScreen));
		});

		dragonButton = MCVer.Button((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> {
			mc.setScreen(new DragonManipulationScreen((PauseScreen) parentScreen));
		});

		spawningButton = MCVer.Button((width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, "Manipulate Spawning", btn -> {
			mc.setScreen(new SpawnManipulationScreen());
		});

		aiButton = MCVer.Button((width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, "Manipulate AI", btn -> {
			mc.setScreen(new AIManipulationScreen());
		});

		// =============== DUPEMOD

		saveItemsButton = MCVer.Button(5, 55, 98, 20, "Save Items", btn -> {
			DupeMod.save(Minecraft.getInstance());
			btn.active = false;
		});

		loadItemsButton = MCVer.Button(5, 77, 98, 20, "Load Items", btn -> {
			DupeMod.load(Minecraft.getInstance());
			btn.active = false;
		});

		// =============== JUMP TICKS

		jumpTicksButton = MCVer.Button(37, 115, 66, 20, "Jump ticks", btn -> {
			TickrateChangerMod.ticksToJump = (int) TickrateChangerMod.ticks[TickrateChangerMod.ji];
			btn.active = false;
			tickDisplayButton.active = false;
			MCVer.setMessage(btn, "Jumping...");
			tickjumpText = false;
		});

		tickDisplayButton = MCVer.Button(5, 115, 30, 20, TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t", btn -> {
			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
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

		// =============== Checkboxes

		avoidDamageCheckbox = new SmallCheckboxWidget(2, height - 20 - 15, "Avoid taking damage", !ConfigUtils.getBoolean("tools", "takeDamage"), box -> {
			ConfigUtils.setBoolean("tools", "takeDamage", !box.isChecked());
			ConfigUtils.save();
		});

		dropTowardsMeCheckbox = new SmallCheckboxWidget(2, height - 32 - 15, "Drop towards me", ConfigUtils.getBoolean("tools", "manipulateVelocityTowards"), box -> {
			ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", box.isChecked());
			if (box.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityAway", false);
				dropAwayCheckbox.silentPress(false);
			}
			ConfigUtils.save();
		});

		dropAwayCheckbox = new SmallCheckboxWidget(2, height - 44 - 15, "Drop away from me", ConfigUtils.getBoolean("tools", "manipulateVelocityAway"), box -> {
			ConfigUtils.setBoolean("tools", "manipulateVelocityAway", box.isChecked());
			if (box.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", false);
				dropTowardsMeCheckbox.silentPress(false);
			}
			ConfigUtils.save();
		});

		optimizeExplosionsCheckbox = new SmallCheckboxWidget(2, height - 56 - 15, "Optimize Explosions", ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance"), box -> {
			ConfigUtils.setBoolean("tools", "manipulateExplosionDropChance", box.isChecked());
			ConfigUtils.save();
		});

		rightAutoClickerCheckbox = new SmallCheckboxWidget(2, height - 68 - 15, "Right Auto Clicker", ConfigUtils.getBoolean("tools", "rAutoClicker"), box -> {
			ConfigUtils.setBoolean("tools", "rAutoClicker", box.isChecked());
			ConfigUtils.save();
		});

		// =============== TIMER

		timerButton = MCVer.Button(width / 2 - 102, height / 4 + 144 + -16, 204, 20, "Reset Timer", btn -> {
			Timer.ticks = -1;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
		});
	}

	public void addCustomButtons() {

		dragonButton.active = MCVer.getCurrentLevel().getDragons().size() > 0; // Disable the button if no dragon is in the current level

		aiButton.active = AIManipMod.isEntityInRange(); // Disable if no entity is in range

		MCVer.addButton(parentScreen, savestateButton);
		MCVer.addButton(parentScreen, loadstateButton);

		MCVer.addButton(parentScreen, tickrateIncreaseButton);
		MCVer.addButton(parentScreen, tickrateDecreaseButton);

		MCVer.addButton(parentScreen, dropButton);
		MCVer.addButton(parentScreen, dragonButton);
		MCVer.addButton(parentScreen, spawningButton);
		MCVer.addButton(parentScreen, aiButton);

		MCVer.addButton(parentScreen, saveItemsButton);
		MCVer.addButton(parentScreen, loadItemsButton);

		MCVer.addButton(parentScreen, jumpTicksButton);
		MCVer.addButton(parentScreen, tickDisplayButton);

		MCVer.addButton(parentScreen, avoidDamageCheckbox);
		MCVer.addButton(parentScreen, dropTowardsMeCheckbox);
		MCVer.addButton(parentScreen, dropAwayCheckbox);
		MCVer.addButton(parentScreen, optimizeExplosionsCheckbox);
		MCVer.addButton(parentScreen, rightAutoClickerCheckbox);

		MCVer.addButton(parentScreen, timerButton);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		// Tickratechanger text
		MCVer.drawShadow("Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);

		// Duping text
		MCVer.drawShadow("Duping", 10, 45, 0xFFFFFF);

		// Tickjump stuff
		MCVer.drawShadow("Tickjump", 10, 105, 0xFFFFFF);
		if (jumpTicksButton.active == false) {
			MCVer.drawShadow("Tickjump is ready,", 8, 137, 0xFFFFFF);
			MCVer.drawShadow("press ESC to continue", 8, 147, 0xFFFFFF);
		}

		boolean isShiftDown = Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT);

		// Show Tickjump number
		String color = isShiftDown ? "\u00A76" : "";
		
		//#if MC>=11900
//$$ 		tickDisplayButton.setMessage(net.minecraft.network.chat.Component.literal(color + TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t"));
		//#else
		//#if MC>=11601
//$$ 		tickDisplayButton.setMessage(new net.minecraft.network.chat.TextComponent(color + TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t"));
		//#else
		tickDisplayButton.setMessage(color + TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t");
		//#endif
		//#endif

		if (isShiftDown && tickjumpText) {
			MCVer.drawCenteredString(parentScreen, "\u00A7a^^^^^^^^", 70, 139, 0xFFFFFF);
			MCVer.drawCenteredString(parentScreen, String.format("\u00A7aReopens the Game Menu"), 70, 145, 0xFFFFFF);
			MCVer.drawCenteredString(parentScreen, String.format("\u00A7aafter %s ticks", TickrateChangerMod.ticks[TickrateChangerMod.ji]), 70, 155, 0xFFFFFF);
		}

		// Draw shift tooltip
		if (isShiftDown) {
			MCVer.setMessage(savestateButton, "\u00A76Name Savestate"); // Make the text gpolden on shift
			MCVer.setMessage(loadstateButton, "\u00A76Choose State");
			MCVer.setMessage(tickrateIncreaseButton, "\u00A76Custom");
			MCVer.setMessage(tickrateDecreaseButton, "\u00A76Tickrate");
		} else {
			MCVer.setMessage(savestateButton, "Savestate");
			MCVer.setMessage(loadstateButton, "Loadstate");
			MCVer.setMessage(tickrateIncreaseButton, "+");
			MCVer.setMessage(tickrateDecreaseButton, "-");
		}

		int width = parentScreen.width;
		int height = parentScreen.height;

		MCVer.drawCenteredString(parentScreen, "Hold Shift to access more features", width / 2, height / 4 + 152, 0xFFFFFF);

		// Render Edit boxes
		if (savestateNameField != null) {
			MCVer.render(savestateNameField, mouseX, mouseY, partialTicks);
			if (savestateNameField.getValue().isEmpty()) {
				//#if MC>=11903
//$$ 				MCVer.drawCenteredString(parentScreen, "Press \u2936 to apply", savestateNameField.getX() + 47, savestateNameField.getY() + 4, 0x777777);
				//#else
				MCVer.drawCenteredString(parentScreen, "Press \u2936 to apply", savestateNameField.x + 47, savestateNameField.y + 4, 0x777777);
				//#endif
			}
		}
		if (tickrateField != null) {
			MCVer.render(tickrateField, mouseX, mouseY, partialTicks);
			if (tickrateField.getValue().isEmpty()) {
				//#if MC>=11903
//$$ 				MCVer.drawCenteredString(parentScreen, "Press \u2936 to apply", tickrateField.getX() + 47, tickrateField.getY() + 4, 0x777777);
				//#else
				MCVer.drawCenteredString(parentScreen, "Press \u2936 to apply", tickrateField.x + 47, tickrateField.y + 4, 0x777777);
				//#endif
			}
		}

		// Render Savestate text
		if (SavestateMod.showSavestateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showSavestateDone = false;
				loadstateButton.active = SavestateMod.hasSavestate();
				return;
			}
			MCVer.drawCenteredString(parentScreen, "\u00A76Savestate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		} else if (SavestateMod.showLoadstateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showLoadstateDone = false;
				return;
			}
			MCVer.drawCenteredString(parentScreen, "\u00A76Loadstate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		}

		// Render tickrate fail
		if (tickrateFail) {
			MCVer.drawCenteredString(parentScreen, "\u00A7cPlease enter a number!", 170, 22, 0xFFFFFF);
		}
	}

	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (savestateNameField != null) {
			if (!savestateNameField.isMouseOver(mouseX, mouseY)) {
				activateSavestateField(false);
			} else {
				savestateNameField.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}

		if (tickrateField != null) {
			if (!tickrateField.isMouseOver(mouseX, mouseY)) {
				activateTickrateField(false);
			} else {
				tickrateField.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		
		boolean somethingFocused = false;
		
		if (savestateNameField != null) {
			savestateNameField.keyPressed(keyCode, scanCode, modifiers);
			
			boolean focused = savestateNameField.isFocused();
			somethingFocused = focused;
			
			if (keyCode == GLFW.GLFW_KEY_ENTER && focused) {
				
				if (savestateNameField.getValue().isEmpty()) {
					SavestateMod.savestate(null);
				} else {
					SavestateMod.savestate(savestateNameField.getValue());
				}
				
				activateSavestateField(false);
			}
		}
		
		if (tickrateField != null) {
			tickrateField.keyPressed(keyCode, scanCode, modifiers);
			
			boolean focused = tickrateField.isFocused();
			somethingFocused = focused;
			
			if (keyCode == GLFW.GLFW_KEY_ENTER && focused) {
				
				if (!tickrateField.getValue().isEmpty()) {
					try {
						TickrateChangerMod.updateTickrate(Float.parseFloat(tickrateField.getValue()));
						activateTickrateField(false);
					} catch (NumberFormatException e) {
						tickrateFail = true;
					}
				}
			}
		}
		return somethingFocused;
	}

	public void charTyped(char typedChar, int keyCode) {
		if (savestateNameField != null)
			savestateNameField.charTyped(typedChar, keyCode);
		if (tickrateField != null)
			tickrateField.charTyped(typedChar, keyCode);
	}

	private void activateTickrateField(boolean activate) {
		if (activate) {
			tickrateField = MCVer.EditBox(fontRenderer, 7, 17, 94, 16, "");
			tickrateIncreaseButton.active = false;
			tickrateDecreaseButton.active = false;
			tickrateIncreaseButton.visible = false;
			tickrateDecreaseButton.visible = false;
			parentScreen.setFocused(tickrateField);
			tickrateField.setFocus(true);
		} else {
			tickrateField = null;
			tickrateFail = false;
			tickrateIncreaseButton.active = true;
			tickrateDecreaseButton.active = true;
			tickrateIncreaseButton.visible = true;
			tickrateDecreaseButton.visible = true;
		}
	}

	private void activateSavestateField(boolean activate) {
		if (activate) {
			savestateNameField = MCVer.EditBox(fontRenderer, parentScreen.width / 2 - 100, parentScreen.height / 4 + 82, 94, 16, "");
			savestateButton.active = false;
			savestateButton.visible = false;
			parentScreen.setFocused(savestateNameField);
			savestateNameField.setFocus(true);
		} else {
			savestateNameField = null;
			savestateButton.active = true;
			savestateButton.visible = true;
		}
	}
	
	public Button constructSavestateButton() {
		//#if MC>=11903
//$$ 		return net.minecraft.client.gui.components.Button.builder(net.minecraft.network.chat.Component.literal("Savestate"), btn -> {
//$$ 			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
//$$ 				activateSavestateField(true);
//$$ 			} else
//$$ 				SavestateMod.savestate(null);
//$$ 		}).width(98).build();
		//#else
		return MCVer.Button(parentScreen.width / 2 - 102, parentScreen.height / 4 + 48 + -16 + 24 + 24, 98, 20, "Savestate", btn -> {
			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				activateSavestateField(true);
			} else
				SavestateMod.savestate(null);
		});
		//#endif
	}
	
	public Button constructLoadstateButton() {
		//#if MC>=11903
//$$ 		return net.minecraft.client.gui.components.Button.builder(net.minecraft.network.chat.Component.literal("Loadstate"), btn -> {
//$$ 			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT))
//$$ 				Minecraft.getInstance().setScreen(new LoadstateScreen());
//$$ 			else
//$$ 				SavestateMod.loadstate(-1);
//$$ 		}).width(98).build();
		//#else
		return MCVer.Button(parentScreen.width / 2 + 4, parentScreen.height / 4 + 48 + -16 + 24 + 24, 98, 20, "Loadstate", btn -> {
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT))
			Minecraft.getInstance().setScreen(new LoadstateScreen());
		else
			SavestateMod.loadstate(-1);
		});
		//#endif
	}
	
	public Button getSavestateButton() {
		return savestateButton;
	}

	public AbstractWidget getLoadstateButton() {
		return loadstateButton;
	}
}
