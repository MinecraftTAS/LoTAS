package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.LoTASIngameGui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Adds Utility Buttons to the Game Menu
 */
@Mixin(PauseScreen.class)
public abstract class MixinGuiIngameMenu extends Screen {
	protected MixinGuiIngameMenu(Component title) {
		super(title);
	}

	@Shadow @Final
	private boolean showPauseMenu;
	
	@Unique
	private LoTASIngameGui lotasGui;
	
	//#if MC>=11903
//$$ 	@Inject(at = @At("HEAD"), method = "init")
	//#else
	@Inject(at = @At("RETURN"), method = "init")
	//#endif
	public void addCustomButtons(CallbackInfo ci) {
		
		if(!showPauseMenu) return;
		
		// Move Buttons higher
		//#if MC<11903
		for (int i=0;i<MCVer.getButtonSize(this); i++) {
			Button guiButton=(Button)MCVer.getButton(this, i);
			guiButton.y -= 24;
		}
		((Button)MCVer.getButton(this, 7)).y += 24;
		//#endif
		
		lotasGui = new LoTASIngameGui(this);
		lotasGui.addCustomButtons();

	}

	public Button getButton(int index) {
		return (Button) MCVer.getButton(this, index);
	}
	
	//#if MC>=11601
//$$ 	@Inject(method = "render", at = @At("TAIL"))
//$$ 	public void drawScreen(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
//$$ 		MCVer.stack = stack;
	//#else
	@Inject(method = "render", at = @At("TAIL"))
	public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {	
	//#endif
		if(!showPauseMenu) return;
		
		lotasGui.drawScreen(mouseX, mouseY, partialTicks);
		
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		lotasGui.mouseClicked(mouseX, mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		boolean focused = lotasGui.keyPressed(keyCode, scanCode, modifiers);
		if(!focused) {
			return super.keyPressed(keyCode, scanCode, modifiers);
		}
		return true;
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		lotasGui.charTyped(typedChar, keyCode);
		return super.charTyped(typedChar, keyCode);
	}
	
	//#if MC>=11903
//$$ 	@Redirect(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/GridWidget$RowHelper;addChild(Lnet/minecraft/client/gui/components/AbstractWidget;I)Lnet/minecraft/client/gui/components/AbstractWidget;"))
//$$ 	public net.minecraft.client.gui.components.AbstractWidget redirect_createPauseMenu(net.minecraft.client.gui.components.GridWidget.RowHelper parent, net.minecraft.client.gui.components.AbstractWidget button, int i) {
//$$ 		parent.addChild(lotasGui.getSavestateButton());
//$$ 		parent.addChild(lotasGui.getLoadstateButton());
//$$ 		return parent.addChild(button, i);
//$$ 	}
//$$
//$$ 	@ModifyArg(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LayoutSettings;paddingTop(I)Lnet/minecraft/client/gui/components/LayoutSettings;"), index = 0)
//$$ 	public int redirect_createPauseMenu(int padding) {
//$$ 		return 25;
//$$ 	}
	//#endif
}
