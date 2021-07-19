package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * List of Seeds, unused
 * @author Pancake
 */
public class SeedListScreen extends Screen {

	public Seed selectedSeed = null;
	public static ArrayList<Seed> seeds = new ArrayList<>();
	/** @see de.pfannekuchen.lotas.LoTAS List of all downloaded seeds */

	public static Map<String, ResourceLocation> seedsId = new HashMap<>();

	/**
	 * Seed class
	 */
	public static class Seed {
		private String seed;
		private String name;
		private String description;

		public Seed(String seed, String name, String description) {
			this.seed = seed;
			this.name = name;
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public String getSeed() {
			return seed;
		}
	}

	public SeedListScreen() {
		super(new TextComponent("Seeds"));
	}

	/**
	 * Listens for mouse clicks and selects the seed entry
	 *
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		mouseY -= 52;
		if (mouseY > 0 && mouseY < seeds.size() * 40) {
			int index = (int) Math.floor(mouseY / 40);
			selectedSeed = seeds.get(index);
			((Button)MCVer.getButton(this, 1)).active = true;
		}
		return super.mouseClicked(mouseX, mouseY + 52, button);
	}

	/**
	 * Downloads the Seed to an Identifier
	 * @param seed
	 */
	public ResourceLocation downloadSeed(String seed) throws IOException {
		if (seedsId.containsKey(seed + ""))
			return seedsId.get(seed + "");
		URL url = new URL("http://mgnet.work/seeds/" + seed + ".png");
		NativeImage image = NativeImage.read(url.openStream());
		DynamicTexture txt = new DynamicTexture(image);
		ResourceLocation iff = Minecraft.getInstance().getTextureManager().register(seed + "", txt);
		seedsId.put(seed + "", iff);
		return iff;
	}

	/**
	 * Adds Done and Create World buttons to Seed menu
	 */
	@Override
	protected void init() {
		MCVer.addButton(this, MCVer.Button(width / 2 - 100, height - 28, 200, 20, "Done", button -> {
			Minecraft.getInstance().setScreen(new SelectWorldScreen(new TitleScreen()));
		}));
		Button create = MCVer.Button(width / 2 - 100, height - 52, 200, 20, "Create World", button -> {
		
		//#if MC>=11700
//$$ 		Minecraft.getInstance().setScreen(new ProgressScreen(true));
		//#else
		Minecraft.getInstance().setScreen(new ProgressScreen());
		//#endif
//			CreateWorldScreen createWorldScreen = new CreateWorldScreen(this);
//			AccessorCreateWorldScreen accessorCWS = (AccessorCreateWorldScreen) createWorldScreen;
//			accessorCWS.setSeed(selectedSeed.seed);
//			accessorCWS.setCheatsEnabled(true);
//			Minecraft.getInstance().setScreen(createWorldScreen);
		});
		create.active = false;
		MCVer.addButton(this, create);
		super.init();
	}

	/**
	 * Renders background from top to bottom
	 */
	protected void renderHoleBackground(int top, int bottom, int alphaTop, int alphaBottom) {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		MCVer.bind(Minecraft.getInstance().getTextureManager(), GuiComponent.BACKGROUND_LOCATION);
		MCVer.color4f(.5f, .5f, .5f, .4F);
		//#if MC>=11700
//$$ 		bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION);
		//#else
		bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
		//#endif
		bufferBuilder.vertex((double) 0, (double) bottom, 0.0D).uv(0.0F, ((float) bottom / 32.0F)).color(64, 64, 64, alphaBottom).endVertex();
		bufferBuilder.vertex((double) (0 + this.width), (double) bottom, 0.0D).uv(((float) this.width / 32.0F), ((float) bottom / 32.0F)).color(64, 64, 64, alphaBottom).endVertex();
		bufferBuilder.vertex((double) (0 + this.width), (double) top, 0.0D).uv(((float) this.width / 32.0F), ((float) top / 32.0F)).color(64, 64, 64, alphaTop).endVertex();
		bufferBuilder.vertex((double) 0, (double) top, 0.0D).uv(0.0F, ((float) top / 32.0F)).color(64, 64, 64, alphaTop).endVertex();
		tessellator.end();
	}

