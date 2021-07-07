package de.pfannekuchen.lotas.mixin.render.gui;

import java.awt.Color;
import java.time.Duration;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.core.utils.Keyboard;
import de.pfannekuchen.lotas.gui.AIManipulationScreen;
import de.pfannekuchen.lotas.gui.DragonManipulationScreen;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.LoadstateScreen;
import de.pfannekuchen.lotas.gui.SpawnManipulationScreen;
import net.minecraft.client.gui.components.Button;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

@Mixin(PauseScreen.class)
public abstract class MixinGuiIngameMenu extends Screen {
	protected MixinGuiIngameMenu(Component title) {
		super(title);
	}

	public EditBox savestateName;
	public EditBox tickrateField;

	SmallCheckboxWidget fw = null; // do not pay attention

	@Inject(at = @At("RETURN"), method = "init")
	public void addCustomButtons(CallbackInfo ci) {
		// Move Buttons higher
		for (net.minecraft.client.gui.components.AbstractWidget guiButton : buttons) 
			guiButton.y -= 24;
		
		buttons.get(7).y += 24;
		
		addButton(new Button(this.width / 2 - 102, this.height / 4 + 48 + -16 + 24 + 24, 98, 20, "Savestate", btn -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				savestateName = new EditBox(Minecraft.getInstance().font, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, "");
				btn.active = false;
				setFocused(savestateName);
			} else
				SavestateMod.savestate(null);
		}));
		addButton(new Button(this.width / 2 + 4, this.height / 4 + 48 + -16 + 24 + 24, 98, 20, "Loadstate", btn -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
				Minecraft.getInstance().setScreen(new LoadstateScreen());
			else
				SavestateMod.loadstate(-1);
		}));
		addButton(new Button(5, 15, 48, 20, "+", b -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				tickrateField = new EditBox(Minecraft.getInstance().font, 4, 15, 103, 20, "");
				b.active = false;
				setFocused(b);
			} else {
				TickrateChangerMod.index++;
				TickrateChangerMod.index = Mth.clamp(TickrateChangerMod.index, 1, 10);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		}));
		addButton(new Button(55, 15, 48, 20, "-", b -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				tickrateField = new EditBox(Minecraft.getInstance().font, 4, 15, 103, 20, "");
				b.active = false;
				setFocused(b);
			} else {
				TickrateChangerMod.index--;
				TickrateChangerMod.index = Mth.clamp(TickrateChangerMod.index, 1, 10);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		}));
		addButton(new Button((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, "Manipulate Drops", btn -> {
			Minecraft.getInstance().setScreen(new DropManipulationScreen((PauseScreen) (Object) this));
		}));

		addButton(new Button((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> {
			Minecraft.getInstance().setScreen(new DragonManipulationScreen((PauseScreen) (Object) this));
		}));
		
		addButton(new Button((width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, "Manipulate Spawning", btn -> {
			Minecraft.getInstance().setScreen(new SpawnManipulationScreen());
		}));
		
		addButton(new Button((width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, "Manipulate AI", btn -> {
			Minecraft.getInstance().setScreen(new AIManipulationScreen());
 		}));
		
		addButton(new Button(5, 55, 98, 20, "Save Items", btn -> {
			DupeMod.save(Minecraft.getInstance());
			btn.active = false;
		}));
		addButton(new Button(5, 75, 98, 20, "Load Items", btn -> {
			DupeMod.load(Minecraft.getInstance());
			btn.active = false;
		}));
		addButton(new Button(37, 115, 66, 20, "Jump ticks", btn -> {
			TickrateChangerMod.ticksToJump = (int) TickrateChangerMod.ticks[TickrateChangerMod.ji];
			btn.active = false;
			btn.setMessage("Jumping...");
		}));

		addButton(new Button(5, 115, 30, 20, TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t", btn -> {
			TickrateChangerMod.ji++;
			if (TickrateChangerMod.ji > 10)
				TickrateChangerMod.ji = 1;
			buttons.clear();
			init();
		}));
		addButton(new SmallCheckboxWidget(2, height - 20 - 15, "Avoid taking damage", !ConfigUtils.getBoolean("tools", "takeDamage"), b -> {
			ConfigUtils.setBoolean("tools", "takeDamage", !b.isChecked());
			ConfigUtils.save();
		}));

		addButton(new SmallCheckboxWidget(2, height - 32 - 15, "Drop towards me", ConfigUtils.getBoolean("tools", "manipulateVelocityTowards"), b -> {
			ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", b.isChecked());
			if (b.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityAway", false);
				fw.silentPress(false);
			}
			ConfigUtils.save();
		}));
		final SmallCheckboxWidget tw = (SmallCheckboxWidget) buttons.get(buttons.size() - 1);
		addButton(new SmallCheckboxWidget(2, height - 44 - 15, "Drop away from me", ConfigUtils.getBoolean("tools", "manipulateVelocityAway"), b -> {
			ConfigUtils.setBoolean("tools", "manipulateVelocityAway", b.isChecked());
			if (b.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", false);
				tw.silentPress(false);
			}
			ConfigUtils.save();
		}));
		fw = (SmallCheckboxWidget) buttons.get(buttons.size() - 1);
		addButton(new SmallCheckboxWidget(2, height - 56 - 15, "Optimize Explosions", ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance"), b -> {
			ConfigUtils.setBoolean("tools", "manipulateExplosionDropChance", b.isChecked());
			ConfigUtils.save();
		}));
		addButton(new SmallCheckboxWidget(2, height - 68 - 15, "Right Auto Clicker", ConfigUtils.getBoolean("tools", "rAutoClicker"), b -> {
			ConfigUtils.setBoolean("tools", "rAutoClicker", b.isChecked());
			ConfigUtils.save();
		}));
		addButton(new Button(this.width / 2 - 102, this.height / 4 + 144 + -16, 204, 20, "Reset Timer", btn -> {
			Timer.ticks = -1;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
		}));

	}

	public Button getButton(int index) {
		return (Button) this.buttons.get(index);
	}
	
	@Inject(method = "render", at = @At("TAIL"))
	public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			getButton(8).setMessage("\u00A76Name Savestate");
			getButton(9).setMessage("\u00A76Choose State");
			getButton(10).setMessage("\u00A76Custom");
			getButton(11).setMessage("\u00A76Tickrate");
		} else {
			getButton(8).setMessage("Savestate");
			getButton(9).setMessage("Loadstate");
			getButton(10).setMessage("+");
			getButton(11).setMessage("-");
		}
		drawCenteredString(Minecraft.getInstance().font, "Hold Shift to access more features", width / 2, this.height / 4 + 152, 0xFFFFFF);
		if (savestateName != null) savestateName.render(mouseX, mouseY, partialTicks);
		if (tickrateField != null) tickrateField.render(mouseX, mouseY, partialTicks);

		if (SavestateMod.showSavestateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showSavestateDone = false;
				buttons.get( 9).active=SavestateMod.hasSavestate();
				return;
			}
			drawCenteredString(Minecraft.getInstance().font, "\u00A76Savestate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		} else if (SavestateMod.showLoadstateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showLoadstateDone = false;
				return;
			}
			drawCenteredString(Minecraft.getInstance().font, "\u00A76Loadstate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		}
		Minecraft.getInstance().font.drawShadow("Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);
		int i = 18;
		Minecraft.getInstance().font.drawShadow("Tickjump", 10, 105, 0xFFFFFF);
		if (getButton(i).active == false) {
			Minecraft.getInstance().font.drawShadow("Tickjump is ready,", 8, 137, 0xFFFFFF);
			Minecraft.getInstance().font.drawShadow("press ESC to continue", 8, 147, 0xFFFFFF);
		}

		Minecraft.getInstance().font.drawShadow("Duping", 10, 45, 0xFFFFFF);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (savestateName != null)
			savestateName.mouseClicked(mouseX, mouseY, mouseButton);
		if (tickrateField != null)
			tickrateField.mouseClicked(mouseX, mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (savestateName != null) {
			savestateName.keyPressed(keyCode, scanCode, modifiers);
			boolean focused = savestateName.isFocused();
			if (keyCode == GLFW.GLFW_KEY_ENTER && focused) {
				if (savestateName.getValue().isEmpty())
					SavestateMod.savestate(null);
				else
					SavestateMod.savestate(savestateName.getValue());
			}
		}
		if (tickrateField != null) {
			tickrateField.keyPressed(keyCode, scanCode, modifiers);
			boolean focused = tickrateField.isFocused();
			if (keyCode == GLFW.GLFW_KEY_ENTER && focused) {
				if (!tickrateField.getValue().isEmpty())
					TickrateChangerMod.updateTickrate(Float.parseFloat(tickrateField.getValue()));
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (savestateName != null)
			savestateName.charTyped(typedChar, keyCode);
		if (tickrateField != null)
			tickrateField.charTyped(typedChar, keyCode);
		return super.charTyped(typedChar, keyCode);
	}

}
