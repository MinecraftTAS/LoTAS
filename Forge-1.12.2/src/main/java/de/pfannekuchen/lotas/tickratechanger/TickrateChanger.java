package de.pfannekuchen.lotas.tickratechanger;

import java.awt.Color;
import java.io.IOException;
import java.time.Duration;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.hotkeys.Hotkeys;
import de.pfannekuchen.lotas.mixin.MixinMinecraft;
import de.pfannekuchen.lotas.mixin.tickratechanger.BindMinecraftServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	
	public static int ticksToJump = -1;
	
	public static int index = 6;
	public static final int[] ticks = new int[] {0, 1, 2, 4, 5, 10, 20, 40, 50, 200, 600};
	
	public static boolean ticksync = true;
	
	public static long ticksPassed = 0;
	public static long ticksPassedServer = 0;
	
	private static Duration rta = Duration.ZERO;

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
	@SideOnly(Side.CLIENT)
	public static void updateClientTickrate(int tickrateIn) {
		RLogAPI.logDebug("[TickrateChanger] Updated Client Tickrate to " + tickrateIn);
		if (tickrateIn != 0f) {
			Minecraft.getMinecraft().timer.tickLength = 1000f / tickrateIn;
		} else {
			if(tickrate!=0) {
				tickrateSaved=tickrate;
			}
			Minecraft.getMinecraft().timer.tickLength = Float.MAX_VALUE;
		}
		tickrate = tickrateIn;
		if (!ConfigManager.getBoolean("ui", "hideTickrateMessages") && Minecraft.getMinecraft().ingameGUI != null) Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("Updated Tickrate to \u00A7b" + tickrateIn));
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
	
	@SubscribeEvent
	public void onJoinWorld(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayer) {
			TickrateChanger.ticksPassed = 0;
			e.getEntity().fallDistance = 0f;
			TickrateChanger.ticksPassedServer = 0;
		}
	}
	
	public static ResourceLocation streaming = new ResourceLocation("textures/gui/stream_indicator.png");
	public static boolean show = false;
	
	/**
	 * Drawing timer and RTA timer
	 * @param e RenderGameOverlayEvent.Post
	 */
	@SubscribeEvent
	public void onDraw(RenderGameOverlayEvent.Post e) {
		if (e.getType() == ElementType.TEXT && Timer.ticks != -1) {
			Gui.drawRect(0, 0, 75, ConfigManager.getBoolean("ui", "hideRTATimer") ? 13 : 24, new Color(0, 0, 0, 175).getRGB());
			Duration dur = Duration.ofMillis(Timer.ticks * 50);
			if (Timer.running) rta = Duration.ofMillis(System.currentTimeMillis() - Timer.startTime.toMillis());
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Timer.getDuration(dur), 1, 3, 0xFFFFFFFF);
			if (!ConfigManager.getBoolean("ui", "hideRTATimer")) Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("RTA: " + Timer.getDuration(rta), 1, 15, 0xFFFFFFFF);
		} 
		if (e.getType() == ElementType.TEXT && ConfigManager.getBoolean("tools", "showTickIndicator") && tickrate <= 5F && show) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(streaming);
			Gui.drawModalRectWithCustomSizedTexture(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
		}
	}
	
	/**
	 * Turn off timer on timer keybinding, and Power Hotkeys
	 * @param e KeyInputEvent
	 */
	@SubscribeEvent
	public void onInput2(KeyInputEvent e) {
		try {
			Hotkeys.keyEvent();
		} catch (IOException e1) {
			RLogAPI.logError(e1, "Savestate Error #3");
		}
		
		if (Hotkeys.timer.isPressed() && ChallengeLoader.map == null) {
			if (Timer.ticks < 1 || Timer.startTime == null) {
				Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
				Timer.ticks = 1;
			}
			Timer.running = !Timer.running;
		}
	}
	
	/**
	 * Increasing or decreasing the tickrate
	 * @param e
	 */
	@SubscribeEvent
	public void onInput(KeyInputEvent e) {
		if (Hotkeys.slower.isPressed()) index++;
		else if (Hotkeys.faster.isPressed()) index--;
		else return;
		index = MathHelper.clamp(index, 0, 10);
		TickrateChanger.updateTickrate(ticks[index]);
	}
}
