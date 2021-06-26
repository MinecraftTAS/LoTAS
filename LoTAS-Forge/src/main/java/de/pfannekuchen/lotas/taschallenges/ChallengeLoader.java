package de.pfannekuchen.lotas.taschallenges;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorMinecraftClient;
import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Session;
//#if MC>=11000
import net.minecraft.world.GameType;
//#else
//$$ import net.minecraft.world.WorldSettings.GameType;
//#endif
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

/**
 * Loader for all TAS Challenges. Manages loading and saving the world
 * @author Pancake
 * @since v1.1
 * @version v1.1
 */
public class ChallengeLoader {

	/** Temporary variable whether the timer should start */
	public static boolean startTimer;
	/** Session saved from the launcher whilst playing as 'TAS Battle' */
	public static Session cachedSession;
	
	/**
	 * Reloads the TAS Map again to reset it.
	 * @throws IOException Throws when the world didn't exist before
	 */
	public static void reload() throws IOException {
		Minecraft.getMinecraft().displayGuiScreen(null);
		SavestateMod.isLoading = true; // turn off rendering
		
		// don't save the world
		for (WorldServer w : MCVer.getWorlds(Minecraft.getMinecraft().getIntegratedServer())) {
			w.disableLevelSaving = true;
		}
		
		// quit the world
        MCVer.world(Minecraft.getMinecraft()).sendQuittingDisconnectingPacket();
        Minecraft.getMinecraft().loadWorld((WorldClient)null);
        // load the new world
        load(false);
	}
	
