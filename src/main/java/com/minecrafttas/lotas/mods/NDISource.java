package com.minecrafttas.lotas.mods;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

import com.minecrafttas.lotas.LoTAS;
import com.minecrafttas.lotas.system.ConfigurationSystem;
import com.minecrafttas.lotas.system.ModSystem.Mod;

import me.walkerknapp.devolay.Devolay;
import me.walkerknapp.devolay.DevolayFrameFourCCType;
import me.walkerknapp.devolay.DevolaySender;
import me.walkerknapp.devolay.DevolayVideoFrame;
import net.minecraft.resources.ResourceLocation;

/**
 * NDI Source for recording TASes without debug overlays
 * @author Pancake
 */
public class NDISource extends Mod {

	/**
	 * NDI Stream sender
	 */
	private DevolaySender stream;
	
	/**
	 * NDI Video buffer
	 */
	private ByteBuffer data;
	
	/**
	 * NDI Video Frame holder
	 */
	private DevolayVideoFrame frame;

	/**
	 * Streaming thread
	 */
	private Thread thread;

	/**
	 * Properties for the NDI Stream
	 */
	private int width, height, framerate;
	
	/**
	 * Whether the ndi stream should be enabled or not
	 */
	private boolean enabled;
	
	/**
	 * Initializes the ndi source
	 */
	public NDISource() {
		super(new ResourceLocation("lotas", "ndisource"));
	}

	/**
	 * Initializes NDI
	 */
	@Override
	protected void onClientsideInitialize() {
		this.enabled = ConfigurationSystem.getBoolean("ndi_enabled", false);
		if (!this.enabled) return;

		this.width = ConfigurationSystem.getInt("ndi_width", 1920);
		this.height = ConfigurationSystem.getInt("ndi_height", 1080);
		this.framerate = ConfigurationSystem.getInt("ndi_framerate", 60);

		if (!Devolay.isSupportedCpu()) {
			LoTAS.LOGGER.warn("NDI not supported on this cpu.");
			return;
		}
		
		if (Devolay.loadLibraries() != 0) {
			LoTAS.LOGGER.error("NDI runtime not found.");
			return;
		}
		
		this.stream = new DevolaySender("LoTAS");
		
		this.data = ByteBuffer.allocateDirect(this.width*this.height*4);
		
		this.frame = new DevolayVideoFrame();
		this.frame.setResolution(this.width, this.height);
		this.frame.setFourCCType(DevolayFrameFourCCType.RGBX);
		this.frame.setData(this.data);
		this.frame.setFrameRate(this.framerate, 1);
		
		this.thread = new Thread(() -> {
			while (true) this.stream.sendVideoFrame(this.frame);
		});
		this.thread.setDaemon(true);
		this.thread.setName("NDI");
		this.thread.start();
		
	}
	
	/**
	 * Fetches the games pixels into the video buffer
	 */
	@Override
	protected void onClientsidePostRender() {
		if (!this.enabled) return;
		
		GL11.glReadPixels(0, 0, this.width, this.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.data);
	}
	
	/**
	 * Closes the ndi stream on shutdown
	 */
	@Override
	protected void onClientsideShutdown() {
		if (!this.enabled) return;
		
		this.thread.interrupt();
		this.frame.close();
		this.stream.close();
	}
	
}
