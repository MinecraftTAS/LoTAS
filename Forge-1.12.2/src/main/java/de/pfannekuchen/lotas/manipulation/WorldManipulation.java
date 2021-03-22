package de.pfannekuchen.lotas.manipulation;

import static rlog.RLogAPI.logError;

import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.LoTASModContainer;
import de.pfannekuchen.lotas.gui.GuiAIRig;
import de.pfannekuchen.lotas.gui.GuiEntitySpawner;
import de.pfannekuchen.lotas.gui.GuiLootManipulation;
import de.pfannekuchen.lotas.renderer.RenderUtils;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rlog.RLogAPI;

/**
*
* This Listener drops the best loot that is able to be dropped from entities and blocks.
*
*/
public class WorldManipulation {
	
	@SubscribeEvent
	public void onSound(PlaySoundEvent e) {
		if (e.getSound().getSoundLocation().getResourcePath().contains("blaze") && e.getSound().getSoundLocation().getResourcePath().contains("death")) {
			if (new Random().nextBoolean()) {
				LoTASModContainer.playSound = true;
				RLogAPI.logDebug("[Sounds] Hijacking Blaze Sound");
				
				try {
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(LoTASModContainer.class.getResourceAsStream("data.wav")));
					((FloatControl) clip.getControl(Type.MASTER_GAIN)).setValue(-10);
					clip.start();
					e.setResultSound(null);
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					logError(e1, "Sound couldn't be played #5");
					e1.printStackTrace();
					FMLCommonHandler.instance().exitJava(1, true);
				}
				
			}
		}
	}
	
	/**
	 * Draws a visual help where a new entity is spawned in
	 * @param e
	 */
	@SubscribeEvent
	public void onRender(RenderWorldLastEvent e) {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if (gui instanceof GuiEntitySpawner) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			double renderX = ((double) ((GuiEntitySpawner) gui).spawnX - 0.5f) - renderManager.renderPosX;
			double renderY = ((double) ((GuiEntitySpawner) gui).spawnY) - renderManager.renderPosY;
			double renderZ = ((double) ((GuiEntitySpawner) gui).spawnZ - 0.5F) - renderManager.renderPosZ;
			
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
		} else if (gui instanceof GuiAIRig) {
			if (GuiAIRig.entities.size() == 0) return;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			double renderX = ((double) GuiAIRig.entities.get(GuiAIRig.selectedIndex).posX - 0.5f) - renderManager.renderPosX;
			double renderY = ((double) GuiAIRig.entities.get(GuiAIRig.selectedIndex).posY) - renderManager.renderPosY;
			double renderZ = ((double) GuiAIRig.entities.get(GuiAIRig.selectedIndex).posZ - 0.5F) - renderManager.renderPosZ;
			
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
			
			renderX = GuiAIRig.spawnX - renderManager.renderPosX;
			renderY = GuiAIRig.spawnY - renderManager.renderPosY;
			renderZ = GuiAIRig.spawnZ - renderManager.renderPosZ;
			
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
	
}
