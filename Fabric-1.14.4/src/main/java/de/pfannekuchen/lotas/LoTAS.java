package de.pfannekuchen.lotas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.gui.SeedListScreen;
import de.pfannekuchen.lotas.utils.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import rlog.RLogAPI;

public class LoTAS implements ClientModInitializer {

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
    	try {
			ConfigManager.init(new File(MinecraftClient.getInstance().runDirectory, "lotas.properties"));
		} catch (IOException e) {
			System.err.println("Couldn't load Configuration");
			e.printStackTrace();
		}
        RLogAPI.logDebug("[PreInit] Downloading Seeds");
        try {
            loadSeeds();
        } catch (Exception e) {
        	RLogAPI.logError(e, "[PreInit] Reading Seeds File failed #0");
        }
    }
    
    /**
     * Loads a list of seeds together with preview images from <a href="http://mgnet.work/seeds/">mgnet.work/seeds/seeds1.14.4.txt</a> and creates a List
     * @throws IOException
     */
    public void loadSeeds() throws Exception {
        File file = new File("seeddata.txt");
        try {
            URL url = new URL("http://mgnet.work/seeds/seeds1.14.4.txt");
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(5000);
            file.createNewFile();
            FileUtils.copyInputStreamToFile(conn.getInputStream(), file);
        } catch (Exception e) {
        	RLogAPI.logError(e, "[Init] Downloading Seeds failed #1");
        }
        List<String> strings = Files.readAllLines(file.toPath());
        for (String line : strings) {
            String seed = line.split(":")[0];
            String name = line.split(":")[1];
            String description = line.split(":")[2];

            SeedListScreen.seeds.add(new SeedListScreen.Seed(seed, name, description));
        }
        RLogAPI.logDebug("[PreInit] " + SeedListScreen.seeds.size() + " seeds loaded and attached to Seed List.");
    }

}
