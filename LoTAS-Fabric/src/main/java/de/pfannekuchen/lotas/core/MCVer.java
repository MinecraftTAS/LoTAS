package de.pfannekuchen.lotas.core;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import de.pfannekuchen.lotas.mixin.accessors.AccessorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
//#if MC>=11601
//$$ import net.minecraft.text.LiteralText;
//#endif
import net.minecraft.client.gui.widget.TextFieldWidget;

public class MCVer {

	public static int clamp(int num, int min, int max) {
		if (num < min) {
			return min;
		} else {
			return num > max ? max : num;
		}
	}

	public static int ceil(float value) {
		int i = (int) value;
		return value > (float) i ? i + 1 : i;
	}

	public static float sin(float value) {
		return (float) Math.sin(value);
	}

	public static float cos(float value) {
		return (float) Math.cos(value);
	}

	public static double sqrt(double value) {
		return Math.sqrt(value);
	}

	public static float sqrt(float value) {
		return (float) Math.sqrt((double) value);
	}

	public static float clamp(float num, float min, float max) {
		if (num < min) {
			return min;
		} else {
			return num > max ? max : num;
		}
	}

	public static float abs(float a) {
		return (a <= 0.0F) ? 0.0F - a : a;
	}

	public static Object matrixStack;
	
	public static CheckboxWidget CheckboxWidget(int x, int y, int width, int height, String title, boolean checked) {
		//#if MC>=11601
//$$ 		return new CheckboxWidget(x, y, width, height, new LiteralText(title), checked);
		//#else
		return new CheckboxWidget(x, y, width, height, title, checked);
		//#endif
	}

	public static void render(SmallCheckboxWidget draw, int mouseX, int mouseY, float delta) {
		//#if MC>=11601
//$$ 		draw.render((net.minecraft.client.util.math.MatrixStack) matrixStack, mouseX, mouseY, delta);
		//#else
		draw.render(mouseX, mouseY, delta);
		//#endif
	}
	
	public static void render(ButtonWidget draw, int mouseX, int mouseY, float delta) {
		//#if MC>=11601
//$$ 		draw.render((net.minecraft.client.util.math.MatrixStack) matrixStack, mouseX, mouseY, delta);
		//#else
		draw.render(mouseX, mouseY, delta);
		//#endif
	}
	
	public static void render(CheckboxWidget draw, int mouseX, int mouseY, float delta) {
		//#if MC>=11601
//$$ 		draw.render((net.minecraft.client.util.math.MatrixStack) matrixStack, mouseX, mouseY, delta);
		//#else
		draw.render(mouseX, mouseY, delta);
		//#endif
	}
	
	public static void color(float a, float b, float c, float d) {
		//#if MC>=11700
//$$ 			com.mojang.blaze3d.systems.RenderSystem.setShaderColor(a, b, c, d);
		//#else
		GlStateManager.color4f(a, b, c, d);
		//#endif
	}

	public static void drawStringWithShadow(String message, int x, int y, int color) {
		//#if MC>=11601
//$$ 			MinecraftClient.getInstance().textRenderer.drawWithShadow((net.minecraft.client.util.math.MatrixStack) matrixStack, message, x, y, color);
		//#else
		MinecraftClient.getInstance().textRenderer.drawWithShadow(message, x, y, color);
		//#endif
	}
	
	public static void renderImage(int a, int b, float c, float d, int what, int did, int you, int expect) {
		//#if MC>=11601
//$$ 		DrawableHelper.drawTexture((net.minecraft.client.util.math.MatrixStack) matrixStack, a, b, c, d, what, did, you, expect);
		//#else
		DrawableHelper.blit(a, b, c, d, what, did, you, expect);
		//#endif
	}
	
	public static void fill(int x, int y, int width, int height, int color) {
		//#if MC>=11601
//$$ 		DrawableHelper.fill((net.minecraft.client.util.math.MatrixStack) matrixStack, x, y, width, height, color);
		//#else
		DrawableHelper.fill(x, y, width, height, color);
		//#endif
	}
	
	//#if MC>=11700
//$$ 	public static <T extends net.minecraft.client.gui.Element & net.minecraft.client.gui.Drawable & net.minecraft.client.gui.Selectable> T  addButton(Screen obj, T drawable) {
//$$ 		return ((CheekyScreenDuck) obj).addDrawableCheekyChild(drawable);
//$$ 	}
	//#else
	public static net.minecraft.client.gui.widget.AbstractButtonWidget addButton(Screen obj, net.minecraft.client.gui.widget.AbstractButtonWidget drawable) {
		return ((AccessorScreen) obj).invokeaddButton(drawable);
	}
	//#endif

	public static void render(TextFieldWidget draw, int mouseX, int mouseY, float delta) {
		//#if MC>=11601
//$$ 		draw.render((net.minecraft.client.util.math.MatrixStack) matrixStack, mouseX, mouseY, delta);
		//#else
		draw.render(mouseX, mouseY, delta);
		//#endif
	}
	
	public static void setMessage(ButtonWidget button, String text) {
		//#if MC>=11601
//$$ 		button.setMessage(new LiteralText(text));
		//#else
		button.setMessage(text);
		//#endif
	}

	public static void drawCenteredString(Screen screen, String message, int x, int y, int color) {
		//#if MC>=11601
		//#if MC>=11700
//$$ 		Screen.drawCenteredText((net.minecraft.client.util.math.MatrixStack) matrixStack, MinecraftClient.getInstance().textRenderer, message, x, y, color);
		//#else
		//$$ screen.drawCenteredString((net.minecraft.client.util.math.MatrixStack) matrixStack, MinecraftClient.getInstance().textRenderer, message, x, y, color);
		//#endif
		//#else
		screen.drawCenteredString(MinecraftClient.getInstance().textRenderer, message, x, y, color);
		//#endif
	}
	
}