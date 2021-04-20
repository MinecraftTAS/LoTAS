package de.pfannekuchen.lotas.gui;

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
	
	public static int spawnX = (int) Minecraft.getMinecraft().thePlayer.posX;
	public static int spawnY = (int) Minecraft.getMinecraft().thePlayer.posY;
	public static int spawnZ = (int) Minecraft.getMinecraft().thePlayer.posZ;
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, 5, 5, 98, 20, "<"));
		buttonList.add(new GuiButton(1, width - 5 - 98, 5, 98, 20, ">"));
		
		xText = new GuiTextField(Minecraft.getMinecraft().fontRendererObj, width / 2 - 100, height - 50, 60, 20);
		xText.setText(spawnX + "");
		yText = new GuiTextField(Minecraft.getMinecraft().fontRendererObj, width / 2 - 30, height - 50, 60, 20);
		yText.setText(spawnY + "");
		zText = new GuiTextField(Minecraft.getMinecraft().fontRendererObj, width / 2 + 40, height - 50, 60, 20);
		zText.setText(spawnZ + "");
		
		buttonList.add(new GuiButton(2, width / 2 - 100, height - 25, 200, 20, "Change Target"));
		
		buttonList.add(new GuiButton(7, width / 2 - 100, height - 72, 60, 20, "X++"));
		buttonList.add(new GuiButton(3, width / 2 - 100, height - 94, 60, 20, "X--"));
		buttonList.add(new GuiButton(5, width / 2 - 30, height - 72, 60, 20, "Y++"));
		buttonList.add(new GuiButton(8, width / 2 - 30, height - 94, 60, 20, "Y--"));
		buttonList.add(new GuiButton(6, width / 2 + 40, height - 72, 60, 20, "Z++"));
		buttonList.add(new GuiButton(4, width / 2 + 40, height - 94, 60, 20, "Z--"));
		buttonList.add(new GuiButton(10, width / 2 - 100, height - 116, 200, 20, "Move to me"));
		buttonList.add(new GuiButton(11, width / 2 - 100, height - 138, 200, 20, "Move to entity"));
		RLogAPI.logDebug("[AiRig] Trying to get all Entities around the Player");
		entities = mc.getIntegratedServer().worldServerForDimension(mc.thePlayer.dimension).getEntitiesWithinAABB(EntityLiving.class, mc.thePlayer.boundingBox.expand(32, 32, 32));
		RLogAPI.logDebug("[AiRig] Found " + entities.size() + " Entities around the Player");
		selectedIndex = 0;
		
		if (selectedIndex + 2 > entities.size()) {
			((GuiButton) buttonList.get(1)).enabled = false;
		} else {
			((GuiButton) buttonList.get(1)).enabled = true;
		}
		
		if (selectedIndex - 1 < 0) {
			((GuiButton) buttonList.get(0)).enabled = false;
		} else {
			((GuiButton) buttonList.get(0)).enabled = true;
		}
		super.initGui();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)  {
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)  {
		int prev = selectedIndex;
		
		((GuiButton) buttonList.get(2)).enabled = true;
		if (button.id == 0) {
			selectedIndex--;
			button.enabled = selectedIndex != 0;
			((GuiButton) buttonList.get(1)).enabled = selectedIndex != entities.size() - 1;
		} else if (button.id == 1) {
			selectedIndex++;
			button.enabled = selectedIndex != entities.size() - 1;
			((GuiButton) buttonList.get(0)).enabled = selectedIndex != 0;
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
		case 10:
			spawnX = (int) mc.thePlayer.posX;
			spawnY = (int) mc.thePlayer.posY;
			spawnZ = (int) mc.thePlayer.posZ;
			xText.setText(spawnX + "");
			yText.setText(spawnY + "");
			zText.setText(spawnZ + "");
			break;
		case 11:
			try {
				spawnX = (int) entities.get(selectedIndex).posX;
				spawnY = (int) entities.get(selectedIndex).posY;
				spawnZ = (int) entities.get(selectedIndex).posZ;
				xText.setText(spawnX + "");
				yText.setText(spawnY + "");
				zText.setText(spawnZ + "");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		}
		
		xText.setText(spawnX + "");
		yText.setText(spawnY + "");
		zText.setText(spawnZ + "");
		
		if (prev != selectedIndex) {
			try {
				spawnX = entities.get(selectedIndex).getNavigator().getPath().getFinalPathPoint().xCoord;
				spawnY = entities.get(selectedIndex).getNavigator().getPath().getFinalPathPoint().yCoord;
				spawnZ = entities.get(selectedIndex).getNavigator().getPath().getFinalPathPoint().zCoord;
				
				xText.setText(spawnX + "");
				yText.setText(spawnY + "");
				zText.setText(spawnZ + "");
			} catch (Exception e) {
				RLogAPI.logError(e, "Parse Invalid Data #2");
			}
		}
		
		if (selectedIndex + 2 > entities.size()) {
			((GuiButton) buttonList.get(1)).enabled = false;
		} else {
			((GuiButton) buttonList.get(1)).enabled = true;
		}
		
		if (selectedIndex - 1  < 0) {
			((GuiButton) buttonList.get(0)).enabled = false;
		} else {
			((GuiButton) buttonList.get(0)).enabled = true;
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode)  {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.textboxKeyTyped(typedChar, keyCode);
			yText.textboxKeyTyped(typedChar, keyCode);
			zText.textboxKeyTyped(typedChar, keyCode);
		}
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
			((GuiButton) buttonList.get(2)).enabled = true;
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
		drawCenteredString(mc.fontRendererObj, entities.get(selectedIndex).getClass().getSimpleName().replaceFirst("Entity", "") + " (" + entities.get(selectedIndex).posX + ", " + entities.get(selectedIndex).posY + ", " + entities.get(selectedIndex).posZ + ")", width / 2, 5, 0xFFFFFF);
	}
	
}
