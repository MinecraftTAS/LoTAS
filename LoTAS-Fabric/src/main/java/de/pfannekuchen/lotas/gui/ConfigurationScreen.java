package de.pfannekuchen.lotas.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.mixin.accessors.AccessorTextFieldWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

/**
 * Manages Configuration of LoTAS
 */
public class ConfigurationScreen extends Screen {

	public ConfigurationScreen() {
		super(new TextComponent("Configuration"));
	}

	public static String[] optionsBoolean = new String[] { "B:tools:saveTickrate:INSERT", "B:ui:hideTickrateMessages:INSERT", "B:tools:showTickIndicator:INSERT", "B:ui:hideRTATimer:INSERT", "B:tools:removePearlDelay:INSERT", "B:tools:noDamageUnbreaking:INSERT"};

	public static String[] optionsInteger = new String[] { "I:hidden:explosionoptimization:INSERT" };

	public static String[] optionsString = new String[] {  };

	public ArrayList<EditBox> strings = new ArrayList<EditBox>();
	public ArrayList<EditBox> ints = new ArrayList<EditBox>();
	public HashMap<Integer, String> messages = new HashMap<Integer, String>();

	@Override
	public void init() {
		strings.clear();
		messages.clear();
		ints.clear();
		
		buttons.clear();
		children.clear();
		int y = 25;
		int i = 0;
		for (String option : optionsBoolean) {
			String title = option.split(":")[2];
			if (option.split(":")[0].equalsIgnoreCase("B")) {
				boolean v = Boolean.parseBoolean(option.split(":")[3]);
				addButton(MCVer.Button(width / 2 - 100, y, 200, 20, title + ": " + (v ? "\u00A7atrue" : "\u00A7cfalse"), actionPerformed(i++)));
			}
			y += 25;
		}
		i = 20;
		for (String option : optionsString) {
			String title = option.split(":")[2];
			if (option.split(":")[0].equalsIgnoreCase("S")) {
				String v = option.split(":")[3];
				strings.add(MCVer.EditBox(Minecraft.getInstance().font, width / 2 - 100, y, 200, 20, v));
				strings.get(strings.size() - 1).setValue(v);
				strings.get(strings.size() - 1).setTextColorUneditable(i++);
				messages.put(y, title);
			}
			y += 25;
		}
		i = 40;
		for (String option : optionsInteger) {
			String title = option.split(":")[2];
			if (option.split(":")[0].equalsIgnoreCase("I")) {
				String v = option.split(":")[3];
				ints.add(MCVer.EditBox(Minecraft.getInstance().font, width / 2 - 100, y, 200, 20, v));
				ints.get(ints.size() - 1).setValue(v);
				ints.get(ints.size() - 1).setTextColorUneditable(i++);
				messages.put(y, title);
			}
			y += 25;
		}
	}

