package de.pfannekuchen.lotas.tickratechanger;

import java.time.Duration;

import de.pfannekuchen.lotas.mixin.accessors.MinecraftClientAccessor;
import de.pfannekuchen.lotas.utils.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import rlog.RLogAPI;

/**
 * Here is the basic Tickrate Changer Management.
 * It contains the Hotkeys, Ticksync, Tickrate and the ticks passed
 */
public class TickrateChanger {
	/**
	 * Current tickrate
	 */
	public static float tickrate = 20F;
	/**
	 * Current tickrate of the server
	 */
	public static int tickrateServer = 20;
	
	public static long timeOffset = 0L;
	public static long timeSinceZero = System.currentTimeMillis();
	
	public static int ji = 5; // <- ignore this
	
	public static Duration rta = Duration.ZERO;
 	
	public static int ticksToJump = -1;
	
	public static int index = 6;
	public static final int[] ticks = new int[] {0, 1, 2, 4, 5, 10, 20, 40, 50, 200, 600};
	
	public static boolean ticksync = true;
	
	public static long ticksPassed = 0;
	public static long ticksPassedServer = 0;

	public static float tickrateSaved=20;

	/**
	 * Signals the MinecraftClient in {@link MixinMinecraft#injectrunTick(org.spongepowered.asm.mixin.injection.callback.CallbackInfo)} to reset the tickrate to 0
	 */
	public static boolean advanceClient=false;
	/**
	 * Signals the MinecraftServer in {@link BindMinecraftServer#redoServerWait(long)} to reset the tickrate to 0
	 */
	public static boolean advanceServer=false;

	public static long timeSinceTC = System.currentTimeMillis();
	public static long fakeTimeSinceTC = System.currentTimeMillis();
	public static boolean show;
	
	/**
	 * Changes the tickrate of the client and server.
	 * @param tickrateIn
	 */
	public static void updateTickrate(int tickrateIn) {
		if (tickrateIn == 0) timeSinceZero = System.currentTimeMillis() - timeOffset;
		
		long time = System.currentTimeMillis() - timeSinceTC - timeOffset;
		fakeTimeSinceTC += (long) (time * (tickrate / 20F));
		timeSinceTC = System.currentTimeMillis() - timeOffset;
		
		RLogAPI.logDebug("[TickrateChanger] Updated Tickrate to " + tickrateIn);
		updateClientTickrate(tickrateIn);
		updateServerTickrate(tickrateIn);
		
		ConfigManager.setInt("hidden", "tickrate", index);
		ConfigManager.save();
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
	public static void updateClientTickrate(int tickrateIn) {
		RLogAPI.logDebug("[TickrateChanger] Updated Client Tickrate to " + tickrateIn);
		if (tickrateIn != 0f) {
			((MinecraftClientAccessor) MinecraftClient.getInstance()).getRenderTickCounter().tickTime = 1000f / tickrateIn;
		} else {
			if(tickrate!=0) {
				tickrateSaved=tickrate;
			}
			((MinecraftClientAccessor) MinecraftClient.getInstance()).getRenderTickCounter().tickTime = Float.MAX_VALUE;
		}
		tickrate = tickrateIn;
		if (!ConfigManager.getBoolean("ui", "hideTickrateMessages") && MinecraftClient.getInstance().inGameHud != null) MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("Updated Tickrate to \u00A7b" + tickrateIn));
	}
	
	/**
	 * Changes the tickrate only on the server
	 * @param tickrate
	 */
	public static void updateServerTickrate(int tickrate) {
		RLogAPI.logDebug("[TickrateChanger] Updated Server to " + tickrate);
		TickrateChanger.tickrateServer = tickrate;
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
		if(tickrate==0) {
			advanceClient=true;
			updateClientTickrate((int)tickrateSaved);
		}
	}
	
	/**
	 * Advances the tick on the server
	 */
	public static void advanceServer() {
		if(tickrateServer==0) {
			advanceServer=true;
			updateServerTickrate((int)tickrateSaved);
		}
	}
	
	/**
	 * Resets the tickadvance to tickrate 0 again once {@link TickrateChanger#advanceClient} is true
	 */
	public static void resetAdvanceClient() {
		if(advanceClient) {
			advanceClient=false;
			index=0;
			updateClientTickrate(0);
		}
	}
	
	/**
	 * Resets the tickadvance to tickrate 0 again once {@link TickrateChanger#advanceServer} is true
	 */
	public static void resetAdvanceServer() {
		if(advanceServer) {
			advanceServer=false;
			updateServerTickrate(0);
		}
	}
	
}
