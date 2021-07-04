package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.reflection.IdentifierAc;
import de.pfannekuchen.lotas.gui.ConfigurationScreen;
import de.pfannekuchen.lotas.gui.VideoUpspeederScreen;
import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
//#if MC>=11700
//$$ import net.minecraft.client.option.GameOptions;
//#else
import net.minecraft.client.options.GameOptions;
//#endif
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Mixin(TitleScreen.class)
public abstract class MixinGuiMainMenu extends Screen {
	protected MixinGuiMainMenu(Text title) {
		super(title);
	}

	private boolean isAcceptance;
	private final Identifier DEMO_BG = new Identifier("textures/gui/demo_background.png");

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
//#if MC>=11601
//$$ public void redirectRender(net.minecraft.client.util.math.MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//$$ 	if (isAcceptance) {
//$$ 		renderBackground(matrices);
//$$ 		MCVer.color(1.0f, 1.0f, 1.0f, 1.0f);
//$$ 	 	MinecraftClient.getInstance().getTextureManager().bindTexture(DEMO_BG);
//$$ 		int i = (this.width - 248) / 2;
//$$ 		int j = (this.height - 166) / 2;
//$$ 		this.drawTexture(matrices, i, j, 0, 0, 248, 166);
//$$ 		i += 10;
//$$ 	 	j += 8;
//$$ 		this.textRenderer.draw(matrices, "LoTAS Cheat prevention", (float) i, (float) j, 2039583);
//$$ 		j += 12;
//$$ 		GameOptions gameOptions = MinecraftClient.getInstance().options;
//$$ 		this.textRenderer.draw(matrices, I18n.translate("This mod collects a bit of data", gameOptions.keyForward.getBoundKeyLocalizedText(), gameOptions.keyLeft.getBoundKeyLocalizedText(), gameOptions.keyBack.getBoundKeyLocalizedText(), gameOptions.keyRight.getBoundKeyLocalizedText()), (float) i, (float) j, 5197647);
//$$ 		this.textRenderer.draw(matrices, I18n.translate("to prevent cheating."), (float) i, (float) (j + 12), 5197647);
//$$ 		this.textRenderer.draw(matrices, I18n.translate("Your data will be hashed and encrypted.", gameOptions.keyJump.getBoundKeyLocalizedText()), (float) i, (float) (j + 24), 5197647);
//$$ 		this.textRenderer.draw(matrices, I18n.translate("\u00A7cYour Data is unreadable to anyone!", gameOptions.keyInventory.getBoundKeyLocalizedText()), (float) i, (float) (j + 36), 5197647);
//$$ 		this.textRenderer.drawTrimmed(new net.minecraft.text.LiteralText("If you are confused or worried, pm me on discord: MCPfannkuchenYT#9745."), i, j + 68, 218, 2039583);
//$$ 		super.render(matrices, mouseX, mouseY, delta);
//$$ 	}
//$$ }
//#else 
	public void redirectRender(CallbackInfo ci) {
		if (isAcceptance) {
			renderBackground();
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.getTextureManager().bindTexture(DEMO_BG);
			int i = (this.width - 248) / 2;
			int j = (this.height - 166) / 2;
			this.blit(i, j, 0, 0, 248, 166);
			i += 10;
			j += 8;
			this.font.draw("LoTAS Cheat prevention", (float) i, (float) j, 2039583);
			j += 12;
			GameOptions gameOptions = this.minecraft.options;
			this.font.draw(I18n.translate("This mod collects a bit of data", gameOptions.keyForward.getLocalizedName(), gameOptions.keyLeft.getLocalizedName(), gameOptions.keyBack.getLocalizedName(), gameOptions.keyRight.getLocalizedName()), (float) i, (float) j, 5197647);
			this.font.draw(I18n.translate("to prevent cheating."), (float) i, (float) (j + 12), 5197647);
			this.font.draw(I18n.translate("Your data will be hashed and encrypted.", gameOptions.keyJump.getLocalizedName()), (float) i, (float) (j + 24), 5197647);
			this.font.draw(I18n.translate("\u00A7cYour Data is unreadable to anyone!", gameOptions.keyInventory.getLocalizedName()), (float) i, (float) (j + 36), 5197647);
			this.font.drawTrimmed(I18n.translate("If you are confused or worried, pm me on discord: MCPfannkuchenYT#9745."), i, j + 68, 218, 2039583);
			super.render(0, 0, 0f);
			ci.cancel();
		}
	}
//#endif

