package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import de.pfannekuchen.lotas.mixin.accessors.AccessorCreateWorldScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//$$ import net.minecraft.resource.DataPackSettings;
//$$ import net.minecraft.util.registry.RegistryTracker;
//$$ import net.minecraft.world.gen.GeneratorOptions;
//#endif
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelInfo;

public class SeedListScreen extends Screen {

	public Seed selectedSeed = null;
	public static ArrayList<Seed> seeds = new ArrayList<>();
	/** @see de.pfannekuchen.lotas.LoTAS List of all downloaded seeds */

	public static Map<String, Identifier> seedsId = new HashMap<>();

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
		super(new LiteralText("Seeds"));
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
			this.buttons.get(1).active = true;
		}
		return super.mouseClicked(mouseX, mouseY + 52, button);
	}

	/**
	 * Downloads the Seed to an Identifier
	 * @param seed
	 */
	public Identifier downloadSeed(String seed) throws IOException {
		if (seedsId.containsKey(seed + ""))
			return seedsId.get(seed + "");
		URL url = new URL("http://mgnet.work/seeds/" + seed + ".png");
		NativeImage image = NativeImage.read(url.openStream());
		NativeImageBackedTexture txt = new NativeImageBackedTexture(image);
		Identifier iff = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(seed + "", txt);
		seedsId.put(seed + "", iff);
		return iff;
	}

	/**
	 * Adds Done and Create World buttons to Seed menu
	 */
	@Override
	protected void init() {
		this.addButton(new NewButtonWidget(width / 2 - 100, height - 28, 200, 20, "Done", button -> {
			MinecraftClient.getInstance().openScreen(new SelectWorldScreen(new TitleScreen()));
		}));
		ButtonWidget create = new NewButtonWidget(width / 2 - 100, height - 52, 200, 20, "Create World", button -> {
			MinecraftClient.getInstance().openScreen(new ProgressScreen());
			//#if MC>=11601
//$$ 			LevelInfo levelInfo = new LevelInfo(selectedSeed.name, GameMode.SURVIVAL, false, Difficulty.NORMAL, true, new GameRules(), new DataPackSettings(new ArrayList<>(), new ArrayList<>()));
//$$ 			CreateWorldScreen createWorldScreen = new CreateWorldScreen(this, levelInfo, new GeneratorOptions(Long.parseLong(selectedSeed.seed), true, false, GeneratorOptions.method_28608(DimensionType.method_28517(Long.parseLong(selectedSeed.seed)), GeneratorOptions.createOverworldGenerator(Long.parseLong(selectedSeed.seed)))), null, RegistryTracker.create());
			//#else
			CreateWorldScreen createWorldScreen = new CreateWorldScreen(this);
			AccessorCreateWorldScreen accessorCWS = (AccessorCreateWorldScreen) createWorldScreen;
			accessorCWS.setSeed(selectedSeed.seed);
			accessorCWS.setCheatsEnabled(true);
			//#endif
			MinecraftClient.getInstance().openScreen(createWorldScreen);
		});
		create.active = false;
		this.addButton(create);
		super.init();
	}

	/**
	 * Renders background from top to bottom
	 */
	protected void renderHoleBackground(int top, int bottom, int alphaTop, int alphaBottom) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		//#if MC>=11601
//$$ 		MinecraftClient.getInstance().getTextureManager().bindTexture(DrawableHelper.BACKGROUND_TEXTURE);
		//#else
		MinecraftClient.getInstance().getTextureManager().bindTexture(DrawableHelper.BACKGROUND_LOCATION);
		//#endif
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
		bufferBuilder.vertex((double) 0, (double) bottom, 0.0D).texture(0.0F, ((float) bottom / 32.0F)).color(64, 64, 64, alphaBottom).next();
		bufferBuilder.vertex((double) (0 + this.width), (double) bottom, 0.0D).texture(((float) this.width / 32.0F), ((float) bottom / 32.0F)).color(64, 64, 64, alphaBottom).next();
		bufferBuilder.vertex((double) (0 + this.width), (double) top, 0.0D).texture(((float) this.width / 32.0F), ((float) top / 32.0F)).color(64, 64, 64, alphaTop).next();
		bufferBuilder.vertex((double) 0, (double) top, 0.0D).texture(0.0F, ((float) top / 32.0F)).color(64, 64, 64, alphaTop).next();
		tessellator.draw();
	}

	/**
	 * Draw all Seed Entries from seeds
	 */
	public void drawSeeds(Object obj) throws IOException {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		int y = 52;
		int x = this.width / 3;
		int n = 32;
		for (Seed seed : seeds) {
			if (selectedSeed == seed) {
				double r = x;
				//#if MC>=11601
//$$ 				int q = x + MinecraftClient.getInstance().textRenderer.getWidth(seed.description) + 38;
				//#else
				int q = x + MinecraftClient.getInstance().textRenderer.getStringWidth(seed.description) + 38;
				//#endif
				GlStateManager.disableTexture();
				float f = 1.0F;
				GlStateManager.color4f(f, f, f, 0.5F);
				bufferBuilder.begin(7, VertexFormats.POSITION);
				bufferBuilder.vertex((double) r - 1, (double) (y + n + 2), 1.0D).next();
				bufferBuilder.vertex((double) q - 1, (double) (y + n + 2), 1.0D).next();
				bufferBuilder.vertex((double) q - 1, (double) (y - 2), 1.0D).next();
				bufferBuilder.vertex((double) r - 1, (double) (y - 2), 1.0D).next();
				tessellator.draw();
				GlStateManager.color4f(0.0F, 0.0F, 0.0F, 0.5F);
				bufferBuilder.begin(7, VertexFormats.POSITION);
				bufferBuilder.vertex((double) (r), (double) (y + n + 1), 1.0D).next();
				bufferBuilder.vertex((double) (q - 2), (double) (y + n + 1), 1.0D).next();
				bufferBuilder.vertex((double) (q - 2), (double) (y - 1), 1.0D).next();
				bufferBuilder.vertex((double) (r), (double) (y - 1), 1.0D).next();
				tessellator.draw();
				GlStateManager.enableTexture();
			}
			//#if MC>=11601
//$$ 			MinecraftClient.getInstance().textRenderer.draw((MatrixStack) obj, seed.name, x + 35, y + 3, 16777215);
//$$ 			MinecraftClient.getInstance().textRenderer.draw((MatrixStack) obj, seed.description, x + 35, y + 14, 8421504);
//$$ 			MinecraftClient.getInstance().textRenderer.draw((MatrixStack) obj, seed.seed, x + 35, y + 24, 8421504);
			//#else
			MinecraftClient.getInstance().textRenderer.draw(seed.name, x + 35, y + 3, 16777215);
			MinecraftClient.getInstance().textRenderer.draw(seed.description, x + 35, y + 14, 8421504);
			MinecraftClient.getInstance().textRenderer.draw(seed.seed, x + 35, y + 24, 8421504);
			//#endif
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			try {
				MinecraftClient.getInstance().getTextureManager().bindTexture(downloadSeed(seed.seed));
				//#if MC>=11601
//$$ 				DrawableHelper.drawTexture((MatrixStack) obj, x + 1, y, 0.0F, 0.0F, 32, 32, 32, 32);
				//#else
				DrawableHelper.blit(x + 1, y, 0.0F, 0.0F, 32, 32, 32, 32);
				//#endif
			} catch (Exception e) {

			}
			y += 40;
		}
	}

	//#if MC>=11601
