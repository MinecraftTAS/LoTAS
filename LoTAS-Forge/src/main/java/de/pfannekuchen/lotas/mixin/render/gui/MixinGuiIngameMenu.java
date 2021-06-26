package de.pfannekuchen.lotas.mixin.render.gui;

import java.awt.Color;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.gui.GuiAiManipulation;
import de.pfannekuchen.lotas.gui.GuiDragonManipulation;
import de.pfannekuchen.lotas.gui.GuiDropChanceManipulation;
import de.pfannekuchen.lotas.gui.GuiEntitySpawnManipulation;
import de.pfannekuchen.lotas.gui.GuiLoadstateMenu;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.client.config.GuiCheckBox;

@Mixin(GuiIngameMenu.class)
public abstract class MixinGuiIngameMenu extends GuiScreen {

	/*	
	 * Adds a few utility buttons
	 */
	
	@Unique
	private static ImmutableList<Integer> glitchedButtons = ImmutableList.of(17, 18, 22);
	@Unique
	private static ImmutableList<Integer> advancedButtons = ImmutableList.of(21, 22, 23, 24, 26, 27, 28, 29, 30);
	
	public GuiTextField savestateName;
	public GuiTextField tickrateField;
	
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
        this.buttonList.add(new GuiButton(13, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, I18n.format("Savestate")));
    	
    	GuiButton loadstate = new GuiButton(14, this.width / 2 + 2, this.height / 4 + 96 + -16, 98, 20, I18n.format("Loadstate"));
    	loadstate.enabled = SavestateMod.hasSavestate();
    	this.buttonList.add(loadstate);
        this.buttonList.add(new GuiButton(15, 5, 15, 48, 20, I18n.format("+")));
        this.buttonList.add(new GuiButton(16, 55, 15, 48, 20, I18n.format("-")));
        this.buttonList.add(new GuiButton(17, 5, 55, 98, 20, I18n.format("Save Items")));
        this.buttonList.add(new GuiButton(18, 5, 75, 98, 20, I18n.format("Load Items")));
        
