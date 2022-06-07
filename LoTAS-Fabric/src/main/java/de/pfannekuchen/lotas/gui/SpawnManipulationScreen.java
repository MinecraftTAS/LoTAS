package de.pfannekuchen.lotas.gui;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.widgets.EntitySliderWidget;
import de.pfannekuchen.lotas.mods.SpawnManipMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * Draws a gui where the player can decide to spawn an entity
 * @author Pancake
 *
 */
public class SpawnManipulationScreen extends Screen {
	
	public SpawnManipulationScreen() {
		super(MCVer.literal(""));
		manip=new SpawnManipMod();
		world=Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().get(0).getLevel();
	}
	
	private final SpawnManipMod manip;
	
	private EditBox xText;
	private EditBox yText;
	private EditBox zText;

	private EntitySliderWidget slider;
	
	private final ServerLevel world;

	//#if MC>=11601
//$$ 	@Override public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float partialTicks) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void render(int mouseX, int mouseY, float partialTicks) {
	//#endif
		for(int k = 0; k < MCVer.getButtonSize(this); ++k) {
			MCVer.render(((AbstractWidget)MCVer.getButton(this,	k)), mouseX, mouseY, partialTicks);
		}
		MCVer.render(xText, mouseX, mouseY, partialTicks);
		MCVer.render(yText, mouseX, mouseY, partialTicks);
		MCVer.render(zText, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.charTyped(typedChar, keyCode);
			yText.charTyped(typedChar, keyCode);
			zText.charTyped(typedChar, keyCode);
			
			try {
				int spawnX = Integer.parseInt(xText.getValue());
				int spawnY = Integer.parseInt(yText.getValue());
				int spawnZ = Integer.parseInt(zText.getValue());
				
				manip.setTarget(new Vec3(spawnX, spawnY, spawnZ));
			} catch (Exception e) {
			}
		}
		return super.charTyped(typedChar, keyCode);
	}

	@Override
	public void init() {
		slider = new EntitySliderWidget(width / 2 - 102, 2, manip.getManipList(), 204, 20, btn -> {
		});
		manip.setEntity(slider.getEntity(world));
		MCVer.addButton(this, slider);
		int margin=10;
		MCVer.addButton(this, MCVer.Button(width / 2 +140 - margin, height - 95, 20, 20, "\u2191", btn -> manip.changeTargetForward()));
		MCVer.addButton(this, MCVer.Button(width / 2 +140 - margin, height - 49, 20, 20, "\u2193", btn -> manip.changeTargetBack()));
		MCVer.addButton(this, MCVer.Button(width / 2 +118 - margin, height - 72, 20, 20, "\u2190", btn -> manip.changeTargetLeft()));
		MCVer.addButton(this, MCVer.Button(width / 2 +162 - margin, height - 72, 20, 20, "\u2192", btn -> manip.changeTargetRight()));
		MCVer.addButton(this, MCVer.Button(width / 2 +118 - margin, height - 25, 30, 20, "Up", btn -> manip.changeTargetUp()));
		MCVer.addButton(this, MCVer.Button(width / 2 +153 - margin, height - 25, 30, 20, "Down", btn -> manip.changeTargetDown()));
		
		Vec3 target=SpawnManipMod.getTargetPos();
		xText = MCVer.EditBox(Minecraft.getInstance().font, width / 2 - 98, height - 71, 58, 18, (int) target.x + "");
		yText = MCVer.EditBox(Minecraft.getInstance().font, width / 2 - 29, height - 71, 59, 18, (int) target.y + "");
		zText = MCVer.EditBox(Minecraft.getInstance().font, width / 2 + 39, height - 71, 59, 18, (int) target.z + "");
		
		setTextToVec(SpawnManipMod.getTargetPos());
		
		MCVer.addButton(this, MCVer.Button(width / 2 - 100, height - 49, 200, 20, "Spawn Entity", btn -> {
			manip.confirm();
			slider.updateManipList(manip.getManipList());
		}));
		MCVer.addButton(this, MCVer.Button(width / 2 - 100, height - 75 + 50, 200, 20, "Done", btn -> Minecraft.getInstance().setScreen(new PauseScreen(true))));
	
		MCVer.addButton(this, MCVer.Button(width / 2 - 100, height - 95, 200, 20, "Move to me", btn -> {
			manip.setTargetToPlayer();
			setTextToVec(SpawnManipMod.getTargetPos());
		}));
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
		boolean b = super.mouseClicked(mouseX, mouseY, mouseButton);
		manip.setEntity(slider.getEntity(world));
		((Button)MCVer.getButton(this, MCVer.getButtonSize(this)-3)).active=SpawnManipMod.canSpawn();
		setTextToVec(SpawnManipMod.getTargetPos());
		return b;
	}


	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state) {
		boolean b = super.mouseReleased(mouseX, mouseY, state);
		manip.setEntity(slider.getEntity(world));
		((Button)MCVer.getButton(this, MCVer.getButtonSize(this)-3)).active=SpawnManipMod.canSpawn();
		setTextToVec(SpawnManipMod.getTargetPos());
		return b;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		xText.keyPressed(keyCode, scanCode, modifiers);
		yText.keyPressed(keyCode, scanCode, modifiers);
		zText.keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	private void setTextToVec(Vec3 vec) {
		xText.setValue((int) vec.x + "");
		yText.setValue((int) vec.y + "");
		zText.setValue((int) vec.z + "");
	}
}
