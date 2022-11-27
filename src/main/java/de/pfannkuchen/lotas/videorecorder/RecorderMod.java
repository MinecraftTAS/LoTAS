/**
 * Here is the logic of the recorder mod:
 */
package de.pfannkuchen.lotas.videorecorder;

import java.io.File;
import java.io.FileOutputStream;
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
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;

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
	private final List<String> NOT_ALLOWED_GUI = Arrays.asList("AdvancementsScreen", "ConnectScreen", "DemoIntroScreen", "GenericDirtMessageScreen", "LevelLoadingScreen", "OptionsScreen", "OptionsSubScreen", "ControlsScreen", "LanguageSelectScreen", "MouseSettingsScreen", "SimpleOptionsSubScreen", "AccessibilityOptionsScreen", "ChatOptionsScreen", "SkinCustomizationScreen", "SoundOptionsScreen", "VideoSettingsScreen", "PackSelectionScreen", "PauseScreen", "PopupScreen", "ProgressScreen", "ReceivingLevelScreen", "ShareToLanScreen", "StatsScreen");

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
	private BufferExchangeList video_list;

	/**
	 * Thread-safe list for serialized sounds
	 */
	private BufferExchangeList sound_list;

	/**
	 * Whether a screenshot should be taken or not.
	 */
	private boolean takeScreenshot;

	/**
	 * Whether a screenshot CAN BE taken or not.
	 * (used for serializing sounds)
	 */
	private boolean currentStatus;

	/**
	 * The current tick*3 (aka frame) of the current recording.
	 * (used for serializing sounds)
	 */
	private int currentFrame;

	/**
	 * Restart the recording if the screen resizes
	 * @param mc Instance of minecraft
	 */
	public void onResize(Minecraft mc) {
		this.stopRecording();
		this.startRecording(mc);
	}

	/**
	 * Serializes a sound instance of necessary
	 * @param instance Sound Instance to serialize
	 */
	public void onSoundPlay(SoundInstance instance) {
		if (!isRecording() || !this.currentStatus)
			return;
		// Take a buffer and fill it
		if (this.sound_list.containsUnfilledUnlocked()) {
			int unfilled = this.sound_list.findUnfilled();
			ByteBuffer b = this.sound_list.getAndLock(unfilled, true);
			SoundWithTimestamp sound = new SoundWithTimestamp(instance, this.currentFrame);
			// Write and flip, so that only the required bytes are copied instead of the full 1k
			sound.write(b);
			b.flip();
			this.sound_list.unlock(unfilled);
		}
	}

	/**
	 * Takes screenshots if necessary
	 * @param mc Instance of minecraft
	 */
	public void onRender(Minecraft mc) {
		if (mc == null)
			return;
		// Check screen resolutions
		if ((this.width != mc.getWindow().getScreenWidth() || this.height != mc.getWindow().getScreenHeight()) && this.isRecording.get())
			this.onResize(mc);
		Screen screen = mc.screen;
		// Update gui screen with NULL if the gui is an allowed gui.
		// Done to pass the next check screen == null
		if (mc.level == null) {
			this.currentStatus = false;
			return;
		}
		if (screen != null)
			if (!this.NOT_ALLOWED_GUI.contains(screen.getClass().getSimpleName()))
				screen = null;
		this.currentStatus = screen == null && !ClientLoTAS.loscreenmanager.isScreenOpened();
		if (this.takeScreenshot && screen == null && !ClientLoTAS.loscreenmanager.isScreenOpened()) {
			this.takeScreenshot = false;
			// Take a screenshot into the screenshot list
			if (this.video_list.containsUnfilledUnlocked()) {
				int unfilled = this.video_list.findUnfilled();
				ByteBuffer b = this.video_list.getAndLock(unfilled, true);
				GL11.glReadPixels(0, 0, this.width, this.height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, b);
				this.video_list.unlock(unfilled);
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
		if (!this.VIDEOS_DIR.exists())
			this.VIDEOS_DIR.mkdir();
		System.gc();
		this.width = mc.getWindow().getScreenWidth();
		this.height = mc.getWindow().getScreenHeight();
		this.video_list = new BufferExchangeList(32, this.width * this.height * 3);
		this.sound_list = new BufferExchangeList(32, 1000);
		this.isRecording.set(true);
		this.currentFrame = 0;
		final String title = Date.from(Instant.now()).toString().replace(' ', '-').replace(':', '-'); // title of the video
		/* Starts a thread for sending the images from the buffer list and ffmpeg */
		new Thread(() -> {
			try {
				// ffmpeg command line
				String ffmpeg = this.COMMAND_LINE.replaceAll("%IN%", this.COMMAND_LINE_IN).replaceAll("%OUT%", this.COMMAND_LINE_OUT).replaceAll("%SIZE%", this.width + "x" + this.height);
				// start process
				final ProcessBuilder pb = new ProcessBuilder(ArrayUtils.add(ArrayUtils.addAll(new String[] { this.FFMPEG.getAbsolutePath() }, ffmpeg.split(" ")), title + ".mp4"));
				pb.redirectOutput(Redirect.INHERIT);
				pb.directory(this.VIDEOS_DIR);
				pb.redirectErrorStream(true);
				pb.redirectError(Redirect.INHERIT);
				final Process p = pb.start();
				OutputStream stream = p.getOutputStream();

				LoTAS.LOGGER.info("Video Recording started");

				// reuse buffers and arrays for optimal memory usage
				ByteBuffer b;
				byte[] array = new byte[this.width * this.height * 3];
				while (this.isRecording.get()) {
					/* Find and lock a Buffer in the list */
					if (this.video_list.containsFilledUnlocked()) {
						int i = this.video_list.findFilled();
						if (i == 32)
							continue;
						// obtain buffer and load into byte array
						b = this.video_list.getAndLock(i, false);
						b.get(array);
						// send that byte array
						stream.write(array);
						// increase the frame count
						this.currentFrame++;
						this.video_list.unlock(i);
					}
				}
				// After the recording was ended stop the process by closing the streams, causing SIGINT
				stream.flush();
				stream.close();
				p.getInputStream().close();
				p.getErrorStream().close();
				LoTAS.LOGGER.info("Video Recording finished");

			} catch (IOException e) {
				mc.tell(() -> {
					mc.setScreen(new ErrorScreen(Component.literal("Something went wrong while trying to record!"), Component.literal("Check the console for error messages.")));
				});
				e.printStackTrace();
			}
		}).start();
		/* Screenshot every 16 Milliseconds (slowed down) for 60 fps */
		new Thread(() -> {
			try {
				LoTAS.LOGGER.info("Frame Grabber started");
				while (this.isRecording.get()) {
					this.takeScreenshot = true;
					Thread.sleep((long) (LoTAS.tickratechanger.getMsPerTick() / 3.0f));
					while (LoTAS.tickadvance.isTickadvanceEnabled()) {
						if (LoTAS.tickadvance.shouldTickClient)
							break;
						Thread.sleep(1L);
					}
				}
				LoTAS.LOGGER.info("Frame Grabber finished");
			} catch (Exception e) {
				mc.tell(() -> {
					mc.setScreen(new ErrorScreen(Component.literal("Something went wrong while trying to record!"), Component.literal("Check the console for error messages.")));
				});
				e.printStackTrace();
			}
		}).start();
		/* Starts a thread for sending the serialized sounds to a file */
		new Thread(() -> {
			try {
				File soundfile = new File(VIDEOS_DIR, Date.from(Instant.now()).toString().replace(' ', '-').replace(':', '-') + ".snd");
				soundfile.createNewFile();
				OutputStream stream = new FileOutputStream(soundfile);

				LoTAS.LOGGER.info("Sound Recording started");

				// reuse buffers and arrays for optimal memory usage
				ByteBuffer b;
				byte[] array = new byte[1000];
				while (this.isRecording.get()) {
					/* Find and lock a Buffer in the list */
					if (this.sound_list.containsFilledUnlocked()) {
						int i = this.sound_list.findFilled();
						if (i == 32)
							continue;
						// obtain buffer and load into byte array
						b = this.sound_list.getAndLock(i, false);
						int length = b.remaining();
						b.get(array, 0, length);
						// send that byte array
						stream.write(array, 0, length);
						this.sound_list.unlock(i);
					}
				}
				// After the recording was stopped, close the input streams
				stream.flush();
				stream.close();
				LoTAS.LOGGER.info("Sound Recording finished");

			} catch (IOException e) {
				mc.tell(() -> {
					mc.setScreen(new ErrorScreen(Component.literal("Something went wrong while trying to record!"), Component.literal("Check the console for error messages.")));
				});
				e.printStackTrace();
			}
		}).start();
		// Check screen resolution
		if (mc.getWindow().getScreenWidth() < 1280 || mc.getWindow().getScreenHeight() < 720)
			mc.gui.getChat().addMessage(Component.literal("\u00A7cWarning: \u00A7fYour Screen resolution is not supported for TAS Recorder"));
	}

	/**
	 * Stops the recording
	 */
	public void stopRecording() {
		this.isRecording.set(false);
		this.video_list.clear();
		this.currentFrame = 0;
	}

	/**
	 * Returns whether it is recording or not
	 * @return Is Recording
	 */
	public boolean isRecording() {
		return this.isRecording.get();
	}

}
