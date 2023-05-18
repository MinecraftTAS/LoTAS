package de.pfannekuchen.lotas.mods;

import java.time.Duration;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.quack.SoundPitchDuck;
import de.pfannekuchen.lotas.mixin.MixinMinecraftServer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorMinecraftClient;
import de.pfannekuchen.lotas.mixin.accessors.AccessorSoundEngine;
import de.pfannekuchen.lotas.mixin.accessors.AccessorTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;

/**
 * Here is the basic Tickrate Changer Management.
 * It contains the Hotkeys, Ticksync, Tickrate and the ticks passed
 * @author Scribble
 * @version v2.0
 * @since v1.0
 */
public class TickrateChangerMod {
	/**
	 * Current tickrate
	 */
	public static float tickrate = 20F;
	/**
	 * Current tickrate of the server
	 */
	public static float tickrateServer = 20F;

	public static long timeOffset = 0L;
	public static long timeSinceZero = System.currentTimeMillis();

	public static int ji = 5; // <- ignore this

	public static int ticksToJump = -1;

	public static int index = 7;
	public static final float[] ticks = new float[] { 0, 0.5f, 1, 2, 4, 5, 10, 20, 40, 50, 200, 600 };

	public static boolean ticksync = true;

	public static long ticksPassed = 0;
	public static long ticksPassedServer = 0;

	public static Duration rta = Duration.ZERO;

	public static float tickrateSaved = 20;

	/**
	 * Signals the MinecraftClient in {@link MixinMinecraft#injectrunTick(org.spongepowered.asm.mixin.injection.callback.CallbackInfo)} to reset the tickrate to 0
	 */
	public static boolean advanceClient = false;
	/**
	 * Signals the MinecraftServer in {@link MixinMinecraftServer#redoServerWait(long)} to reset the tickrate to 0
	 */
	public static boolean advanceServer = false;

	public static long timeSinceTC = System.currentTimeMillis();
	public static long fakeTimeSinceTC = System.currentTimeMillis();

	/**
	 * Changes the tickrate of the client and server.
	 * @param tickrateIn
	 */
	public static void updateTickrate(float tickrateIn) {
		if (tickrateIn == 0F)
			timeSinceZero = System.currentTimeMillis() - timeOffset;

		long time = System.currentTimeMillis() - timeSinceTC - timeOffset;
		fakeTimeSinceTC += (long) (time * (tickrate / 20F));
		timeSinceTC = System.currentTimeMillis() - timeOffset;

		updateClientTickrate(tickrateIn);
		updateServerTickrate(tickrateIn);

		ConfigUtils.setInt("hidden", "tickrate", index);
		ConfigUtils.save();
	}

	public static long getMilliseconds() {
		long time = System.currentTimeMillis() - timeSinceTC - timeOffset;
		time *= (tickrate / 20F);
		return (long) (fakeTimeSinceTC + time);
	}

	/**
	 * Changes the tickrate only on the client
	 * @param tickrateIn tickrate to change
	 */
	public static void updateClientTickrate(float tickrateIn) {
		if (tickrateIn != 0f) {
			((AccessorTimer) ((AccessorMinecraftClient) Minecraft.getInstance()).getTimer()).setTickTime(1000f / tickrateIn);
		} else {
			if (tickrate != 0) {
				tickrateSaved = tickrate;
			}
			((AccessorTimer) ((AccessorMinecraftClient) Minecraft.getInstance()).getTimer()).setTickTime(Float.MAX_VALUE);
		}
		tickrate = tickrateIn;
		if (!ConfigUtils.getBoolean("ui", "hideTickrateMessages") && Minecraft.getInstance().gui != null) {
			Minecraft mc=Minecraft.getInstance();
			mc.gui.getChat().addMessage(MCVer.literal(I18n.get("tickratechanger.lotas.message", "\u00A7b" + tickrateIn)));//"Updated Tickrate to %s"
		}
		updatePitch();
	}

	/**
	 * Changes the tickrate only on the server
	 * @param tickrate
	 */
	public static void updateServerTickrate(float tickrate) {
		TickrateChangerMod.tickrateServer = tickrate;
	}

	/**
	 * Advances the tick on both client and server
	 */
	public static void advanceTick() {
		advanceClient();
		advanceServer();
	}

	/**
	 * Advances the tick on the client
	 */
	public static void advanceClient() {
		if (tickrate == 0) {
			advanceClient = true;
			updateClientTickrate(tickrateSaved);
		}
	}

	/**
	 * Advances the tick on the server
	 */
	public static void advanceServer() {
		if (tickrateServer == 0) {
			advanceServer = true;
			updateServerTickrate(tickrateSaved);
		}
	}

	/**
	 * Resets the tickadvance to tickrate 0 again once {@link TickrateChangerMod#advanceClient} is true
	 */
	public static void resetAdvanceClient() {
		if (advanceClient) {
			advanceClient = false;
			index = 0;
			updateClientTickrate(0);
		}
	}

	/**
	 * Resets the tickadvance to tickrate 0 again once {@link TickrateChangerMod#advanceServer} is true
	 */
	public static void resetAdvanceServer() {
		if (advanceServer) {
			advanceServer = false;
			updateServerTickrate(0);
		}
	}
	
	public static void updatePitch() {
		AccessorSoundEngine soundEngine = (AccessorSoundEngine)Minecraft.getInstance().getSoundManager();
		
		if(soundEngine == null)
			return;
		
		SoundPitchDuck soundManager=(SoundPitchDuck)soundEngine.getSoundEngine();
		
		if(soundManager == null)
			return;
		
		soundManager.updatePitch();
	}

	//public static ResourceLocation streaming = new ResourceLocation("textures/gui/stream_indicator.png");
	public static boolean show = false;
	public static boolean watchGuiClose;
}
