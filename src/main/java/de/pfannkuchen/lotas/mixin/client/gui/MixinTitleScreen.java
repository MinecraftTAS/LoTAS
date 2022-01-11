package de.pfannkuchen.lotas.mixin.client.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.gui.EmptyScreen;
import de.pfannkuchen.lotas.gui.RecorderLoScreen;
import de.pfannkuchen.lotas.gui.widgets.CustomImageButton;
import de.pfannkuchen.lotas.util.OSUtils;
import de.pfannkuchen.lotas.util.OSUtils.OS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Adds another button the the Title Screen for starting and ending a recording
 * @author Pancake
 */
@Mixin(TitleScreen.class)
@Environment(EnvType.CLIENT)
public abstract class MixinTitleScreen extends Screen {

	protected MixinTitleScreen(Component component) { super(component); }

	/**
	 * Texture for the stream indicator
	 */
	@Unique
	private ResourceLocation streaming = new ResourceLocation("textures/gui/stream_indicator.png");

	/**
	 * Add a recording buton
	 * @param ci Callback Info
	 */
	@Inject(method = "init", at = @At("RETURN"))
	public void afterInit(CallbackInfo ci) {
		this.addRenderableWidget(new CustomImageButton(this.width / 2 - 148, this.height / 4 + 48 + 84, 20, 20, 0, 0, 0, this.streaming, 16, 64, b -> {
			if (((CustomImageButton) b).isToggled) {
				this.minecraft.setScreen(new EmptyScreen());
				ClientLoTAS.loscreenmanager.setScreen(new RecorderLoScreen());
			} else {
				ClientLoTAS.recordermod.stopRecording();
			}
		}, (buttonWidget, matrixStack, i, j) -> {
			if (buttonWidget.isMouseOver(i, j)) this.renderTooltip(matrixStack, this.minecraft.font.split(new TextComponent(OSUtils.getOS() == OS.WINDOWS ? "If you turn on the recording then LoTAS will record your gameplay and save it to your Videos Folder. It is automatically adapting to the tickrate and removes the pause screens. IT IS RECOMMENDED TO TURN OFF VSYNC" : "This feature is only available for Windows Users"), Math.max(this.width / 2 - 43, 170)), i, j);
		}, ClientLoTAS.recordermod == null ? false : ClientLoTAS.recordermod.isRecording())).active = OSUtils.getOS() == OS.WINDOWS;
	}

}
