package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.ConfigurationScreen;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Removes and adds Buttons from the Title Screen
 * @author Pancake
 */
@Mixin(TitleScreen.class)
public abstract class MixinGuiMainMenu extends Screen {
	protected MixinGuiMainMenu(Component title) {
		super(title);
	}

	//#if MC>=12000
//$$ 	@Shadow
//$$ 	private net.minecraft.client.gui.components.SplashRenderer splash;
	//#else
	@Shadow
	private String splash;
	//#endif
	
	@Inject(method="init", at = @At("HEAD"))
	private void changeSplash(CallbackInfo ci) {
		if (ConfigUtils.getBoolean("tools", "saveTickrate")) {
			TickrateChangerMod.updatePitch();
		}
		//#if MC>=12000
//$$ 		splash=new SplashRenderer("TaS iS cHeAtInG !!1");
		//#else
		splash="TaS iS cHeAtInG !!1";
		//#endif
	}

	/**
	 * @reason Why do you want me to put this here javac
	 * @author Pancake
	 */
	@Overwrite
	private void createNormalMenuOptions(int y, int spacingY) {
		MCVer.addButton(this, MCVer.Button(this.width / 2 - 100, y, 200, 20, I18n.get("menu.singleplayer"), (Button) -> {
			Minecraft.getInstance().setScreen(new SelectWorldScreen(this));
		}));
		MCVer.addButton(this, MCVer.Button(this.width / 2 - 100, y + spacingY * 1, 200, 20, I18n.get("LoTAS doesn't work in multiplayer :("), (Button) -> {
			
		})).active=false;
		MCVer.addButton(this, MCVer.Button(this.width / 2 - 100, y + spacingY * 2, 200, 20, I18n.get("Configuration"), (Button) -> {
			Minecraft.getInstance().setScreen(new ConfigurationScreen());
		}));
	}

	protected abstract void switchToRealms();

}
