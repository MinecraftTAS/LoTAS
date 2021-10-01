package de.pfannekuchen.lotas.core.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

import de.pfannekuchen.lotas.gui.VideoUpspeederScreen;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.SystemToast.SystemToastIds;
import net.minecraft.network.chat.TextComponent;

/**
 * 
 * This Class utilizes FFmpeg to speed up Video and Audio of a Video.
 * 
 * @author Pancake
 */
public class VideoUpspeeder {

	/** This will be the result of the last ffprobe call */
	private static FFmpegProbeResult latestFFprobe;
	private static File latestFile;

	/** FFprobe and FFmpeg wrappers */
	private static FFprobe ffprobe;
	private static FFmpeg ffmpeg;

	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}

	public static boolean isMac() {
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}

	public static boolean isUnix() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
	}

	/** Setup FFprobe and FFmpeg */
	public final static boolean instantiate(final File bin) {
		if (ffmpeg != null)
			return true;
		try {
			if (isWindows()) {
				ffprobe = new FFprobe(new File(bin, "bin/ffprobe.exe").getAbsolutePath());
				ffmpeg = new FFmpeg(new File(bin, "bin/ffmpeg.exe").getAbsolutePath());
			} else if (isMac()) {
				ffprobe = new FFprobe(new File(bin, "bin/macos/ffprobe").getAbsolutePath());
				ffmpeg = new FFmpeg(new File(bin, "bin/macos/ffmpeg").getAbsolutePath());
			} else if (isUnix()) {
				ffprobe = new FFprobe(new File(bin, "bin/linux/ffprobe").getAbsolutePath());
				ffmpeg = new FFmpeg(new File(bin, "bin/linux/ffmpeg").getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** FFprobe a File and return results */
	public final static FFmpegProbeResult ffprobe(final File probe) {
		try {
			latestFile = probe;
			return latestFFprobe = ffprobe.probe(probe.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("FFprobe failed:");
			e.printStackTrace();
		}
		return null;
	}

	/** Download FFmpeg and install it 
	 * @param videoUpspeederScreen */
	public static void installFFmpeg(final Minecraft client) {
		VideoUpspeederScreen.downloadingFFmpeg = true;
		new Thread(() -> {
			try {
				final File ffmpeg = new File(client.gameDirectory, "ffmpeg");
				final File ffmpegZip = new File(client.gameDirectory, "ffmpeg.zip");
				if(!ffmpegZip.exists()) {
					VideoUpspeederScreen.installingProgress = "Connecting to https://mgnet.work/";
					URLConnection conn = new URL("https://data.mgnet.work/ffmpeg.zip").openConnection();
					VideoUpspeederScreen.installingProgress = "Downloading https://data.mgnet.work/ffmpeg.zip (This may take a while, ca. 220MB)";
					FileUtils.copyInputStreamToFile(conn.getInputStream(), ffmpegZip);
				}
				VideoUpspeederScreen.installingProgress = "Extracting ffmpeg.zip";
				unzip(ffmpegZip.getAbsolutePath(), ffmpeg.getAbsolutePath());
				File linuxdir=new File(ffmpeg, "linux/");
				VideoUpspeederScreen.installingProgress = "Wrapping FFmpeg";
				instantiate(ffmpeg);
//				VideoUpspeederScreen.installingProgress = "Deleting FFmpeg";
//				ffmpegZip.delete();
				VideoUpspeederScreen.installingProgress = "Done";
				VideoUpspeederScreen.onStatsReady();
			} catch (IOException e) {
				e.printStackTrace();
				Minecraft.getInstance().crash(new CrashReport("Downloading of FFmpeg failed", e));
				client.destroy();
			}
		}).start();
	}

	/* ====== Unzipping ======= */

	private static final int BUFFER_SIZE = 4096;

	/** Unzip an .zip Archive */
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

	/* Speed up the Video using the Wrapper. TODO: Remove the Wrapper */
	public static void speedup(final int tickrate, int bitrate, String codec, float frames) {
		Minecraft.getInstance().setScreen(new VideoUpspeederScreen());
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ffmpeg.run(ffmpeg.builder().addInput(latestFFprobe).overrideOutputFiles(true).addExtraArgs("-hwaccel", "auto").addOutput(new File(latestFile.getParentFile(), latestFile.getName().split("\\.")[0] + "-upspeeder.mp4").getAbsolutePath()).setAudioFilter("asetrate=48000*" + (1F / (tickrate / 20F) + ",aresample=48000")).setVideoFilter("setpts=" + (tickrate / 20F) + "*PTS").disableSubtitle().setFormat("mp4").setVideoBitRate(bitrate).setVideoCodec(codec).setVideoFrameRate(60).done(), new ProgressListener() {
						@Override
						public void progress(Progress p) {
							VideoUpspeederScreen.progress = p.frame / frames;
							VideoUpspeederScreen.framesDone = p.frame;
							VideoUpspeederScreen.size = p.total_size / 1024F / 1024F;
							VideoUpspeederScreen.est = Duration.ofSeconds((long) ((frames - p.frame) * p.speed / 60f));
							VideoUpspeederScreen.bitrate = "Bitrate: " + p.bitrate / 1024 / 1024 + " Mbps" + ", File Size: " + String.format("%.2f", VideoUpspeederScreen.size);
						}
					});
					// Done
					VideoUpspeederScreen.progress = 0f;
					VideoUpspeederScreen.isEncoding = false;
					try {
						if (Minecraft.getInstance().screen instanceof VideoUpspeederScreen)
							Minecraft.getInstance().setScreen(new VideoUpspeederScreen());
					} catch (Exception e) {

					}
					Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToastIds.WORLD_BACKUP, new TextComponent("Video Encoding is done."), new TextComponent("Video Size: " + String.format("%.2f MB", VideoUpspeederScreen.size) + " Mb")));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
