package de.pfannekuchen.lotas.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.gui.InfoHud;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Forge Mod Entry Point, initializes stuff here
 * 
 * @author Pancake
 * @since v1.0
 * @version v2.0
 */
@Mod(acceptedMinecraftVersions = "@MC_VERSION@", version = "@MOD_VERSION@", modid = "lotas", clientSideOnly = true, canBeDeactivated = false, name = "LoTAS")
public class LoTASModContainer {

	/** Custom shield */
	public static ResourceLocation shield;
	/** World spawn offset X */
	public static int offsetX = 0;
	/** World spawn offset Z */
	public static int offsetZ = 0;
	/** Only ever InfoHud instance */
	public static InfoHud hud;

	/** Minecraft Version */
	public static String version = ForgeVersion.mcVersion;
	
	/**
	 * Called by MinecraftForge whenever our Mod gets initialized
	 * 
	 * @param e FMLInitializationEvent provided by MinecraftForge
	 */
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		loadShieldsMCTAS(); // load custom shields
		KeybindsUtils.registerKeybinds(); // register keybinds
		hud = new InfoHud(); // load info gui
	}

	/**
	 * Before the Mod Loader launches the Minecraft Gui
	 * 
	 * @param e FMLPreInitializationEvent provided by MinecraftForge
	 */
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		new Thread(() -> {
			// Load configuration with suggested config file
			ConfigUtils.init(new Configuration(e.getSuggestedConfigurationFile()));

			// Load the tickrate if it's supposed to be saved
			if (ConfigUtils.getBoolean("tools", "saveTickrate")) {
				TickrateChangerMod.index = ConfigUtils.getInt("hidden", "tickrate");
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}

		}).start();

		// register forge events
		MinecraftForge.EVENT_BUS.register(new EventUtils());
	}

	/**
	 * Method that loads the custom shields for LoTAS from the <a href=
	 * "https://github.com/ScribbleLP/MC-TASTools/tree/1.12.2/shields">TASTools<a/>
	 * repository
	 * 
	 * @author Scribble
	 */
	public void loadShieldsTASTools() {
		// load shields from server
		// #if MC>=10900
		String uuid = Minecraft.getMinecraft().getSession().getProfile().getId().toString();

		try {
			// read file that contains all uuids for shields
			URL url = new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/shieldnames.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = reader.readLine();
			while (line != null) {
				if (line.split(":")[0].equalsIgnoreCase(uuid)) {
					// load shield texture from uuid and return
					ThreadDownloadImageData thread = new ThreadDownloadImageData(null, "https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/" + line.split(":")[1], null, new IImageBuffer() {

						@Override
						public void skinAvailable() {

						}

						@Override
						public java.awt.image.BufferedImage parseUserSkin(java.awt.image.BufferedImage image) {
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
		// when there is no custom shield, set the texture to the normal one
		LoTASModContainer.shield = new ResourceLocation("lotas", "misc/bottleshield.png");
		// #endif
	}

	/**
	 * Method that loads the custom shields for LoTAS from
	 * <a href="https://minecrafttas.com">minecrafttas.com</a>
	 * 
	 * @author Scribble
	 */
	public void loadShieldsMCTAS() {
		// load shields from server
		// #if MC>=10900
		String uuid = Minecraft.getMinecraft().getSession().getProfile().getId().toString();

		String urlname = "https://minecrafttas.com/" + uuid;
		URL url = null;

		try {
			url = new URL(urlname);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setReadTimeout(5000);
			connection.setInstanceFollowRedirects(false);
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_MOVED_PERM) {
				urlname = connection.getHeaderField("Location");
				// load shield texture from uuid and return
				ThreadDownloadImageData thread = new ThreadDownloadImageData(null, urlname, null, new IImageBuffer() {

					@Override
					public void skinAvailable() {

					}

					@Override
					public java.awt.image.BufferedImage parseUserSkin(java.awt.image.BufferedImage image) {
						return image;
					}
				});
				LoTASModContainer.shield = new ResourceLocation("shield.png");
				Minecraft.getMinecraft().getTextureManager().loadTexture(LoTASModContainer.shield, thread);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// when there is no custom shield, set the texture to the normal one
		LoTASModContainer.shield = new ResourceLocation("lotas", "misc/bottleshield.png");
		// #endif
	}
}
