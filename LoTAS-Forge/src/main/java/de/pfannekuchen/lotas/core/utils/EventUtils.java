package de.pfannekuchen.lotas.core.utils;

import java.time.Duration;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiAiManipulation;
import de.pfannekuchen.lotas.gui.GuiEntitySpawnManipulation;
import de.pfannekuchen.lotas.mixin.accessors.AccessorRenderManager;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * All events that MinecraftForge provides end up here, excluding the initialization event.
 * @author Pancake
 * @since v1.0
 * @version v1.1
 */
public class EventUtils {
	
	/**
	 * Event for LoTAS that renders text overlays onto the screen
	 * @param e RenderEvent provided by MinecraftForge.
	 */
	@SubscribeEvent public void onDraw(RenderGameOverlayEvent.Post e) {
		if (checkNonText(e)) return; // Check whether the event is not a text render event.
		de.pfannekuchen.lotas.gui.HudSettings.drawOverlay(); // Render the Info-HUD overlay
		
		if (ConfigUtils.getBoolean("tools", "showTickIndicator") && TickrateChangerMod.tickrate <= 5F && TickrateChangerMod.show) {
			// Render the Tick Indicator whenever ever second tick occurs and the Tickrate is below 5
			Minecraft.getMinecraft().getTextureManager().bindTexture(TickrateChangerMod.streaming);
			Gui.drawModalRectWithCustomSizedTexture(new net.minecraft.client.gui.ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
		}
	}
	
	/**
	 * Pre-processed way, to check whether the current RenderGameOverlayEvent(.Post) is rendering text or not.
	 * @since v1.1
	 */
	private boolean checkNonText(RenderGameOverlayEvent e) {
		//#if MC>=10900
		if (e.getType() != ElementType.TEXT) return true;
		//#else
//$$ 		if (e.type != ElementType.TEXT) return true;
		//#endif
		return false;
	}

	/**
	 * Event for LoTAS that renders the Keybinds onto the screen
	 * @param e RenderGameOverlayEvent provided by MinecraftForge.
	 */
	@SubscribeEvent public void render(RenderGameOverlayEvent e) {
		if (checkNonText(e)) return; // Check whether the event is not a text render event.
		
		/* Render the Keybind Overlay */
		net.minecraft.client.gui.FontRenderer renderer = MCVer.getFontRenderer(Minecraft.getMinecraft());
    	int height = new net.minecraft.client.gui.ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
		String out1 = "";
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        /* Obtain every pressed key and add it to a string */
		for (net.minecraft.client.settings.KeyBinding binds : gs.keyBindings) {
			try {
				if (binds.isKeyDown()) out1 += org.lwjgl.input.Keyboard.getKeyName(binds.getKeyCode()) + " ";
			} catch (Exception e3) {
				
			}
		}
		// Add left and right-click to the string if pressed.
		if (gs.keyBindAttack.isKeyDown()) out1 += "LC ";
		if (gs.keyBindUseItem.isKeyDown()) out1 += "RC ";
		// Render the Keybinds onto the screen
        renderer.drawStringWithShadow(out1, 5, height - 11, 0xFFFFFF);
	}
	
	/**
	 * Handles the timer and all other keybind events
	 */
	public static void onInput2() {
		/* Handle all keybinds */
		try {
			KeybindsUtils.keyEvent(); // Trigger the KeybindsUtils method to handle most of the keybinds
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// Timer keybinds
		if (KeybindsUtils.toggleTimerKeybind.isPressed() && de.pfannekuchen.lotas.taschallenges.ChallengeMap.currentMap == null) { // Start/Stop the timer if there are no tas challenges running
			if (Timer.ticks < 0 || Timer.startTime == null) { // Start the timer
				Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
				Timer.ticks = 0;
			}
			Timer.running = !Timer.running; // Start/stop the timers state
		}
		
		// Tickrate keybinds
		if (KeybindsUtils.increaseTickrateKeybind.isPressed()) TickrateChangerMod.index++;
		else if (KeybindsUtils.decreaseTickrateKeybind.isPressed()) TickrateChangerMod.index--;
		else return;
		TickrateChangerMod.index = MCVer.clamp(TickrateChangerMod.index, 0, 11); // Update the index of the recommended Tickrates array
		TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]); // Update the Tickrate
	}
	