        this.buttonList.add(new GuiButton(19, (width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Drops")));
    	try {
    		this.buttonList.add(new GuiButton(20, (width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Dragon")));
    		this.buttonList.get(this.buttonList.size() - 1).enabled = MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), MCVer.player(mc).dimension).getEntities(EntityDragon.class, Predicates.alwaysTrue()).size() >= 1;
    	} catch (Exception e) {
    		System.out.println("No Enderdragon found.");
    	}
        this.buttonList.add(new GuiButton(21, (width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Spawning")));
        
        this.buttonList.add(new GuiCheckBox(22, 2, height - 20 - 15, I18n.format("Avoid taking damage"), !ConfigUtils.getBoolean("tools", "takeDamage")));
        this.buttonList.add(new GuiButton(23, 37, 115, 66, 20, I18n.format("Jump ticks")));
        this.buttonList.add(new GuiButton(24, 5, 115, 30, 20, I18n.format(((int) TickrateChangerMod.ticks[TickrateChangerMod.ji]) + "t")));
		this.buttonList.add(new GuiButton(25, this.width / 2 - 100, this.height / 4 + 144 + -16, I18n.format("Reset Timer")));
		this.buttonList.add(new GuiCheckBox(26, 2, height - 32 - 15, I18n.format("Drop towards me"), ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")));
		this.buttonList.add(new GuiCheckBox(27, 2, height - 44 - 15, I18n.format("Drop away from me"), ConfigUtils.getBoolean("tools", "manipulateVelocityAway")));
		this.buttonList.add(new GuiCheckBox(28, 2, height - 56 - 15, I18n.format("Optimize Explosions"), ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance")));
		this.buttonList.add(new GuiCheckBox(30, 2, height - 68 - 15, I18n.format("Toggle R Auto Clicker"), ConfigUtils.getBoolean("tools", "lAutoClicker")));
		
		this.buttonList.add(new GuiButton(29, (width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, I18n.format("Rig AI")));
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
	    
	    
		if (getClass().getSimpleName().contains("GuiIngameMenu")) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				this.buttonList.get(7).displayString = "\u00A76Name Savestate";
				this.buttonList.get(8).displayString = "\u00A76Choose State";
			} else {
				this.buttonList.get(7).displayString = "Savestate";
				this.buttonList.get(8).displayString = "Loadstate";
			}	
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.buttonList.get(9).displayString = "\u00A76Custom";
			this.buttonList.get(10).displayString = "\u00A76Tickrate";
		} else {
			this.buttonList.get(9).displayString = "+";
			this.buttonList.get(10).displayString = "-";
		}	
		
		drawString(MCVer.getFontRenderer(mc), "Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);
		drawCenteredString(MCVer.getFontRenderer(mc), "Hold Shift to access more features", width / 2, this.height / 4 + 150, 0xFFFFFF);
		
		if (savestateName != null) savestateName.drawTextBox();
		if (tickrateField != null) tickrateField.drawTextBox();
		
		if (SavestateMod.showSavestateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showSavestateDone = false;
				return;
			}
			drawCenteredString(MCVer.getFontRenderer(mc), "\u00A76Savestate successful...", width / 2, 40, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		} else if (SavestateMod.showLoadstateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showLoadstateDone = false;
				return;
			}
			drawCenteredString(MCVer.getFontRenderer(mc), "\u00A76Loadstate successful...", width / 2, 40, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		}
		
		MCVer.getFontRenderer(mc).drawStringWithShadow("Tickjump", 10, 105, 0xFFFFFF);
		if(buttonList.get(17).enabled==false) {
			MCVer.getFontRenderer(mc).drawStringWithShadow("Tickjump is ready,", 8, 137, 0xFFFFFF);
			MCVer.getFontRenderer(mc).drawStringWithShadow("press ESC to continue", 8, 147, 0xFFFFFF);
		}
		MCVer.getFontRenderer(mc).drawStringWithShadow("Duping", 10, 45, 0xFFFFFF);
		int w = width - 5;
		MCVer.getFontRenderer(mc).drawStringWithShadow("Tracked Items Delay: ", w - MCVer.getFontRenderer(mc).getStringWidth("Tracked Items Delay: ") - 1, 10, 0xFFFFFFFF);
		/*int h = 22;
		for (EntityItem item : DupeMod.trackedObjects) {
			
			Temporary removed as to Mixin Bug, that is patched in 0.8, but 0.8 doesn't work with 1.8
			//#if MC>=11200
			MCVer.getFontRenderer(mc).drawStringWithShadow(((AccessorEntityItem) item).pickupDelay() + "t " + item.getItem().getDisplayName(), w - MCVer.getFontRenderer(mc).getStringWidth("Tracked Items Delay: "), h, 0xFFFFFFFF);
			//#else
//$$ 			MCVer.getFontRenderer(mc).drawStringWithShadow(((AccessorEntityItem) item).pickupDelay() + "t " + item.getEntityItem().getDisplayName(), w - MCVer.getFontRenderer(mc).getStringWidth("Tracked Items Delay: "), h, 0xFFFFFFFF);
			//#endif
			
			h += 10;
		}*/
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (savestateName != null) {
			savestateName.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if (tickrateField != null) {
			tickrateField.mouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (savestateName != null) {
			boolean focused = savestateName.isFocused();
			savestateName.textboxKeyTyped(typedChar, keyCode);
			if (keyCode == Keyboard.KEY_RETURN && focused) {
				if (savestateName.getText().isEmpty()) {
					SavestateMod.savestate(null);
				} else {
					SavestateMod.savestate(savestateName.getText());
				}
			}
		}
		if (tickrateField != null) {
			boolean focused = tickrateField.isFocused();
			tickrateField.textboxKeyTyped(typedChar, keyCode);
			if (keyCode == Keyboard.KEY_RETURN && focused) {
				if (!tickrateField.getText().isEmpty()) {
					TickrateChangerMod.updateTickrate(Float.parseFloat(tickrateField.getText()));
				}
			}
		}
		// new GuiTextField(0, MCVer.getFontRenderer(Minecraft.getMinecraft()), 4, 15, 103, 20)
		super.keyTyped(typedChar, keyCode);
	}
	
	@Inject(method = "actionPerformed", at = @At("HEAD"))
	public void redoactionPerformed(GuiButton button, CallbackInfo ci) {
		if (button.id == 13) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				savestateName = new GuiTextField(93, MCVer.getFontRenderer(mc), this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20);
				button.enabled = false;
				savestateName.setFocused(true);
			} else SavestateMod.savestate(null);
		} else if (button.id == 14) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) mc.displayGuiScreen(new GuiLoadstateMenu());
			else SavestateMod.loadstate(-1);
		} else if (button.id == 15) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				tickrateField = new GuiTextField(0, MCVer.getFontRenderer(Minecraft.getMinecraft()), 4, 15, 103, 20);
				button.enabled = false;
				tickrateField.setFocused(true);
			} else {
				TickrateChangerMod.index++;
				TickrateChangerMod.index = MCVer.clamp(TickrateChangerMod.index, 0, 11);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
			// 
		} else if (button.id == 16) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				tickrateField = new GuiTextField(0, MCVer.getFontRenderer(Minecraft.getMinecraft()), 4, 15, 103, 20);
				button.enabled = false;
				tickrateField.setFocused(true);
			} else {
				TickrateChangerMod.index--;
				TickrateChangerMod.index = MCVer.clamp(TickrateChangerMod.index, 0, 11);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		} else if (button.id == 17) {
			DupeMod.saveItems();
			DupeMod.saveChests();
			button.enabled = false;
		} else if (button.id == 18) {
			DupeMod.loadItems();
			DupeMod.loadChests();
			button.enabled = false;
		} else if (button.id == 19) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiDropChanceManipulation((GuiIngameMenu) (Object) this));
		} else if (button.id == 20) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiDragonManipulation(this));
		} else if (button.id == 21) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiEntitySpawnManipulation());
		} else if (button.id == 22) {
			ConfigUtils.setBoolean("tools", "takeDamage", !((GuiCheckBox) button).isChecked());
			ConfigUtils.save();
		} else if (button.id == 23) {
			TickrateChangerMod.ticksToJump = (int) TickrateChangerMod.ticks[TickrateChangerMod.ji];
			button.enabled = false;
			button.displayString = "Jumping...";
		} else if (button.id == 24) {
			TickrateChangerMod.ji++;
			if (TickrateChangerMod.ji > 11) TickrateChangerMod.ji = 2;
			buttonList.clear();
			initGui();
		} else if (button.id == 25) {
			Timer.ticks = -1;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
		} else if (button.id == 26) {
			if (((GuiCheckBox) button).isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityAway", false);
				ConfigUtils.save();
				((GuiCheckBox) this.buttonList.get(21)).setIsChecked(false);
			}
			ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", ((GuiCheckBox) button).isChecked());
			ConfigUtils.save();
		} else if (button.id == 27) {
			if (((GuiCheckBox) button).isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", false);
				ConfigUtils.save();
				((GuiCheckBox) this.buttonList.get(20)).setIsChecked(false);
			}
			ConfigUtils.setBoolean("tools", "manipulateVelocityAway", ((GuiCheckBox) button).isChecked());
			ConfigUtils.save();
		} else if (button.id == 28) {
			ConfigUtils.setBoolean("tools", "manipulateExplosionDropChance", ((GuiCheckBox) button).isChecked());
			ConfigUtils.save();
		} else if (button.id == 29) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiAiManipulation());
		} else if (button.id == 30) {
			ConfigUtils.setBoolean("tools", "lAutoClicker", ((GuiCheckBox) button).isChecked());
			ConfigUtils.save();
		}
	}
	
}