	/**
	 * Loads the TAS Challenge map from a backup
	 * @param reload Whether a new Thread should be used to download a unmodifid map or not
	 * @throws IOException Throws when the world didn't exist before
	 */
	public static void load(boolean reload) throws IOException {
		final File worldDir = new File(Minecraft.getMinecraft().mcDataDir, "challenges" + File.separator + "");
		if (worldDir.exists()) FileUtils.deleteDirectory(worldDir); // deletes the world
		
		// download the world
		if (reload) {
			final URL url = ChallengeMap.currentMap.map;
			ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
			
			final FileOutputStream fileOutputStream = new FileOutputStream("ChallengeMap.currentMap.zip");
			final FileChannel fileChannel = fileOutputStream.getChannel();
			
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			
			fileOutputStream.close();
			fileChannel.close();
		}
		// unzip the map
		unzip("ChallengeMap.currentMap.zip", "challenges/" + ChallengeMap.currentMap.name);
		
		// create a new thread to download the world again
		new Thread(() -> {
			try {
				// Download
				final URL url = ChallengeMap.currentMap.map;
				final ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
				
				final FileOutputStream fileOutputStream = new FileOutputStream("ChallengeMap.currentMap.zip");
				final FileChannel fileChannel = fileOutputStream.getChannel();
				
				fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
				
				fileOutputStream.close();
				fileChannel.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
		
		// load the world again
		startTimer = true; // request timer start
		launchIntegratedServer(ChallengeMap.currentMap.name, ChallengeMap.currentMap.name, new WorldSettings(0L, GameType.ADVENTURE, false, true, WorldType.FLAT));
	}
	
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Unzips a .zip file to a folder 
	 * @param zipFilePath Path to the Zip File
	 * @param destDirectory Path to the destination directory
	 * @throws IOException Throws whenever the folder does not exist
	 */
	private static void unzip(String zipFilePath, String destDirectory) throws IOException {
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
    
	/**
	 * Extracts a single file from a zip stream
	 * @param zipIn zip stream to read file from
	 * @param filePath path of file in file system
	 * @throws IOException zip reading error
	 */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    
    /**
     * Launch the integrated server with the TAS-Challenge Map 
     * @param folderName Name of the world
     * @param worldName Name of the world pt. 2
     * @param worldSettingsIn Settings of the world
     */
    private static void launchIntegratedServer(String folderName, String worldName, @Nullable WorldSettings worldSettingsIn) {
        // save session and play as tasbot
    	if (cachedSession == null) {
        	cachedSession = Minecraft.getMinecraft().getSession();
        	System.out.println(Minecraft.getMinecraft().getSession().getPlayerID());
        	System.out.println(Minecraft.getMinecraft().getSession().getToken());
        	((AccessorMinecraftClient) Minecraft.getMinecraft()).session(new Session("TASBot", "b8abdafc-5002-40df-ab68-63206ea4c7e8", "b8abdafc-5002-40df-ab68-63206ea4c7e8", "MOJANG"));
        }
        // overwrite the save loader to the second directory
        try {
        	Field h = Minecraft.getMinecraft().getClass().getDeclaredField("field_71469_aa");
        	h.setAccessible(true);
        	h.set(Minecraft.getMinecraft(), ChallengeMap.currentMap.getSaveLoader());
        } catch (Exception e) {
			e.printStackTrace();
		}
        
        // start the server
    	net.minecraftforge.fml.client.FMLClientHandler.instance().startIntegratedServer(folderName, worldName, worldSettingsIn);
        //#if MC>=10900
    	Minecraft.getMinecraft().loadWorld((WorldClient)null);
        //#endif
    	System.gc();

        
        ISaveHandler isavehandler = ChallengeMap.currentMap.getSaveLoader().getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (worldinfo == null && worldSettingsIn != null)
        {
            worldinfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }

        if (worldSettingsIn == null)
        {
            worldSettingsIn = new WorldSettings(worldinfo);
        }

        try
        {
        	//#if MC>=10900
        	com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService yggdrasilauthenticationservice = new com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), UUID.randomUUID().toString());
        	com.mojang.authlib.minecraft.MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
        	com.mojang.authlib.GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(Minecraft.getMinecraft().mcDataDir, MinecraftServer.USER_CACHE_FILE.getName()));
            TileEntitySkull.setProfileCache(playerprofilecache);
            TileEntitySkull.setSessionService(minecraftsessionservice);
            PlayerProfileCache.setOnlineMode(false);
            ((AccessorMinecraftClient) Minecraft.getMinecraft()).integratedServer(new IntegratedServer(Minecraft.getMinecraft(), folderName, worldName, worldSettingsIn, yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, playerprofilecache));
            //#else
            //$$ ((AccessorMinecraftClient) Minecraft.getMinecraft()).integratedServer(new IntegratedServer(Minecraft.getMinecraft(), folderName, worldName, worldSettingsIn));
            //#endif
            Minecraft.getMinecraft().getIntegratedServer().startServerThread();
            ((AccessorMinecraftClient) Minecraft.getMinecraft()).integratedServerIsRunning(true);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", worldName);
            throw new ReportedException(crashreport);
        }

        Minecraft.getMinecraft().loadingScreen.displaySavingString(I18n.format("menu.loadingLevel"));

        while (!Minecraft.getMinecraft().getIntegratedServer().serverIsInRunLoop())
        {
            if (!net.minecraftforge.fml.common.StartupQuery.check() || Minecraft.getMinecraft().getIntegratedServer().isServerStopped())
            {
            	Minecraft.getMinecraft().loadWorld(null);
                Minecraft.getMinecraft().displayGuiScreen(null);
                return;
            }
            String s = Minecraft.getMinecraft().getIntegratedServer().getUserMessage();

            if (s != null)
            {
            	Minecraft.getMinecraft().loadingScreen.displayLoadingString(I18n.format(s));
            }
            else
            {
            	Minecraft.getMinecraft().loadingScreen.displayLoadingString("");
            }

            try
            {
                Thread.sleep(200L);
            }
            catch (InterruptedException var10)
            {
                ;
            }
        }

        Minecraft.getMinecraft().displayGuiScreen(new GuiScreenWorking());
        SocketAddress socketaddress = Minecraft.getMinecraft().getIntegratedServer().getNetworkSystem().addLocalEndpoint();
        NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, Minecraft.getMinecraft(), (GuiScreen)null));
        networkmanager.sendPacket(MCVer.handshake(socketaddress.toString(), 0, EnumConnectionState.LOGIN, true));

        com.mojang.authlib.GameProfile gameProfile = Minecraft.getMinecraft().getSession().getProfile();
        if (!Minecraft.getMinecraft().getSession().hasCachedProperties())
        {
            gameProfile = Minecraft.getMinecraft().getSessionService().fillProfileProperties(gameProfile, true); //Forge: Fill profile properties upon game load. Fixes MC-52974.
            Minecraft.getMinecraft().getSession().setProperties(gameProfile.getProperties());
        }
        networkmanager.sendPacket(MCVer.loginStart(gameProfile));
        try {
        	Field networkManager2 = Minecraft.getMinecraft().getClass().getDeclaredField("myNetworkManager");
        	networkManager2.setAccessible(true);
        	networkManager2.set(Minecraft.getMinecraft(), networkmanager);
        } catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * Reloads a Session from overwriting it with TASbot
     */
	public static void backupSession() {
		if (ChallengeLoader.cachedSession != null) {
    		((AccessorMinecraftClient) Minecraft.getMinecraft()).session(ChallengeLoader.cachedSession);
    		ChallengeLoader.cachedSession = null;
    	}
	}
	
}