	protected OnPress actionPerformed(int id) {
		return b -> {
			if (optionsBoolean[id].startsWith("B:")) {
				optionsBoolean[id] = setBoolean(optionsBoolean[id], !Boolean.parseBoolean(optionsBoolean[id].split(":")[3]));
				ConfigUtils.setBoolean(optionsBoolean[id].split(":")[1], optionsBoolean[id].split(":")[2], Boolean.parseBoolean(optionsBoolean[id].split(":")[3]));
				ConfigUtils.save();
			}
			init();
		};
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		for (EditBox field : strings) {
			field.mouseClicked(mouseX, mouseY, mouseButton);
		}
		for (EditBox field : ints) {
			field.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (EditBox field : strings) {
			if (field.keyReleased(keyCode, scanCode, modifiers)) {
				String line = optionsString[((AccessorTextFieldWidget) field).getUneditableColor() - 20];
				ConfigUtils.setString(line.split(":")[1], line.split(":")[2], field.getValue());
				ConfigUtils.save();
				optionsString[((AccessorTextFieldWidget) field).getUneditableColor() - 20] = setString(line.split(":")[1], line.split(":")[2], field.getValue());
			}
		}
		for (EditBox field : ints) {
			String textBefore = field.getValue();
			if (field.keyReleased(keyCode, scanCode, modifiers)) {
				String line = optionsInteger[((AccessorTextFieldWidget) field).getUneditableColor() - 40];
				try {
					if (field.getValue().isEmpty())
						field.setValue("0");
					if (field.getValue().startsWith("0") && field.getValue().length() != 1)
						field.setValue(field.getValue().substring(1));
					ConfigUtils.setInt(line.split(":")[1], line.split(":")[2], Integer.parseInt(field.getValue()));
					ConfigUtils.save();
					optionsInteger[((AccessorTextFieldWidget) field).getUneditableColor() - 40] = setInt(line.split(":")[1], line.split(":")[2], Integer.parseInt(field.getValue()));
				} catch (Exception e) {
					field.setValue(textBefore);
				}
			}
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (EditBox field : strings) {
			if (field.keyPressed(keyCode, scanCode, modifiers)) {
				String line = optionsString[((AccessorTextFieldWidget) field).getUneditableColor() - 20];
				ConfigUtils.setString(line.split(":")[1], line.split(":")[2], field.getValue());
				ConfigUtils.save();
				optionsString[((AccessorTextFieldWidget) field).getUneditableColor() - 20] = setString(line.split(":")[1], line.split(":")[2], field.getValue());
			}
		}
		for (EditBox field : ints) {
			String textBefore = field.getValue();
			if (field.keyPressed(keyCode, scanCode, modifiers)) {
				String line = optionsInteger[((AccessorTextFieldWidget) field).getUneditableColor() - 40];
				try {
					if (field.getValue().isEmpty())
						field.setValue("0");
					if (field.getValue().startsWith("0") && field.getValue().length() != 1)
						field.setValue(field.getValue().substring(1));
					ConfigUtils.setInt(line.split(":")[1], line.split(":")[2], Integer.parseInt(field.getValue()));
					ConfigUtils.save();
					optionsInteger[((AccessorTextFieldWidget) field).getUneditableColor() - 40] = setInt(line.split(":")[1], line.split(":")[2], Integer.parseInt(field.getValue()));
				} catch (Exception e) {
					field.setValue(textBefore);
				}
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		for (EditBox field : strings) {
			if (field.charTyped(typedChar, keyCode)) {
				String line = optionsString[((AccessorTextFieldWidget) field).getUneditableColor() - 20];
				ConfigUtils.setString(line.split(":")[1], line.split(":")[2], field.getValue());
				ConfigUtils.save();
				optionsString[((AccessorTextFieldWidget) field).getUneditableColor() - 20] = setString(line.split(":")[1], line.split(":")[2], field.getValue());
			}
		}
		for (EditBox field : ints) {
			String textBefore = field.getValue();
			if (field.charTyped(typedChar, keyCode)) {
				String line = optionsInteger[((AccessorTextFieldWidget) field).getUneditableColor() - 40];
				try {
					if (field.getValue().isEmpty())
						field.setValue("0");
					if (field.getValue().startsWith("0") && field.getValue().length() != 1)
						field.setValue(field.getValue().substring(1));
					ConfigUtils.setInt(line.split(":")[1], line.split(":")[2], Integer.parseInt(field.getValue()));
					ConfigUtils.save();
					optionsInteger[((AccessorTextFieldWidget) field).getUneditableColor() - 40] = setInt(line.split(":")[1], line.split(":")[2], Integer.parseInt(field.getValue()));
				} catch (Exception e) {
					field.setValue(textBefore);
				}
			}
		}
		return super.charTyped(typedChar, keyCode);
	}

	//#if MC>=11600
//$$ 	@Override public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void render(int mouseX, int mouseY, float delta) {
	//#endif
		MCVer.renderBackground(this);
		MCVer.drawCenteredString(this, "Configuration Menu", width / 2, 5, 0xFFFFFFFF);
		for (Entry<Integer, String> entry : messages.entrySet()) {
			MCVer.drawShadow(entry.getValue(), 35, entry.getKey() + 5, 0xFFFFFFFF);
		}
		for (EditBox field : strings) {
			MCVer.render(field, mouseX, mouseY, delta);
		}
		for (EditBox field : ints) {
			MCVer.render(field, mouseX, mouseY, delta);
		}
		for(int k = 0; k < this.buttons.size(); ++k) {
			MCVer.render(((AbstractWidget)this.buttons.get(k)), mouseX, mouseY, delta);
		}
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