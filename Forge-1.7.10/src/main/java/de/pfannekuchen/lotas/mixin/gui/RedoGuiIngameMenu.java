package de.pfannekuchen.lotas.mixin.gui;

import java.awt.Color;
import java.time.Duration;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpw.mods.fml.client.config.GuiCheckBox;
import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.dupemod.DupeMod;
import de.pfannekuchen.lotas.gui.GuiAIRig;
import de.pfannekuchen.lotas.gui.GuiEntitySpawner;
import de.pfannekuchen.lotas.gui.GuiLoadstate;
import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.savestate.SavestateMod;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

@Mixin(GuiIngameMenu.class)
public abstract class RedoGuiIngameMenu extends GuiScreen {

	/*	
	 * Adds a few utility buttons
	 */
	
	public GuiTextField savestateName;
	
	@ModifyArg(index = 3, method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngameMenu;drawCenteredString(Lnet/minecraft/client/gui/fontRendererObj;Ljava/lang/String;III)V"))
	public int cheeseIt(int in) {
		return in == 40 ? 15 : in;
	}
	
	@Inject(method = "initGui", at = @At("RETURN"))
	public void injectinitGui(CallbackInfo ci) {
		for (Object guiButton : buttonList) {
			((GuiButton) guiButton).yPosition -= 24;
		}
		
		double pX = Minecraft.getMinecraft().thePlayer.posX;
		double pY = Minecraft.getMinecraft().thePlayer.posY;
		double pZ = Minecraft.getMinecraft().thePlayer.posZ;
		DupeMod.trackedObjects = new ArrayList<EntityItem>();
        for (Object item : Minecraft.getMinecraft().theIntegratedServer.worldServerForDimension(Minecraft.getMinecraft().thePlayer.dimension).getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(pX - 16, pY - 16, pZ - 16, pX + 16, pY + 16, pZ + 16))) {
        	DupeMod.trackedObjects.add((EntityItem) item);
        }
		
        ((GuiButton) buttonList.get(0)).yPosition += 24;
    	buttonList.add(new GuiButton(13, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, I18n.format("Savestate")));
    	
    	GuiButton loadstate = new GuiButton(14, this.width / 2 + 2, this.height / 4 + 96 + -16, 98, 20, I18n.format("Loadstate"));
    	loadstate.enabled = SavestateMod.hasSavestate();
    	buttonList.add(loadstate);
        buttonList.add(new GuiButton(15, 5, 15, 48, 20, I18n.format("+")));
        buttonList.add(new GuiButton(16, 55, 15, 48, 20, I18n.format("-")));
        buttonList.add(new GuiButton(17, 5, 55, 98, 20, I18n.format("Save Items")));
        buttonList.add(new GuiButton(18, 5, 75, 98, 20, I18n.format("Load Items")));
        
        buttonList.add(new GuiButton(19, (width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Drops")));
    	try {
    		buttonList.add(new GuiButton(20, (width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, I18n.format("Dragon Attack")));
    		((GuiButton) buttonList.get(buttonList.size() - 1)).enabled = Minecraft.getMinecraft().theIntegratedServer.worldServerForDimension(mc.thePlayer.dimension).getEntitiesWithinAABB(EntityDragon.class, AxisAlignedBB.getBoundingBox(-500, 0, -500, 500, 255, 500)).size() >= 1;
    	} catch (Exception e) {
    		System.out.println("No Enderdragon found.");
    	}
        buttonList.add(new GuiButton(21, (width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Spawning")));
        
        buttonList.add(new GuiCheckBox(22, 2, height - 20 - 15, I18n.format("Avoid taking damage"), !ConfigManager.getBoolean("tools", "takeDamage")));
        buttonList.add(new GuiButton(23, 37, 107, 66, 20, I18n.format("Jump ticks")));
        buttonList.add(new GuiButton(24, 5, 107, 30, 20, I18n.format(TickrateChanger.ticks[TickrateChanger.ji] + "t")));
		buttonList.add(new GuiButton(25, this.width / 2 - 100, this.height / 4 + 144 + -16, I18n.format("Reset Timer")));
		buttonList.add(new GuiCheckBox(26, 2, height - 32 - 15, I18n.format("Drop towards me"), ConfigManager.getBoolean("tools", "manipulateVelocityTowards")));
		buttonList.add(new GuiCheckBox(27, 2, height - 44 - 15, I18n.format("Drop away from me"), ConfigManager.getBoolean("tools", "manipulateVelocityAway")));
		buttonList.add(new GuiCheckBox(28, 2, height - 56 - 15, I18n.format("Optimize Explosions"), ConfigManager.getBoolean("tools", "manipulateExplosionDropChance")));
		buttonList.add(new GuiCheckBox(30, 2, height - 68 - 15, I18n.format("Toggle R Auto Clicker"), ConfigManager.getBoolean("tools", "lAutoClicker")));
		
		buttonList.add(new GuiButton(29, (width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, I18n.format("Rig AI")));
	}
	
	@Inject(method = "drawScreen", at = @At("TAIL"))
	public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		
		if (getClass().getSimpleName().contains("GuiIngameMenu")) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				((GuiButton) buttonList.get(7)).displayString = "\u00A76Name Savestate";
				((GuiButton) buttonList.get(8)).displayString = "\u00A76Choose State";
			} else {
				((GuiButton) buttonList.get(7)).displayString = "Savestate";
				((GuiButton) buttonList.get(8)).displayString = "Loadstate";
			}	
		}
		
		drawString(mc.fontRendererObj, "Tickrate Changer (" + TickrateChanger.tickrate + ")", 5, 5, 0xFFFFFF);
		
		drawCenteredString(mc.fontRendererObj, "Hold Shift to access more features", width / 2, this.height / 4 + 150, 0xFFFFFF);
		
		if (savestateName != null) savestateName.drawTextBox();
		
		if (SavestateMod.showSavestateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showSavestateDone = false;
				return;
			}
			drawCenteredString(mc.fontRendererObj, "\u00A76Savestate successful...", width / 2, 40, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		} else if (SavestateMod.showLoadstateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showLoadstateDone = false;
				return;
			}
			drawCenteredString(mc.fontRendererObj, "\u00A76Loadstate successful...", width / 2, 40, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		}
		
		mc.fontRendererObj.drawStringWithShadow("Tickrate Changer", 10, 97, 0xFFFFFF);
		mc.fontRendererObj.drawStringWithShadow("Duping", 10, 45, 0xFFFFFF);
		int w = width - 5;
		mc.fontRendererObj.drawStringWithShadow("Tracked Items Delay: ", w - mc.fontRendererObj.getStringWidth("Tracked Items Delay: ") - 1, 10, 0xFFFFFFFF);
		int h = 22;
		for (EntityItem item : DupeMod.trackedObjects) {
			mc.fontRendererObj.drawStringWithShadow(item.delayBeforeCanPickup + "t " + item.getEntityItem().getDisplayName(), w - mc.fontRendererObj.getStringWidth("Tracked Items Delay: "), h, 0xFFFFFFFF);
			h += 10;
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (savestateName != null) {
			savestateName.mouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
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
		super.keyTyped(typedChar, keyCode);
	}
	
	@Inject(method = "actionPerformed", at = @At("HEAD"))
	public void redoactionPerformed(GuiButton button, CallbackInfo ci) {
		if (button.id == 13) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				savestateName = new GuiTextField(mc.fontRendererObj, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20);
				button.enabled = false;
				savestateName.setFocused(true);
			} else SavestateMod.savestate(null);
		} else if (button.id == 14) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) mc.displayGuiScreen(new GuiLoadstate());
			else SavestateMod.loadstate(-1);
		} else if (button.id == 15) {
			TickrateChanger.index++;
			TickrateChanger.index = MathHelper.clamp_int(TickrateChanger.index, 1, 10);
			TickrateChanger.updateTickrate(TickrateChanger.ticks[TickrateChanger.index]);
		} else if (button.id == 16) {
			TickrateChanger.index--;
			TickrateChanger.index = MathHelper.clamp_int(TickrateChanger.index, 1, 10);
			TickrateChanger.updateTickrate(TickrateChanger.ticks[TickrateChanger.index]);
		} else if (button.id == 17) {
			DupeMod.saveItems();
			DupeMod.saveChests();
			button.enabled = false;
		} else if (button.id == 18) {
			DupeMod.loadItems();
			DupeMod.loadChests();
			button.enabled = false;
		} else if (button.id == 19) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiLootManipulation((GuiIngameMenu) (Object) this));
		} else if (button.id == 20) {
			((EntityDragon) Minecraft.getMinecraft().theIntegratedServer.worldServerForDimension(mc.thePlayer.dimension).getEntitiesWithinAABB(EntityDragon.class, AxisAlignedBB.getBoundingBox(-500, 0, -500, 500, 255, 500)).get(0)).setNewTarget();
			button.enabled = false;
		} else if (button.id == 21) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiEntitySpawner());
		} else if (button.id == 22) {
			ConfigManager.setBoolean("tools", "takeDamage", !((GuiCheckBox) button).isChecked());
			ConfigManager.save();
		} else if (button.id == 23) {
			TickrateChanger.ticksToJump = TickrateChanger.ticks[TickrateChanger.ji];
			button.enabled = false;
			button.displayString = "Jumping...";
		} else if (button.id == 24) {
			TickrateChanger.ji++;
			if (TickrateChanger.ji > 10) TickrateChanger.ji = 1;
			buttonList.clear();
			initGui();
		} else if (button.id == 25) {
			Timer.ticks = -1;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
		} else if (button.id == 26) {
			if (((GuiCheckBox) button).isChecked()) {
				ConfigManager.setBoolean("tools", "manipulateVelocityAway", false);
				ConfigManager.save();
				((GuiCheckBox) buttonList.get(21)).setIsChecked(false);
			}
			ConfigManager.setBoolean("tools", "manipulateVelocityTowards", ((GuiCheckBox) button).isChecked());
			ConfigManager.save();
		} else if (button.id == 27) {
			if (((GuiCheckBox) button).isChecked()) {
				ConfigManager.setBoolean("tools", "manipulateVelocityTowards", false);
				ConfigManager.save();
				((GuiCheckBox) buttonList.get(20)).setIsChecked(false);
			}
			ConfigManager.setBoolean("tools", "manipulateVelocityAway", ((GuiCheckBox) button).isChecked());
			ConfigManager.save();
		} else if (button.id == 28) {
			ConfigManager.setBoolean("tools", "manipulateExplosionDropChance", ((GuiCheckBox) button).isChecked());
			ConfigManager.save();
		} else if (button.id == 29) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiAIRig());
		} else if (button.id == 30) {
			ConfigManager.setBoolean("tools", "lAutoClicker", ((GuiCheckBox) button).isChecked());
			ConfigManager.save();
		}
	}
	
}
