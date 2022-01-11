/**
 * Here is the logic of the recorder mod:
 */
package de.pfannkuchen.lotas.mods;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.util.BufferExchangeList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

/**
 * TAS recorder mod
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class RecorderMod {

	/**
	 * The windows videos folder
	 */
	private final File VIDEOS_DIR = new File(System.getenv("userprofile"), "Videos");

	/**
	 * The FFMpeg executable
	 */
	private File FFMPEG;

	/**
	 * Command line arguments for FFmpeg
	 */
	private String COMMAND_LINE = "-y %IN% -f rawvideo -c:v rawvideo -s %SIZE% -pix_fmt rgb24 -r 60 -i - -vf vflip %OUT% -pix_fmt yuv420p", COMMAND_LINE_IN, COMMAND_LINE_OUT;

	/**
	 * The not allowed guis that may be filtered out
	 */
	private final List<String> NOT_ALLOWED_GUI = Arrays.asList("AdvancementsScreen", "ConnectScreen", "DemoIntroScreen", "GenericDirtMessageScreen", "LevelLoadingScreen", "OptionsScreen",
			"OptionsSubScreen", "ControlsScreen", "LanguageSelectScreen", "MouseSettingsScreen", "SimpleOptionsSubScreen", "AccessibilityOptionsScreen", "ChatOptionsScreen", "SkinCustomizationScreen",
			"SoundOptionsScreen", "VideoSettingsScreen", "PackSelectionScreen", "PauseScreen", "PopupScreen", "ProgressScreen", "ReceivingLevelScreen", "ShareToLanScreen", "StatsScreen");

	/**
	 * Thread-safe atomic boolean to see if the recording is running
	 */
	private AtomicBoolean isRecording = new AtomicBoolean(false);

	/**
	 * Width and height of the Screen
	 */
	private int width, height;

	/**
	 * Thread-safe list for raw screenshots
	 */
	private BufferExchangeList list;

	/**
	 * Whether a screenshot should be taken or not.
	 */
	private boolean takeScreenshot;

	/**
	 * Restart the recording if the screen resizes
	 * @param mc Instance of minecraft
	 */
	public void onResize(Minecraft mc) {
		this.stopRecording();
		this.startRecording(mc);
	}

	/**
	 * Takes screenshots if necessary
	 * @param mc Instance of minecraft
	 */
	public void onRender(Minecraft mc) {
		if (mc == null) return;
		// Check screen resolutions
		if ((this.width != mc.getWindow().getScreenWidth() || this.height != mc.getWindow().getScreenHeight()) && this.isRecording.get()) this.onResize(mc);
		Screen screen = mc.screen;
		// Update gui screen with NULL if the gui is an allowed gui.
		// Done to pass the next check screen == null
		if (mc.level == null) return;
		if (screen != null) if (!this.NOT_ALLOWED_GUI.contains(screen.getClass().getSimpleName())) screen = null;
		if (this.takeScreenshot && screen == null && !ClientLoTAS.loscreenmanager.isScreenOpened()) {
			this.takeScreenshot = false;
			// Take a screenshot into the screenshot list
			if (this.list.containsUnfilledUnlocked()) {
				int unfilled = this.list.findUnfilled();
				ByteBuffer b = this.list.getAndLock(unfilled, true);
				GL11.glReadPixels(0, 0, this.width, this.height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, b);
				this.list.unlock(unfilled);
			}
		}
	}

	/**
	 * Starts the recording
	 * @param mc Instance of minecraft
	 */
	public void startRecording(Minecraft mc) {
		this.FFMPEG = new File(LoTAS.configmanager.getString("recorder", "ffmpeg"));
		this.COMMAND_LINE_IN = LoTAS.configmanager.getString("recorder", "ffmpeg_cmd_in");
		this.COMMAND_LINE_OUT = LoTAS.configmanager.getString("recorder", "ffmpeg_cmd_out");
		if (!this.VIDEOS_DIR.exists()) this.VIDEOS_DIR.mkdir();
		System.gc();
		this.width = mc.getWindow().getScreenWidth();
		this.height = mc.getWindow().getScreenHeight();
		this.list = new BufferExchangeList(32, this.width*this.height*3);
		this.isRecording.set(true);
		/* Starts a thread for sending the images from the buffer list and ffmpeg */
		new Thread(() -> {
			try {
				// ffmpeg command line
				String ffmpeg = this.COMMAND_LINE.replaceAll("%IN%", this.COMMAND_LINE_IN).replaceAll("%OUT%", this.COMMAND_LINE_OUT).replaceAll("%SIZE%", this.width + "x" + this.height);
				// start process
				final ProcessBuilder pb = new ProcessBuilder(ArrayUtils.add(ArrayUtils.addAll(new String[] {this.FFMPEG.getAbsolutePath()}, ffmpeg.split(" ")), Date.from(Instant.now()).toString().replace(' ', '-').replace(':', '-') + ".mp4"));
				pb.redirectOutput(Redirect.INHERIT);
				pb.directory(this.VIDEOS_DIR);
				pb.redirectErrorStream(true);
				pb.redirectError(Redirect.INHERIT);
				final Process p = pb.start();
				OutputStream stream = p.getOutputStream();

				LoTAS.LOGGER.info("Recording started");

				// reuse buffers and arrays for optimal memory usage
				ByteBuffer b;
				byte[] array = new byte[this.width*this.height*3];
				while (this.isRecording.get()) {
					/* Find and lock a Buffer in the list */
					if (this.list.containsFilledUnlocked()) {
						int i = this.list.findFilled();
						if (i == 32) continue;
						// obtain buffer and load into byte array
						b = this.list.getAndLock(i, false);
						b.get(array);
						// send that byte array
						stream.write(array);
						this.list.unlock(i);
					}
				}
				// After /r /record has been run again stop the process by closing the streams, causing SIGINT
				stream.flush();
				stream.close();
				p.getInputStream().close();
				p.getErrorStream().close();
				LoTAS.LOGGER.info("Recording finished");

			} catch (IOException e) {
				mc.tell(() -> {
					mc.setScreen(new ErrorScreen(new TextComponent("Something went wrong while trying to record!"), new TextComponent("Check the console for error messages.")));
				});
				e.printStackTrace();
			}
		}).start();
		/* Screenshot every 16 Milliseconds for 60 fps */
		new Thread(() ->  {
			try {
				while (this.isRecording.get()) {
					this.takeScreenshot = true;
					Thread.sleep((long) (LoTAS.tickratechanger.getMsPerTick()/3.0f));
					while (LoTAS.tickadvance.isTickadvanceEnabled()) {
						if (LoTAS.tickadvance.shouldTick) break;
						Thread.sleep(1L);
					}
				}
			} catch (Exception e) {}
		}).start();
	}

	/**
	 * Stops the recording
	 */
	public void stopRecording() {
		this.isRecording.set(false);
		this.list.clear();
	}

	/**
	 * Returns whether it is recording or not
	 * @return Is Recording
	 */
	public boolean isRecording() {
		return this.isRecording.get();
	}

}
