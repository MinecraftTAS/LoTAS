package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.util.List;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.EntityLiving;

public class GuiAiManipulation extends GuiScreen {

	public static int selectedIndex = 0;
	public static List<EntityLiving> entities;
	
	public GuiTextField xText;
	public GuiTextField yText;
	public GuiTextField zText;
	
	public static int spawnX = (int) MCVer.player(Minecraft.getMinecraft()).posX;
	public static int spawnY = (int) MCVer.player(Minecraft.getMinecraft()).posY;
	public static int spawnZ = (int) MCVer.player(Minecraft.getMinecraft()).posZ;
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, 5, 5, 98, 20, "<"));
		buttonList.add(new GuiButton(1, width - 5 - 98, 5, 98, 20, ">"));
		
		xText = new GuiTextField(91, MCVer.getFontRenderer(Minecraft.getMinecraft()), width / 2 - 100, height - 50, 60, 20);
		xText.setText(spawnX + "");
		yText = new GuiTextField(92, MCVer.getFontRenderer(Minecraft.getMinecraft()), width / 2 - 30, height - 50, 60, 20);
		yText.setText(spawnY + "");
		zText = new GuiTextField(93, MCVer.getFontRenderer(Minecraft.getMinecraft()), width / 2 + 40, height - 50, 60, 20);
		zText.setText(spawnZ + "");
		
		buttonList.add(new GuiButton(2, width / 2 - 100, height - 25, 200, 20, "Change Target"));
		
		this.buttonList.add(new GuiButton(7, width / 2 - 100, height - 72, 60, 20, "X++"));
		this.buttonList.add(new GuiButton(3, width / 2 - 100, height - 94, 60, 20, "X--"));
		this.buttonList.add(new GuiButton(5, width / 2 - 30, height - 72, 60, 20, "Y++"));
		this.buttonList.add(new GuiButton(8, width / 2 - 30, height - 94, 60, 20, "Y--"));
		this.buttonList.add(new GuiButton(6, width / 2 + 40, height - 72, 60, 20, "Z++"));
		this.buttonList.add(new GuiButton(4, width / 2 + 40, height - 94, 60, 20, "Z--"));
		this.buttonList.add(new GuiButton(10, width / 2 - 100, height - 116, 200, 20, "Move to me"));
		this.buttonList.add(new GuiButton(11, width / 2 - 100, height - 138, 200, 20, "Move to entity"));
		entities = MCVer.world(mc.getIntegratedServer(), MCVer.player(mc).dimension).getEntitiesWithinAABB(EntityLiving.class, MCVer.player(mc).getEntityBoundingBox().expand(16, 16, 16).expand(-16, -16, -16));
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
			button.enabled = !entities.get(selectedIndex).getNavigator().tryMoveToXYZ(spawnX, spawnY, spawnZ, 1.0f);
			entities.get(selectedIndex).getMoveHelper().setMoveTo(spawnX, spawnY, spawnZ, 1.0f);
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
			spawnX = (int) MCVer.player(mc).posX;
			spawnY = (int) MCVer.player(mc).posY;
			spawnZ = (int) MCVer.player(mc).posZ;
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
				//#if MC>=11200
				spawnX = entities.get(selectedIndex).getNavigator().getPath().getTarget().x;
				spawnY = entities.get(selectedIndex).getNavigator().getPath().getTarget().y;
				spawnZ = entities.get(selectedIndex).getNavigator().getPath().getTarget().z;
				//#else
				//#if MC>=11100
				//$$ spawnX = entities.get(selectedIndex).getNavigator().getPath().getTarget().xCoord;
				//$$ spawnY = entities.get(selectedIndex).getNavigator().getPath().getTarget().yCoord;
				//$$ spawnZ = entities.get(selectedIndex).getNavigator().getPath().getTarget().zCoord;
				//#else
//$$ 				spawnX = entities.get(selectedIndex).getNavigator().getPath().getFinalPathPoint().xCoord;
//$$ 				spawnY = entities.get(selectedIndex).getNavigator().getPath().getFinalPathPoint().yCoord;
//$$ 				spawnZ = entities.get(selectedIndex).getNavigator().getPath().getFinalPathPoint().zCoord;
				//#endif
				//#endif
				
				xText.setText(spawnX + "");
				yText.setText(spawnY + "");
				zText.setText(spawnZ + "");
			} catch (Exception e) {
				e.printStackTrace();
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
			e.printStackTrace();
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
		drawCenteredString(MCVer.getFontRenderer(mc), entities.get(selectedIndex).getClass().getSimpleName().replaceFirst("Entity", "") + " (" + entities.get(selectedIndex).getPosition().getX() + ", " + entities.get(selectedIndex).getPosition().getY() + ", " + entities.get(selectedIndex).getPosition().getZ() + ")", width / 2, 5, 0xFFFFFF);
	}
	
}