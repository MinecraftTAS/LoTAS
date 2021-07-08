package de.pfannekuchen.lotas.gui;


import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mods.AIManipMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.phys.Vec3;

/**
 * Screen for manipulating the AI
 * @author Pancake
 */
public class AIManipulationScreen extends Screen {

	public AIManipulationScreen() {
		super(new TextComponent(""));
	}

	private AIManipMod manip;

	private EditBox xText;
	private EditBox yText;
	private EditBox zText;

	@Override
	public void init() {
		manip = new AIManipMod();
		addButton(MCVer.Button(5, 5, 98, 20, "<", btn -> {
			manip.selectPrevious();
			buttons.get(0).active = manip.hasPrevious();
			buttons.get(1).active = manip.hasNext();
			buttons.get(2).active = !manip.contains(AIManipMod.getSelectedEntity());
			
		}));
		addButton(MCVer.Button(width - 5 - 98, 5, 98, 20, ">", button -> {
			manip.selectNext();
			buttons.get(0).active = manip.hasPrevious();
			buttons.get(1).active = manip.hasNext();
			buttons.get(2).active = !manip.contains(AIManipMod.getSelectedEntity());
		}));

		Vec3 target = AIManipMod.getTargetPos();
		xText = MCVer.EditBox(Minecraft.getInstance().font, width / 2 - 98, height - 48, 58, 19, (int) target.x + "");
		yText = MCVer.EditBox(Minecraft.getInstance().font, width / 2 - 29, height - 48, 59, 19, (int) target.y + "");
		zText = MCVer.EditBox(Minecraft.getInstance().font, width / 2 + 39, height - 48, 59, 19, (int) target.z + "");

		addButton(MCVer.Button(width / 2 - 100, height - 25, 200, 20, "Change Target", button -> {
			manip.confirm();
			button.active=false;
		}));

		int margin=10;
		addButton(MCVer.Button(width / 2 +140 - margin, height - 95, 20, 20, "\u2191", btn -> manip.changeTargetForward()));
		addButton(MCVer.Button(width / 2 +140 - margin, height - 49, 20, 20, "\u2193", btn -> manip.changeTargetBack()));
		addButton(MCVer.Button(width / 2 +118 - margin, height - 72, 20, 20, "\u2190", btn -> manip.changeTargetLeft()));
		addButton(MCVer.Button(width / 2 +162 - margin, height - 72, 20, 20, "\u2192", btn -> manip.changeTargetRight()));
		addButton(MCVer.Button(width / 2 +118 - margin, height - 25, 30, 20, "Up", btn -> manip.changeTargetUp()));
		addButton(MCVer.Button(width / 2 +153 - margin, height - 25, 30, 20, "Down", btn -> manip.changeTargetDown()));
		addButton(MCVer.Button(width / 2 - 100, height - 72, 200, 20, "Move to me", btn -> {
			manip.setTargetToPlayer();
			setTextToVec(AIManipMod.getTargetPos());
		}));
		addButton(MCVer.Button(width / 2 - 100, height - 95, 200, 20, "Move to entity", btn -> {
			manip.setTargetToEntity();
			setTextToVec(AIManipMod.getTargetPos());
		}));

		buttons.get( 1).active = manip.hasNext();

		buttons.get( 0).active = manip.hasPrevious();

		super.init();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		buttons.get( 2).active = !manip.contains(AIManipMod.getSelectedEntity());

		boolean i = super.mouseClicked(mouseX, mouseY, mouseButton);

		setTextToVec(AIManipMod.getTargetPos());

		buttons.get( 1).active = manip.hasNext();

		buttons.get( 0).active = manip.hasPrevious();
		
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
		return i;
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.charTyped(typedChar, keyCode);
			yText.charTyped(typedChar, keyCode);
			zText.charTyped(typedChar, keyCode);
		}
		try {
			int spawnX = Integer.parseInt(xText.getValue());
			int spawnY = Integer.parseInt(yText.getValue());
			int spawnZ = Integer.parseInt(zText.getValue());
			
			manip.setTarget(new Vec3(spawnX, spawnY, spawnZ));
			buttons.get( 2).active=!manip.contains(AIManipMod.getSelectedEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.charTyped(typedChar, keyCode);
	}

	//#if MC>=11600
//$$ 	@Override public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float partialTicks) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void render(int mouseX, int mouseY, float partialTicks) {
	//#endif
		for(int k = 0; k < this.buttons.size(); ++k) {
			MCVer.render(((AbstractWidget)this.buttons.get(k)), mouseX, mouseY, partialTicks);
		}
		if (AIManipMod.getSelectedEntity()==null)return;
		MCVer.render(xText, mouseX, mouseY, partialTicks);
		MCVer.render(yText, mouseX, mouseY, partialTicks);
		MCVer.render(zText, mouseX, mouseY, partialTicks);
		Vec3 entityPos=AIManipMod.getSelectedEntityPos();
		MCVer.drawCenteredString(this, AIManipMod.getSelectedEntity().getName().getString() + " (" + (int)entityPos.x + ", " + (int)entityPos.y + ", " + (int)entityPos.z + ")", width / 2, 5, 0xFFFFFF);
	}
	

	private void setTextToVec(Vec3 vec) {
		xText.setValue((int) vec.x + "");
		yText.setValue((int) vec.y + "");
		zText.setValue((int) vec.z + "");
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		xText.keyPressed(keyCode, scanCode, modifiers);
		yText.keyPressed(keyCode, scanCode, modifiers);
		zText.keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
