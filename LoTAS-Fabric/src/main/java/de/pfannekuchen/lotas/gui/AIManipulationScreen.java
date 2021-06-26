package de.pfannekuchen.lotas.gui;

import java.util.List;

import com.google.common.base.Predicates;

import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.LiteralText;

public class AIManipulationScreen extends Screen {

	public AIManipulationScreen() {
		super(new LiteralText(""));
	}

	public static int selectedIndex = 0;
	public static List<MobEntity> entities;

	public TextFieldWidget xText;
	public TextFieldWidget yText;
	public TextFieldWidget zText;
	
	//#if MC>=11601
//$$ 	public static int spawnX = (int) MinecraftClient.getInstance().player.getX();
//$$     public static int spawnY = (int) MinecraftClient.getInstance().player.getY();
//$$     public static int spawnZ = (int) MinecraftClient.getInstance().player.getZ();
	//#else
	 public static int spawnX = (int) MinecraftClient.getInstance().player.x;
    public static int spawnY = (int) MinecraftClient.getInstance().player.y;
    public static int spawnZ = (int) MinecraftClient.getInstance().player.z;
	//#endif
	
	@Override
	public void init() {
		//#if MC>=11700
//$$ 		addDrawableChild(new NewButtonWidget(5, 5, 98, 20, "<", btn -> {
//$$ 			selectedIndex--;
//$$ 			btn.active = selectedIndex != 0;
//$$ 			((ButtonWidget)drawables.get(1)).active = selectedIndex != entities.size() - 1;
//$$ 		}));
//$$ 		addDrawableChild(new NewButtonWidget(width - 5 - 98, 5, 98, 20, ">", button -> {
//$$ 			selectedIndex++;
//$$ 			button.active = selectedIndex != entities.size() - 1;
//$$ 			((ButtonWidget)drawables.get(0)).active = selectedIndex != 0;
//$$ 		}));
		//#else
		addButton(new NewButtonWidget(5, 5, 98, 20, "<", btn -> {
			selectedIndex--;
			btn.active = selectedIndex != 0;
			buttons.get(1).active = selectedIndex != entities.size() - 1;
		}));
		addButton(new NewButtonWidget(width - 5 - 98, 5, 98, 20, ">", button -> {
			selectedIndex++;
			button.active = selectedIndex != entities.size() - 1;
			buttons.get(0).active = selectedIndex != 0;
		}));
		//#endif
		
		//#if MC>=11601
//$$ 		xText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 100, height - 50, 60, 20, new LiteralText(spawnX + ""));
//$$ 		yText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 30, height - 50, 60, 20, new LiteralText(spawnY + ""));
//$$ 		zText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 + 40, height - 50, 60, 20, new LiteralText(spawnZ + ""));
		//#else
		xText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 100, height - 50, 60, 20, spawnX + "");
        yText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 - 30, height - 50, 60, 20, spawnY + "");
        zText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 2 + 40, height - 50, 60, 20, spawnZ + "");
		//#endif
		
		//#if MC>=11700