	/**
	 * Draw all Seed Entries from seeds
	 */
	public void drawSeeds(Object obj) throws IOException {
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();

		int y = 52;
		int x = this.width / 3;
		int n = 32;
		for (Seed seed : seeds) {
			if (selectedSeed == seed) {
				double r = x;
				int q = x + Minecraft.getInstance().font.width(seed.description) + 38;
				MCVer.disableTexture();
				float f = 1.0F;
				MCVer.color4f(f, f, f, 0.5F);
				//#if MC>=11700
//$$ 				bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION);
				//#else
				bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
				//#endif
				bufferBuilder.vertex((double) r - 1, (double) (y + n + 2), 1.0D).endVertex();
				bufferBuilder.vertex((double) q - 1, (double) (y + n + 2), 1.0D).endVertex();
				bufferBuilder.vertex((double) q - 1, (double) (y - 2), 1.0D).endVertex();
				bufferBuilder.vertex((double) r - 1, (double) (y - 2), 1.0D).endVertex();
				tessellator.end();
				MCVer.color4f(0.0F, 0.0F, 0.0F, 0.5F);
				//#if MC>=11700
//$$ 				bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION);
				//#else
				bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
				//#endif
				bufferBuilder.vertex((double) (r), (double) (y + n + 1), 1.0D).endVertex();
				bufferBuilder.vertex((double) (q - 2), (double) (y + n + 1), 1.0D).endVertex();
				bufferBuilder.vertex((double) (q - 2), (double) (y - 1), 1.0D).endVertex();
				bufferBuilder.vertex((double) (r), (double) (y - 1), 1.0D).endVertex();
				tessellator.end();
				MCVer.enableTexture();
			}
			MCVer.drawShadow(seed.name, x + 35, y + 3, 16777215);
			MCVer.drawShadow(seed.description, x + 35, y + 14, 8421504);
			MCVer.drawShadow(seed.seed, x + 35, y + 24, 8421504);
			MCVer.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			try {
				MCVer.bind(Minecraft.getInstance().getTextureManager(), downloadSeed(seed.seed));
				MCVer.blit(x + 1, y, 0.0F, 0.0F, 32, 32, 32, 32);
			} catch (Exception e) {

			}
			y += 40;
		}
	}

	//#if MC>=11600
//$$ 	@Override public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float partialTicks) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void render(int mouseX, int mouseY, float partialTicks) {
	//#endif
		int left = 0;
		int right = width;
		int top = 48;
		int bottom = height - 64;

		MCVer.renderBackground(this);
		
		MCVer.disableLighting();
		MCVer.disableFog();
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		
		MCVer.bind(Minecraft.getInstance().getTextureManager(), GuiComponent.BACKGROUND_LOCATION);
		
		MCVer.color4f(1f, 1f, 1f, 1F);
		//#if MC>=11700
//$$ 		bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION);
		//#else
		bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
		//#endif
		
		bufferBuilder.vertex((double) left, (double) bottom, 0.0D).uv(((float) left / 32.0F), ((float) (bottom + (int) 1) / 32.0F)).color(32, 32, 32, 255).endVertex();
		bufferBuilder.vertex((double) right, (double) bottom, 0.0D).uv(((float) right / 32.0F), ((float) (bottom + (int) 1) / 32.0F)).color(32, 32, 32, 255).endVertex();
		bufferBuilder.vertex((double) right, (double) top, 0.0D).uv(((float) right / 32.0F), ((float) (top + (int) 1) / 32.0F)).color(32, 32, 32, 255).endVertex();
		bufferBuilder.vertex((double) left, (double) top, 0.0D).uv(((float) left / 32.0F), ((float) (top + (int) 1) / 32.0F)).color(32, 32, 32, 255).endVertex();
		tessellator.end();

		// OpenGL Stuff
		MCVer.disableDepthTest();
		this.renderHoleBackground(0, top, 255, 255);
		this.renderHoleBackground(bottom, this.height, 255, 255);
		MCVer.enableBlend();
		MCVer.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
		MCVer.disableAlphaTest();
		MCVer.shadeModel(7425);
		
		MCVer.disableTexture();

		// Draw top gradient
		//#if MC>=11700
//$$ 		bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION);
		//#else
		bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
		//#endif
		bufferBuilder.vertex(left, top + 4, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 0).endVertex();
		bufferBuilder.vertex(right, top + 4, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 0).endVertex();
		bufferBuilder.vertex(right, top, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 255).endVertex();
		bufferBuilder.vertex(left, top, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 255).endVertex();
		tessellator.end();

		// Draw bottom gradient
		//#if MC>=11700
//$$ 		bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION);
		//#else
		bufferBuilder.begin(7, DefaultVertexFormat.POSITION);
		//#endif
		bufferBuilder.vertex(left, bottom, 0.0D).uv(0.0F, 1.0F).color(0, 0, 0, 255).endVertex();
		bufferBuilder.vertex(right, bottom, 0.0D).uv(1.0F, 1.0F).color(0, 0, 0, 255).endVertex();
		bufferBuilder.vertex(right, bottom - 4, 0.0D).uv(1.0F, 0.0F).color(0, 0, 0, 0).endVertex();
		bufferBuilder.vertex(left, bottom - 4, 0.0D).uv(0.0F, 0.0F).color(0, 0, 0, 0).endVertex();
		tessellator.end();

		MCVer.enableTexture();
		MCVer.shadeModel(7424);
		MCVer.enableAlphaTest();
		MCVer.disableBlend();
		
		MCVer.drawCenteredString(this, "Seeds", width / 2, 8, 0xFFFFFF);

		try {
			drawSeeds(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}