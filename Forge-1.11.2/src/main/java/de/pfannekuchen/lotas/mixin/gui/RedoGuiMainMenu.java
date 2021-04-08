package de.pfannekuchen.lotas.mixin.gui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.gui.GuiAcceptance;
import de.pfannekuchen.lotas.gui.GuiConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import work.mgnet.identifier.Client;

@Mixin(GuiMainMenu.class)
public abstract class RedoGuiMainMenu extends GuiScreen {

	/**
	 *
	 * What does this file do?
	 * 
	 * This File removes the Multiplayer + Realms Button in the Title Screen and also
	 * changes the splash text to "TaS iS cHeAtInG !!1"
	 * 
	 */
	
	@Shadow
	public String splashText;
	@Shadow
	public GuiButton modButton;
	@Shadow
	public GuiButton realmsButton;

	@Inject(at = @At("RETURN"), method = "addSingleplayerMultiplayerButtons")
	public void redoaddSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_, CallbackInfo ci) {
		this.buttonList.get(1).id = 24;
		this.buttonList.get(1).displayString = "Speed up Video";
		this.buttonList.get(1).enabled = false;
		this.modButton.width = this.buttonList.get(1).width;
		this.modButton.visible = false;
		buttonList.add(new GuiButton(69, modButton.xPosition, modButton.yPosition, modButton.width, modButton.height, "Configuration"));
		this.realmsButton.visible = false;
        
        if (!ConfigManager.getBoolean("hidden", "acceptedDataSending")) {
        	Minecraft.getMinecraft().displayGuiScreen(new GuiAcceptance());
        } else {
        	// REMOVE THIS TRY CATCH!
        	try {
        		Client.main(null);
        	} catch (Exception e) {
				e.printStackTrace();
				// THIS IS BAD, NEED TO FIND A SOLUTION!
			}
        }
	}
	
    private static final int BUFFER_SIZE = 4096;

    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    
	@Inject(at= @At("HEAD"), method = "actionPerformed")
	public void actionPerformed(GuiButton button, CallbackInfo ci) throws IOException {
		if (button.id == 69) {
			mc.displayGuiScreen(new GuiConfiguration());
		} else if (button.id == 24) {
			
		}
	}
	
	@Inject(at = @At("HEAD"), method = "drawScreen")
	public void injectdrawScreen(CallbackInfo ci) {
		this.splashText = "TaS iS cHeAtInG !!1";
	}
	
}
