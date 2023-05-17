package de.pfannekuchen.lotas.core;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import de.pfannekuchen.lotas.mixin.accessors.AccessorButtons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * Cross Version Compatibility File
 * @author Pancake
 */
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
//$$ 	public static void setMessage(AbstractWidget component, String message) {
		//#if MC>=11601
//$$ 		component.setMessage(MCVer.literal(message));
		//#else
//$$ 		component.setMessage(message);
		//#endif
//$$ 	}
//$$ 	public static net.minecraft.server.level.ServerLevel getCurrentLevel() {
//$$ 		ServerPlayer player = Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().get(0);
		//#if MC>=12000
//$$ 		return player.serverLevel();
		//#else
//$$ 		return (ServerLevel) player.level;
		//#endif
//$$ 	}
//$$ 	public static String getCurrentWorldFolder() {
		//#if MC>=11601
//$$ 		return ((de.pfannekuchen.lotas.mixin.accessors.AccessorLevelStorage)Minecraft.getInstance().getSingleplayerServer()).getStorageSource().getLevelId();
		//#else
//$$ 		return Minecraft.getInstance().getSingleplayerServer().getLevelIdName();
		//#endif
//$$ 	}
	//#else
	public static double getX(Entity e) {
		return e.position().x;
	}
	public static double getY(Entity e) {
		return e.position().y;
	}
	public static double getZ(Entity e) {
		return e.position().z;
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
	
	
	
	//#if MC>=11601
	//#if MC>=12000
//$$ 	// =============================================== 1.20 MATRICES  =========================================
//$$  	public static net.minecraft.client.gui.GuiGraphics stack;
//$$
//$$  	private static ResourceLocation currentTexture;
//$$
//$$  	public static void bind(ResourceLocation texture) {
//$$  		currentTexture = texture;
//$$  	}
//$$
//$$ 	public static void blit(int a, int b, int c, float d, float e, int f, int g, int h, int i) {
//$$ 		stack.blit(currentTexture, a, b, c, d, e, f, g, h, i);
//$$ 	}
//$$ 	public static void fill(int a, int b, int c, int d, int e) {
//$$ 		stack.fill(a, b, c, d, e);
//$$ 	}
//$$ 	public static void drawCenteredString(Screen s, String text, int x, int y, int color) {
//$$ 		Minecraft mc = Minecraft.getInstance();
//$$ 		if (text == null) text = " ";
//$$ 		stack.drawCenteredString(mc.font, text, x, y, color);
//$$ 	}
//$$ 	public static Checkbox Checkbox(int i, int j, int k, int l, String text, boolean bl) {
//$$ 		return new Checkbox(i, j, k, l, MCVer.literal(text), bl);
//$$ 	}
//$$ 	public static Button Button(int i, int j, int k, int l, String text, OnPress onpress) {
//$$ 		return Button.builder(MCVer.literal(text), onpress).pos(i, j).size(k, l).build();
//$$ 	}
//$$ 	public static EditBox EditBox(Font f, int i, int j, int k, int l, String text) {
//$$ 		return new EditBox(f, i, j, k, l, MCVer.literal(text));
//$$ 	}
//$$ 	public static void blit(int i, int j, float k, float f, int g, int l) {
//$$ 		stack.blit(currentTexture, i, j, k, f, g, l, 256, 256);
//$$ 	}
//$$ 	public static void blit(int i, int j, float k, float f, int g, int l, int m, int n) {
//$$ 		stack.blit(currentTexture, i, j, k, f, g, l, m, n);
//$$ 	}
//$$ 	public static void render(AbstractWidget component, int mouseX, int mouseY, float delta) {
//$$ 		component.render(stack, mouseX, mouseY, delta);
//$$ 	}
//$$ 	public static void drawShadow(String text, int x, int y, int color) {
//$$ 		Minecraft mc = Minecraft.getInstance();
//$$ 		if(text!=null) {
//$$ 		stack.drawString(mc.font, text, x, y, color);
//$$ 		}
//$$ 	}
//$$ 	public static void renderBackground(Screen screen) {
//$$ 		screen.renderBackground(stack);
//$$ 	}
	//#else
//$$ 	// =============================================== 1.15.2 | 1.16.1 MATRICES  =========================================
//$$ 	public static com.mojang.blaze3d.vertex.PoseStack stack;
//$$ 	public static void blit(int a, int b, int c, float d, float e, int f, int g, int h, int i) {
//$$ 		net.minecraft.client.gui.GuiComponent.blit(stack, a, b, c, d, e, f, g, h, i);
//$$ 	}
//$$ 	public static void fill(int a, int b, int c, int d, int e) {
//$$ 		net.minecraft.client.gui.GuiComponent.fill(stack, a, b, c, d, e);
//$$ 	}
//$$ 	public static void drawCenteredString(Screen s, String text, int x, int y, int color) {
//$$ 		Minecraft mc = Minecraft.getInstance();
//$$ 		if (text == null) text = " ";
		//#if MC>=11605
//$$ 		Screen.drawCenteredString(stack, mc.font, text, x, y, color);
		//#else
//$$ 		s.drawCenteredString(stack, mc.font, net.minecraft.network.chat.FormattedText.of(text), x, y, color);
		//#endif
//$$ 	}
//$$ 	public static Checkbox Checkbox(int i, int j, int k, int l, String text, boolean bl) {
//$$ 		return new Checkbox(i, j, k, l, MCVer.literal(text), bl);
//$$ 	}
//$$ 	public static Button Button(int i, int j, int k, int l, String text, OnPress onpress) {
		//#if MC>=11903
//$$ 		return Button.builder(MCVer.literal(text), onpress).pos(i, j).size(k, l).build();
		//#else
//$$ 		return new Button(i, j, k, l, MCVer.literal(text), onpress);
		//#endif
//$$ 	}
//$$ 	public static EditBox EditBox(Font f, int i, int j, int k, int l, String text) {
//$$ 		return new EditBox(f, i, j, k, l, MCVer.literal(text));
//$$ 	}
//$$ 	public static void blit(int i, int j, float k, float f, int g, int l) {
//$$ 		net.minecraft.client.gui.GuiComponent.blit(stack, i, j, k, f, g, l, 256, 256);
//$$ 	}
//$$ 	public static void blit(int i, int j, float k, float f, int g, int l, int m, int n) {
//$$ 		net.minecraft.client.gui.GuiComponent.blit(stack, i, j, k, f, g, l, m, n);
//$$ 	}
//$$ 	public static void render(AbstractWidget component, int mouseX, int mouseY, float delta) {
//$$ 		component.render(stack, mouseX, mouseY, delta);
//$$ 	}
//$$ 	public static void drawShadow(String text, int x, int y, int color) {
//$$ 		Minecraft mc = Minecraft.getInstance();
//$$ 		if(text!=null) {
		//#if MC>=11605
//$$ 		mc.font.drawShadow(stack, text, x, y, color);
		//#else
//$$ 		mc.font.drawShadow(stack, net.minecraft.network.chat.FormattedText.of(text), x, y, color);
		//#endif
//$$ 		}
//$$ 	}
//$$ 	public static void renderBackground(Screen screen) {
//$$ 		screen.renderBackground(stack);
//$$ 	}
	//#endif
	//#if MC>=11903
//$$     public static org.joml.Quaternionf fromYXZ(float f, float g, float h) {
//$$     	org.joml.Quaternionf quaternion = new org.joml.Quaternionf(0.0f, 0.0f, 0.0f, 1.0f);
//$$         quaternion.mul(new org.joml.Quaternionf(0.0f, (float)Math.sin(f / 2.0f), 0.0f, (float)Math.cos(f / 2.0f)));
//$$         quaternion.mul(new org.joml.Quaternionf((float)Math.sin(g / 2.0f), 0.0f, 0.0f, (float)Math.cos(g / 2.0f)));
//$$         quaternion.mul(new org.joml.Quaternionf(0.0f, 0.0f, (float)Math.sin(h / 2.0f), (float)Math.cos(h / 2.0f)));
//$$         return quaternion;
//$$     }
    //#endif
	//#else
	public static Checkbox Checkbox(int i, int j, int k, int l, String text, boolean bl) {
		return new Checkbox(i, j, k, l, text, bl);
	}
	public static Button Button(int i, int j, int k, int l, String text, OnPress onpress) {
		return new Button(i, j, k, l, text, onpress);
	}
	public static void blit(int i, int j, float k, float f, int g, int l, int m, int n) {
		net.minecraft.client.gui.GuiComponent.blit(i, j, k, f, g, l, m, n);
	}
	public static void blit(int i, int j, float k, float f, int g, int l) {
		net.minecraft.client.gui.GuiComponent.blit(i, j, k, f, g, l, 256, 256);
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
		net.minecraft.client.gui.GuiComponent.blit(a, b, c, d, e, f, g, h, i);
	}
	public static void fill(int x, int y, int x2, int y2, int color) {
		net.minecraft.client.gui.GuiComponent.fill(x, y, x2, y2, color);
	}
	public static void drawCenteredString(Screen s, String text, int x, int y, int color) {
		s.drawCenteredString(Minecraft.getInstance().font, text, x, y, color);
	}
	//#endif
	// =============================================== 1.14.4 | 1.15.2 | 1.17 RENDERING =========================================
	//#if MC>=11500
	//#if MC>=11700
//$$ 	public static void blendFunc(SourceFactor srcAlpha, DestFactor oneMinusSrcAlpha) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.blendFunc(srcAlpha, oneMinusSrcAlpha);
//$$ 	}
//$$
//$$ 	public static void color4f(float r, float g, float b, float a) {
//$$ 		 com.mojang.blaze3d.systems.RenderSystem.setShaderColor(r, g, b, a);
//$$ 	}
//$$
//$$ 	public static void disableDepthTest() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
//$$ 	}
//$$
//$$ 	public static void enableTexture() {
		//#if MC<11904
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableTexture();
		//#endif
//$$ 	}
//$$
//$$ 	public static void shadeModel(int i) {
//$$ 	}
//$$
//$$ 	public static void enableAlphaTest() {
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
//$$ 	}
//$$
//$$ 	public static void disableTexture() {
		//#if MC<11904
//$$ 		com.mojang.blaze3d.systems.RenderSystem.disableTexture();
		//#endif
//$$ 	}
//$$
//$$ 	public static void disableFog() {
//$$ 	}
//$$
//$$ 	public static void disableLighting() {
//$$ 	}
//$$
//$$ 	public static void matrixMode(int i) {
//$$ 	}
//$$
//$$ 	public static void loadIdentity() {
//$$ 	}
//$$
//$$ 	public static void pushMatrix(Object stack) {
//$$ 		com.mojang.blaze3d.vertex.PoseStack poseStack = (com.mojang.blaze3d.vertex.PoseStack)stack;
//$$ 		poseStack.pushPose();
//$$ 	}
//$$
//$$ 	public static void enableLighting() {
//$$ 	}
//$$
//$$ 	public static void popMatrix(Object stack) {
//$$ 		com.mojang.blaze3d.vertex.PoseStack poseStack = (com.mojang.blaze3d.vertex.PoseStack)stack;
//$$ 		poseStack.popPose();
//$$ 	}
//$$
//$$ 	public static void translated(Object stack, double x, double y, double z) {
//$$ 		com.mojang.blaze3d.vertex.PoseStack poseStack = (com.mojang.blaze3d.vertex.PoseStack)stack;
//$$ 		poseStack.translate(x, y, z);
//$$ 	}
//$$
//$$ 	public static void scaled(Object stack, double scale, double scale2, double scale3) {
//$$ 		com.mojang.blaze3d.vertex.PoseStack poseStack = (com.mojang.blaze3d.vertex.PoseStack)stack;
//$$ 		poseStack.scale((float)scale, (float)scale2, (float)scale3);
//$$ 	}
//$$
//$$ 	public static void rotated(Object stack, double i, double j, double k, double l) {
//$$ 		com.mojang.blaze3d.vertex.PoseStack poseStack = (com.mojang.blaze3d.vertex.PoseStack)stack;
		//#if MC>=11903
//$$ 		poseStack.mulPose(new org.joml.Quaternionf((float)i,(float) j,(float) k,(float) l));
		//#else
//$$ 		poseStack.mulPose(new com.mojang.math.Quaternion((float)i,(float) j,(float) k,(float) l));
		//#endif
//$$ 	}
//$$
//$$ 	public static void enableDepthTest() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
//$$ 	}
//$$
//$$ 	public static BlockEntityRenderDispatcher getBlockEntityDispatcher() {
//$$ 		return Minecraft.getInstance().getBlockEntityRenderDispatcher();
//$$ 	}
//$$
	//#else
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
//$$ 	public static void pushMatrix(Object poseStack) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.pushMatrix();
//$$ 	}
//$$
//$$ 	public static void enableLighting() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableLighting();
//$$ 	}
//$$
//$$ 	public static void popMatrix(Object object) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.popMatrix();
//$$ 	}
//$$
//$$ 	public static void translated(Object poseStack, double x, double y, double z) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.translated(x, y, z);
//$$ 	}
//$$
//$$ 	public static void scaled(Object poseStack, double scale, double scale2, double scale3) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.scaled(scale, scale2, scale3);
//$$ 	}
//$$
//$$ 	public static void rotated(Object poseStack, double i, double j, double k, double l) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.rotatef((float)i,(float) j,(float) k,(float) l);
//$$ 	}
//$$
//$$ 	public static void enableDepthTest() {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
//$$ 	}
//$$
//$$ 	public static void translated(int i, int y, double d) {
//$$ 		com.mojang.blaze3d.systems.RenderSystem.translated(i, y, d);
//$$ 	}
//$$
//$$ 	public static BlockEntityRenderDispatcher getBlockEntityDispatcher() {
//$$ 		return BlockEntityRenderDispatcher.instance;
//$$ 	}
//$$ 	//end of 1.17 if
	//#endif
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

	public static void pushMatrix(Object poseStack) {
		com.mojang.blaze3d.platform.GlStateManager.pushMatrix();
	}

	public static void enableLighting() {
		com.mojang.blaze3d.platform.GlStateManager.enableLighting();
	}

	public static void popMatrix(Object object) {
		com.mojang.blaze3d.platform.GlStateManager.popMatrix();
	}

	public static void scaled(Object poseStack, double scale, double scale2, double scale3) {
		com.mojang.blaze3d.platform.GlStateManager.scaled(scale, scale2, scale3);
	}

	public static void rotated(Object poseStack, double i, double j, double k, double l) {
		com.mojang.blaze3d.platform.GlStateManager.rotated(i, j, k, l);
	}

	public static void enableDepthTest() {
		com.mojang.blaze3d.platform.GlStateManager.enableDepthTest();
	}

	public static void translated(Object poseStack, double i, double y, double d) {
		com.mojang.blaze3d.platform.GlStateManager.translated(i, y, d);
	}

	public static BlockEntityRenderDispatcher getBlockEntityDispatcher() {
		return BlockEntityRenderDispatcher.instance;
	}
	//#endif
	
	// =============================================== 1.19.3 BUTTONS ================================================
	//#if MC>=11903
//$$ 	public static <T extends net.minecraft.client.gui.components.events.GuiEventListener & net.minecraft.client.gui.components.Renderable & net.minecraft.client.gui.narration.NarratableEntry> T addButton(Screen screen, T button) {
//$$ 		((de.pfannekuchen.lotas.core.utils.AccessorScreen2)screen).addRenderableWidget(button);
//$$ 		return button;
//$$ 	}
//$$
//$$ 	public static net.minecraft.client.gui.components.Renderable getButton(Screen obj, int buttonID) {
//$$ 		return ((AccessorButtons)obj).getButtons().get(buttonID);
//$$ 	}
//$$
//$$
//$$ 	public static int getButtonSize(Screen obj) {
//$$ 		return ((AccessorButtons)obj).getButtons().size();
//$$ 	}
	//#else
	// =============================================== 1.16.5 | 1.17 | 1.19.2 BUTTONS =========================================
	//#if MC>=11700
//$$ 	public static <T extends net.minecraft.client.gui.components.events.GuiEventListener & net.minecraft.client.gui.components.Widget & net.minecraft.client.gui.narration.NarratableEntry> T addButton(Screen screen, T button) {
//$$ 		((de.pfannekuchen.lotas.core.utils.AccessorScreen2)screen).addRenderableWidget(button);
//$$ 		return button;
//$$ 	}
	//#else
	public static <T extends AbstractWidget> T addButton(Screen screen, T button) {
		((de.pfannekuchen.lotas.mixin.accessors.AccessorScreen)screen).invokeAddButton(button);
		return button;
	}
	//#endif

	//#if MC>=11700
//$$ 	public static net.minecraft.client.gui.components.Widget getButton(Screen obj, int buttonID) {
//$$ 		return ((AccessorButtons)obj).getButtons().get(buttonID);
//$$ 	}
	//#else
		public static AbstractWidget getButton(Screen obj, int buttonID){
			return (AbstractWidget) ((AccessorButtons)obj).getButtons().get(buttonID);
		}
	//#endif

	//#if MC>=11700
//$$ 	public static int getButtonSize(Screen obj) {
//$$ 		return ((AccessorButtons)obj).getButtons().size();
//$$ 	}
		//#else
		public static int getButtonSize(Screen obj){
			return ((AccessorButtons)obj).getButtons().size();
		}
		//#endif
	//#endif
	// =============================================== 1.16.5 | 1.17 TEXTURES =========================================
	
	public static void bind(TextureManager textureManager, ResourceLocation resource) {
		//#if MC>=11700
		//#if MC>=12000
//$$ 		currentTexture = resource;
		//#else
//$$ 		com.mojang.blaze3d.systems.RenderSystem.setShaderTexture(0, resource);
		//#endif
		//#else
		textureManager.bind(resource);
		//#endif
	}
	
	// =============================================== 1.17.1 | 1.18 WINDOW =========================================
	
	public static com.mojang.blaze3d.platform.Window getGLWindow() {
		//#if MC>=11500
//$$ 		return Minecraft.getInstance().getWindow();
		//#else
		return Minecraft.getInstance().window;
		//#endif
	}
	
	public static Component literal(String s) {
		//#if MC>=11900
//$$ 		return Component.literal(s);
		//#else
		return new net.minecraft.network.chat.TextComponent(s);
		//#endif
	}
}
