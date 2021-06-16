package de.pfannekuchen.lotas.gui;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.nio.file.Files;
import java.time.Duration;

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.core.utils.VideoUpspeeder;
import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class VideoUpspeederScreen extends Screen {

	String[] PROGRESS_BAR_STAGES = new String[]{"oooooo", "Oooooo", "oOoooo", "ooOooo", "oooOoo", "ooooOo", "oooooO"};
	public static Duration est = Duration.ZERO;
	public static String installingProgress = "Idle";
	public static String codecFFmpeg = null;
	public static boolean downloadingFFmpeg;
	public static boolean isEncoding;
	private static File selectedFile;
	private static int tickrate = 20;
	
	static String videoFormat = "Unknown";
	static String codec = "Unknown";
	static String resolution = "Unknown";
	static String length = "Unknown";
	static String filesize = "Unknown";
	public static String bitrate = "0";
	public static long framesDone = 0L;
	static long lengthInMilliseconds = 0L;
	public static float progress;
	public static float size;
	
	public static MinecraftClient client;
	
	public VideoUpspeederScreen() {
		super(new LiteralText(""));
		client = MinecraftClient.getInstance();
		if (!VideoUpspeeder.instantiate(new File(client.runDirectory, "ffmpeg"))) {
			VideoUpspeeder.installFFmpeg(client);
		}
		if (codecFFmpeg == null) codecFFmpeg = GL11.glGetString(GL11.GL_VENDOR).toUpperCase().contains("NVIDIA") ? "nvenc_h264" : "h264";
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		super.charTyped(chr, modifiers);
		try {
			tickrate = Integer.parseInt(((TextFieldWidget) buttons.get(buttons.size() - 1)).getText());
		} catch (Exception e) {
			
		}
		return true;
	}
	
    public static String getDuration(Duration d) {
        return String.format("%02d", d.toHours()) + ":" + String.format("%02d", d.toMinutes() % 60) + ":" + String.format("%02d", d.getSeconds() % 60);
    }
	
	@Override
	protected void init() {
		if (isEncoding) {
			addButton(new NewButtonWidget(width / 2 - 153, height - 40, 306, 20, "Continue encoding in the background >>", (b) -> {
				MinecraftClient.getInstance().openScreen(new TitleScreen	());
			}));
			return;
		}
		
		//#if MC>=11601
//$$ 		addButton(new TextFieldWidget(client.textRenderer, (width / 12) * 1 - (width / 24), (height / 8), (width / 12) * 9, 20, LiteralText.EMPTY)).setMaxLength(999);
        //#else
        addButton(new TextFieldWidget(client.textRenderer, (width / 12) * 1 - (width / 24), (height / 8), (width / 12) * 9, 20, "")).setMaxLength(999);
        //#endif
		addButton(new NewButtonWidget((width / 12) * 10 + 5 - (width / 24), (height / 8), (width / 12) * 2, 20, "Select File", (b) -> {
			if (client.window.isFullscreen()) client.window.toggleFullscreen();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					System.setProperty("java.awt.headless", "false");
					FileDialog dialog = new FileDialog((Frame) null, "Select File to Open", FileDialog.LOAD);
				    dialog.setMultipleMode(false);
				    dialog.setVisible(true);
				    try {
				    	selectedFile = dialog.getFiles()[0];
				    	((TextFieldWidget) buttons.get(0)).setText(selectedFile.getAbsolutePath());
				    	FFmpegProbeResult result = VideoUpspeeder.ffprobe(selectedFile);
						
						videoFormat = result.format.format_name.split(",")[0];
						codec = result.getStreams().get(0).codec_name; 
						length = getDuration(Duration.ofMillis((long) (result.format.duration * 1000)));
						resolution = result.streams.get(0).width + "x" + result.streams.get(0).height;
						filesize = (Files.size(selectedFile.toPath()) / 1024 / 1024) + " MB";
						lengthInMilliseconds = (long) (result.format.duration * 1000);
						
						buttons.get(4).active = true;
				    } catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}));
		addButton(new NewButtonWidget((width / 2) - 70, (height / 8) * 2 + 20, 20, 20, "-", (b) -> {
			if (tickrate != 1) tickrate--;
			((TextFieldWidget) buttons.get(buttons.size() - 1)).setText(tickrate + "");
		}));
		addButton(new NewButtonWidget((width / 2) + 50, (height / 8) * 2 + 20, 20, 20, "+", (b) -> {
			tickrate++;
			((TextFieldWidget) buttons.get(buttons.size() - 1)).setText(tickrate + "");
		}));
		addButton(new NewButtonWidget((width / 2) - 98, this.height - (this.height / 10) - 15 - 20 - 5, 204, 20, "Speed up video", (b) -> {
			b.active = false;
			isEncoding = true;
			VideoUpspeeder.speedup(tickrate, bitrate(), codecFFmpeg, (long) ((lengthInMilliseconds / 16L) * (tickrate / 20F)));
		})).active = selectedFile == null ? false : selectedFile.exists();
		//#if MC>=11601
//$$         addButton(new CheckboxWidget(2, this.height - 22, 20, 20, new LiteralText("High Quality"), false));
//$$         addButton(new TextFieldWidget(client.textRenderer, (width / 2) - 45, (height / 8) * 2 + 23, 90, 14, new LiteralText("20"))).setText("20");
        //#else
        addButton(new CheckboxWidget(2, this.height - 22, 20, 20, "High Quality", false));
        addButton(new TextFieldWidget(client.textRenderer, (width / 2) - 45, (height / 8) * 2 + 23, 90, 14, "20")).setText("20");
        //#endif
		super.init();
	}
	
	//#if MC>=11601
//$$ 	@Override public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//$$ 	    renderBackground(matrices);
//$$ 	    if (isEncoding) {
//$$ 	        int i = this.width / 2 - 150;
//$$ 	        int j = this.width / 2 + 150;
//$$ 	        int k = this.height / 4 + 100;
//$$ 	        int l = k + 10;
//$$ 	        int m = 0;
//$$ 	        fill(matrices, i - 1, k - 1, j + 1, l + 1, -16777216);
//$$ 	        int n;
//$$ 	        n = MathHelper.floor(progress * (float) (j - i));
//$$ 	        fill(matrices, i + m, k, i + m + n, l, -13408734);
//$$ 	        super.render(matrices, mouseX, mouseY, delta);
//$$ 	        int var10004 = k + (l - k) / 2;
//$$
//$$
//$$
//$$ 	        drawStringWithShadow(matrices, client.textRenderer, "Est: " + getDuration(est), i, 40 + (9 + 3) * 2, 10526880);
//$$ 	        drawStringWithShadow(matrices, client.textRenderer, bitrate + "", i, 40 + 9 + 3, 10526880);
//$$
//$$ 	        drawCenteredString(matrices, client.textRenderer, framesDone + " / " + (long) ((lengthInMilliseconds / 16L) * (tickrate / 20F)) + " Frames", width / 2, var10004 - 9 / 2, 0xFFFFFF);
//$$
//$$ 	        return;
//$$ 	    }
//$$
//$$ 	    if (VideoUpspeederScreen.downloadingFFmpeg) {
//$$ 	        this.renderBackground(matrices);
//$$ 	        drawCenteredString(matrices, client.textRenderer, installingProgress, this.width / 2, this.height / 2, 16777215);
//$$ 	        String var10002 = PROGRESS_BAR_STAGES[(int) (Util.getMeasuringTimeMs() / 150L % (long) PROGRESS_BAR_STAGES.length)];
//$$ 	        int var10003 = this.width / 2;
//$$ 	        int var10004 = this.height / 2;
//$$ 	        drawCenteredString(matrices, client.textRenderer, var10002, var10003, var10004 + 9 * 2, 16777215);
//$$ 	        return;
//$$ 	    }
//$$
//$$ 	    drawCenteredString(matrices, client.textRenderer, "Tickrate", (width / 2), (height / 8) * 2, 0xFFFFFF);
//$$
//$$ 	    if (selectedFile != null) drawStringWithShadow(matrices, client.textRenderer, selectedFile.getAbsolutePath(), (width / 12) * 1 - (width / 24) + 4, (height / 8) + 6, 0xFFFFFF);
//$$
//$$ 	    drawCenteredString(matrices, client.textRenderer, "Input File", (width / 4), (height / 8) * 3 + 10, 0x808080);
//$$ 	    drawCenteredString(matrices, client.textRenderer, "Output File", (width / 4) * 3, (height / 8) * 3 + 10, 0x808080);
//$$
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Format: " + videoFormat, (width / 4) - (width / 12), (height / 8) * 4, 0xFFFFFF);
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Codec: " + codec, (width / 4) - (width / 12), (height / 8) * 4 + 10, 0xFFFFFF);
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Resolution: " + resolution, (width / 4) - (width / 12), (height / 8) * 4 + 20, 0xFFFFFF);
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Length: " + length, (width / 4) - (width / 12), (height / 8) * 4 + 30, 0xFFFFFF);
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Size: " + filesize, (width / 4) - (width / 12), (height / 8) * 4 + 40, 0xFFFFFF);
//$$
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Format: mp4", (width / 4) * 3 - (width / 12), (height / 8) * 4, 0xFFFFFF);
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Encoder: " + codecFFmpeg, (width / 4) * 3 - (width / 12), (height / 8) * 4 + 10, 0xFFFFFF);
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Resolution: " + resolution, (width / 4) * 3 - (width / 12), (height / 8) * 4 + 20, 0xFFFFFF);
//$$ 	    String dur = null;
//$$ 	    try {
//$$ 	        dur = getDuration(Duration.ofMillis((long) (lengthInMilliseconds * (tickrate / 20F))));
//$$ 	    } catch (Exception e) {
//$$
//$$ 	    }
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Length: " + dur, (width / 4) * 3 - (width / 12), (height / 8) * 4 + 30, 0xFFFFFF);
//$$ 	    drawStringWithShadow(matrices, client.textRenderer, "Size: " + calcSize(), (width / 4) * 3 - (width / 12), (height / 8) * 4 + 40, 0xFFFFFF);
//$$
//$$ 	    super.render(matrices, mouseX, mouseY, delta);
//$$ 	}
	//#else
	@Override public void render(int mouseX, int mouseY, float delta) {
	    renderBackground();
	    if (isEncoding) {
	        int i = this.width / 2 - 150;
	        int j = this.width / 2 + 150;
	        int k = this.height / 4 + 100;
	        int l = k + 10;
	        int m = 0;
	        fill(i - 1, k - 1, j + 1, l + 1, -16777216);
	        int n;
	        n = MathHelper.floor(progress * (float) (j - i));
	        fill(i + m, k, i + m + n, l, -13408734);
	        super.render(mouseX, mouseY, delta);
	        int var10004 = k + (l - k) / 2;



	        drawString(client.textRenderer, "Est: " + getDuration(est), i, 40 + (9 + 3) * 2, 10526880);
	        drawString(client.textRenderer, bitrate + "", i, 40 + 9 + 3, 10526880);

	        drawCenteredString(client.textRenderer, framesDone + " / " + (long) ((lengthInMilliseconds / 16L) * (tickrate / 20F)) + " Frames", width / 2, var10004 - 9 / 2, 0xFFFFFF);

	        return;
	    }

	    if (VideoUpspeederScreen.downloadingFFmpeg) {
	        this.renderBackground();
	        drawCenteredString(client.textRenderer, installingProgress, this.width / 2, this.height / 2, 16777215);
	        String var10002 = PROGRESS_BAR_STAGES[(int) (Util.getMeasuringTimeMs() / 150L % (long) PROGRESS_BAR_STAGES.length)];
	        int var10003 = this.width / 2;
	        int var10004 = this.height / 2;
	        drawCenteredString(client.textRenderer, var10002, var10003, var10004 + 9 * 2, 16777215);
	        return;
	    }

	    drawCenteredString(client.textRenderer, "Tickrate", (width / 2), (height / 8) * 2, 0xFFFFFF);

	    if (selectedFile != null) drawString(client.textRenderer, selectedFile.getAbsolutePath(), (width / 12) * 1 - (width / 24) + 4, (height / 8) + 6, 0xFFFFFF);

	    drawCenteredString(client.textRenderer, "Input File", (width / 4), (height / 8) * 3 + 10, 0x808080);
	    drawCenteredString(client.textRenderer, "Output File", (width / 4) * 3, (height / 8) * 3 + 10, 0x808080);

	    drawString(client.textRenderer, "Format: " + videoFormat, (width / 4) - (width / 12), (height / 8) * 4, 0xFFFFFF);
	    drawString(client.textRenderer, "Codec: " + codec, (width / 4) - (width / 12), (height / 8) * 4 + 10, 0xFFFFFF);
	    drawString(client.textRenderer, "Resolution: " + resolution, (width / 4) - (width / 12), (height / 8) * 4 + 20, 0xFFFFFF);
	    drawString(client.textRenderer, "Length: " + length, (width / 4) - (width / 12), (height / 8) * 4 + 30, 0xFFFFFF);
	    drawString(client.textRenderer, "Size: " + filesize, (width / 4) - (width / 12), (height / 8) * 4 + 40, 0xFFFFFF);

	    drawString(client.textRenderer, "Format: mp4", (width / 4) * 3 - (width / 12), (height / 8) * 4, 0xFFFFFF);
	    drawString(client.textRenderer, "Encoder: " + codecFFmpeg, (width / 4) * 3 - (width / 12), (height / 8) * 4 + 10, 0xFFFFFF);
	    drawString(client.textRenderer, "Resolution: " + resolution, (width / 4) * 3 - (width / 12), (height / 8) * 4 + 20, 0xFFFFFF);
	    String dur = null;
	    try {
	        dur = getDuration(Duration.ofMillis((long) (lengthInMilliseconds * (tickrate / 20F))));
	    } catch (Exception e) {

	    }
	    drawString(client.textRenderer, "Length: " + dur, (width / 4) * 3 - (width / 12), (height / 8) * 4 + 30, 0xFFFFFF);
	    drawString(client.textRenderer, "Size: " + calcSize(), (width / 4) * 3 - (width / 12), (height / 8) * 4 + 40, 0xFFFFFF);

	    super.render(mouseX, mouseY, delta);
	}
	//#endif
		
	
	/* Gets the Bitrate from the button */
	private int bitrate() {
		return ((CheckboxWidget) buttons.get(5)).isChecked() ? 20000000 : 8000000;
	}
	
	/* Calculates the Size if the Video with a max bitrate: bitrate * frames */
	private String calcSize() {
		try {
			return "~" + (int) (((bitrate() * lengthInMilliseconds / 1000F) / 8F / 1000F / 1000F) * (tickrate / 20F)) + " MB";
		} catch (final Exception e) {
			return "Unknown";
		}
	}

	public static void onStatsReady() {
		downloadingFFmpeg = false;
	}
	
}
