package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiConfiguration extends GuiScreen {
	
	public static String[] optionsBoolean = new String[] {
		"B:tools:saveTickrate:INSERT",
		"B:ui:hideTickrateMessages:INSERT",
		"B:tools:showTickIndicator:INSERT",
		"B:ui:hideRTATimer:INSERT",
		"B:tools:removePearlDelay:INSERT",
		"B:tools:noDamageUnbreaking:INSERT",
		"B:tools:showSpeedometer:INSERT"
	};
	
	public static String[] optionsInteger = new String[] {
			"I:hidden:explosionoptimization:INSERT"
	};
	
	public static String[] optionsString = new String[] {
			"S:ui:runner:INSERT"
	};
	
	public ArrayList<GuiTextField> strings = new ArrayList<GuiTextField>();
	public ArrayList<GuiTextField> ints = new ArrayList<GuiTextField>();
	public HashMap<Integer, String> messages = new HashMap<Integer, String>();
	
	@Override
	public void initGui() {
		strings.clear();
		messages.clear();
		ints.clear();
		buttonList.clear();
		int y = 25;
		int i = 0;
		for (String option : optionsBoolean) {
			String title = option.split(":")[2];
			if (option.split(":")[0].equalsIgnoreCase("B")) {
				boolean v = Boolean.parseBoolean(option.split(":")[3]);
				buttonList.add(new GuiButton(i++, width / 2 - 100, y, 200, 20, title + ": " + (v ? "\u00A7atrue" : "\u00A7cfalse")));
			}
			y += 25;
		}
		i = 20;
		for (String option : optionsString) {
			String title = option.split(":")[2];
			if (option.split(":")[0].equalsIgnoreCase("S")) {
				String v = option.split(":")[3];
				strings.add(new GuiTextField(i++, MCVer.getFontRenderer(mc), width / 2 - 100, y, 200, 20));
				strings.get(strings.size() - 1).setText(v);
				messages.put(y, title);
			}
			y += 25;
		}
		i = 40;
		for (String option : optionsInteger) {
			String title = option.split(":")[2];
			if (option.split(":")[0].equalsIgnoreCase("I")) {
				String v = option.split(":")[3];
				ints.add(new GuiTextField(i++, MCVer.getFontRenderer(mc), width / 2 - 100, y, 200, 20));
				ints.get(ints.size() - 1).setText(v);
				messages.put(y, title);
			}
			y += 25;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (optionsBoolean[button.id].startsWith("B:")) {
			optionsBoolean[button.id] = setBoolean(optionsBoolean[button.id], !Boolean.parseBoolean(optionsBoolean[button.id].split(":")[3]));
			ConfigUtils.setBoolean(optionsBoolean[button.id].split(":")[1], optionsBoolean[button.id].split(":")[2], Boolean.parseBoolean(optionsBoolean[button.id].split(":")[3]));
			ConfigUtils.save();
		}
		initGui();
		super.actionPerformed(button);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (GuiTextField field : strings) {
			field.mouseClicked(mouseX, mouseY, mouseButton);
		}
		for (GuiTextField field : ints) {
			field.mouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (GuiTextField field : strings) {
			if (field.textboxKeyTyped(typedChar, keyCode)) {
				String line = optionsString[field.getId() - 20];
				ConfigUtils.setString(line.split(":")[1], line.split(":")[2], field.getText());
				ConfigUtils.save();
				optionsString[field.getId() - 20] = setString(line.split(":")[1], line.split(":")[2], field.getText());
			}
		}
		for (GuiTextField field : ints) {
			String textBefore = field.getText();
			if (field.textboxKeyTyped(typedChar, keyCode)) {
				String line = optionsInteger[field.getId() - 40];
				try {
					if (field.getText().isEmpty()) field.setText("0");
					if (field.getText().startsWith("0") && field.getText().length() != 1) field.setText(field.getText().substring(1));
					ConfigUtils.setInt(line.split(":")[1], line.split(":")[2], Integer.parseInt(field.getText()));
					ConfigUtils.save();
					optionsInteger[field.getId() - 40] = setInt(line.split(":")[1], line.split(":")[2], Integer.parseInt(field.getText()));
				} catch (Exception e) {
					field.setText(textBefore);
				}
			}
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		drawCenteredString(MCVer.getFontRenderer(mc), "Configuration Menu", width / 2, 5, 0xFFFFFFFF);
		for (Entry<Integer, String> entry : messages.entrySet()) {
			drawString(MCVer.getFontRenderer(mc), entry.getValue(), 35, entry.getKey() + 5, 0xFFFFFFFF);
		}
		for (GuiTextField field : strings) {
			field.drawTextBox();
		}
		for (GuiTextField field : ints) {
			field.drawTextBox();
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public static String getTitle(String line) {
		return line.split(":")[2];
	}
	
	public static String getCategory(String line) {
		return line.split(":")[1];
	}
	
	public static String setString(String cat, String title, String val) {
		return "S:" + cat + ":" + title + ":" + val;
	}
	
	public static String setInt(String cat, String title, int val) {
		return "I:" + cat + ":" + title + ":" + val;
	}
	
	
	public static String setBoolean(String line, boolean value) {
		return line.endsWith("true") ? line.replaceFirst("true", value + "") : line.replaceFirst("false", value + "").replaceFirst("INSERT", value + "");
	}
	
}
