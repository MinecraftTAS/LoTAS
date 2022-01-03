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
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TextComponent;

/**
 * LoScreen shown when starting a new recording
 * @author Pancake
 */
public class RecorderLoScreen extends LoScreen {

	// Label Color
	private static final int LABEL_COLOR = 0xff149b5b;
	// Title Color
	private static final int TITLE_COLOR = 0xffffffff;
	// Background Color
	private static final int BACKGROUND_COLOR = 0xff161618;
	// Error Color
	private static final int ERROR_COLOR = 0xffd74747;
	
	// Variables for printing
	private String path;
	private String input;
	private String output;
	// Hover animation
	float animationProgress;
	
	// Complating for why we cannot start recording
	TextComponent complaint;
	
	/**
	 * Initializes the Recorder LoScreen
	 */
	public RecorderLoScreen() {
		path = LoTAS.configmanager.getString("recorder", "ffmpeg").replaceAll("null", "");
		input = LoTAS.configmanager.getString("recorder", "ffmpeg_cmd_in").replaceAll("null", "");
		output = LoTAS.configmanager.getString("recorder", "ffmpeg_cmd_out").replaceAll("null", "");
	}
	
	@Override
	protected void init() {
		addWidget(new TextFieldLoWidget(true, 0.06, 0.27, 0.75, c -> {
			this.path = c;
			LoTAS.configmanager.setString("recorder", "ffmpeg", this.path);
		}, this.path));
		addWidget(new TextFieldLoWidget(true, 0.06, 0.39, 0.75, c -> {
			this.input = c;
			LoTAS.configmanager.setString("recorder", "ffmpeg_cmd_in", this.input);
		}, this.input));
		addWidget(new TextFieldLoWidget(true, 0.06, 0.51, 0.75, c -> {
			this.output = c;
			LoTAS.configmanager.setString("recorder", "ffmpeg_cmd_out", this.output);
		}, this.output));
		addWidget(new SliderLoWidget(false, .5, .5, 0.75, 0.5, c -> {
			double bitrate = c*32;
			return new TextComponent("Bitrate: " + String.format(Locale.ENGLISH, "%.1fM", bitrate));
		}, new TextComponent("Bitrate: 16.0M")));
		
		addWidget(new ButtonLoWidget(true, .02, .94, .96, () -> {
			if (complaint == null) {
				ClientLoTAS.recordermod.startRecording(this.mc);
				ClientLoTAS.loscreenmanager.setScreen(null);
				this.mc.setScreen(new TitleScreen());
			}
		}, new TextComponent("Start Recording...")));
		super.init();
	}
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		fill(stack, 0, 0, 1, 1, BACKGROUND_COLOR);
		draw(stack, new TextComponent("LoTAS Recorder"), 0.06, .08, 60, TITLE_COLOR, false);
		draw(stack, new TextComponent("Path to ffmpeg"), 0.06, .22, 30, LABEL_COLOR, false);
		draw(stack, new TextComponent("Input options"), 0.06, .34, 30, LABEL_COLOR, false);
		draw(stack, new TextComponent("Output options"), 0.06, .46, 30, LABEL_COLOR, false);
		
		complaint = null;
		if (path == null || path.isEmpty() || path.isBlank() || !new File(path).exists()) complaint = new TextComponent("Cannot start recording: Path to ffmpeg invalid");
		if (!output.contains("b:v ")) complaint = new TextComponent("Cannot start recording: Bitrate not specified. Specify the bitrate with -b:v.");
		if (!output.contains("c:v ")) complaint = new TextComponent("Cannot start recording: Codec not specified. Specify the codec with -c:v.");
		if (input.contains("-y")) complaint = new TextComponent("Cannot start recording: Option is present twice: -y");
		if (input.contains("-c:v")) complaint = new TextComponent("Cannot start recording: Do not override the decoder");
		if (input.contains("-f")) complaint = new TextComponent("Cannot start recording: Do not override the input format");
		if (input.contains("-s")) complaint = new TextComponent("Cannot start recording: Do not override the input scale");
		if (input.contains("-pix_fmt")) complaint = new TextComponent("Cannot start recording: Do not override the input pixel format");
		if (input.contains("-i")) complaint = new TextComponent("Cannot start recording: Do not override the input file");
		if (input.contains("-r")) complaint = new TextComponent("Cannot start recording: Do not override the input framerate");
		if (output.contains("-pix_fmt")) complaint = new TextComponent("Cannot start recording: Do not override the output pixel format");
		if (complaint != null) draw(stack, complaint, 0.06, .66, 30, ERROR_COLOR, true);
		
		super.render(stack, curX, curY);
	}
	
}
