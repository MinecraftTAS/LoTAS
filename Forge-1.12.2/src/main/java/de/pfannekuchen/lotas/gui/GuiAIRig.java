package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.EntityLiving;
import rlog.RLogAPI;

public class GuiAIRig extends GuiScreen {

	public static int selectedIndex = 0;
	public static List<EntityLiving> entities;
	
	public GuiTextField xText;
	public GuiTextField yText;
	public GuiTextField zText;
	
	public static int spawnX = (int) Minecraft.getMinecraft().player.posX;
	public static int spawnY = (int) Minecraft.getMinecraft().player.posY;
	public static int spawnZ = (int) Minecraft.getMinecraft().player.posZ;
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, 5, 5, 98, 20, "<"));
		buttonList.add(new GuiButton(1, width - 5 - 98, 5, 98, 20, ">"));
		
		xText = new GuiTextField(91, Minecraft.getMinecraft().fontRenderer, width / 2 - 100, height - 50, 60, 20);
		xText.setText(spawnX + "");
		yText = new GuiTextField(92, Minecraft.getMinecraft().fontRenderer, width / 2 - 30, height - 50, 60, 20);
		yText.setText(spawnY + "");
		zText = new GuiTextField(93, Minecraft.getMinecraft().fontRenderer, width / 2 + 50, height - 50, 60, 20);
		zText.setText(spawnZ + "");
		
		buttonList.add(new GuiButton(2, width / 2 - 100, height - 25, 200, 20, "Change Target"));
		
		this.buttonList.add(new GuiButton(7, width / 2 - 35, 80, 30, 20, "X++"));
		this.buttonList.add(new GuiButton(3, width / 2 + 5, 80, 30, 20, "X--"));
		this.buttonList.add(new GuiButton(5, width / 2 - 35, 105, 30, 20, "Y++"));
		this.buttonList.add(new GuiButton(8, width / 2 + 5, 105, 30, 20, "Y--"));
		this.buttonList.add(new GuiButton(6, width / 2 - 35, 130, 30, 20, "Z++"));
		this.buttonList.add(new GuiButton(4, width / 2 + 5, 130, 30, 20, "Z--"));
		RLogAPI.logDebug("[AiRig] Trying to get all Entities around the Player");
		entities = mc.getIntegratedServer().getWorld(mc.player.dimension).getEntitiesWithinAABB(EntityLiving.class, mc.player.getEntityBoundingBox().grow(32, 32, 32));
		RLogAPI.logDebug("[AiRig] Found " + entities.size() + " Entities around the Player");
		selectedIndex = 0;
		
		if (selectedIndex + 2 > entities.size()) {
			buttonList.get(1).enabled = false;
		} else {
			buttonList.get(1).enabled = true;
		}
		
		if (selectedIndex - 1 < 0) {
			buttonList.get(0).enabled = false;
		} else {
			buttonList.get(0).enabled = true;
		}
		super.initGui();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		int prev = selectedIndex;
		
		buttonList.get(2).enabled = true;
		if (button.id == 0) {
			selectedIndex--;
			button.enabled = selectedIndex != 0;
			buttonList.get(1).enabled = selectedIndex != entities.size() - 1;
		} else if (button.id == 1) {
			selectedIndex++;
			button.enabled = selectedIndex != entities.size() - 1;
			buttonList.get(0).enabled = selectedIndex != 0;
		} else if (button.id == 2) {
			RLogAPI.logDebug("[AiRig] Trying to move " + entities.get(selectedIndex).getClass().getSimpleName().split("Entity")[1]);
			button.enabled = !entities.get(selectedIndex).getNavigator().tryMoveToXYZ(spawnX, spawnY, spawnZ, 1.0f);
			entities.get(selectedIndex).getMoveHelper().setMoveTo(spawnX, spawnY, spawnZ, 1.0f);
			RLogAPI.logDebug("[AiRig] Entity Ai manipulated.");
		}
		
		switch (button.id) {
		case 7:
			spawnX++;
			break;
		case 3:
			spawnX--;
			break;
		case 5:
			spawnY++;
			break;
		case 8:
			spawnY--;
			break;
		case 6:
			spawnZ++;
			break;
		case 4:
			spawnZ--;
			break;
		}
		
		xText.setText(spawnX + "");
		yText.setText(spawnY + "");
		zText.setText(spawnZ + "");
		
		if (prev != selectedIndex) {
			try {
				spawnX = entities.get(selectedIndex).getNavigator().getPath().getTarget().x;
				spawnY = entities.get(selectedIndex).getNavigator().getPath().getTarget().y;
				spawnZ = entities.get(selectedIndex).getNavigator().getPath().getTarget().z;
				
				xText.setText(spawnX + "");
				yText.setText(spawnY + "");
				zText.setText(spawnZ + "");
			} catch (Exception e) {
				RLogAPI.logError(e, "Parse Invalid Data #2");
			}
		}
		
		if (selectedIndex + 2 > entities.size()) {
			buttonList.get(1).enabled = false;
		} else {
			buttonList.get(1).enabled = true;
		}
		
		if (selectedIndex - 1  < 0) {
			buttonList.get(0).enabled = false;
		} else {
			buttonList.get(0).enabled = true;
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.textboxKeyTyped(typedChar, keyCode);
			yText.textboxKeyTyped(typedChar, keyCode);
			zText.textboxKeyTyped(typedChar, keyCode);
		}
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
			buttonList.get(2).enabled = true;
		} catch (Exception e) {
			RLogAPI.logError(e, "Parse Invalid Data #2");
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (entities.size() == 0) return;
		xText.drawTextBox();
		yText.drawTextBox();
		zText.drawTextBox();
		drawCenteredString(mc.fontRenderer, entities.get(selectedIndex).getClass().getSimpleName().replaceFirst("Entity", ""), width / 2, 5, 0xFFFFFF);
	}
	
}
