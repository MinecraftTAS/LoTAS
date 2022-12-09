package de.pfannekuchen.lotas.mixin.render.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.LoTASGuiIngameMenu;
import de.pfannekuchen.lotas.mods.DupeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;

@Mixin(GuiIngameMenu.class)
public abstract class MixinGuiIngameMenu extends GuiScreen {

	/*	
	 * Adds a few utility buttons
	 */
	
	public GuiTextField savestateName;
	public GuiTextField tickrateField;
	
	private LoTASGuiIngameMenu lotasGui;
	
	@Inject(method = "initGui", at = @At("RETURN"))
	public void injectinitGui(CallbackInfo ci) {
		this.buttonList.set(1, new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + -16, I18n.format("menu.returnToGame")) {
			@Override
			public void playPressSound(SoundHandler soundHandlerIn) {
				// Don't play the sound when returning to game
			}
		});
		for (GuiButton guiButton : buttonList) {
			//#if MC>=11200
			guiButton.y -= 24;
			//#else
//$$ 			guiButton.yPosition -= 24;
			//#endif
		}
		double pX = MCVer.player(Minecraft.getMinecraft()).posX;
		double pY = MCVer.player(Minecraft.getMinecraft()).posY;
		double pZ = MCVer.player(Minecraft.getMinecraft()).posZ;
		DupeMod.trackedObjects = new ArrayList<EntityItem>();
        for (EntityItem item : MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension).getEntitiesWithinAABB(EntityItem.class, MCVer.aabb(pX - 16, pY - 16, pZ - 16, pX + 16, pY + 16, pZ + 16))) {
        	DupeMod.trackedObjects.add(item);
        }
		
        //#if MC>=11200
        buttonList.get(0).y += 24;
    	//#else
    //$$     buttonList.get(0).yPosition += 24;
        //#endif
		
        lotasGui = new LoTASGuiIngameMenu((GuiIngameMenu)(Object)this);
        lotasGui.addButtons();
	}
	
	/**
	 * All of this is just so eclipe doesn't throw a warning when exporting (worth it kappa)
	 * @reason We overwrite this because it's empty anyways
	 * @author Pancake
	 */
	@Overwrite
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(MCVer.getFontRenderer(mc), I18n.format("menu.game"), this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
	    
	    lotasGui.drawScreen(mouseX, mouseY, partialTicks);
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		lotasGui.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		lotasGui.keyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
	@Inject(method = "actionPerformed", at = @At("HEAD"))
	public void redoactionPerformed(GuiButton button, CallbackInfo ci) {
		lotasGui.actionPerformed(button);
	}
	
}