	/**
	 * Render Event triggered by MinecraftForge that renders all 3D stuff for LoTAS
	 * @param e RenderWorldLastEvent provided by MinecraftForge.
	 */
	@SubscribeEvent public void onRender(RenderWorldLastEvent e) {
		final GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		/* Render the Entity Spawn Manipulation or AI Manipulation Outline 3D Box */
		if (gui instanceof GuiEntitySpawnManipulation) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH); // enable wireframe mode
			GL11.glLineWidth(2); // set wireframe line width
			
			// offset the view to the cameras perspective
			final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			final double renderX = ((double) ((GuiEntitySpawnManipulation) gui).spawnX - 0.5f) - ((AccessorRenderManager) renderManager).renderPosX();
			final double renderY = ((double) ((GuiEntitySpawnManipulation) gui).spawnY) - ((AccessorRenderManager) renderManager).renderPosY();
			final double renderZ = ((double) ((GuiEntitySpawnManipulation) gui).spawnZ - 0.5F) - ((AccessorRenderManager) renderManager).renderPosZ();
			GL11.glTranslated(renderX, renderY, renderZ);
			
			// draw a box
			GL11.glScalef(1, 2, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(0F, 1F, 0F, 0.15F);
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		} else if (gui instanceof GuiAiManipulation) {
			if (GuiAiManipulation.entities.size() == 0) return;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH); // wireframe mode
			GL11.glLineWidth(2); // wireframe size
			
			// offset the view to the camera perspective
			final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			double renderX = ((double) GuiAiManipulation.entities.get(GuiAiManipulation.selectedIndex).posX - 0.5f) - ((AccessorRenderManager) renderManager).renderPosX();
			double renderY = ((double) GuiAiManipulation.entities.get(GuiAiManipulation.selectedIndex).posY) - ((AccessorRenderManager) renderManager).renderPosY();
			double renderZ = ((double) GuiAiManipulation.entities.get(GuiAiManipulation.selectedIndex).posZ - 0.5F) - ((AccessorRenderManager) renderManager).renderPosZ();
			GL11.glTranslated(renderX, renderY, renderZ);
			
			// draw first box around the entity
			GL11.glScalef(1, 2, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(1F, 0F, 0F, 0.15F);
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			renderX = GuiAiManipulation.spawnX - ((AccessorRenderManager) renderManager).renderPosX();
			renderY = GuiAiManipulation.spawnY - ((AccessorRenderManager) renderManager).renderPosY();
			renderZ = GuiAiManipulation.spawnZ - ((AccessorRenderManager) renderManager).renderPosZ();
			
			// render second box around the target
			GL11.glTranslated(renderX, renderY, renderZ);
			GL11.glScalef(1, 1, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(0F, 0F, 1F, 0.15F);
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		}
	}
	
	/**
	 * Timer struct that contains timer data
	 * @author Pancake
	 * @since v1.0
	 * @version v1.1
	 */
	public static class Timer {

		/** RTA-Time when the timer was started */
		public static Duration startTime;
		/** Amount of Ticks passed since the timer was started */
		public static int ticks = -1;
		/** Whether the timer should be counting ticks or not */
		public static boolean running;
		/** List of GuiScreens where the timer should continue running */
		public static final List<String> allowed = java.util.Arrays.asList("guichest", "guibeacon", "guibrewingstand", "guichat", "guinewchat", "guicommandblock", "guidispenser",
				"guienchantment", "guifurnace", "guihopper", "guiinventory", "guirecipebook", "guigameover", "guirecipeoverlay", "guimerchant", "guicontainercreative", "guishulkerbox", "guirepair", "guicrafting");
	    
		/**
		 * Formats a Duration to a string with MM:ss:mm format
		 * @param d Duration to format
		 * @return Returns a formatted duration string
		 */
	    public static String getDuration(Duration d) {
	        return String.format("%02d", d.toMinutes()) + ":" + String.format("%02d", d.getSeconds() % 60) + "." + (int) ((d.toMillis() % 1000));
	    }
		
		/**
		 * Formats the current time to a string with MM:ss:mm format
		 * @return Returns the current timers formatted duration string
		 */
		public static String getCurrentTimerFormatted() {
			Duration d = Duration.ofMillis(ticks * 50);
			return String.format("%02d", d.toMinutes()) + ":" + String.format("%02d", d.getSeconds() % 60) + "." + (int) ((d.toMillis() % 1000));
		}
		
	}
	
}
