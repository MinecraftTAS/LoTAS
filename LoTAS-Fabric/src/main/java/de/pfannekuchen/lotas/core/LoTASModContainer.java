package de.pfannekuchen.lotas.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.core.utils.TextureYoinker;
import de.pfannekuchen.lotas.gui.SeedListScreen;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class LoTASModContainer implements ModInitializer {

	public static Identifier shield;
	
	@Override
	public void onInitialize() {
		KeybindsUtils.registerKeybinds();
		try {
			loadSeeds();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			ConfigUtils.init(new File(MinecraftClient.getInstance().runDirectory, "lotas.properties"));
			if (ConfigUtils.getBoolean("tools", "saveTickrate")) TickrateChangerMod.updateTickrate(ConfigUtils.getInt("hidden", "tickrate"));
		} catch (IOException e) {
			System.err.println("Couldn't load Configuration");
			e.printStackTrace();
		}
	}
	
    public static void loadShields() {	
		String uuid = MinecraftClient.getInstance().getSession().getProfile().getId().toString();
		
		try {
			URL url = new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/shieldnames.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				if (line.split(":")[0].equalsIgnoreCase(uuid)) {
					LoTASModContainer.shield = TextureYoinker.downloadShield(uuid, new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/" + line.split(":")[1]).openStream());
					return;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * Loads a list of seeds together with preview images from <a href="http://mgnet.work/seeds/">mgnet.work/seeds/seedsX.XX.X.txt</a> and creates a List
     * @throws IOException
     */
    public void loadSeeds() throws Exception {
        File file = new File("seeddata.txt");
        try {
        	//#if MC>=11502
        //$$ 	URL url = new URL("http://mgnet.work/seeds/seeds1.15.2.txt");
        	//#else
            URL url = new URL("http://mgnet.work/seeds/seeds1.14.4.txt");
            //#endif
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(5000);
            file.createNewFile();
            FileUtils.copyInputStreamToFile(conn.getInputStream(), file);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        List<String> strings = Files.readAllLines(file.toPath());
        for (String line : strings) {
            String seed = line.split(":")[0];
            String name = line.split(":")[1];
            String description = line.split(":")[2];

            SeedListScreen.seeds.add(new SeedListScreen.Seed(seed, name, description));
        }
    }
    
}
