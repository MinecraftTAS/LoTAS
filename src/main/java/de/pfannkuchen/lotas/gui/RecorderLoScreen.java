package de.pfannkuchen.lotas.gui;

import java.io.File;
import java.util.Locale;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.gui.widgets.ButtonLoWidget;
import de.pfannkuchen.lotas.gui.widgets.SliderLoWidget;
import de.pfannkuchen.lotas.gui.widgets.TextFieldLoWidget;
import de.pfannkuchen.lotas.loscreen.LoScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

/**
 * LoScreen shown when starting a new recording
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class RecorderLoScreen extends LoScreen {

	// Label color
	private static final int LABEL_COLOR = 0xff149b5b;
	// Title color
	private static final int TITLE_COLOR = 0xffffffff;
	// Background color
	private static final int BACKGROUND_COLOR = 0xff161618;
	// Error color
	private static final int ERROR_COLOR = 0xffd74747;

	// Variables for printing
	private String path;
	private String input;
	private String output;
	// Hover animation
	float animationProgress;

	// Complaining text component
	Component complaint;

	/**
	 * Initializes the Recorder LoScreen
	 */
	public RecorderLoScreen() {
		this.path = LoTAS.configmanager.getString("recorder", "ffmpeg");
		this.input = LoTAS.configmanager.getString("recorder", "ffmpeg_cmd_in");
		this.output = LoTAS.configmanager.getString("recorder", "ffmpeg_cmd_out");
		if (this.path.equals("null"))
			LoTAS.configmanager.setString("recorder", "ffmpeg", this.path = "C:\\ffmpeg\\bin\\ffmpeg.exe");
		if (this.input.equals("null"))
			LoTAS.configmanager.setString("recorder", "ffmpeg_cmd_in", this.input = "-hwaccel auto -hwaccel_output_format auto");
		if (this.output.equals("null"))
			LoTAS.configmanager.setString("recorder", "ffmpeg_cmd_out", this.output = "-b:v 32M -c:v h264_nvenc");
	}

	@Override
	protected void init() {
		this.addWidget(new TextFieldLoWidget(true, 0.06, 0.27, 0.75, c -> {
			this.path = c;
			LoTAS.configmanager.setString("recorder", "ffmpeg", this.path);
		}, this.path));
		this.addWidget(new TextFieldLoWidget(true, 0.06, 0.39, 0.75, c -> {
			this.input = c;
			LoTAS.configmanager.setString("recorder", "ffmpeg_cmd_in", this.input);
		}, this.input));
		this.addWidget(new TextFieldLoWidget(true, 0.06, 0.51, 0.75, c -> {
			this.output = c;
			LoTAS.configmanager.setString("recorder", "ffmpeg_cmd_out", this.output);
		}, this.output));
		this.addWidget(new SliderLoWidget(false, .5, .5, 0.75, 0.5, c -> {
			double bitrate = c * 32;
			return Component.literal("Bitrate: " + String.format(Locale.ENGLISH, "%.1fM", bitrate));
		}, Component.literal("Bitrate: 16.0M")));

		this.addWidget(new ButtonLoWidget(true, .02, .94, .75, () -> {
			if (this.complaint == null) {
				ClientLoTAS.recordermod.startRecording(this.mc);
				ClientLoTAS.loscreenmanager.setScreen(null);
				if (this.mc.level == null)
					this.mc.setScreen(new TitleScreen());
				else
					this.mc.setScreen(new PauseScreen(true));
			}
		}, Component.literal("Start Recording...")));
		this.addWidget(new ButtonLoWidget(true, .78, .94, .2, () -> {
			ClientLoTAS.loscreenmanager.setScreen(null);
			if (this.mc.level == null)
				this.mc.setScreen(new TitleScreen());
			else
				this.mc.setScreen(new PauseScreen(true));
		}, Component.literal("Cancel")));
		super.init();
	}

	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		this.animationProgress = Math.min(12, this.animationProgress + ClientLoTAS.internaltimer.tickDelta); // Move the animation
		if (this.animationProgress != 12)
			stack.translate(0, -1000 + this.ease(this.animationProgress, 0, 1, 12) * 1000, 0);
		this.fill(stack, 0, 0, 1, 1, RecorderLoScreen.BACKGROUND_COLOR);
		this.draw(stack, Component.literal("LoTAS Recorder"), 0.06, .08, 60, RecorderLoScreen.TITLE_COLOR, false);
		this.draw(stack, Component.literal("Path to ffmpeg"), 0.06, .22, 30, RecorderLoScreen.LABEL_COLOR, false);
		this.draw(stack, Component.literal("Input options"), 0.06, .34, 30, RecorderLoScreen.LABEL_COLOR, false);
		this.draw(stack, Component.literal("Output options"), 0.06, .46, 30, RecorderLoScreen.LABEL_COLOR, false);

		this.complaint = null;
		if (this.path == null || this.path.isEmpty() || this.path.isBlank() || !new File(this.path).exists())
			this.complaint = Component.literal("Cannot start recording: Path to ffmpeg invalid");
		if (!this.output.contains("b:v "))
			this.complaint = Component.literal("Cannot start recording: Bitrate not specified. Specify the bitrate with -b:v.");
		if (!this.output.contains("c:v "))
			this.complaint = Component.literal("Cannot start recording: Codec not specified. Specify the codec with -c:v.");
		if (this.input.contains("-y"))
			this.complaint = Component.literal("Cannot start recording: Option is present twice: -y");
		if (this.input.contains("-c:v"))
			this.complaint = Component.literal("Cannot start recording: Do not override the decoder");
		if (this.input.contains("-f"))
			this.complaint = Component.literal("Cannot start recording: Do not override the input format");
		if (this.input.contains("-s"))
			this.complaint = Component.literal("Cannot start recording: Do not override the input scale");
		if (this.input.contains("-pix_fmt"))
			this.complaint = Component.literal("Cannot start recording: Do not override the input pixel format");
		if (this.input.contains("-i"))
			this.complaint = Component.literal("Cannot start recording: Do not override the input file");
		if (this.input.contains("-r"))
			this.complaint = Component.literal("Cannot start recording: Do not override the input framerate");
		if (this.output.contains("-pix_fmt"))
			this.complaint = Component.literal("Cannot start recording: Do not override the output pixel format");
		if (this.complaint != null)
			this.draw(stack, this.complaint, 0.06, .66, 30, RecorderLoScreen.ERROR_COLOR, true);

		super.render(stack, curX, curY);
	}

}
