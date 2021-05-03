package de.pfannekuchen.lotas.challenges;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.StartupQuery;
import de.pfannekuchen.lotas.savestate.SavestateMod;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class ChallengeLoader {

	public static ChallengeMap map;
	public static boolean startTimer;
	
	public static void reload() throws IOException {
		Minecraft.getMinecraft().displayGuiScreen(null);
		SavestateMod.isLoading = true;
		
		for (WorldServer w : Minecraft.getMinecraft().theIntegratedServer.worldServers) {
			w.disableLevelSaving = true;
		}
		
        Minecraft.getMinecraft().theWorld.sendQuittingDisconnectingPacket();
        Minecraft.getMinecraft().loadWorld((WorldClient)null);
        load(false);
	}
	
	public static void load(boolean reload) throws IOException {
		final File worldDir = new File(Minecraft.getMinecraft().mcDataDir, "challenges" + File.separator + "");
		if (worldDir.exists()) FileUtils.deleteDirectory(worldDir);
		
		if (reload) {
			final URL url = map.map;
			ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
			
			final FileOutputStream fileOutputStream = new FileOutputStream("map.zip");
			final FileChannel fileChannel = fileOutputStream.getChannel();
			
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			
			fileOutputStream.close();
			fileChannel.close();
		}
		unzip("map.zip", "challenges/" + map.name);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Download
					final URL url = map.map;
					final ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
					
					final FileOutputStream fileOutputStream = new FileOutputStream("map.zip");
					final FileChannel fileChannel = fileOutputStream.getChannel();
					
					fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
					
					fileOutputStream.close();
					fileChannel.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		// Load World
		startTimer = true;
		launchIntegratedServer(map.name, map.name, new WorldSettings(0L, GameType.ADVENTURE, false, true, WorldType.FLAT));
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
    
    private static void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn) {
        
        try {
        	Field h = Minecraft.getMinecraft().getClass().getDeclaredField(FMLLaunchHandler.isDeobfuscatedEnvironment() ? "saveLoader" : "field_71469_aa");
        	h.setAccessible(true);
        	h.set(Minecraft.getMinecraft(), map.getSaveLoader());
        } catch (Exception e) {
			e.printStackTrace();
		}
    	FMLClientHandler.instance().startIntegratedServer(folderName, worldName, worldSettingsIn);
        System.gc();

        
        ISaveHandler isavehandler = map.getSaveLoader().getSaveLoader(folderName, false);
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
            Minecraft.getMinecraft().theIntegratedServer = new IntegratedServer(Minecraft.getMinecraft(), folderName, worldName, worldSettingsIn);
            Minecraft.getMinecraft().getIntegratedServer().startServerThread();
            Minecraft.getMinecraft().integratedServerIsRunning = true;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", worldName);
            throw new ReportedException(crashreport);
        }

        while (!Minecraft.getMinecraft().getIntegratedServer().serverIsInRunLoop())
        {
            if (!StartupQuery.check() || Minecraft.getMinecraft().getIntegratedServer().isServerStopped())
            {
            	Minecraft.getMinecraft().loadWorld(null);
                Minecraft.getMinecraft().displayGuiScreen(null);
                return;
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

        Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
        SocketAddress socketaddress = Minecraft.getMinecraft().theIntegratedServer.getNetworkSystem().addLocalEndpoint();
        NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, Minecraft.getMinecraft(), (GuiScreen)null));
        networkmanager.scheduleOutboundPacket(new C00Handshake(4, socketaddress.toString(), 0, EnumConnectionState.LOGIN), new GenericFutureListener[0]);
        networkmanager.scheduleOutboundPacket(new C00PacketLoginStart(Minecraft.getMinecraft().getSession().getProfile()), new GenericFutureListener[0]);
        try {
        	Field networkManager2 = Minecraft.getMinecraft().getClass().getDeclaredField("myNetworkManager");
        	networkManager2.setAccessible(true);
        	networkManager2.set(Minecraft.getMinecraft(), networkmanager);
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
	
}
