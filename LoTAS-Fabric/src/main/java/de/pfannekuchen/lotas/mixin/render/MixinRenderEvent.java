package de.pfannekuchen.lotas.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.RenderUtils;
import de.pfannekuchen.lotas.gui.AIManipulationScreen;
import de.pfannekuchen.lotas.gui.SpawnManipulationScreen;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.SpawnManipMod;
import net.minecraft.client.Camera;
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
	//#if MC>=11700
//$$ 	public void renderWorldLastEvent(PoseStack poseStack, Camera camera, float f, CallbackInfo ci) {
	//#else
	public void renderWorldLastEvent(CallbackInfo ci) {
	//#endif
		Minecraft mc=Minecraft.getInstance();
		final Screen gui = mc.screen;
		if (gui instanceof SpawnManipulationScreen) {
			MCVer.stack=poseStack;
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
			double renderX = ((int)targetPos.x);
			double renderY = ((int)targetPos.y);
			double renderZ = ((int)targetPos.z);

			MCVer.translated(renderX, renderY, renderZ);
			MCVer.scaled(1, 2, 1);
			RenderUtils.drawOutlinedBox();
//			RenderUtils.drawCrossBox();
			
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
			MCVer.stack=poseStack;
			MCVer.pushMatrix();
			MCVer.disableTexture();
			MCVer.enableBlend();
			MCVer.enableDepthTest();
			MCVer.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

			//#if MC>=11500
//$$ 			RenderUtils.applyCameraRotationOnly();
			//#endif
			RenderUtils.applyRenderOffset();

			Vec3 entityPos=AIManipMod.getSelectedEntityPos();
			double renderX = entityPos.x-0.5;
			double renderY = entityPos.y;
			double renderZ = entityPos.z-0.5;

			MCVer.translated(renderX, renderY, renderZ);
			MCVer.scaled(1, 2, 1);
			RenderUtils.drawOutlinedBox();
//			RenderUtils.drawCrossBox();
			RenderUtils.drawSolidBox(1F, 0F, 0F, 0.15F);

			MCVer.disableBlend();
			MCVer.enableTexture();
			MCVer.disableDepthTest();
			MCVer.popMatrix();

			MCVer.pushMatrix();
			MCVer.disableTexture();
			MCVer.enableBlend();
			MCVer.enableDepthTest();
			MCVer.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

			// Draw output
			//#if MC>=11500
//$$ 			RenderUtils.applyCameraRotationOnly();
			//#endif
			RenderUtils.applyRenderOffset();

			Vec3 targetPos=AIManipMod.getTargetPos();
			renderX = (int)targetPos.x;
			renderY = (int)targetPos.y;
			renderZ = (int)targetPos.z;

			MCVer.translated(renderX, renderY, renderZ);
			
			RenderUtils.drawOutlinedBox();
//			RenderUtils.drawCrossBox();
			RenderUtils.drawSolidBox(0F, 0F, 1F, 0.15F);

			MCVer.disableBlend();
			MCVer.enableTexture();
			MCVer.disableDepthTest();
			MCVer.popMatrix();
		}
	}

}
