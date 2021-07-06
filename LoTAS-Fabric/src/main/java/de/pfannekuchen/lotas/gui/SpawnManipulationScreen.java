package de.pfannekuchen.lotas.gui;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.widgets.EntitySliderWidget;
import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.SpawnManipMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif

/**
 * Draws a gui where the player can decide to spawn an entity
 * @author Pancake
 *
 */
public class SpawnManipulationScreen extends Screen {
	
	public SpawnManipulationScreen() {
		super(new LiteralText(""));
		manip=new SpawnManipMod();
		world=MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld();
	}
	
	private final SpawnManipMod manip;
	
	private TextFieldWidget xText;
	private TextFieldWidget yText;
	private TextFieldWidget zText;

	public static LivingEntity e;
	
	public EntitySliderWidget slider;
	
	private final ServerWorld world;

	//#if MC>=11601
//$$ 	    @Override
//$$ 	    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
//$$ 	        super.render(matrices, mouseX, mouseY, partialTicks);
//$$ 	        xText.render(matrices, mouseX, mouseY, partialTicks);
//$$ 	        yText.render(matrices, mouseX, mouseY, partialTicks);
//$$ 	        zText.render(matrices, mouseX, mouseY, partialTicks);
//$$ 	    }
	//#else
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		xText.render(mouseX, mouseY, partialTicks);
		yText.render(mouseX, mouseY, partialTicks);
		zText.render(mouseX, mouseY, partialTicks);
	}
	//#endif

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.charTyped(typedChar, keyCode);
			yText.charTyped(typedChar, keyCode);
			zText.charTyped(typedChar, keyCode);
			
			try {
				int spawnX = Integer.parseInt(xText.getText());
				int spawnY = Integer.parseInt(yText.getText());
				int spawnZ = Integer.parseInt(zText.getText());
				
				manip.setTarget(new Vec3d(spawnX, spawnY, spawnZ));
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
		MCVer.addButton(this, new NewButtonWidget(width / 2 +140 - margin, height - 95, 20, 20, "\u2191", btn -> manip.changeTargetForward()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +140 - margin, height - 49, 20, 20, "\u2193", btn -> manip.changeTargetBack()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +118 - margin, height - 72, 20, 20, "\u2190", btn -> manip.changeTargetLeft()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +162 - margin, height - 72, 20, 20, "\u2192", btn -> manip.changeTargetRight()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +118 - margin, height - 25, 30, 20, "Up", btn -> manip.changeTargetUp()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +153 - margin, height - 25, 30, 20, "Down", btn -> manip.changeTargetDown()));
		
		Vec3d target=SpawnManipMod.getTargetPos();
		xText = MCVer.TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 98, height - 71, 58, 18, (int) target.x + "");
		yText = MCVer.TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 29, height - 71, 59, 18, (int) target.y + "");
		zText = MCVer.TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 + 39, height - 71, 59, 18, (int) target.z + "");
		
		setTextToVec(SpawnManipMod.getTargetPos());
		
		MCVer.addButton(this, new NewButtonWidget(width / 2 - 100, height - 49, 200, 20, "Spawn Entity", btn -> manip.confirm()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 - 100, height - 75 + 50, 200, 20, "Done", btn -> MinecraftClient.getInstance().openScreen(new GameMenuScreen(true))));
	
		MCVer.addButton(this, new NewButtonWidget(width / 2 - 100, height - 95, 200, 20, "Move to me", btn -> {
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
		buttons.get(buttons.size() - 3).active=SpawnManipMod.canSpawn();
		setTextToVec(SpawnManipMod.getTargetPos());
		return b;
	}


	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state) {
		boolean b = super.mouseReleased(mouseX, mouseY, state);
		manip.setEntity(slider.getEntity(world));
		buttons.get(buttons.size() - 3).active=SpawnManipMod.canSpawn();
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
	
	private void setTextToVec(Vec3d vec) {
		xText.setText((int) vec.x + "");
		yText.setText((int) vec.y + "");
		zText.setText((int) vec.z + "");
	}
}
