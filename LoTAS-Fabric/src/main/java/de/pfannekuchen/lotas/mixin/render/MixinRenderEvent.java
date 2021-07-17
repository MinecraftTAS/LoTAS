package de.pfannekuchen.lotas.mixin.render;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.RenderUtils;
import de.pfannekuchen.lotas.gui.AIManipulationScreen;
import de.pfannekuchen.lotas.gui.SpawnManipulationScreen;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.SpawnManipMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;

/**
 * Renders 3D Stuff
 * @author Pancake
 */
@Mixin(GameRenderer.class)
public class MixinRenderEvent {

	@Inject(at = @At("HEAD"), method = "renderItemInHand")
	public void renderWorldLastEvent(CallbackInfo ci) {
		final Screen gui = Minecraft.getInstance().screen;
		if (gui instanceof SpawnManipulationScreen) {
			MCVer.pushMatrix();
			MCVer.disableTexture();
			MCVer.enableBlend();
			MCVer.enableDepthTest();
			MCVer.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

			//#if MC>=11500
//$$ 			RenderUtils.applyCameraRotationOnly();
			//#endif
			RenderUtils.applyRenderOffset();
			
			Vec3 targetPos=SpawnManipMod.getTargetPos();
			double renderX =  ((int)targetPos.x);
			double renderY =  ((int)targetPos.y);
			double renderZ =  ((int)targetPos.z);

			MCVer.translated(renderX, renderY, renderZ);
			MCVer.scaled(1, 2, 1);
			MCVer.color4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			
			if(SpawnManipMod.canSpawn()) {
				RenderUtils.drawSolidBox(0F, 1F, 0F, 0.15F);
			}else {
				RenderUtils.drawSolidBox(1F, 0F, 0F, 0.15F);
			}
			

			MCVer.disableBlend();
			MCVer.enableTexture();
			MCVer.disableDepthTest();
			MCVer.popMatrix();
		} else if (gui instanceof AIManipulationScreen) {
			if (AIManipMod.getSelectedEntityPos() == null)
				return;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glLineWidth(2);

			//#if MC>=11500
//$$ 			RenderUtils.applyCameraRotationOnly();
			//#endif
			RenderUtils.applyRenderOffset();

			Vec3 entityPos=AIManipMod.getSelectedEntityPos();
			double renderX = ((int)entityPos.x);
			double renderY = ((int)entityPos.y);
			double renderZ = ((int)entityPos.z);

			GL11.glTranslated(renderX, renderY, renderZ);
			GL11.glScalef(1, 2, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			RenderUtils.drawSolidBox(1F, 0F, 0F, 0.15F);

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
			//#if MC>=11500
//$$ 			RenderUtils.applyCameraRotationOnly();
			//#endif
			RenderUtils.applyRenderOffset();

			Vec3 targetPos=AIManipMod.getTargetPos();
			renderX = (int)targetPos.x;
			renderY = (int)targetPos.y;
			renderZ = (int)targetPos.z;

			GL11.glTranslated(renderX, renderY, renderZ);
			GL11.glScalef(1, 1, 1);
			GL11.glColor4f(28, 188, 220, 0.5f);
			RenderUtils.drawOutlinedBox();
			RenderUtils.drawCrossBox();
			GL11.glColor4f(0F, 0F, 1F, 0.15F);
			RenderUtils.drawSolidBox(0F, 0F, 1F, 0.15F);

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glPopMatrix();
		}
	}

}
