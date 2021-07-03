package de.pfannekuchen.lotas.gui;

import java.util.List;

import com.google.common.base.Predicates;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import de.pfannekuchen.lotas.mods.AIManipMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.ai.pathing.EntityNavigation;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//$$ import net.minecraft.entity.ai.pathing.Path;
//#endif
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;

public class AIManipulationScreen extends Screen {

	public AIManipulationScreen() {
		super(new LiteralText(""));
		manip = new AIManipMod();
	}

	private final AIManipMod manip;

	public TextFieldWidget xText;
	public TextFieldWidget yText;
	public TextFieldWidget zText;

	@Override
	public void init() {
		MCVer.addButton(this, new NewButtonWidget(5, 5, 98, 20, "<", btn -> {
			manip.selectPrevious();
			buttons.get(0).active = manip.hasPrevious();
			buttons.get(1).active = manip.hasNext();
			buttons.get(2).active = !manip.contains(AIManipMod.getSelectedEntity());
			
		}));
		MCVer.addButton(this, new NewButtonWidget(width - 5 - 98, 5, 98, 20, ">", button -> {
			manip.selectNext();
			buttons.get(1).active = manip.hasNext();
			buttons.get(0).active = manip.hasPrevious();
			buttons.get(2).active = !manip.contains(AIManipMod.getSelectedEntity());
		}));

		Vec3d target = AIManipMod.getTargetPos();
		xText = MCVer.TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 100, height - 50, 60, 20, (int) target.x + "");
		yText = MCVer.TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 30, height - 50, 60, 20, (int) target.y + "");
		zText = MCVer.TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 + 40, height - 50, 60, 20, (int) target.z + "");

		MCVer.addButton(this, new NewButtonWidget(width / 2 - 100, height - 25, 200, 20, "Change Target", button -> {
			manip.confirm();
			button.active=false;
		}));

		MCVer.addButton(this, new NewButtonWidget(width - 170 -10, height - 92, 20, 20, "\u2191", btn -> manip.changeTargetForward()));
		MCVer.addButton(this, new NewButtonWidget(width - 170 -10, height - 52, 20, 20, "\u2193", btn -> manip.changeTargetBack()));
		MCVer.addButton(this, new NewButtonWidget(width - 190 -10, height - 72, 20, 20, "\u2190", btn -> manip.changeTargetLeft()));
		MCVer.addButton(this, new NewButtonWidget(width - 150 -10, height - 72, 20, 20, "\u2192", btn -> manip.changeTargetRight()));
		MCVer.addButton(this, new NewButtonWidget(width - 100 -30, height - 86, 60, 20, "Up", btn -> manip.changeTargetUp()));
		MCVer.addButton(this, new NewButtonWidget(width - 100 -30, height - 56, 60, 20, "Down", btn -> manip.changeTargetDown()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 - 100, height - 76, 200, 20, "Move to me", btn -> {
			manip.setTargetToPlayer();
			setTextToVec(AIManipMod.getTargetPos());
		}));
		MCVer.addButton(this, new NewButtonWidget(width / 2 - 100, height - 98, 200, 20, "Move to entity", btn -> {
			manip.setTargetToEntity();
			setTextToVec(AIManipMod.getTargetPos());
		}));

		buttons.get(1).active = manip.hasNext();

		buttons.get(0).active = manip.hasPrevious();

		super.init();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		//#if MC>=11700
//$$ 		((ButtonWidget)drawables.get(2)).active = true;
		//#else
		buttons.get(2).active = !manip.contains(AIManipMod.getSelectedEntity());
		//#endif

		boolean i = super.mouseClicked(mouseX, mouseY, mouseButton);

		setTextToVec(AIManipMod.getTargetPos());

		buttons.get(1).active = manip.hasNext();

		buttons.get(0).active = manip.hasPrevious();
		
		// #endif
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
			int spawnX = Integer.parseInt(xText.getText());
			int spawnY = Integer.parseInt(yText.getText());
			int spawnZ = Integer.parseInt(zText.getText());
			
			manip.setTarget(new Vec3d(spawnX, spawnY, spawnZ));
			//#if MC>=11700
//$$ 			((ButtonWidget)drawables.get(2)).active = true;
//$$ 			// #else
//$$ 			buttons.get(2).active = true;
			//#endif
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.charTyped(typedChar, keyCode);
	}

	//#if MC>=11601
//$$ 	@Override
//$$ 	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//$$ 		super.render(matrices, mouseX, mouseY, delta);
//$$ 		if (AIManipMod.getSelectedEntity()==null) return;
//$$ 		xText.render(matrices, mouseX, mouseY, delta);
//$$ 		yText.render(matrices, mouseX, mouseY, delta);
//$$ 		zText.render(matrices, mouseX, mouseY, delta);
//$$ 		Vec3d entityPos=AIManipMod.getSelectedEntityPos();
	//#if MC>=11700
//$$ 		drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, AIManipMod.getSelectedEntity().getClass().getSimpleName().replaceFirst("Entity", "") + " (" + entityPos.x + ", " + entityPos.y + ", " + entityPos.z + ")", width / 2, 5, 0xFFFFFF);
	//#else
//$$ 		drawCenteredString(matrices, MinecraftClient.getInstance().textRenderer, AIManipMod.getSelectedEntity().getClass().getSimpleName().replaceFirst("Entity", "") + " (" + entityPos.x + ", " + entityPos.y + ", " + entityPos.z + ")", width / 2, 5, 0xFFFFFF);
	//#endif
//$$ 	}
	//#else
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		if (AIManipMod.getSelectedEntity()==null)
			return;
		xText.render(mouseX, mouseY, partialTicks);
		yText.render(mouseX, mouseY, partialTicks);
		zText.render(mouseX, mouseY, partialTicks);
		Vec3d entityPos=AIManipMod.getSelectedEntityPos();
		drawCenteredString(MinecraftClient.getInstance().textRenderer, AIManipMod.getSelectedEntity().getClass().getSimpleName().replaceFirst("Entity", "") + " (" + entityPos.x + ", " + entityPos.y + ", " + entityPos.z + ")", width / 2, 5, 0xFFFFFF);
	}
	//#endif

	private void setTextToVec(Vec3d vec) {
		xText.setText((int) vec.x + "");
		yText.setText((int) vec.y + "");
		zText.setText((int) vec.z + "");
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		xText.keyPressed(keyCode, scanCode, modifiers);
		yText.keyPressed(keyCode, scanCode, modifiers);
		zText.keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
