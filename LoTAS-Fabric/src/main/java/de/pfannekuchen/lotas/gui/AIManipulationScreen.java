package de.pfannekuchen.lotas.gui;


import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import de.pfannekuchen.lotas.mods.AIManipMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;

public class AIManipulationScreen extends Screen {

	public AIManipulationScreen() {
		super(new LiteralText(""));
	}

	private AIManipMod manip;

	private TextFieldWidget xText;
	private TextFieldWidget yText;
	private TextFieldWidget zText;

	@Override
	public void init() {
		manip = new AIManipMod();
		MCVer.addButton(this, new NewButtonWidget(5, 5, 98, 20, "<", btn -> {
			manip.selectPrevious();
			MCVer.getButton(this, 0).active = manip.hasPrevious();
			MCVer.getButton(this,1).active = manip.hasNext();
			MCVer.getButton(this,2).active = !manip.contains(AIManipMod.getSelectedEntity());
			
		}));
		MCVer.addButton(this, new NewButtonWidget(width - 5 - 98, 5, 98, 20, ">", button -> {
			manip.selectNext();
			MCVer.getButton(this,0).active = manip.hasPrevious();
			MCVer.getButton(this,1).active = manip.hasNext();
			MCVer.getButton(this,2).active = !manip.contains(AIManipMod.getSelectedEntity());
		}));

		Vec3d target = AIManipMod.getTargetPos();
		xText = MCVer.TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 98, height - 48, 58, 19, (int) target.x + "");
		yText = MCVer.TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 29, height - 48, 59, 19, (int) target.y + "");
		zText = MCVer.TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 + 39, height - 48, 59, 19, (int) target.z + "");

		MCVer.addButton(this, new NewButtonWidget(width / 2 - 100, height - 25, 200, 20, "Change Target", button -> {
			manip.confirm();
			button.active=false;
		}));

		int margin=10;
		MCVer.addButton(this, new NewButtonWidget(width / 2 +140 - margin, height - 95, 20, 20, "\u2191", btn -> manip.changeTargetForward()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +140 - margin, height - 49, 20, 20, "\u2193", btn -> manip.changeTargetBack()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +118 - margin, height - 72, 20, 20, "\u2190", btn -> manip.changeTargetLeft()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +162 - margin, height - 72, 20, 20, "\u2192", btn -> manip.changeTargetRight()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +118 - margin, height - 25, 30, 20, "Up", btn -> manip.changeTargetUp()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 +153 - margin, height - 25, 30, 20, "Down", btn -> manip.changeTargetDown()));
		MCVer.addButton(this, new NewButtonWidget(width / 2 - 100, height - 72, 200, 20, "Move to me", btn -> {
			manip.setTargetToPlayer();
			setTextToVec(AIManipMod.getTargetPos());
		}));
		MCVer.addButton(this, new NewButtonWidget(width / 2 - 100, height - 95, 200, 20, "Move to entity", btn -> {
			manip.setTargetToEntity();
			setTextToVec(AIManipMod.getTargetPos());
		}));

		MCVer.getButton(this, 1).active = manip.hasNext();

		MCVer.getButton(this, 0).active = manip.hasPrevious();

		super.init();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		MCVer.getButton(this, 2).active = !manip.contains(AIManipMod.getSelectedEntity());

		boolean i = super.mouseClicked(mouseX, mouseY, mouseButton);

		setTextToVec(AIManipMod.getTargetPos());

		MCVer.getButton(this, 1).active = manip.hasNext();

		MCVer.getButton(this, 0).active = manip.hasPrevious();
		
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
			MCVer.getButton(this, 2).active=!manip.contains(AIManipMod.getSelectedEntity());
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
	//#else
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		if (AIManipMod.getSelectedEntity()==null)return;
		xText.render(mouseX, mouseY, partialTicks);
		yText.render(mouseX, mouseY, partialTicks);
		zText.render(mouseX, mouseY, partialTicks);
	//#endif
		Vec3d entityPos=AIManipMod.getSelectedEntityPos();
		MCVer.drawCenteredString(this, AIManipMod.getSelectedEntity().getName().getString() + " (" + (int)entityPos.x + ", " + (int)entityPos.y + ", " + (int)entityPos.z + ")", width / 2, 5, 0xFFFFFF);
	}
	

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
