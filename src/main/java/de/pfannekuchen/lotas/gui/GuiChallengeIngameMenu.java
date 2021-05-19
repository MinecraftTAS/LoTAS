package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicates;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.mixin.accessors.AccessorMinecraftClient;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeLoader;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiChallengeIngameMenu extends GuiIngameMenu {
	
	@Override
    public void initGui() {
		double pX = MCVer.player(Minecraft.getMinecraft()).posX;
		double pY = MCVer.player(Minecraft.getMinecraft()).posY;
		double pZ = MCVer.player(Minecraft.getMinecraft()).posZ;
		DupeMod.trackedObjects = new ArrayList<EntityItem>();
        for (EntityItem item : MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension).getEntitiesWithinAABB(EntityItem.class, MCVer.aabb(pX - 16, pY - 16, pZ - 16, pX + 16, pY + 16, pZ + 16))) {
        	DupeMod.trackedObjects.add(item);
        }
        
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 - 16, I18n.format("menu.returnToMenu")));

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + -16, I18n.format("menu.returnToGame")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 48 + -16, I18n.format("menu.options")));
        
        this.buttonList.add(new GuiButton(31, this.width / 2 - 100, this.height / 4 + 72 + -16, I18n.format("Leaderboard")));
        this.buttonList.add(new GuiButton(32, this.width / 2 - 100, this.height / 4 + 96 + -16, I18n.format("Restart")));
        
        this.buttonList.add(new GuiButton(17, 5, 55, 98, 20, I18n.format("Save Items")));
        this.buttonList.add(new GuiButton(18, 5, 75, 98, 20, I18n.format("Load Items")));

        
        this.buttonList.add(new GuiButton(19, (width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Drops")));
        try {
    		this.buttonList.add(new GuiButton(20, (width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Dragon")));
    		this.buttonList.get(this.buttonList.size() - 1).enabled = MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension).getEntities(EntityDragon.class, Predicates.alwaysTrue()).size() != 0;
    	} catch (Exception e) {
    		System.out.println("No Enderdragon found.");
    	}
        this.buttonList.add(new GuiButton(15, 5, 15, 48, 20, I18n.format("+")));
        this.buttonList.add(new GuiButton(16, 55, 15, 48, 20, I18n.format("-")));
        
        this.buttonList.add(new GuiButton(21, (width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, I18n.format("Manipulate Spawning")));
        
        this.buttonList.add(new GuiCheckBox(22, 2, height - 20 - 15, I18n.format("Avoid taking damage"), !ConfigUtils.getBoolean("tools", "takeDamage")));
        this.buttonList.add(new GuiButton(23, 37, 115, 66, 20, I18n.format("Jump ticks")));
        this.buttonList.add(new GuiButton(24, 5, 115, 30, 20, I18n.format(((int) TickrateChangerMod.ticks[TickrateChangerMod.ji]) + "t")));
		this.buttonList.add(new GuiCheckBox(26, 2, height - 32 - 15, I18n.format("Drop towards me"), ConfigUtils.getBoolean("tools", "manipulateVelocityTowards")));
		this.buttonList.add(new GuiCheckBox(27, 2, height - 44 - 15, I18n.format("Drop away from me"), ConfigUtils.getBoolean("tools", "manipulateVelocityAway")));
		this.buttonList.add(new GuiCheckBox(28, 2, height - 56 - 15, I18n.format("Optimize Explosions"), ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance")));
		this.buttonList.add(new GuiCheckBox(30, 2, height - 68 - 15, I18n.format("Toggle R Auto Clicker"), ConfigUtils.getBoolean("tools", "lAutoClicker")));
		
		this.buttonList.add(new GuiButton(29, (width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, I18n.format("Rig AI")));
    }
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
            	ChallengeLoader.backupSession();
                button.enabled = false;
                MCVer.world(Minecraft.getMinecraft()).sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient)null);
                
                ChallengeMap.currentMap = null;
                try {
                	Field h = Minecraft.getMinecraft().getClass().getDeclaredField("field_71469_aa");
                	h.setAccessible(true);
                	//#if MC>=10900
                	h.set(Minecraft.getMinecraft(), new AnvilSaveConverter(new File(mc.mcDataDir, "saves"), Minecraft.getMinecraft().getDataFixer()));
                	//#else
                //$$ 	h.set(Minecraft.getMinecraft(), new AnvilSaveConverter(new File(mc.mcDataDir, "saves")));
                	//#endif
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
        
        // For Tickrate + and - call super, so we can access the tickrateField which is only in the mixin
        if (button.id == 15) {
			super.actionPerformed(button);
		} else if (button.id == 16) {
			super.actionPerformed(button);
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
		} else if (button.id == 26) {
			if (((GuiCheckBox) button).isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityAway", false);
				ConfigUtils.save();
				((GuiCheckBox) this.buttonList.get(16)).setIsChecked(false);
			}
			ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", ((GuiCheckBox) button).isChecked());
			ConfigUtils.save();
		} else if (button.id == 27) {
			if (((GuiCheckBox) button).isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", false);
				ConfigUtils.save();
				((GuiCheckBox) this.buttonList.get(15)).setIsChecked(false);
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
		} else if (button.id == 31) {
			mc.displayGuiScreen(new GuiChallengeLeaderboard());
		} else if (button.id == 32) {
			ChallengeLoader.reload();
		}
	}
	
}
