package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.reflection.IdentifierAc;
import de.pfannekuchen.lotas.gui.ConfigurationScreen;
import de.pfannekuchen.lotas.gui.VideoUpspeederScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Mixin(TitleScreen.class)
public abstract class MixinGuiMainMenu extends Screen {
	protected MixinGuiMainMenu(Component title) {
		super(title);
	}

	private boolean isAcceptance;
	private final ResourceLocation DEMO_BG = new ResourceLocation("textures/gui/demo_background.png");

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	public void redirectRender(CallbackInfo ci) {
		if (isAcceptance) {
			renderBackground();
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.getTextureManager().bind(DEMO_BG);
			int i = (this.width - 248) / 2;
			int j = (this.height - 166) / 2;
			this.blit(i, j, 0, 0, 248, 166);
			i += 10;
			j += 8;
			this.font.draw("LoTAS Cheat prevention", (float) i, (float) j, 2039583);
			j += 12;
			Options gameOptions = this.minecraft.options;
			this.font.draw(I18n.get("This mod collects a bit of data", gameOptions.keyUp.getTranslatedKeyMessage(), gameOptions.keyLeft.getTranslatedKeyMessage(), gameOptions.keyDown.getTranslatedKeyMessage(), gameOptions.keyRight.getTranslatedKeyMessage()), (float) i, (float) j, 5197647);
			this.font.draw(I18n.get("to prevent cheating."), (float) i, (float) (j + 12), 5197647);
			this.font.draw(I18n.get("Your data will be hashed and encrypted.", gameOptions.keyJump.getTranslatedKeyMessage()), (float) i, (float) (j + 24), 5197647);
			this.font.draw(I18n.get("\u00A7cYour Data is unreadable to anyone!", gameOptions.keyInventory.getTranslatedKeyMessage()), (float) i, (float) (j + 36), 5197647);
			this.font.drawWordWrap(I18n.get("If you are confused or worried, pm me on discord: MCPfannkuchenYT#9745."), i, j + 68, 218, 2039583);
			super.render(0, 0, 0f);
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "init", cancellable = true)
	public void redirectInit(CallbackInfo ci) {
		if (!ConfigUtils.getBoolean("hidden", "acceptedDataSending")) {
			isAcceptance = true;
			addButton(new Button(width / 2 - 116, height / 2 + 62 + -16, 114, 20, "Accept", c1 -> {
				ConfigUtils.setBoolean("hidden", "acceptedDataSending", true);
				ConfigUtils.save();
				Minecraft.getInstance().setScreen(new TitleScreen());
			}));
			addButton(new Button(width / 2 + 2, height / 2 + 62 + -16, 114, 20, "Decline", c2 -> {
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

	
	@Shadow
	private String splash;
	
	@Inject(method="init", at = @At("HEAD"))
	private void changeSplash(CallbackInfo ci) {
		splash="TaS iS cHeAtInG !!1";
	}

	/**
	 * @reason Why do you want me to put this here javac
	 * @author Pancake
	 */
	@Overwrite
	private void createNormalMenuOptions(int y, int spacingY) {
		addButton(new Button(this.width / 2 - 100, y, 200, 20, I18n.get("menu.singleplayer"), (Button) -> {
			Minecraft.getInstance().setScreen(new SelectWorldScreen(this));
		}));
		addButton(new Button(this.width / 2 - 100, y + spacingY * 1, 200, 20, I18n.get("Video Upspeeder"), (Button) -> {
			Minecraft.getInstance().setScreen(new VideoUpspeederScreen());
		}));
		addButton(new Button(this.width / 2 - 100, y + spacingY * 2, 200, 20, I18n.get("Configuration"), (Button) -> {
			Minecraft.getInstance().setScreen(new ConfigurationScreen());
		}));
	}

	protected abstract void switchToRealms();

}
