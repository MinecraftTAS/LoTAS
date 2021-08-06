package de.pfannekuchen.lotas.gui;

import java.io.IOException;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.widgets.ButtonWidget;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.AIManipMod.Vec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiAiManipulation extends GuiScreen {
	
	private AIManipMod manip;
	
	private GuiTextField xText;
	private GuiTextField yText;
	private GuiTextField zText;
	
	@Override
	public void initGui() {
		manip = new AIManipMod();
		buttonList.add(new ButtonWidget(5, 5, 98, 20, "<", b -> {
			manip.selectPrevious();
			buttonList.get(0).enabled = manip.hasPrevious();
			buttonList.get(1).enabled = manip.hasNext();
			buttonList.get(2).enabled = !manip.contains(AIManipMod.getSelectedEntity());
		}));
		buttonList.add(new ButtonWidget(width - 5 - 98, 5, 98, 20, ">", b -> {
			manip.selectNext();
			buttonList.get(0).enabled = manip.hasPrevious();
			buttonList.get(1).enabled = manip.hasNext();
			buttonList.get(2).enabled = !manip.contains(AIManipMod.getSelectedEntity());
		}));
		
		Vec target = AIManipMod.getTargetPos();
		xText = new GuiTextField(91, MCVer.getFontRenderer(Minecraft.getMinecraft()), width / 2 - 98, height - 48, 58, 19);
		yText = new GuiTextField(92, MCVer.getFontRenderer(Minecraft.getMinecraft()), width / 2 - 29, height - 48, 59, 19);
		zText = new GuiTextField(93, MCVer.getFontRenderer(Minecraft.getMinecraft()), width / 2 + 39, height - 48, 59, 19);
		setTextToVec(target);
		
		buttonList.add(new ButtonWidget(width / 2 - 100, height - 25, 200, 20, "Change Target", b -> {
			manip.confirm();
			b.enabled = false;
		}));
		
		int margin = 10;
		buttonList.add(new ButtonWidget(width / 2 +140 - margin, height - 95, 20, 20, "\u2191", btn -> manip.changeTargetForward()));
		buttonList.add(new ButtonWidget(width / 2 +140 - margin, height - 49, 20, 20, "\u2193", btn -> manip.changeTargetBack()));   
		buttonList.add(new ButtonWidget(width / 2 +118 - margin, height - 72, 20, 20, "\u2190", btn -> manip.changeTargetLeft()));   
		buttonList.add(new ButtonWidget(width / 2 +162 - margin, height - 72, 20, 20, "\u2192", btn -> manip.changeTargetRight()));  
		buttonList.add(new ButtonWidget(width / 2 +118 - margin, height - 25, 30, 20, "Up", btn -> manip.changeTargetUp()));         
		buttonList.add(new ButtonWidget(width / 2 +153 - margin, height - 25, 30, 20, "Down", btn -> manip.changeTargetDown()));     
		buttonList.add(new ButtonWidget(width / 2 - 100, height - 72, 200, 20, "Move to me", btn -> {
			manip.setTargetToPlayer();
			setTextToVec(AIManipMod.getTargetPos());
		}));
		buttonList.add(new ButtonWidget(width / 2 - 100, height - 95, 200, 20, "Move to entity", btn -> {
			manip.setTargetToEntity();
			setTextToVec(AIManipMod.getTargetPos());
		}));
		
		buttonList.get(1).enabled = manip.hasNext();
		buttonList.get(0).enabled = manip.hasPrevious();
		
		super.initGui();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		buttonList.get(2).enabled = !manip.contains(AIManipMod.getSelectedEntity());
		
		setTextToVec(AIManipMod.getTargetPos());
		buttonList.get(1).enabled = manip.hasNext();
		buttonList.get(0).enabled = manip.hasPrevious();
		
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	private void setTextToVec(Vec vec) {
	    xText.setText((int) vec.x + "");
	    yText.setText((int) vec.y + "");
	    zText.setText((int) vec.z + "");
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.textboxKeyTyped(typedChar, keyCode);
			yText.textboxKeyTyped(typedChar, keyCode);
			zText.textboxKeyTyped(typedChar, keyCode);
		}
		try {
			int spawnX = Integer.parseInt(xText.getText());
			int spawnY = Integer.parseInt(yText.getText());
			int spawnZ = Integer.parseInt(zText.getText());
			manip.setTarget(new Vec(spawnX, spawnY, spawnZ));
			buttonList.get(2).enabled = !manip.contains(AIManipMod.getSelectedEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (AIManipMod.getSelectedEntity() == null) return;
		xText.drawTextBox();
		yText.drawTextBox();
		zText.drawTextBox();
		
		Vec entityPos = AIManipMod.getSelectedEntityPos();
		drawCenteredString(MCVer.getFontRenderer(mc), AIManipMod.getSelectedEntity().getName() + " (" + (int)entityPos.x + ", " + (int)entityPos.y + ", " + (int)entityPos.z + ")", width / 2, 5, 0xFFFFFF);
	}
	
}