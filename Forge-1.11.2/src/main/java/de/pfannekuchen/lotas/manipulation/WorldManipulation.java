package de.pfannekuchen.lotas.manipulation;

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.gui.GuiAIRig;
import de.pfannekuchen.lotas.gui.GuiEntitySpawner;
import de.pfannekuchen.lotas.renderer.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
*
* This Listener drops the best loot that is able to be dropped from entities and blocks.
*
*/
public class WorldManipulation {
	
	/**
	 * Draws a visual help where a new entity is spawned in
	 * @param e
	 */
	@SubscribeEvent
	public void onRender(RenderWorldLastEvent e) {
		final GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if (gui instanceof GuiEntitySpawner) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);
			
			final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			final double renderX = ((double) ((GuiEntitySpawner) gui).spawnX - 0.5f) - renderManager.renderPosX;
			final double renderY = ((double) ((GuiEntitySpawner) gui).spawnY) - renderManager.renderPosY;
			final double renderZ = ((double) ((GuiEntitySpawner) gui).spawnZ - 0.5F) - renderManager.renderPosZ;
			
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
			
			final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
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
