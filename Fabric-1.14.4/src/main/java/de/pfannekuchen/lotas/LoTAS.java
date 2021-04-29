package de.pfannekuchen.lotas;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.challenges.ChallengeMap;
import de.pfannekuchen.lotas.gui.SeedListScreen;
import de.pfannekuchen.lotas.utils.ConfigManager;
import de.pfannekuchen.lotas.utils.Hotkeys;
import de.pfannekuchen.lotas.utils.TextureYoinker;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import rlog.RLogAPI;

public class LoTAS implements ClientModInitializer {

	public static Identifier shield;
	public static final List<ChallengeMap> maps = new ArrayList<>();
	
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
        Hotkeys.registerKeybindings();
    }
    
    public static void loadShields() {	
		String uuid = MinecraftClient.getInstance().getSession().getProfile().getId().toString();
		
		try {
			URL url = new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/shieldnames.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				if (line.split(":")[0].equalsIgnoreCase(uuid)) {
					LoTAS.shield = TextureYoinker.downloadShield(uuid, new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/" + line.split(":")[1]).openStream());
					return;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
