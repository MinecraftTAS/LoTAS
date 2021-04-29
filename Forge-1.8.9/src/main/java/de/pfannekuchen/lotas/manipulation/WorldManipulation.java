package de.pfannekuchen.lotas.manipulation;

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.gui.GuiAIRig;
import de.pfannekuchen.lotas.gui.GuiEntitySpawner;
import de.pfannekuchen.lotas.renderer.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
*
* This Listener drops the best loot that is able to be dropped from entities and blocks.
*
*/
public class WorldManipulation {
	
	ResourceLocation potion = new ResourceLocation("lotas", "potion_small.png");
	
	@SubscribeEvent
	public void drawStuff2(RenderGameOverlayEvent event){
		Minecraft mc = Minecraft.getMinecraft();
		if (event.isCancelable() || event.type != ElementType.AIR || event.type == ElementType.TEXT) {
			return;
		}
		int posX = event.resolution.getScaledWidth() / 2;
		int posY = event.resolution.getScaledHeight();
		if(!mc.thePlayer.capabilities.isCreativeMode&&!mc.thePlayer.isSpectator()) {
			mc.renderEngine.bindTexture(potion);
			ItemStack stack = new ItemStack(Items.potionitem);
			
			NBTTagCompound cp = new NBTTagCompound();
			cp.setInteger("CustomPotionColor", 0x546980);
			stack.setTagCompound(cp);
			stack.addEnchantment(Enchantment.luckOfTheSea, 1);
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.4, 1.4, 1.4);
			mc.getRenderItem().renderItemIntoGUI(stack, (int) ((posX - 12) / 1.4f), (int) ((posY - 54) / 1.4f));
			GlStateManager.popMatrix();
		}
	}
	
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
