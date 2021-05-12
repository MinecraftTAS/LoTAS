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

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

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
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ReportedException;
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

public class ChallengeLoader {

	public static boolean startTimer;
	
	public static void reload() throws IOException {
		Minecraft.getMinecraft().displayGuiScreen(null);
		SavestateMod.isLoading = true;
		
		for (WorldServer w : MCVer.getWorlds(Minecraft.getMinecraft().getIntegratedServer())) {
			w.disableLevelSaving = true;
		}
		
        MCVer.world(Minecraft.getMinecraft()).sendQuittingDisconnectingPacket();
        Minecraft.getMinecraft().loadWorld((WorldClient)null);
        load(false);
	}
	
	public static void load(boolean reload) throws IOException {
		final File worldDir = new File(Minecraft.getMinecraft().mcDataDir, "challenges" + File.separator + "");
		if (worldDir.exists()) FileUtils.deleteDirectory(worldDir);
		
		if (reload) {
			final URL url = ChallengeMap.currentMap.map;
			ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
			
			final FileOutputStream fileOutputStream = new FileOutputStream("ChallengeMap.currentMap.zip");
			final FileChannel fileChannel = fileOutputStream.getChannel();
			
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			
			fileOutputStream.close();
			fileChannel.close();
		}
		unzip("ChallengeMap.currentMap.zip", "challenges/" + ChallengeMap.currentMap.name);
		
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
		
		// Load World
		startTimer = true;
		launchIntegratedServer(ChallengeMap.currentMap.name, ChallengeMap.currentMap.name, new WorldSettings(0L, GameType.ADVENTURE, false, true, WorldType.FLAT));
	}
	
	private static final int BUFFER_SIZE = 4096;

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
    
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    
    private static void launchIntegratedServer(String folderName, String worldName, @Nullable WorldSettings worldSettingsIn) {
        
        try {
        	Field h = Minecraft.getMinecraft().getClass().getDeclaredField("field_71469_aa");
        	h.setAccessible(true);
        	h.set(Minecraft.getMinecraft(), ChallengeMap.currentMap.getSaveLoader());
        } catch (Exception e) {
			e.printStackTrace();
		}
    	net.minecraftforge.fml.client.FMLClientHandler.instance().startIntegratedServer(folderName, worldName, worldSettingsIn);
        Minecraft.getMinecraft().loadWorld((WorldClient)null);
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
            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), UUID.randomUUID().toString());
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(Minecraft.getMinecraft().mcDataDir, MinecraftServer.USER_CACHE_FILE.getName()));
            TileEntitySkull.setProfileCache(playerprofilecache);
            TileEntitySkull.setSessionService(minecraftsessionservice);
            PlayerProfileCache.setOnlineMode(false);
            ((AccessorMinecraftClient) Minecraft.getMinecraft()).integratedServer(new IntegratedServer(Minecraft.getMinecraft(), folderName, worldName, worldSettingsIn, yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, playerprofilecache));
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
        //#if MC>=11202
        networkmanager.sendPacket(new C00Handshake(socketaddress.toString(), 0, EnumConnectionState.LOGIN, true));
        //#else
        //$$ networkmanager.sendPacket(new C00Handshake(316, socketaddress.toString(), 0, EnumConnectionState.LOGIN, true));
        //#endif
        // TODO: Read 316 from somewhere, it's the protocol version
        com.mojang.authlib.GameProfile gameProfile = Minecraft.getMinecraft().getSession().getProfile();
        if (!Minecraft.getMinecraft().getSession().hasCachedProperties())
        {
            gameProfile = Minecraft.getMinecraft().getSessionService().fillProfileProperties(gameProfile, true); //Forge: Fill profile properties upon game load. Fixes MC-52974.
            Minecraft.getMinecraft().getSession().setProperties(gameProfile.getProperties());
        }
        networkmanager.sendPacket(new CPacketLoginStart(gameProfile));
        try {
        	Field networkManager2 = Minecraft.getMinecraft().getClass().getDeclaredField("myNetworkManager");
        	networkManager2.setAccessible(true);
        	networkManager2.set(Minecraft.getMinecraft(), networkmanager);
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
	
}
