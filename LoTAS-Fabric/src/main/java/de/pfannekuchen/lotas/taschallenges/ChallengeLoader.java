package de.pfannekuchen.lotas.taschallenges;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.LevelInfo;

public class ChallengeLoader {

	public static ChallengeMap map;
	public static boolean startTimer;
	
	public static void reload() throws IOException {
		MinecraftClient.getInstance().openScreen(null);
		SavestateMod.isLoading = true;
		
		// Imagine Saving the World when loading a state ._.
        for (ServerWorld worldserver : MinecraftClient.getInstance().getServer().getWorlds()) {
        	worldserver.savingDisabled = true;
        }
		
        MinecraftClient.getInstance().getServer().stop(true);
        load(false);
	}
	
	public static void load(boolean reload) throws IOException {
		final File worldDir = new File(MinecraftClient.getInstance().runDirectory, "challenges" + File.separator + "");
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
		
		new Thread(() -> {
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
		}).start();
		
		// Load World
		startTimer = true;
		launchIntegratedServer(map.name, map.name, map.getInfo());
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
    
    private static void launchIntegratedServer(String folderName, String worldName, LevelInfo levelInfo) {
        try {
        	Field h = MinecraftClient.class.getDeclaredField("levelStorage");
        	h.setAccessible(true);
        	h.set(MinecraftClient.getInstance(), map.getLevelStorage());
        } catch (Exception e) {
			e.printStackTrace();
		}
        MinecraftClient.getInstance().startIntegratedServer(folderName, worldName, levelInfo);
    }
	
}
