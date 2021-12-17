package de.pfannekuchen.lotas.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.TextureYoinker;
import de.pfannekuchen.lotas.gui.InfoHud;
import de.pfannekuchen.lotas.gui.SeedListScreen;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

/**
 * Fabric Mod Entry Point, initializes stuff here
 * @author Pancake
 * @since v1.0
 * @version v2.0
 */
public class LoTASModContainer implements ModInitializer {

	/** Texture for the Shield */
	public static ResourceLocation shield;
	/** The only info gui */
	public static InfoHud hud;
	
	public static long i = -1;
	
	/**
	 * Called by the Fabric Loader, whenever the Mod is being initialized
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onInitialize() {
		hud = new InfoHud();
		/* Load the Seeds for the (disabled) Seeds Menu */
		try {
			loadSeeds();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/* Load the Configuration and set the tickrate if required */
		try {
			ConfigUtils.init(new File(Minecraft.getInstance().gameDirectory, "lotas.properties"));
			if (ConfigUtils.getBoolean("tools", "saveTickrate"))
				TickrateChangerMod.updateTickrate(ConfigUtils.getInt("hidden", "tickrate"));
		} catch (IOException e) {
			System.err.println("Couldn't load Configuration");
			e.printStackTrace();
		}
		/* Open the cubiomes helper thread */
		new Thread(() -> {
			try {
				ServerSocket s = new ServerSocket(4200);
				while (true) {
					try {
						Socket sock = s.accept();
						if (sock != null) {
							DataInputStream stream = new DataInputStream(sock.getInputStream());
							i = Long.parseLong(stream.readLine());
							
							stream.close();
							sock.close();
						}						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * Load Shields loads a shield texture that has been modified for specific people
	 */
	public static void loadShieldsTASTools() {
		String uuid = Minecraft.getInstance().getUser().getGameProfile().getId().toString();

		try {
			URL url = new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/shieldnames.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				if (line.split(":")[0].equalsIgnoreCase(uuid)) {
					LoTASModContainer.shield = TextureYoinker.downloadShield(uuid, new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/" + line.split(":")[1]).openStream());
					break;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		LoTASModContainer.shield=new ResourceLocation("textures/shield/bottleshield.png");
		//		AccessorModelLoader.setShieldBase(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, new Identifier("lotas","pan_cake")));
	}
	
	public static void loadShieldsMCTAS() {
		String uuid = Minecraft.getInstance().getUser().getGameProfile().getId().toString();

		String urlname = "https://minecrafttas.com/" + uuid;
		URL url = null;
		
		try {
			url = new URL(urlname);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setReadTimeout(5000);
			connection.setInstanceFollowRedirects(false);
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_MOVED_PERM) {
				urlname = connection.getHeaderField("Location");
				LoTASModContainer.shield = TextureYoinker.downloadShield(uuid, new URL(urlname).openStream());
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LoTASModContainer.shield=new ResourceLocation("textures/shield/bottleshield.png");
	}
	

	/**
	 * Loads a list of seeds together with preview images from <a href="http://mgnet.work/seeds/">mgnet.work/seeds/seedsX.XX.X.txt</a> and creates a List
	 * @throws IOException
	 */
	public void loadSeeds() throws Exception {
		File file = new File("seeddata.txt");
		try {
			URL url = new URL("https://data.mgnet.work/lotas/seeds/1.14.4.txt"); // TODO: wait why does this say 1.14.4...
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