	@Inject(at = @At("HEAD"), method = "init", cancellable = true)
	public void redirectInit(CallbackInfo ci) {
		if (!ConfigUtils.getBoolean("hidden", "acceptedDataSending")) {
			isAcceptance = true;
			MCVer.addButton(this, new NewButtonWidget(width / 2 - 116, height / 2 + 62 + -16, 114, 20, "Accept", c1 -> {
				ConfigUtils.setBoolean("hidden", "acceptedDataSending", true);
				ConfigUtils.save();
				MinecraftClient.getInstance().openScreen(new TitleScreen());
			}));
			MCVer.addButton(this, new NewButtonWidget(width / 2 + 2, height / 2 + 62 + -16, 114, 20, "Decline", c2 -> {
				System.exit(29);
			}));
			ci.cancel();
		} else {
        	new Thread(() -> {
        		try {
        			((IdentifierAc) Class.forName("Accessor").newInstance()).identify();
        		} catch (Exception e) {
					e.printStackTrace();
				}
        	}).start();
		}
	}

	//#if MC>=11601
	//#if MC>=11605
	//#if MC>=11700
//$$ 	@org.spongepowered.asm.mixin.injection.ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
//$$     private String modifyText(String original) {
//$$         return "TaS iS cHeAtInG !!1";
		//#else
//$$ 		@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredString(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
//$$ 	private void redirectdrawSplash(net.minecraft.client.util.math.MatrixStack matrices, TextRenderer textRenderer, String str, int centerX, int y, int color) {
//$$ 		TitleScreen.drawCenteredString(matrices, textRenderer, "TaS iS cHeAtInG !!1", centerX, y, color);
		//#endif
//$$ 	}
	//#else
//$$ 	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredString(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
//$$ 	private void redirectdrawSplash(TitleScreen titleScreen, net.minecraft.client.util.math.MatrixStack matrices, TextRenderer textRenderer, String str, int centerX, int y, int color) {
//$$ 		titleScreen.drawCenteredString(matrices, textRenderer, "TaS iS cHeAtInG !!1", centerX, y, color);
//$$ 	} 
	//#endif    
	//#else
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
	private void redirectdrawSplash(TitleScreen titleScreen, TextRenderer textRenderer, String str, int centerX, int y, int color) {
		titleScreen.drawCenteredString(textRenderer, "TaS iS cHeAtInG !!1", centerX, y, color);
	}
	//#endif

	/**
	 * @reason Why do you want me to put this here javac
	 * @author Pancake
	 */
	@Overwrite
	private void initWidgetsNormal(int y, int spacingY) {
		MCVer.addButton(this, new NewButtonWidget(this.width / 2 - 100, y, 200, 20, I18n.translate("menu.singleplayer"), (buttonWidget) -> {
			MinecraftClient.getInstance().openScreen(new SelectWorldScreen(this));
		}));
		MCVer.addButton(this, new NewButtonWidget(this.width / 2 - 100, y + spacingY * 1, 200, 20, I18n.translate("Video Upspeeder"), (buttonWidget) -> {
			MinecraftClient.getInstance().openScreen(new VideoUpspeederScreen());
		}));
		MCVer.addButton(this, new NewButtonWidget(this.width / 2 - 100, y + spacingY * 2, 200, 20, I18n.translate("Configuration"), (buttonWidget) -> {
			MinecraftClient.getInstance().openScreen(new ConfigurationScreen());
		}));
	}

	protected abstract void switchToRealms();

}
