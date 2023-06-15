package de.pfannekuchen.lotas.gui;

import java.awt.Color;
import java.time.Duration;

import org.lwjgl.glfw.GLFW;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
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
import net.minecraft.client.resources.language.I18n;
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
			if (Screen.hasShiftDown()) {
				activateTickrateField(true);
			} else {
				TickrateChangerMod.index++;
				TickrateChangerMod.index = Mth.clamp(TickrateChangerMod.index, 1, 10);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		});

		tickrateDecreaseButton = MCVer.Button(55, 15, 48, 20, "-", btn -> {
			if (Screen.hasShiftDown()) {
				activateTickrateField(true);
			} else {
				TickrateChangerMod.index--;
				TickrateChangerMod.index = Mth.clamp(TickrateChangerMod.index, 1, 10);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		});

		// =============== MANIPULATION

		dropButton = MCVer.Button((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, I18n.get("pausegui.lotas.dropmanip"), btn -> { //"Manipulate Drops"
			mc.setScreen(new DropManipulationScreen((PauseScreen) parentScreen));
		});

		dragonButton = MCVer.Button((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, I18n.get("pausegui.lotas.dragonmanip"), btn -> { //"Manipulate Dragon"
			mc.setScreen(new DragonManipulationScreen((PauseScreen) parentScreen));
		});

		spawningButton = MCVer.Button((width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, I18n.get("pausegui.lotas.spawnmanip"), btn -> { //"Manipulate Spawning"
			mc.setScreen(new SpawnManipulationScreen());
		});

		aiButton = MCVer.Button((width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, I18n.get("pausegui.lotas.aimanip"), btn -> {//"Manipulate AI"
			mc.setScreen(new AIManipulationScreen());
		});

		// =============== DUPEMOD

		saveItemsButton = MCVer.Button(5, 55, 98, 20, I18n.get("pausegui.lotas.duping.save"), btn -> {//"Save Items"
			DupeMod.save(Minecraft.getInstance());
			btn.active = false;
		});

		loadItemsButton = MCVer.Button(5, 77, 98, 20, I18n.get("pausegui.lotas.duping.load"), btn -> {//"Load Items"
			DupeMod.load(Minecraft.getInstance());
			btn.active = false;
		});

		// =============== JUMP TICKS

		jumpTicksButton = MCVer.Button(37, 115, 66, 20, I18n.get("pausegui.lotas.jump"), btn -> {//"Jump ticks"
			TickrateChangerMod.ticksToJump = (int) TickrateChangerMod.ticks[TickrateChangerMod.ji];
			btn.active = false;
			tickDisplayButton.active = false;
			MCVer.setMessage(btn, I18n.get("pausegui.lotas.jump.success"));//"Jumping..."
			tickjumpText = false;
		});

		tickDisplayButton = MCVer.Button(5, 115, 30, 20, TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t", btn -> {
			if (Screen.hasShiftDown()) {
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

		avoidDamageCheckbox = new SmallCheckboxWidget(2, height - 20 - 15, I18n.get("pausegui.lotas.checkbox.invincible"), !ConfigUtils.getBoolean("tools", "takeDamage"), box -> {//"Avoid taking damage"
			ConfigUtils.setBoolean("tools", "takeDamage", !box.isChecked());
			ConfigUtils.save();
		});

		dropTowardsMeCheckbox = new SmallCheckboxWidget(2, height - 32 - 15, I18n.get("pausegui.lotas.checkbox.dropToMe"), ConfigUtils.getBoolean("tools", "manipulateVelocityTowards"), box -> {//"Drop towards me"
			ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", box.isChecked());
			if (box.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityAway", false);
				dropAwayCheckbox.silentPress(false);
			}
			ConfigUtils.save();
		});

		dropAwayCheckbox = new SmallCheckboxWidget(2, height - 44 - 15, I18n.get("pausegui.lotas.checkbox.dropAway"), ConfigUtils.getBoolean("tools", "manipulateVelocityAway"), box -> {//"Drop away from me"
			ConfigUtils.setBoolean("tools", "manipulateVelocityAway", box.isChecked());
			if (box.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", false);
				dropTowardsMeCheckbox.silentPress(false);
			}
			ConfigUtils.save();
		});

		optimizeExplosionsCheckbox = new SmallCheckboxWidget(2, height - 56 - 15, I18n.get("pausegui.lotas.checkbox.explosion"), ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance"), box -> {//"Optimize Explosions"
			ConfigUtils.setBoolean("tools", "manipulateExplosionDropChance", box.isChecked());
			ConfigUtils.save();
		});

		rightAutoClickerCheckbox = new SmallCheckboxWidget(2, height - 68 - 15, I18n.get("pausegui.lotas.checkbox.autoclicker"), ConfigUtils.getBoolean("tools", "rAutoClicker"), box -> {//"Right Auto Clicker"
			ConfigUtils.setBoolean("tools", "rAutoClicker", box.isChecked());
			ConfigUtils.save();
		});

		// =============== TIMER

		timerButton = MCVer.Button(width / 2 - 102, height / 4 + 144 + -16, 204, 20, I18n.get("pausegui.lotas.resettimer"), btn -> {//"Reset Timer"
			Timer.ticks = -1;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
		});
	}

	public void addCustomButtons() {

		dragonButton.active = MCVer.getCurrentLevel().getDragons().size() > 0; // Disable the button if no dragon is in the current level

		aiButton.active = AIManipMod.isEntityInRange(); // Disable if no entity is in range
		
		loadstateButton.active = SavestateMod.hasSavestate();

		MCVer.addButton(parentScreen, savestateButton);
		MCVer.addButton(parentScreen, loadstateButton);

		MCVer.addButton(parentScreen, tickrateIncreaseButton);
		MCVer.addButton(parentScreen, tickrateDecreaseButton);

		//#if MC<12000
		MCVer.addButton(parentScreen, dropButton);
		//#endif
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
		MCVer.drawShadow(I18n.get("pausegui.lotas.category.tickratechanger", TickrateChangerMod.tickrate), 5, 5, 0xFFFFFF);//"Tickrate Changer (%s)"

		// Duping text
		MCVer.drawShadow(I18n.get("pausegui.lotas.category.duping"), 10, 45, 0xFFFFFF);//"Duping"

		// Tickjump stuff
		MCVer.drawShadow(I18n.get("pausegui.lotas.category.jump"), 10, 105, 0xFFFFFF);//"Tickjump"
		if (jumpTicksButton.active == false) {
			MCVer.drawShadow(I18n.get("pausegui.lotas.jump.ready.1"), 8, 137, 0xFFFFFF);//"Tickjump is ready,"
			MCVer.drawShadow(I18n.get("pausegui.lotas.jump.ready.2"), 8, 147, 0xFFFFFF);//"press ESC to continue"
		}

		boolean isShiftDown = Screen.hasShiftDown();

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

		// Hint
		if (isShiftDown && tickjumpText && jumpTicksButton.active) {
			MCVer.drawCenteredString(parentScreen, "\u00A7a^^^^^^^^", 70, 139, 0xFFFFFF);
			MCVer.drawCenteredString(parentScreen, String.format(I18n.get("pausegui.lotas.jump.tutorial.1")), 70, 145, 0xFFFFFF);//"\u00A7aReopens the Game Menu"
			MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.jump.tutorial.2", TickrateChangerMod.ticks[TickrateChangerMod.ji]), 70, 155, 0xFFFFFF);//"\u00A7aafter %s ticks"
		}

		// Draw shift tooltip
		if (isShiftDown) {
			MCVer.setMessage(savestateButton, I18n.get("pausegui.lotas.buttontext.shift.savestate")); // Make the text golden on shift "\u00A76Name Savestate"
			MCVer.setMessage(loadstateButton, I18n.get("pausegui.lotas.buttontext.shift.loadstate"));//"\u00A76Choose State"
			MCVer.setMessage(tickrateIncreaseButton, I18n.get("pausegui.lotas.buttontext.shift.trc1"));//"\u00A76Custom"
			MCVer.setMessage(tickrateDecreaseButton, I18n.get("pausegui.lotas.buttontext.shift.trc2"));//"\u00A76Tickrate"
		} else {
			MCVer.setMessage(savestateButton, I18n.get("pausegui.lotas.buttontext.unshift.savestate"));//"Savestate"
			MCVer.setMessage(loadstateButton, I18n.get("pausegui.lotas.buttontext.unshift.loadstate"));//"Loadstate"
			MCVer.setMessage(tickrateIncreaseButton, "+");
			MCVer.setMessage(tickrateDecreaseButton, "-");
		}

		int width = parentScreen.width;
		int height = parentScreen.height;

		MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.shifttext"), width / 2, height / 4 + 152, 0xFFFFFF);//"Hold Shift to access more features"

		// Render Edit boxes
		if (savestateNameField != null) {
			MCVer.render(savestateNameField, mouseX, mouseY, partialTicks);
			if (savestateNameField.getValue().isEmpty()) {
				//#if MC>=11903
//$$ 				MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.textfield.apply"), savestateNameField.getX() + 47, savestateNameField.getY() + 4, 0x777777);//"Press \u2936 to apply"
				//#else
				MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.textfield.apply"), savestateNameField.x + 47, savestateNameField.y + 4, 0x777777);//"Press \u2936 to apply"
				//#endif
			}
		}
		if (tickrateField != null) {
			MCVer.render(tickrateField, mouseX, mouseY, partialTicks);
			if (tickrateField.getValue().isEmpty()) {
				//#if MC>=11903
//$$ 				MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.textfield.apply"), tickrateField.getX() + 47, tickrateField.getY() + 4, 0x777777);//"Press \u2936 to apply"
				//#else
				MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.textfield.apply"), tickrateField.x + 47, tickrateField.y + 4, 0x777777);//"Press \u2936 to apply"
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
			MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.successtext.savestate"), width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());//"\u00A76Savestate successful..."
		} else if (SavestateMod.showLoadstateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showLoadstateDone = false;
				return;
			}
			MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.successtext.loadstate"), width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());//"\u00A76Loadstate successful..."
		}

		// Render tickrate fail
		if (tickrateFail) {
			MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.textfield.fail"), 170, 22, 0xFFFFFF);//"\u00A7cPlease enter a number!"
		}
		
		//#if MC>=12000
//$$  		MCVer.drawCenteredString(parentScreen, I18n.get("pausegui.lotas.dropmanip.vanish"), 80, height-15, 0xFFFFFF);//"\u00A7oRIP DropManipulation!"
		//#endif
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
			
			//#if MC>=11904
//$$ 			tickrateField.setFocused(true);
			//#else
			tickrateField.setFocus(true);
			//#endif
			
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
			//#if MC>=11904
//$$ 			savestateNameField.setFocused(true);
			//#else
			savestateNameField.setFocus(true);
			//#endif
		} else {
			savestateNameField = null;
			savestateButton.active = true;
			savestateButton.visible = true;
		}
	}
	
	public Button constructSavestateButton() {
		//#if MC>=11903
//$$ 		return net.minecraft.client.gui.components.Button.builder(net.minecraft.network.chat.Component.literal(I18n.get("pausegui.lotas.buttontext.unshift.savestate")), btn -> {//"Savestate"
//$$ 			if (Screen.hasShiftDown()) {
//$$ 				activateSavestateField(true);
//$$ 			} else
//$$ 				SavestateMod.savestate(null);
//$$ 		}).width(98).build();
		//#else
		return MCVer.Button(parentScreen.width / 2 - 102, parentScreen.height / 4 + 48 + -16 + 24 + 24, 98, 20, I18n.get("pausegui.lotas.buttontext.unshift.savestate"), btn -> {//"Savestate"
			if (Screen.hasShiftDown()) {
				activateSavestateField(true);
			} else
				SavestateMod.savestate(null);
		});
		//#endif
	}
	
	public Button constructLoadstateButton() {
		//#if MC>=11903
//$$ 		return net.minecraft.client.gui.components.Button.builder(net.minecraft.network.chat.Component.literal(I18n.get("pausegui.lotas.buttontext.unshift.loadstate")), btn -> {//"Loadstate"
//$$ 			if (Screen.hasShiftDown())
//$$ 				Minecraft.getInstance().setScreen(new LoadstateScreen());
//$$ 			else
//$$ 				SavestateMod.loadstate(-1);
//$$ 		}).width(98).build();
		//#else
		return MCVer.Button(parentScreen.width / 2 + 4, parentScreen.height / 4 + 48 + -16 + 24 + 24, 98, 20, I18n.get("pausegui.lotas.buttontext.unshift.loadstate"), btn -> {//"Loadstate"
		if (Screen.hasShiftDown())
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
