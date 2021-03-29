package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.google.common.base.Predicates;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.dupemod.DupeMod;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiChallengeEscape extends GuiIngameMenu {
	
	@Override
    public void initGui() {
		double pX = Minecraft.getMinecraft().thePlayer.posX;
		double pY = Minecraft.getMinecraft().thePlayer.posY;
		double pZ = Minecraft.getMinecraft().thePlayer.posZ;
		DupeMod.trackedObjects = new ArrayList<EntityItem>();
        for (EntityItem item : Minecraft.getMinecraft().getIntegratedServer().worldServerForDimension(Minecraft.getMinecraft().thePlayer.dimension).getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pX - 16, pY - 16, pZ - 16, pX + 16, pY + 16, pZ + 16))) {
        	DupeMod.trackedObjects.add(item);
        }
        
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 - 16, I18n.format("menu.returnToMenu")));

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + -16, I18n.format("menu.returnToGame")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 48 + -16, I18n.format("menu.options")));
        
        this.buttonList.add(new GuiButton(31, this.width / 2 - 100, this.height / 4 + 72 + -16, I18n.format("Leaderboard")));
        this.buttonList.add(new GuiButton(32, this.width / 2 - 100, this.height / 4 + 96 + -16, I18n.format("Restart")));
        
        this.buttonList.add(new GuiButton(15, 5, 15, 48, 20, I18n.format("+")));
        this.buttonList.add(new GuiButton(16, 55, 15, 48, 20, I18n.format("-")));
        this.buttonList.add(new GuiButton(17, 5, 55, 98, 20, I18n.format("Save Items")));
        this.buttonList.add(new GuiButton(18, 5, 75, 98, 20, I18n.format("Load Items")));
        
        this.buttonList.add(new GuiButton(19, (width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Drops")));
    	try {
    		this.buttonList.add(new GuiButton(20, (width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Dragon")));
    		this.buttonList.get(this.buttonList.size() - 1).enabled = Minecraft.getMinecraft().theIntegratedServer.worldServerForDimension(mc.thePlayer.dimension).getEntities(EntityDragon.class, Predicates.alwaysTrue()).size() != 0;
    	} catch (Exception e) {
    		System.out.println("No Enderdragon found.");
    	}
        this.buttonList.add(new GuiButton(21, (width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Spawning")));
        
        this.buttonList.add(new GuiCheckBox(22, 2, height - 20 - 15, I18n.format("Avoid taking damage"), !ConfigManager.getBoolean("tools", "takeDamage")));
        this.buttonList.add(new GuiButton(23, 35, 107, 68, 20, I18n.format("Jump ticks")));
        this.buttonList.add(new GuiButton(24, 5, 107, 30, 20, I18n.format(TickrateChanger.ticks[TickrateChanger.ji] + "t")));
		this.buttonList.add(new GuiCheckBox(26, 2, height - 32 - 15, I18n.format("Drop towards me"), ConfigManager.getBoolean("tools", "manipulateVelocityTowards")));
		this.buttonList.add(new GuiCheckBox(27, 2, height - 44 - 15, I18n.format("Drop away from me"), ConfigManager.getBoolean("tools", "manipulateVelocityAway")));
		this.buttonList.add(new GuiCheckBox(28, 2, height - 56 - 15, I18n.format("Optimize Explosions"), ConfigManager.getBoolean("tools", "manipulateExplosionDropChance")));
		this.buttonList.add(new GuiCheckBox(30, 2, height - 68 - 15, I18n.format("Toggle R Auto Clicker"), ConfigManager.getBoolean("tools", "lAutoClicker")));
		
		this.buttonList.add(new GuiButton(29, (width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, I18n.format("Rig AI")));
    }
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                button.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient)null);
                
                ChallengeLoader.map = null;
                try {
                	Field h = Minecraft.getMinecraft().getClass().getDeclaredField("saveLoader");
                	h.setAccessible(true);
                	h.set(Minecraft.getMinecraft(), new AnvilSaveConverter(new File(mc.mcDataDir, "saves")));
                } catch (Exception e) {
        			e.printStackTrace();
        		}
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
            case 4:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                break;
        }
        
        
		if (button.id == 15) {
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
			
		} else if (button.id == 21) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiEntitySpawner());
		} else if (button.id == 22) {
			ConfigManager.setBoolean("tools", "takeDamage", !((GuiCheckBox) button).isChecked());
			ConfigManager.save();
		} else if (button.id == 23) {
			TickrateChanger.ticksToJump = TickrateChanger.ticks[TickrateChanger.ji];
			button.enabled = false;
		} else if (button.id == 24) {
			TickrateChanger.ji++;
			if (TickrateChanger.ji > 10) TickrateChanger.ji = 0;
			buttonList.clear();
			initGui();
		} else if (button.id == 26) {
			if (((GuiCheckBox) button).isChecked()) {
				ConfigManager.setBoolean("tools", "manipulateVelocityAway", false);
				ConfigManager.save();
				((GuiCheckBox) this.buttonList.get(16)).setIsChecked(false);
			}
			ConfigManager.setBoolean("tools", "manipulateVelocityTowards", ((GuiCheckBox) button).isChecked());
			ConfigManager.save();
		} else if (button.id == 27) {
			if (((GuiCheckBox) button).isChecked()) {
				ConfigManager.setBoolean("tools", "manipulateVelocityTowards", false);
				ConfigManager.save();
				((GuiCheckBox) this.buttonList.get(15)).setIsChecked(false);
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
		} else if (button.id == 31) {
			mc.displayGuiScreen(new GuiLeaderboard());
		} else if (button.id == 32) {
			ChallengeLoader.reload();
		}
	}
	
}
