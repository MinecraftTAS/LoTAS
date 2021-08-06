package de.pfannekuchen.lotas.gui;

import java.io.IOException;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.widgets.ButtonWidget;
import de.pfannekuchen.lotas.gui.widgets.EntitySliderWidget;
import de.pfannekuchen.lotas.mods.AIManipMod.Vec;
import de.pfannekuchen.lotas.mods.SpawnManipMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.EntityLiving;
//#if MC>10900
import net.minecraft.util.math.BlockPos;
//#endif
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldServer;

/**
 * Draws a gui where the player can decide to spawn an entity
 * @author Pancake
 *
 */
public class GuiEntitySpawnManipulation extends GuiScreen {
	
	public GuiEntitySpawnManipulation() {
		manip=new SpawnManipMod();
		world=MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension);
	}
	
	private final SpawnManipMod manip;
	
	private GuiTextField xText;
	private GuiTextField yText;
	private GuiTextField zText;
	
	private EntitySliderWidget slider;
	
	private final WorldServer world;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		xText.drawTextBox();
		yText.drawTextBox();
		zText.drawTextBox();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.textboxKeyTyped(typedChar, keyCode);
			yText.textboxKeyTyped(typedChar, keyCode);
			zText.textboxKeyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void initGui() {
		slider = new EntitySliderWidget(5, width / 2 -102, 2, manip.getManipList(), 204, 20);
		manip.setEntity(slider.getEntity(world));
		this.buttonList.add(slider);
		
		int margin = 10;
		buttonList.add(new ButtonWidget(width / 2 +140 - margin, height - 95, 20, 20, "\u2191", btn -> manip.changeTargetForward()));
		buttonList.add(new ButtonWidget(width / 2 +140 - margin, height - 49, 20, 20, "\u2193", btn -> manip.changeTargetBack()));   
		buttonList.add(new ButtonWidget(width / 2 +118 - margin, height - 72, 20, 20, "\u2190", btn -> manip.changeTargetLeft()));   
		buttonList.add(new ButtonWidget(width / 2 +162 - margin, height - 72, 20, 20, "\u2192", btn -> manip.changeTargetRight()));  
		buttonList.add(new ButtonWidget(width / 2 +118 - margin, height - 25, 30, 20, "Up", btn -> manip.changeTargetUp()));         
		buttonList.add(new ButtonWidget(width / 2 +153 - margin, height - 25, 30, 20, "Down", btn -> manip.changeTargetDown()));     
		
		
		Vec target = SpawnManipMod.getTargetPos();
		xText = new GuiTextField(91, MCVer.getFontRenderer(Minecraft.getMinecraft()), width / 2 - 98, height - 71, 58, 18);
		yText = new GuiTextField(92, MCVer.getFontRenderer(Minecraft.getMinecraft()), width / 2 - 29, height - 71, 59, 18);
		zText = new GuiTextField(93, MCVer.getFontRenderer(Minecraft.getMinecraft()), width / 2 + 39, height - 71, 59, 18);
		setTextToVec(target);
		
		this.buttonList.add(new ButtonWidget(width / 2 - 100, height - 49, 200, 20, "Spawn Entity", btn -> {
			manip.confirm();
			slider.updateManipList(manip.getManipList());
		}));
		this.buttonList.add(new ButtonWidget(width / 2 - 100, height - 75 + 50, 200, 20, "Done", btn -> Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu())));
		
		buttonList.add(new ButtonWidget(width / 2 - 100, height - 95, 200, 20, "Move to me", btn -> {
			manip.setTargetToPlayer();
			setTextToVec(SpawnManipMod.getTargetPos());
		}));
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
		manip.setEntity(slider.getEntity(world));
		buttonList.get(buttonList.size()-3).enabled=SpawnManipMod.canSpawn();
		setTextToVec(SpawnManipMod.getTargetPos());
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		manip.setEntity(slider.getEntity(world));
		buttonList.get(buttonList.size()-3).enabled=SpawnManipMod.canSpawn();
		setTextToVec(SpawnManipMod.getTargetPos());
	}
	
	private void setTextToVec(Vec vec) {
	    xText.setText((int) vec.x + "");
	    yText.setText((int) vec.y + "");
	    zText.setText((int) vec.z + "");
	}
}
