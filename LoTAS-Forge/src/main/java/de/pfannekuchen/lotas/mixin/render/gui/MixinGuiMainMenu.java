package de.pfannekuchen.lotas.mixin.render.gui;

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

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiConfiguration;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
	
	@Shadow
	public String splashText;
	//#if MC>=10900
	@Shadow(remap = false)
	public GuiButton modButton;
	//#endif
	@Shadow
	public GuiButton realmsButton;

    private static final int BUFFER_SIZE = 4096;

	@Inject(at = @At("RETURN"), method = "addSingleplayerMultiplayerButtons")
	public void redoaddSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_, CallbackInfo ci) {
		this.buttonList.get(1).enabled=false;
		this.buttonList.get(1).displayString = I18n.format("menu.lotas.multiplayer");
		
		//#if MC>=10900
		this.modButton.width = this.buttonList.get(1).width;
		this.modButton.visible = false;
		//#endif
		
		buttonList.add(new GuiButton(69, width / 2 - (this.buttonList.get(1).width / 2), MCVer.y(this.buttonList.get(1)) + 24, this.buttonList.get(1).width, 20, I18n.format("config.lotas.buttontext")));
		this.realmsButton.visible = false;
	}
	
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
		}
	}
	
	@Inject(at = @At("HEAD"), method = "drawScreen")
	public void injectdrawScreen(CallbackInfo ci) {
		this.splashText = "TaS iS cHeAtInG !!1";
	}
	
}