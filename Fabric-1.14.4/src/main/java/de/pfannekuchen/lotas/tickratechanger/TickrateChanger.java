package de.pfannekuchen.lotas.tickratechanger;

import de.pfannekuchen.lotas.ConfigManager;
import de.pfannekuchen.lotas.duck.TickDuck;
import de.pfannekuchen.lotas.hotkeys.Hotkeys;
import de.pfannekuchen.lotas.mixin.tickratechanger.InjectMinecraftClient;
import de.pfannekuchen.lotas.mixin.tickratechanger.MixinMinecraftServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.realms.RealmsScreenProxy;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import rlog.RLogAPI;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TickrateChanger {
	public static Set<Class<? extends Screen>> exceptions = new HashSet<>(Arrays.asList(RealmsScreenProxy.class, TitleScreen.class, SelectWorldScreen.class, LevelLoadingScreen.class, ProgressScreen.class, SaveLevelScreen.class));

	/**
	 * Current tickrate of the client
	 */
	public static float tickrate = 20;
	/**
	 * Saved tickrate of the client
	 */
	public static float tickrateSaved=20;
	/**
	 * Current tickrate of the server
	 */
	public static int tickrateServer = 20;

	// public static int ji = 5;

	public static long timeOffset;
	
	public static int ticksToJump = -1;

	public static boolean isScreenBlocking;
	
	public static int index = 6;
	public static final int[] ticks = new int[] {0, 1, 2, 4, 5, 10, 20, 40, 50, 200, 600};
	
	public static boolean ticksync = true;
	
	public static long ticksPassed = 0;
	public static long ticksPassedServer = 0;
	
	public static boolean showTickIndicator = false;
	
	/**
	 * Signals the MinecraftClient in {@link InjectMinecraftClient#injectrunTick(org.spongepowered.asm.mixin.injection.callback.CallbackInfo)} to reset the tickrate to 0
	 */
	public static boolean advanceClient=false;
	/**
	 * Signals the MinecraftServer in {@link MixinMinecraftServer#injectrunTick(java.util.function.BooleanSupplier, org.spongepowered.asm.mixin.injection.callback.CallbackInfo)} to reset the tickrate to 0
	 */
	public static boolean advanceServer=false;

	public static Screen whatScreenIsCausingBlock;

	// private static Duration rta = Duration.ZERO;
	
	/**
	 * Changes the tickrate of client and server.
	 * @param tickrate
	 */
	public static void updateTickrate(int tickrate) {
		RLogAPI.logDebug("[TickrateChanger] Updated Tickrate to " + tickrate);
		updateClientTickrate(tickrate);
		updateServerTickrate(tickrate);
	}
	
	/**
	 * Changes the tickrate only on the client
	 * @param tickrateIn tickrate to change
	 */
	@Environment(EnvType.CLIENT)
	public static void updateClientTickrate(int tickrateIn) {
		TickrateChanger.tickrate = tickrateIn;
		if (tickrateIn != 0f) {
			setTickTime(1000f / tickrateIn);
		} else {
			if(tickrate!=0) {
				tickrateSaved=tickrate;
			}
			setTickTime(Float.MAX_VALUE);
		}
		MinecraftClient mc = MinecraftClient.getInstance();
		if (!ConfigManager.getBoolean("ui", "hideTickrateMessages")) mc.inGameHud.getChatHud().addMessage(new LiteralText("\u00A7b\u00BB \u00A7fUpdated Tickrate to \u00A7b" + tickrateIn));
		if (ConfigManager.getBoolean("tools", "saveTickrate")) {
			ConfigManager.setInt("hidden", "tickrate", TickrateChanger.index);
			ConfigManager.save();
		}
	}
	
	/**
	 * Changes the tickrate only on the server
	 * @param tickrate
	 */
	public static void updateServerTickrate(int tickrate) {
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
			updateClientTickrate(20);
		}
	}
	
	/**
	 * Advances the tick on the server
	 */
	public static void advanceServer() {
		if(tickrateServer==0) {
			advanceServer=true;
			updateServerTickrate(20);
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
	
	/**
	 * @return Current tick time of the RenderTickCounter
	 */
	public static float getTickTime() {
		return ((TickDuck)MinecraftClient.getInstance()).getTickTime();
	}
	
	/**
	 * 
	 * @param ticksPerSeconds Replaces the ticktime in RenderTickCounter
	 */
	public static void setTickTime(float ticksPerSeconds) {
		((TickDuck)MinecraftClient.getInstance()).setTickTime(ticksPerSeconds);
	}

	public static void onJoinWorld(AbstractClientPlayerEntity player) {
		TickrateChanger.ticksPassed = 0;
		player.fallDistance = 0f;
		TickrateChanger.ticksPassedServer = 0;
	}

	/**
	 * Increasing or decreasing the tickrate
	 */
	public static void onInput() {
		if (Hotkeys.slower.wasPressed())
			index++;
		else if (Hotkeys.faster.wasPressed())
			index--;
		else
			return;
		index = MathHelper.clamp(index, 0, 10);
		TickrateChanger.updateTickrate(ticks[index]);
	}

	public static void onDraw() {
		if (TickrateChanger.tickrate <= 5 && showTickIndicator && ConfigManager.getBoolean("ui", "showTickIndicator")) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("textures/gui/stream_indicator.png"));
			Screen.blit(MinecraftClient.getInstance().window.getScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
		}
	}
}
