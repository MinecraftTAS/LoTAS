package de.pfannekuchen.lotas.gui;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.LiteralText;
import rlog.RLogAPI;

public class AiRigScreen extends Screen {

	public AiRigScreen() {
		super(new LiteralText("Ai Rig Screen"));
	}

	public static int selectedIndex = 0;
	public static List<MobEntity> entities = new ArrayList<MobEntity>();

	public TextFieldWidget xText;
	public TextFieldWidget yText;
	public TextFieldWidget zText;

	public static int spawnX = (int) MinecraftClient.getInstance().player.x;
	public static int spawnY = (int) MinecraftClient.getInstance().player.y;
	public static int spawnZ = (int) MinecraftClient.getInstance().player.z;

	@Override
	public void init() {
		addButton(new ButtonWidget(width / 2 - 100, height - 25, 200, 20, "Change Target", b -> { 
			b.active = !entities.get(selectedIndex).getNavigation().startMovingTo(spawnX, spawnY, spawnZ, 1.0f);
			RLogAPI.logDebug("[AiRig] Entity Ai manipulated.");
		}));

		addButton(new ButtonWidget(5, 5, 98, 20, "<", button -> {
			selectedIndex--;
			button.active = selectedIndex != 0;
			buttons.get(1).active = selectedIndex != entities.size() - 1;
		}));
		addButton(new ButtonWidget(width - 5 - 98, 5, 98, 20, ">", button -> {
			selectedIndex++;
			button.active = selectedIndex != entities.size() - 1;
			buttons.get(0).active = selectedIndex != 0;
		}));

		xText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 100, height - 50, 60, 20, "");
		xText.setText(spawnX + "");
		yText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 30, height - 50, 60, 20, "");
		yText.setText(spawnY + "");
		zText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 + 50, height - 50, 60, 20, "");
		zText.setText(spawnZ + "");


		addButton(new ButtonWidget(width / 2 - 35, 80, 30, 20, "X++", button -> spawnX++));
		addButton(new ButtonWidget(width / 2 + 5, 80, 30, 20, "X--", button -> spawnX--));
		addButton(new ButtonWidget(width / 2 - 35, 105, 30, 20, "Y++", button -> spawnY++));
		addButton(new ButtonWidget(width / 2 + 5, 105, 30, 20, "Y--", button -> spawnY--));
		addButton(new ButtonWidget(width / 2 - 35, 130, 30, 20, "Z++", button -> spawnZ++));
		addButton(new ButtonWidget(width / 2 + 5, 130, 30, 20, "Z--", button -> spawnZ--));
		entities = minecraft.getServer().getWorld(minecraft.world.dimension.getType()).getEntities(MobEntity.class, minecraft.player.getBoundingBox().expand(16, 16, 16), Predicates.alwaysTrue());
		RLogAPI.logDebug("[AiRig] Found " + entities.size() + " Entities around the Player");
		selectedIndex = 0;
		buttons.get(0).active = false;
		if (entities.size() == 0 || entities.size() == 1) {
			buttons.get(0).active = false;
			buttons.get(1).active = false;
			buttons.get(2).active = false;
		} else if (entities.size() == 2) {
			buttons.get(1).active = false;
		}
		super.init();
	}

	@Override
	public boolean charTyped(char chr, int keyCode) {
		xText.charTyped(chr, keyCode);
		yText.charTyped(chr, keyCode);
		zText.charTyped(chr, keyCode);
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
			buttons.get(2).active = true;
		} catch (Exception e) {
			RLogAPI.logError(e, "Parse Invalid Data #2");
		}
		return super.charTyped(chr, keyCode);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		int prev = selectedIndex;
		buttons.get(2).active = true;
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);

		xText.setText(spawnX + "");
		yText.setText(spawnY + "");
		zText.setText(spawnZ + "");
		
		boolean f = super.mouseClicked(mouseX, mouseY, mouseButton);
		
		if (prev != selectedIndex) {
			try {
				spawnX = entities.get(selectedIndex).getNavigation().getCurrentPath().getEnd().x;
				spawnY = entities.get(selectedIndex).getNavigation().getCurrentPath().getEnd().y;
				spawnZ = entities.get(selectedIndex).getNavigation().getCurrentPath().getEnd().z;

				xText.setText(spawnX + "");
				yText.setText(spawnY + "");
				zText.setText(spawnZ + "");
			} catch (Exception e) {
				RLogAPI.logError(e, "Parse Invalid Data #2");
			}
		}
		return f;
	}
	
	
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		xText.keyReleased(keyCode, scanCode, modifiers);
		yText.keyReleased(keyCode, scanCode, modifiers);
		zText.keyReleased(keyCode, scanCode, modifiers);
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
			buttons.get(2).active = true;
		} catch (Exception e) {
			RLogAPI.logError(e, "Parse Invalid Data #2");
		}
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		xText.keyPressed(keyCode, scanCode, modifiers);
		yText.keyPressed(keyCode, scanCode, modifiers);
		zText.keyPressed(keyCode, scanCode, modifiers);
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
			buttons.get(2).active = true;
		} catch (Exception e) {
			RLogAPI.logError(e, "Parse Invalid Data #2");
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		super.render(mouseX, mouseY, delta);
		if (entities.size() == 0)
			return;
		xText.render(mouseX, mouseY, delta);
		yText.render(mouseX, mouseY, delta);
		zText.render(mouseX, mouseY, delta);
		drawCenteredString(minecraft.textRenderer, entities.get(selectedIndex).getClass().getSimpleName().replaceFirst("Entity", ""), width / 2, 5, 0xFFFFFF);
	}

}