//$$ 		addDrawableChild(new NewButtonWidget(width / 2 - 100, height - 25, 200, 20, "Change Target", button -> {
//$$ 			button.active = !entities.get(selectedIndex).getNavigation().startMovingTo(spawnX, spawnY, spawnZ, 1.0f);
//$$ 			entities.get(selectedIndex).getMoveControl().moveTo(spawnX, spawnY, spawnZ, 1.0F);
//$$ 		}));
//$$
//$$ 		addDrawableChild(new NewButtonWidget(width / 2 - 100, height - 72, 60, 20, "X++", btn -> spawnX++));
//$$ 		addDrawableChild(new NewButtonWidget(width / 2 - 100, height - 94, 60, 20, "X--", btn -> spawnX--));
//$$ 		addDrawableChild(new NewButtonWidget(width / 2 - 30, height - 72, 60, 20, "Y++", btn -> spawnY++));
//$$ 		addDrawableChild(new NewButtonWidget(width / 2 - 30, height - 94, 60, 20, "Y--", btn -> spawnY--));
//$$ 		addDrawableChild(new NewButtonWidget(width / 2 + 40, height - 72, 60, 20, "Z++", btn -> spawnZ++));
//$$ 		addDrawableChild(new NewButtonWidget(width / 2 + 40, height - 94, 60, 20, "Z--", btn -> spawnZ--));
//$$ 		addDrawableChild(new NewButtonWidget(width / 2 - 100, height - 116, 200, 20, "Move to me", btn -> {
//$$ 		    MinecraftClient.getInstance().player.getX();
//$$ 		    MinecraftClient.getInstance().player.getY();
//$$ 		    MinecraftClient.getInstance().player.getZ();
//$$ 			xText.setText(spawnX + "");
//$$ 			yText.setText(spawnY + "");
//$$ 			zText.setText(spawnZ + "");
//$$ 		}));
//$$ 		addDrawableChild(new NewButtonWidget(width / 2 - 100, height - 138, 200, 20, "Move to entity", btn -> {
//$$ 			try {
//$$ 			    spawnX = (int) entities.get(selectedIndex).getX();  
//$$ 			    spawnY = (int) entities.get(selectedIndex).getY();  
//$$ 			    spawnZ = (int) entities.get(selectedIndex).getZ();  
//$$ 				xText.setText(spawnX + "");
//$$ 				yText.setText(spawnY + "");
//$$ 				zText.setText(spawnZ + "");
//$$ 			} catch (Exception e1) {
//$$ 				e1.printStackTrace();
//$$ 			}
//$$ 		}));
		//#else
		addButton(new NewButtonWidget(width / 2 - 100, height - 25, 200, 20, "Change Target", button -> {
			button.active = !entities.get(selectedIndex).getNavigation().startMovingTo(spawnX, spawnY, spawnZ, 1.0f);
			entities.get(selectedIndex).getMoveControl().moveTo(spawnX, spawnY, spawnZ, 1.0F);
		}));

		addButton(new NewButtonWidget(width / 2 - 100, height - 72, 60, 20, "X++", btn -> spawnX++));
		addButton(new NewButtonWidget(width / 2 - 100, height - 94, 60, 20, "X--", btn -> spawnX--));
		addButton(new NewButtonWidget(width / 2 - 30, height - 72, 60, 20, "Y++", btn -> spawnY++));
		addButton(new NewButtonWidget(width / 2 - 30, height - 94, 60, 20, "Y--", btn -> spawnY--));
		addButton(new NewButtonWidget(width / 2 + 40, height - 72, 60, 20, "Z++", btn -> spawnZ++));
		addButton(new NewButtonWidget(width / 2 + 40, height - 94, 60, 20, "Z--", btn -> spawnZ--));
		addButton(new NewButtonWidget(width / 2 - 100, height - 116, 200, 20, "Move to me", btn -> {
		    //#if MC>=11601
//$$ 		    MinecraftClient.getInstance().player.getX();
//$$ 		    MinecraftClient.getInstance().player.getY();
//$$ 		    MinecraftClient.getInstance().player.getZ();
		    //#else
			spawnX = (int) minecraft.player.x;
			spawnY = (int) minecraft.player.y;
			spawnZ = (int) minecraft.player.z;
			//#endif
			xText.setText(spawnX + "");
			yText.setText(spawnY + "");
			zText.setText(spawnZ + "");
		}));
		addButton(new NewButtonWidget(width / 2 - 100, height - 138, 200, 20, "Move to entity", btn -> {
			try {
			    //#if MC>=11601
//$$ 			    spawnX = (int) entities.get(selectedIndex).getX();  
//$$ 			    spawnY = (int) entities.get(selectedIndex).getY();  
//$$ 			    spawnZ = (int) entities.get(selectedIndex).getZ();  
			    //#else
				spawnX = (int) entities.get(selectedIndex).x;
				spawnY = (int) entities.get(selectedIndex).y;
				spawnZ = (int) entities.get(selectedIndex).z;
				//#endif
				xText.setText(spawnX + "");
				yText.setText(spawnY + "");
				zText.setText(spawnZ + "");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}));
		//#endif
		//#if MC>=11601
		//#if MC>=11605
//$$ 		entities = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().getEntitiesByClass(MobEntity.class, MinecraftClient.getInstance().player.getBoundingBox().expand(32, 32, 32), Predicates.alwaysTrue());
		//#else
//$$ 		entities = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().getEntities(MobEntity.class, MinecraftClient.getInstance().player.getBoundingBox().expand(32, 32, 32), Predicates.alwaysTrue()); 
		//#endif
		//#else
		entities = minecraft.getServer().getWorld(MinecraftClient.getInstance().player.dimension).getEntities(MobEntity.class, minecraft.player.getBoundingBox().expand(32, 32, 32), Predicates.alwaysTrue());
		//#endif
		selectedIndex = 0;

		//#if MC>=11700
//$$ 		if (selectedIndex + 2 > entities.size()) {
//$$ 			((ButtonWidget)drawables.get(1)).active = false;
//$$ 		} else {
//$$ 			((ButtonWidget)drawables.get(1)).active = true;
//$$ 		}
//$$
//$$ 		if (selectedIndex - 1 < 0) {
//$$ 			((ButtonWidget)drawables.get(0)).active = false;
//$$ 		} else {
//$$ 			((ButtonWidget)drawables.get(0)).active = true;
//$$ 		}
		//#else
		if (selectedIndex + 2 > entities.size()) {
			buttons.get(1).active = false;
		} else {
			buttons.get(1).active = true;
		}

		if (selectedIndex - 1 < 0) {
			buttons.get(0).active = false;
		} else {
			buttons.get(0).active = true;
		}
		//#endif
		
		super.init();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		int prev = selectedIndex;

		//#if MC>=11700
//$$ 		((ButtonWidget)drawables.get(2)).active = true;
		//#else
		buttons.get(2).active = true;
		//#endif
		
		boolean i = super.mouseClicked(mouseX, mouseY, mouseButton);

		xText.setText(spawnX + "");
		yText.setText(spawnY + "");
		zText.setText(spawnZ + "");

		if (prev != selectedIndex) {
			try {
				spawnX = entities.get(selectedIndex).getNavigation().getCurrentPath().getEnd().x;
				spawnY = entities.get(selectedIndex).getNavigation().getCurrentPath().getEnd().y;
				spawnZ = entities.get(selectedIndex).getNavigation().getCurrentPath().getEnd().z;

				xText.setText(spawnX + "");
				yText.setText(spawnY + "");
				zText.setText(spawnZ + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//#if MC>=11700
//$$ 		if (selectedIndex + 2 > entities.size()) {
//$$ 			((ButtonWidget)drawables.get(1)).active = false;
//$$ 		} else {
//$$ 			((ButtonWidget)drawables.get(1)).active = true;
//$$ 		}
//$$
//$$ 		if (selectedIndex - 1 < 0) {
//$$ 			((ButtonWidget)drawables.get(0)).active = false;
//$$ 		} else {
//$$ 			((ButtonWidget)drawables.get(0)).active = true;
//$$ 		}
		//#else
		if (selectedIndex + 2 > entities.size()) {
			buttons.get(1).active = false;
		} else {
			buttons.get(1).active = true;
		}

		if (selectedIndex - 1 < 0) {
			buttons.get(0).active = false;
		} else {
			buttons.get(0).active = true;
		}
		//#endif
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
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
			//#if MC>=11700
//$$ 			((ButtonWidget)drawables.get(2)).active = true;
			//#else
			buttons.get(2).active = true;
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
//$$ 		if (entities.size() == 0) return;
//$$ 		xText.render(matrices, mouseX, mouseY, delta);
//$$ 		yText.render(matrices, mouseX, mouseY, delta);
//$$ 		zText.render(matrices, mouseX, mouseY, delta);
		//#if MC>=11700
//$$ 		drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, entities.get(selectedIndex).getClass().getSimpleName().replaceFirst("Entity", "") + " (" + entities.get(selectedIndex).getX() + ", " + entities.get(selectedIndex).getY() + ", " + entities.get(selectedIndex).getZ() + ")", width / 2, 5, 0xFFFFFF);
		//#else
//$$ 		drawCenteredString(matrices, MinecraftClient.getInstance().textRenderer, entities.get(selectedIndex).getClass().getSimpleName().replaceFirst("Entity", "") + " (" + entities.get(selectedIndex).getX() + ", " + entities.get(selectedIndex).getY() + ", " + entities.get(selectedIndex).getZ() + ")", width / 2, 5, 0xFFFFFF);
		//#endif
//$$ 	}
	//#else
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		if (entities.size() == 0)
			return;
		xText.render(mouseX, mouseY, partialTicks);
		yText.render(mouseX, mouseY, partialTicks);
		zText.render(mouseX, mouseY, partialTicks);
		drawCenteredString(MinecraftClient.getInstance().textRenderer, entities.get(selectedIndex).getClass().getSimpleName().replaceFirst("Entity", "") + " (" + entities.get(selectedIndex).x + ", " + entities.get(selectedIndex).y + ", " + entities.get(selectedIndex).z + ")", width / 2, 5, 0xFFFFFF);
	}
	//#endif
}
