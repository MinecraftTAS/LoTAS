package de.pfannekuchen.lotas.core.utils;

import java.awt.Color;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.GuiAiManipulation;
import de.pfannekuchen.lotas.gui.GuiEntitySpawnManipulation;
import de.pfannekuchen.lotas.mixin.accessors.AccessorRenderManager;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
//#if MC>=10900
import net.minecraft.util.math.MathHelper;
//#else
//$$ import net.minecraft.util.MathHelper;
//#endif
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class EventUtils {
	
	@SubscribeEvent
	public void onDraw(RenderGameOverlayEvent.Post e) {
		if (e.getType() == ElementType.TEXT && Timer.ticks != -1) {
			Gui.drawRect(0, 0, 75, ConfigUtils.getBoolean("ui", "hideRTATimer") ? 13 : 24, new Color(0, 0, 0, 175).getRGB());
			Duration dur = Duration.ofMillis(Timer.ticks * 50);
			if (Timer.running) TickrateChangerMod.rta = Duration.ofMillis(System.currentTimeMillis() - Timer.startTime.toMillis());
			MCVer.getFontRenderer(Minecraft.getMinecraft()).drawStringWithShadow(Timer.getDuration(dur), 1, 3, 0xFFFFFFFF);
			if (!ConfigUtils.getBoolean("ui", "hideRTATimer")) MCVer.getFontRenderer(Minecraft.getMinecraft()).drawStringWithShadow("RTA: " + Timer.getDuration(TickrateChangerMod.rta), 1, 15, 0xFFFFFFFF);
		} 
		if (e.getType() == ElementType.TEXT && ConfigUtils.getBoolean("tools", "showTickIndicator") && TickrateChangerMod.tickrate <= 5F && TickrateChangerMod.show) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(TickrateChangerMod.streaming);
			Gui.drawModalRectWithCustomSizedTexture(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - 17, 1, 0, 0, 16, 16, 16, 64);
		}
	}
	
	@SubscribeEvent
	public void render(RenderGameOverlayEvent e) {
		if (e.getType() != ElementType.TEXT) return;
		FontRenderer renderer = MCVer.getFontRenderer(Minecraft.getMinecraft());
    	int height = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
		String out1 = "";
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
		for (KeyBinding binds : gs.keyBindings) {
			try {
				if (binds.isKeyDown()) out1 += Keyboard.getKeyName(binds.getKeyCode()) + " ";
			} catch (Exception e3) {
				
			}
		}
		if (gs.keyBindAttack.isKeyDown()) out1 += "LC ";
		if (gs.keyBindUseItem.isKeyDown()) out1 += "RC ";
        renderer.drawStringWithShadow(out1, 5, height - 11, 0xFFFFFF);
	}
	
	// TODO: Move onInput2 | onInput to Keybinds.keyEvent();
	
	/**
	 * Turn off timer on timer keybinding, and Power Hotkeys
	 * @param e KeyInputEvent
	 */
	@SubscribeEvent
	public void onInput2(KeyInputEvent e) {
		try {
			KeybindsUtils.keyEvent();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if (KeybindsUtils.toggleTimerKeybind.isPressed() && ChallengeMap.currentMap == null) {
			if (Timer.ticks < 0 || Timer.startTime == null) {
				Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
				Timer.ticks = 0;
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
		if (KeybindsUtils.increaseTickrateKeybind.isPressed()) TickrateChangerMod.index++;
		else if (KeybindsUtils.decreaseTickrateKeybind.isPressed()) TickrateChangerMod.index--;
		else return;
		//#if MC>=11100
		TickrateChangerMod.index = MathHelper.clamp(TickrateChangerMod.index, 0, 10);
		//#else
//$$ 		TickrateChangerMod.index = MathHelper.clamp_int(TickrateChangerMod.index, 0, 10);
		//#endif
		TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
	}
	
	@SubscribeEvent
	public void onRender(RenderWorldLastEvent e) {
		final GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if (gui instanceof GuiEntitySpawnManipulation) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			final double renderX = ((double) ((GuiEntitySpawnManipulation) gui).spawnX - 0.5f) - ((AccessorRenderManager) renderManager).renderPosX();
			final double renderY = ((double) ((GuiEntitySpawnManipulation) gui).spawnY) - ((AccessorRenderManager) renderManager).renderPosY();
			final double renderZ = ((double) ((GuiEntitySpawnManipulation) gui).spawnZ - 0.5F) - ((AccessorRenderManager) renderManager).renderPosZ();
			
			GL11.glTranslated(renderX, renderY, renderZ);
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
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			double renderX = ((double) GuiAiManipulation.entities.get(GuiAiManipulation.selectedIndex).posX - 0.5f) - ((AccessorRenderManager) renderManager).renderPosX();
			double renderY = ((double) GuiAiManipulation.entities.get(GuiAiManipulation.selectedIndex).posY) - ((AccessorRenderManager) renderManager).renderPosY();
			double renderZ = ((double) GuiAiManipulation.entities.get(GuiAiManipulation.selectedIndex).posZ - 0.5F) - ((AccessorRenderManager) renderManager).renderPosZ();
			
			GL11.glTranslated(renderX, renderY, renderZ);
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
			
			// Draw output
			
			renderX = GuiAiManipulation.spawnX - ((AccessorRenderManager) renderManager).renderPosX();
			renderY = GuiAiManipulation.spawnY - ((AccessorRenderManager) renderManager).renderPosY();
			renderZ = GuiAiManipulation.spawnZ - ((AccessorRenderManager) renderManager).renderPosZ();
			
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
	
	public static class Timer {

		public static Duration startTime;
		public static int ticks = -1;
		public static boolean running;
		
		public static final List<String> allowed = ImmutableList.of("guichest", "guibeacon", "guibrewingstand", "guichat", "guinewchat", "guicommandblock", "guidispenser",
				"guienchantment", "guifurnace", "guihopper", "guiinventory", "guirecipebook", "guigameover", "guirecipeoverlay", "guimerchant", "guicontainercreative", "guishulkerbox", "guirepair", "guicrafting");
	    
	    public static String getDuration(Duration d) {
	        return String.format("%02d", d.toMinutes()) + ":" + String.format("%02d", d.getSeconds() % 60) + "." + (int) ((d.toMillis() % 1000));
	    }
		
		public static String getCurrentTimerFormatted() {
			Duration d = Duration.ofMillis(ticks * 50);
			return String.format("%02d", d.toMinutes()) + ":" + String.format("%02d", d.getSeconds() % 60) + "." + (int) ((d.toMillis() % 1000));
		}
		
	}
	
}
