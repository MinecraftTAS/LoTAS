package de.pfannekuchen.lotas.gui;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiCheckBox;
import de.pfannekuchen.lotas.renderer.PotionRenderer;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.resources.I18n;
import rlog.RLogAPI;

public class GuiVideoUpspeeder extends GuiScreen implements IProgressMeter {

	static GuiTextField text;
	static float progress = 0f;
	static float lerp = 0f;
	static GuiTextField tickrate;
	static File f;
	static String videoFormat;
	static String codec;
	static String resolution;
	static String length;
	static String filesize;
	static long lengthInMilliseconds;
	
	static FFmpeg ffmpeg;
	static FFprobe ffprobe;
	
	static String codecFFMPEG;
	static FFmpegProbeResult result;
	
	static boolean installed;
	
	public GuiVideoUpspeeder() {
		installed = new File(System.getenv("localappdata") + "\\ffmpeg\\bin\\ffmpeg.exe").exists();
		if (!installed) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						URLConnection conn = new URL("http://mgnet.work/ffmpeg.zip").openConnection();
						FileUtils.copyInputStreamToFile(conn.getInputStream(), new File(System.getenv("localappdata"), "ffmpeg.zip"));
						new File(System.getenv("localappdata"), "ffmpeg").mkdir();
						unzip(new File(System.getenv("localappdata"), "ffmpeg.zip").getAbsolutePath(), new File(System.getenv("localappdata"), "ffmpeg").getAbsolutePath());
						ffmpeg = new FFmpeg(System.getenv("localappdata") + "\\ffmpeg\\bin\\ffmpeg.exe");
						ffprobe = new FFprobe(System.getenv("localappdata") + "\\ffmpeg\\bin\\ffprobe.exe");
						doneLoading();
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(29);
					}
				}
			}).start();
		} else {
			try {
				ffmpeg = new FFmpeg(System.getenv("localappdata") + "\\ffmpeg\\bin\\ffmpeg.exe");
				ffprobe = new FFprobe(System.getenv("localappdata") + "\\ffmpeg\\bin\\ffprobe.exe");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		codecFFMPEG = GL11.glGetString(GL11.GL_VENDOR).toUpperCase().contains("NVIDIA") ? "nvenc_h264" : "h264";
	}
	
	private static final int BUFFER_SIZE = 4096;
	
	private static void unzip(String zipFilePath, String destDirectory) throws IOException  {
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
    
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException  {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
	
	@Override
	public void initGui() {
		text = new GuiTextField(mc.fontRendererObj, (width / 12) * 1 - (width / 24), (height / 8), (width / 12) * 9, 20);
		buttonList.add(new GuiButton(1, (width / 12) * 10 + 5 - (width / 24), (height / 8), (width / 12) * 2, 20, "Select File"));
		buttonList.add(new GuiButton(2, (width / 2) - 70, (height / 8) * 2 + 20, 20, 20, "-"));
		buttonList.add(new GuiButton(3, (width / 2) + 50, (height / 8) * 2 + 20, 20, 20, "+"));
		buttonList.add(new GuiButton(4, (width / 2) - 98, this.height - (this.height / 10) - 15 - 20 - 5, 204, 20, "Speed up video"));
		buttonList.add(new GuiCheckBox(5, 2, this.height  - (this.height / 10), "High Quality", false));
		((GuiButton) buttonList.get(3)).enabled = f == null ? false : f.exists();
		tickrate = new GuiTextField(mc.fontRendererObj, (width / 2) - 45, (height / 8) * 2 + 23, 90, 14);
		tickrate.setText("20");
		super.initGui();
	}
	
	public void loadFile() throws Exception {
		if (!f.exists() || f.isDirectory()) throw new Exception();
		
		result = ffprobe.probe(f.getAbsolutePath());
		
		videoFormat = result.format.format_name.split(",")[0];
		codec = result.getStreams().get(0).codec_name; 
		length = de.pfannekuchen.lotas.tickratechanger.Timer.getDuration(Duration.ofMillis((long) result.format.duration * 1000));
		resolution = result.streams.get(0).width + "x" + result.streams.get(0).height;
		filesize = (Files.size(f.toPath()) / 1024 / 1024) + " MB";
		lengthInMilliseconds = (long) (result.format.duration * 1000);
		
		((GuiButton) buttonList.get(3)).enabled = true;
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode)  {
		tickrate.textboxKeyTyped(typedChar, keyCode);
		try {
			int ticks = Integer.parseInt(tickrate.getText());
			
			if (ticks <= 0) {
				throw new Exception();
			}
			
			((GuiButton) buttonList.get(1)).enabled = true;
			((GuiButton) buttonList.get(2)).enabled = true;
		} catch (Exception e) {
			RLogAPI.logError(e, "Parse Invalid Data #2");
			((GuiButton) buttonList.get(1)).enabled = false;
			((GuiButton) buttonList.get(2)).enabled = false;
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)  {
		tickrate.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)  {
		if (button.id == 1) {
			if (mc.isFullScreen()) mc.toggleFullscreen();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
				    FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
				    dialog.setMode(FileDialog.LOAD);
				    dialog.setMultipleMode(false);
				    dialog.setVisible(true);
				    try {
				    	f = dialog.getFiles()[0];
				    	loadFile();
				    } catch (Exception e) {
						
					}
				}
			}).start();
		} else if (button.id == 2) {
			if (Integer.parseInt(tickrate.getText()) <= 1) return;
			tickrate.setText((Integer.parseInt(tickrate.getText()) - 1) + "");
		} else if (button.id == 3) {
			tickrate.setText((Integer.parseInt(tickrate.getText()) + 1) + "");
		} else if (button.id == 4) {
			button.enabled = false;
			final float fullFrames = result.getStreams().get(0).nb_frames * (Integer.parseInt(tickrate.getText()) / 20F);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						new Timer().scheduleAtFixedRate(new TimerTask() {
							
							public void run() {
								lerp = PotionRenderer.lerp(lerp, progress, .025f);
							}
						}, 16L, 16L);
						ffmpeg.run(ffmpeg.builder().addInput(f.getAbsolutePath())
								.overrideOutputFiles(true)
								.addExtraArgs("-hwaccel", "auto")
								.addOutput(new File(f.getParentFile(), f.getName().split("\\.")[0] + "-upspeeder.mp4").getAbsolutePath())
									.setAudioFilter("asetrate=48000*" + (1F / (Integer.parseInt(tickrate.getText()) / 20F) + ",aresample=48000"))
									.setVideoFilter("setpts=" + (Integer.parseInt(tickrate.getText()) / 20F) + "*PTS")
									.disableSubtitle()
									.setFormat("mp4")
									.setVideoBitRate(((GuiCheckBox) buttonList.get(4)).isChecked() ? 20000000 : 8000000)
									.setVideoCodec(codecFFMPEG)
									.setVideoFrameRate(60)
									.done(), new ProgressListener() {
										@Override
										public void progress(Progress p) {
											progress = p.frame / fullFrames;
										}
									});
						
						if (mc.isFullScreen()) mc.toggleFullscreen();
						Runtime.getRuntime().exec(("explorer.exe \"" + f.getParentFile().getAbsolutePath() + "\"").split(" "));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
		}
		super.actionPerformed(button);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		if (!installed) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("Downloading FFmpeg"), this.width / 2, this.height / 2, 16777215);
            this.drawCenteredString(this.fontRendererObj, lanSearchStates[(int)(Minecraft.getSystemTime() / 150L % (long)lanSearchStates.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
			return;
		}
		
		drawBackground(0);
		text.drawTextBox();
		tickrate.drawTextBox();
		drawCenteredString(mc.fontRendererObj, "Tickrate", (width / 2), (height / 8) * 2, 0xFFFFFF);
		
		if (f != null) drawString(mc.fontRendererObj, f.getAbsolutePath(), (width / 12) * 1 - (width / 24) + 4, (height / 8) + 6, 0xFFFFFF);
		
		drawCenteredString(mc.fontRendererObj, "Input File", (width / 4), (height / 8) * 3 + 10, 0x808080);
		drawCenteredString(mc.fontRendererObj, "Output File", (width / 4) * 3, (height / 8) * 3 + 10, 0x808080);
	    
		drawString(mc.fontRendererObj, "Format: " + videoFormat, (width / 4) - (width / 12), (height / 8) * 4, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Codec: " + codec, (width / 4) - (width / 12), (height / 8) * 4 + 10, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Resolution: " + resolution, (width / 4) - (width / 12), (height / 8) * 4 + 20, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Length: " + length, (width / 4) - (width / 12), (height / 8) * 4 + 30, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Size: " + filesize, (width / 4) - (width / 12), (height / 8) * 4 + 40, 0xFFFFFF);
		
		drawString(mc.fontRendererObj, "Format: mp4", (width / 4) * 3 - (width / 12), (height / 8) * 4, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Encoder: " + codecFFMPEG, (width / 4) * 3 - (width / 12), (height / 8) * 4 + 10, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Resolution: " + resolution, (width / 4) * 3 - (width / 12), (height / 8) * 4 + 20, 0xFFFFFF);
		String dur = null;
		try {
			dur = de.pfannekuchen.lotas.tickratechanger.Timer.getDuration(Duration.ofMillis((long) (lengthInMilliseconds * (Integer.parseInt(tickrate.getText()) / 20F))));
		} catch (Exception e) {
			
		}
		drawString(mc.fontRendererObj, "Length: " + dur, (width / 4) * 3 - (width / 12), (height / 8) * 4 + 30, 0xFFFFFF);
		drawString(mc.fontRendererObj, "Size: " + calcSize(), (width / 4) * 3 - (width / 12), (height / 8) * 4 + 40, 0xFFFFFF);
		
		
		drawRect(0, this.height - 10, progressToPixels(lerp), this.height, 0x66CDAAFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private String calcSize() {
		try {
			return ((int) ((Duration.ofMillis((long) (lengthInMilliseconds * (Integer.parseInt(tickrate.getText()) / 20F))).getSeconds() * (((GuiCheckBox) buttonList.get(4)).isChecked() ? 20000 : 8000)) / 1024 / 8)) + " MB";
		} catch (Exception e) {
			return null;
		}
	}

	public int progressToPixels(float progress) {
	    return (int) (progress * this.width);
	}
	@Override
	public void doneLoading() {
		installed = true;
	}
		
}

