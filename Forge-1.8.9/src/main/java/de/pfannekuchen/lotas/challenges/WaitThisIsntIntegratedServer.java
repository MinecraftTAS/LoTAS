package de.pfannekuchen.lotas.challenges;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;

public class WaitThisIsntIntegratedServer extends IntegratedServer {

	public WaitThisIsntIntegratedServer(Minecraft mcIn, String folderName, String worldName, WorldSettings settings) {
		super(mcIn, folderName, worldName, settings);
		this.anvilFile = new File(Minecraft.getMinecraft().mcDataDir, "challenges"); // EPIC
		this.anvilConverterForAnvilFile = new AnvilSaveConverter(anvilFile);
		// Never has been...
	}

	
	
}
