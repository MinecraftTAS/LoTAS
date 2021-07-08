package de.pfannekuchen.lotas.core;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class MCVer {
	// =============================================== 1.15.2 | 1.16.1 REST      =========================================
	//#if MC>=11600
//$$ 	public static double getX(Entity e) {
//$$ 		return e.getX();
//$$ 	}
//$$ 	public static double getY(Entity e) {
//$$ 		return e.getY();
//$$ 	}
//$$ 	public static double getZ(Entity e) {
//$$ 		return e.getZ();
//$$ 	}
//$$ 	public static void setXYZ(Entity e, double x, double y, double z) {
//$$ 		e.setPos(x, y, z);
//$$ 	}
//$$ 	public static void setMessage(AbstractWidget component, String message) {
//$$ 		component.setMessage(new net.minecraft.network.chat.TextComponent(message));
//$$ 	}
//$$ 	public static net.minecraft.server.level.ServerLevel getCurrentLevel() {
//$$ 		return (ServerLevel) Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().get(0).level;
//$$ 	}
//$$ 	public static String getCurrentWorldFolder() {
//$$ 		return ((de.pfannekuchen.lotas.mixin.accessors.AccessorLevelStorage)Minecraft.getInstance().getSingleplayerServer()).getStorageSource().getLevelId();
//$$ 	}
	//#else
	public static double getX(Entity e) {
		return e.x;
	}
	public static double getY(Entity e) {
		return e.y;
	}
	public static double getZ(Entity e) {
		return e.z;
	}
	public static void setXYZ(Entity e, double x, double y, double z) {
		e.x = x;
		e.y = y;
		e.z = z;
	}
	public static void setMessage(AbstractWidget component, String message) {
		component.setMessage(message);
	}
	public static ServerLevel getCurrentLevel() {
		return (ServerLevel) Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().get(0).level;
	}
	public static String getCurrentWorldFolder() {
		return Minecraft.getInstance().getSingleplayerServer().getLevelIdName();
	}
	//#endif
	// =============================================== 1.15.2 | 1.16.1 MATRICES  =========================================
	//#if MC>=11600
//$$ 	public static com.mojang.blaze3d.vertex.PoseStack stack;
//$$ 	public static void blit(int a, int b, int c, float d, float e, int f, int g, int h, int i) {
//$$ 		GuiComponent.blit(stack, a, b, c, d, e, f, g, h, i);
//$$ 	}
//$$ 	public static void fill(int a, int b, int c, int d, int e) {
//$$ 		GuiComponent.fill(stack, a, b, c, d, e);
//$$ 	}
//$$ 	public static void drawCenteredString(Screen s, String text, int x, int y, int color) {
//$$ 		if (text == null) text = " ";
		//#if MC>=11606
//$$ 		Screen.drawCenteredString(stack, Minecraft.getInstance().font, text, x, y, color);
		//#else
//$$ 		s.drawCenteredString(stack, Minecraft.getInstance().font, net.minecraft.network.chat.FormattedText.of(text), x, y, color);
		//#endif
//$$ 	}
//$$ 	public static Checkbox Checkbox(int i, int j, int k, int l, String text, boolean bl) {
//$$ 		return new Checkbox(i, j, k, l, new net.minecraft.network.chat.TextComponent(text), bl);
//$$ 	}
//$$ 	public static Button Button(int i, int j, int k, int l, String text, OnPress onpress) {
//$$ 		return new Button(i, j, k, l, new net.minecraft.network.chat.TextComponent(text), onpress);
//$$ 	}
//$$ 	public static EditBox EditBox(Font f, int i, int j, int k, int l, String text) {
//$$ 		return new EditBox(f, i, j, k, l, new net.minecraft.network.chat.TextComponent(text));
//$$ 	}
//$$ 	public static void blit(int i, int j, float k, float f, int g, int l) {
//$$ 		GuiComponent.blit(stack, i, j, k, f, g, l, g, l); // FIXME: wth
//$$ 	}
//$$ 	public static void blit(int i, int j, float k, float f, int g, int l, int x, int y) {
//$$ 		GuiComponent.blit(stack, i, j, k, f, g, l, x, y);
//$$ 	}
//$$ 	public static void render(AbstractWidget component, int mouseX, int mouseY, float delta) {
//$$ 		component.render(stack, mouseX, mouseY, delta);
//$$ 	}
//$$ 	public static void drawShadow(String text, int x, int y, int color) {
		//#if MC>=11605
//$$ 		Minecraft.getInstance().font.drawShadow(stack, text, x, y, color);
		//#else
//$$ 		Minecraft.getInstance().font.drawShadow(stack, net.minecraft.network.chat.FormattedText.of(text), x, y, color);
		//#endif
//$$ 	}
//$$ 	public static void renderBackground(Screen screen) {
//$$ 		screen.renderBackground(stack);
//$$ 	}
	//#else
	public static Checkbox Checkbox(int i, int j, int k, int l, String text, boolean bl) {
		return new Checkbox(i, j, k, l, text, bl);
	}
	public static Button Button(int i, int j, int k, int l, String text, OnPress onpress) {
		return new Button(i, j, k, l, text, onpress);
	}
	public static void blit(int i, int j, float f, float g, int k, int l, int m, int n) {
		GuiComponent.blit(i, j, k, f, g, l, m, n, n);
	}
	public static void blit(int i, int j, float k, float f, int g, int l) {
 		GuiComponent.blit(i, j, k, f, g, l, g, l); // FIXME: wth
 	}
	public static void render(AbstractWidget component, int mouseX, int mouseY, float delta) {
		component.render(mouseX, mouseY, delta);
	}
	public static void drawShadow(String text, int x, int y, int color) {
		Minecraft.getInstance().font.drawShadow(text, x, y, color);
	}
	public static void renderBackground(Screen screen) {
		screen.renderBackground();
	}
	public static EditBox EditBox(Font f, int i, int j, int k, int l, String text) {
		return new EditBox(f, i, j, k, l, text);
	}
	public static void blit(int a, int b, int c, float d, float e, int f, int g, int h, int i) {
		GuiComponent.blit(a, b, c, d, e, f, g, h, i);
	}
	public static void fill(int a, int b, int c, int d, int e) {
		GuiComponent.fill(a, b, c, d, e);
	}
	public static void drawCenteredString(Screen s, String text, int x, int y, int color) {
		s.drawCenteredString(Minecraft.getInstance().font, text, x, y, color);
	}
	//#endif
	// =============================================== 1.14.4 | 1.15.2 RENDERING =========================================
	//#if MC>=11500
//$$ 	public static void color4f(float r, float g, float b, float a) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.color4f(r, g, b, a);
//$$ 	}
//$$
//$$ 	public static void blendFunc(SourceFactor srcAlpha, DestFactor oneMinusSrcAlpha) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.blendFunc(srcAlpha, oneMinusSrcAlpha);
//$$ 	}
//$$
//$$ 	public static void disableDepthTest() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
//$$ 	}
//$$
//$$ 	public static void enableTexture() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableTexture();
//$$ 	}
//$$
//$$ 	public static void shadeModel(int i) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.shadeModel(i);
//$$ 	}
//$$
//$$ 	public static void enableAlphaTest() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableAlphaTest();
//$$ 	}
//$$
//$$ 	public static void disableBlend() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.disableBlend();
//$$ 	}
//$$
//$$ 	public static void enableBlend() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableBlend();
//$$ 	}
//$$
//$$ 	public static void blendFuncSeparate(SourceFactor srcAlpha, DestFactor oneMinusSrcAlpha, SourceFactor zero, DestFactor one) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.blendFuncSeparate(srcAlpha, oneMinusSrcAlpha, zero, one);
//$$ 	}
//$$
//$$ 	public static void disableAlphaTest() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.disableAlphaTest();
//$$ 	}
//$$
//$$ 	public static void disableTexture() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.disableTexture();
//$$ 	}
//$$
//$$ 	public static void disableFog() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.disableFog();
//$$ 	}
//$$
//$$ 	public static void disableLighting() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.disableLighting();
//$$ 	}
//$$
//$$ 	public static void matrixMode(int i) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.matrixMode(i);
//$$ 	}
//$$
//$$ 	public static void loadIdentity() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.loadIdentity();
//$$ 	}
//$$
//$$ 	public static void pushMatrix() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.pushMatrix();
//$$ 	}
//$$
//$$ 	public static void enableLighting() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableLighting();
//$$ 	}
//$$
//$$ 	public static void popMatrix() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.popMatrix();
//$$ 	}
//$$
//$$ 	public static void translated(int i, double j, int k) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.translated(i, j, k);
//$$ 	}
//$$
//$$ 	public static void scaled(double scale, double scale2, double scale3) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.scaled(scale, scale2, scale3);
//$$ 	}
//$$
//$$ 	public static void rotated(int i, int j, int k, int l) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.rotatef(i, j, k, l);
//$$ 	}
//$$
//$$ 	public static void enableDepthTest() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
//$$ 	}
//$$
//$$ 	public static void translated(int i, int y, double d) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.translated(i, y, d);
//$$ 	}
	//#else
	public static void color4f(float r, float g, float b, float a) {
		com.mojang.blaze3d.platform.GlStateManager.color4f(r, g, b, a);
	}

	public static void blendFunc(SourceFactor srcAlpha, DestFactor oneMinusSrcAlpha) {
		com.mojang.blaze3d.platform.GlStateManager.blendFunc(srcAlpha, oneMinusSrcAlpha);
	}

	public static void disableDepthTest() {
		com.mojang.blaze3d.platform.GlStateManager.disableDepthTest();
	}

	public static void enableTexture() {
		com.mojang.blaze3d.platform.GlStateManager.enableTexture();
	}

	public static void shadeModel(int i) {
		com.mojang.blaze3d.platform.GlStateManager.shadeModel(i);
	}

	public static void enableAlphaTest() {
		com.mojang.blaze3d.platform.GlStateManager.enableAlphaTest();
	}

	public static void disableBlend() {
		com.mojang.blaze3d.platform.GlStateManager.disableBlend();
	}

	public static void enableBlend() {
		com.mojang.blaze3d.platform.GlStateManager.enableBlend();
	}

	public static void blendFuncSeparate(SourceFactor srcAlpha, DestFactor oneMinusSrcAlpha, SourceFactor zero, DestFactor one) {
		com.mojang.blaze3d.platform.GlStateManager.blendFuncSeparate(srcAlpha, oneMinusSrcAlpha, zero, one);
	}

	public static void disableAlphaTest() {
		com.mojang.blaze3d.platform.GlStateManager.disableAlphaTest();
	}

	public static void disableTexture() {
		com.mojang.blaze3d.platform.GlStateManager.disableTexture();
	}

	public static void disableFog() {
		com.mojang.blaze3d.platform.GlStateManager.disableFog();
	}

	public static void disableLighting() {
		com.mojang.blaze3d.platform.GlStateManager.disableLighting();
	}

	public static void matrixMode(int i) {
		com.mojang.blaze3d.platform.GlStateManager.matrixMode(i);
	}

	public static void loadIdentity() {
		com.mojang.blaze3d.platform.GlStateManager.loadIdentity();
	}

	public static void pushMatrix() {
		com.mojang.blaze3d.platform.GlStateManager.pushMatrix();
	}

	public static void enableLighting() {
		com.mojang.blaze3d.platform.GlStateManager.enableLighting();
	}

	public static void popMatrix() {
		com.mojang.blaze3d.platform.GlStateManager.popMatrix();
	}

	public static void translated(int i, double j, int k) {
		com.mojang.blaze3d.platform.GlStateManager.translated(i, j, k);
	}

	public static void scaled(double scale, double scale2, double scale3) {
		com.mojang.blaze3d.platform.GlStateManager.scaled(scale, scale2, scale3);
	}

	public static void rotated(int i, int j, int k, int l) {
		com.mojang.blaze3d.platform.GlStateManager.rotated(i, j, k, l);
	}

	public static void enableDepthTest() {
		com.mojang.blaze3d.platform.GlStateManager.enableDepthTest();
	}

	public static void translated(int i, int y, double d) {
		com.mojang.blaze3d.platform.GlStateManager.translated(i, y, d);
	}
	//#endif
}