//$$ 	@Override public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
	//#else
	@Override public void render(int mouseX, int mouseY, float delta) {
	//#endif
		int left = 0;
		int right = width;
		int top = 48;
		int bottom = height - 64;

		//#if MC>=11601
//$$ 		renderBackground(matrices);
		//#else
		renderBackground();
		//#endif

		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		//#if MC>=11601
//$$ 		MinecraftClient.getInstance().getTextureManager().bindTexture(DrawableHelper.BACKGROUND_TEXTURE);
		//#else
		MinecraftClient.getInstance().getTextureManager().bindTexture(DrawableHelper.BACKGROUND_LOCATION);
		//#endif
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
		bufferBuilder.vertex((double) left, (double) bottom, 0.0D).texture(((float) left / 32.0F), ((float) (bottom + (int) 1) / 32.0F)).color(32, 32, 32, 255).next();
		bufferBuilder.vertex((double) right, (double) bottom, 0.0D).texture(((float) right / 32.0F), ((float) (bottom + (int) 1) / 32.0F)).color(32, 32, 32, 255).next();
		bufferBuilder.vertex((double) right, (double) top, 0.0D).texture(((float) right / 32.0F), ((float) (top + (int) 1) / 32.0F)).color(32, 32, 32, 255).next();
		bufferBuilder.vertex((double) left, (double) top, 0.0D).texture(((float) left / 32.0F), ((float) (top + (int) 1) / 32.0F)).color(32, 32, 32, 255).next();
		tessellator.draw();

		// OpenGL Stuff
		GlStateManager.disableDepthTest();
		this.renderHoleBackground(0, top, 255, 255);
		this.renderHoleBackground(bottom, this.height, 255, 255);
		GlStateManager.enableBlend();
		//#if MC>=11502
		//#if MC>=11601
//$$ 		GlStateManager.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.field_22545, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.field_22528, GlStateManager.SrcFactor.ZERO.field_22545, GlStateManager.DstFactor.ONE.field_22528);
		//#else
//$$ 		GlStateManager.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SrcFactor.ZERO.value, GlStateManager.DstFactor.ONE.value);
		//#endif
		//#else
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
		//#endif
		GlStateManager.disableAlphaTest();
		GlStateManager.shadeModel(7425);
		GlStateManager.disableTexture();

		// Draw top gradient
		bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
		bufferBuilder.vertex(left, top + 4, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 0).next();
		bufferBuilder.vertex(right, top + 4, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 0).next();
		bufferBuilder.vertex(right, top, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 255).next();
		bufferBuilder.vertex(left, top, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
		tessellator.draw();

		// Draw bottom gradient
		bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
		bufferBuilder.vertex(left, bottom, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 255).next();
		bufferBuilder.vertex(right, bottom, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 255).next();
		bufferBuilder.vertex(right, bottom - 4, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 0).next();
		bufferBuilder.vertex(left, bottom - 4, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 0).next();
		tessellator.draw();

		GlStateManager.enableTexture();
		GlStateManager.shadeModel(7424);
		GlStateManager.enableAlphaTest();
		GlStateManager.disableBlend();

		//#if MC>=11601
//$$ 		drawCenteredString(matrices, MinecraftClient.getInstance().textRenderer, "Seeds", width / 2, 8, 0xFFFFFF);
		//#else
		drawCenteredString(MinecraftClient.getInstance().textRenderer, "Seeds", width / 2, 8, 0xFFFFFF);
		//#endif

		try {
			 //#if MC>=11601
//$$ 			drawSeeds(matrices);
			//#else
			drawSeeds(null);
			//#endif
		} catch (IOException e) {
			e.printStackTrace();
		}
		//#if MC>=11601
//$$ 		super.render(matrices, mouseX, mouseY, delta);
		//#else
		super.render(mouseX, mouseY, delta);
		//#endif
	}
}