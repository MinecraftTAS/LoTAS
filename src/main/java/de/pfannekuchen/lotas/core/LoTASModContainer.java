package de.pfannekuchen.lotas.core;

//#if MC>=10900
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
//#endif
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

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.gui.GuiSeedList.SeedListExtended;
import de.pfannekuchen.lotas.gui.GuiSeedList.SeedListExtended.SeedEntry;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(acceptedMinecraftVersions = "@MC_VERSION@", version = "@MOD_VERSION@", modid = "lotas", clientSideOnly = true, canBeDeactivated = false, name = "LoTAS")
public class LoTASModContainer {

	public static final List<ChallengeMap> maps = new ArrayList<>();
	public static ResourceLocation shield;
	
	public static String version = ForgeVersion.mcVersion;
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		loadShields();
		KeybindsUtils.registerKeybinds();
	}
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		new Thread(() -> {
			ConfigUtils.init(new Configuration(e.getSuggestedConfigurationFile()));
			
			try {
				loadSeeds();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			if (ConfigUtils.getBoolean("tools", "saveTickrate")) {
				TickrateChangerMod.index = ConfigUtils.getInt("hidden", "tickrate");
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
			
			try {
				BufferedReader stream = new BufferedReader(new InputStreamReader(new URL("http://mgnet.work/taschallenges/maps" + version + ".txt").openStream()));
				int maps = Integer.parseInt(stream.readLine().charAt(0) + "");
				for (int i = 0; i < maps; i++) {
					ChallengeMap map = new ChallengeMap();
					
					map.displayName = stream.readLine();
					map.name = stream.readLine();
					map.description = stream.readLine();
					map.map = new URL("http://mgnet.work/taschallenges/" + stream.readLine());
					int board = Integer.parseInt(stream.readLine().charAt(0) + "");
					map.leaderboard = new String[board];
					for (int j = 0; j < board; j++) {
						map.leaderboard[j] = stream.readLine();
					}
					
					//#if MC>=10900
					ResourceLocation loc = new ResourceLocation("maps", map.name);
					ThreadDownloadImageData dw = new ThreadDownloadImageData((File) null, "http://mgnet.work/taschallenges/" + map.name + ".png", null, new ImageBufferDownload());
					Minecraft.getMinecraft().getTextureManager().loadTexture(loc, dw);
					map.resourceLoc = loc.getResourcePath();
					//#endif
					
					LoTASModContainer.maps.add(map);
					
					stream.readLine(); // Empty
				}
				stream.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}).start();
		
		MinecraftForge.EVENT_BUS.register(new EventUtils());
	}
	
	public void loadShields() {	
		String uuid = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
		
		try {
			URL url = new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/shieldnames.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				if (line.split(":")[0].equalsIgnoreCase(uuid)) {
					ThreadDownloadImageData thread = new ThreadDownloadImageData(null, "https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/" + line.split(":")[1], null, new IImageBuffer() {
						
						@Override
						public void skinAvailable() {
							
						}
						
						@Override
						public BufferedImage parseUserSkin(BufferedImage image) {
							return image;
						}
					});
					LoTASModContainer.shield = new ResourceLocation("shield.png");
					Minecraft.getMinecraft().getTextureManager().loadTexture(LoTASModContainer.shield, thread);
					return;
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		LoTASModContainer.shield = BannerTextures.SHIELD_BASE_TEXTURE;
	}

	public void loadSeeds() throws Exception {
		File file = new File("seeddata.txt");
		try {
			URL url = new URL("http://mgnet.work/seeds/seeds" + version + ".txt");
			URLConnection conn = url.openConnection();
			conn.setReadTimeout(5000);
			file.createNewFile();
			FileUtils.copyInputStreamToFile(conn.getInputStream(), file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> strings = Files.readAllLines(file.toPath());
		int c = 0;
		for (String line : strings) {
			String seed = line.split(":")[0];
			String name = line.split(":")[1];
			String description = line.split(":")[2];
			SeedEntry entry = new SeedEntry(name, description, seed, c);
			//#if MC>=10900
			new Thread(() -> {
				entry.loc = new ResourceLocation("seeds", seed);
				ThreadDownloadImageData dw = new ThreadDownloadImageData((File) null, "http://mgnet.work/seeds/" + seed + ".png", null, new ImageBufferDownload());
				Minecraft.getMinecraft().getTextureManager().loadTexture(entry.loc, dw);
			}).start();
			//#endif
			SeedListExtended.seeds.add(entry);
			c++;
		}
	}
	
}
