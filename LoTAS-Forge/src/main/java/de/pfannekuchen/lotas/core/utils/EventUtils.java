package de.pfannekuchen.lotas.core.utils;

import java.time.Duration;
import java.util.List;

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiAiManipulation;
import de.pfannekuchen.lotas.gui.GuiSpawnManipulation;
import de.pfannekuchen.lotas.mixin.accessors.AccessorRenderManager;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.SpawnManipMod;
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
		LoTASModContainer.hud.drawHud();
		
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
	 * Render Event triggered by MinecraftForge that renders all 3D stuff for LoTAS
	 * @param e RenderWorldLastEvent provided by MinecraftForge.
	 */
	@SubscribeEvent public void onRender(RenderWorldLastEvent e) {
		final GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		/* Render the Entity Spawn Manipulation or AI Manipulation Outline 3D Box */
		if (gui instanceof GuiSpawnManipulation) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH); // enable wireframe mode
			GL11.glLineWidth(2); // set wireframe line width
			
			// offset the view to the cameras perspective
			final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			double renderX = Math.floor(SpawnManipMod.getTargetPos().x) - ((AccessorRenderManager) renderManager).renderPosX();
			double renderY = Math.floor(SpawnManipMod.getTargetPos().y) - ((AccessorRenderManager) renderManager).renderPosY();
			double renderZ = Math.floor(SpawnManipMod.getTargetPos().z) - ((AccessorRenderManager) renderManager).renderPosZ();
			GL11.glTranslated(renderX, renderY, renderZ);
			
			// draw a box
			GL11.glScalef(1, 2, 1);
			
			GL11.glColor4f(28, 188, 220, 0.5f);
			
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			if (SpawnManipMod.canSpawn()) {
				GL11.glColor4f(0F, 1F, 0F, 0.15F);
			} else {
				GL11.glColor4f(1F, 0F, 0F, 0.15F);
			}
			RenderUtils.drawSolidBox();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		} else if (gui instanceof GuiAiManipulation) {
			if (AIManipMod.getSelectedEntityPos() == null) return;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH); // wireframe mode
			GL11.glLineWidth(2); // wireframe size
			
			// offset the view to the camera perspective
			final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			double renderX = Math.floor(AIManipMod.getSelectedEntityPos().x) - ((AccessorRenderManager) renderManager).renderPosX();
			double renderY = Math.floor(AIManipMod.getSelectedEntityPos().y) - ((AccessorRenderManager) renderManager).renderPosY();
			double renderZ = Math.floor(AIManipMod.getSelectedEntityPos().z) - ((AccessorRenderManager) renderManager).renderPosZ();
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
			
			renderX = Math.floor(AIManipMod.getTargetPos().x) - ((AccessorRenderManager) renderManager).renderPosX();
			renderY = Math.floor(AIManipMod.getTargetPos().y) - ((AccessorRenderManager) renderManager).renderPosY();
			renderZ = Math.floor(AIManipMod.getTargetPos().z) - ((AccessorRenderManager) renderManager).renderPosZ();
			
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
