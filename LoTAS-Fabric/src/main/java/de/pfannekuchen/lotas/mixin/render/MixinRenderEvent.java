package de.pfannekuchen.lotas.mixin.render;

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
//$$ 	public void renderWorldLastEvent(com.mojang.blaze3d.vertex.PoseStack poseStack, Camera camera, float f, CallbackInfo ci) {
	//#else
	public void renderWorldLastEvent(CallbackInfo ci) {
	//#endif
		Minecraft mc=Minecraft.getInstance();
		final Screen gui = mc.screen;
		//#if MC<=11605
		Object poseStack=null;
		//#endif
		if (gui instanceof SpawnManipulationScreen) {
			MCVer.pushMatrix(poseStack);
			MCVer.disableTexture();
			MCVer.enableBlend();
			MCVer.enableDepthTest();
			MCVer.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

			//#if MC>=11500
			//#if MC<=11605
//$$ 			RenderUtils.applyCameraRotationOnly(poseStack);
			//#endif
			//#endif
			RenderUtils.applyRenderOffset(poseStack);
			
			Vec3 targetPos=SpawnManipMod.getTargetPos();
			double renderX = ((int)targetPos.x);
			double renderY = ((int)targetPos.y);
			double renderZ = ((int)targetPos.z);

			MCVer.translated(poseStack, renderX, renderY, renderZ);
			MCVer.scaled(poseStack, 1, 2, 1);
			RenderUtils.drawOutlinedBox(poseStack);
//			RenderUtils.drawCrossBox();
			
			if(SpawnManipMod.canSpawn()) {
				RenderUtils.drawSolidBox(poseStack, 0F, 1F, 0F, 0.15F);
			}else {
				RenderUtils.drawSolidBox(poseStack, 1F, 0F, 0F, 0.15F);
			}
			
			MCVer.disableBlend();
			MCVer.enableTexture();
			MCVer.disableDepthTest();
			MCVer.popMatrix(poseStack);
		} else if (gui instanceof AIManipulationScreen) {
			if (AIManipMod.getSelectedEntityPos() == null)
				return;
			MCVer.pushMatrix(poseStack);
			MCVer.disableTexture();
			MCVer.enableBlend();
			MCVer.enableDepthTest();
			MCVer.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

			//#if MC>=11500
			//#if MC<=11605
//$$ 			RenderUtils.applyCameraRotationOnly(poseStack);
			//#endif
			//#endif
			RenderUtils.applyRenderOffset(poseStack);

			Vec3 entityPos=AIManipMod.getSelectedEntityPos();
			double renderX = entityPos.x-0.5;
			double renderY = entityPos.y;
			double renderZ = entityPos.z-0.5;

			MCVer.translated(poseStack, renderX, renderY, renderZ);
			MCVer.scaled(poseStack, 1, 2, 1);
			RenderUtils.drawOutlinedBox(poseStack);
//			RenderUtils.drawCrossBox();
			RenderUtils.drawSolidBox(poseStack, 1F, 0F, 0F, 0.15F);

			MCVer.disableBlend();
			MCVer.enableTexture();
			MCVer.disableDepthTest();
			MCVer.popMatrix(poseStack);

			MCVer.pushMatrix(poseStack);
			MCVer.disableTexture();
			MCVer.enableBlend();
			MCVer.enableDepthTest();
			MCVer.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

			// Draw output
			//#if MC>=11500
			//#if MC<=11605
//$$ 			RenderUtils.applyCameraRotationOnly(poseStack);
			//#endif
			//#endif
			RenderUtils.applyRenderOffset(poseStack);

			Vec3 targetPos=AIManipMod.getTargetPos();
			renderX = (int)targetPos.x;
			renderY = (int)targetPos.y;
			renderZ = (int)targetPos.z;

			MCVer.translated(poseStack, renderX, renderY, renderZ);
			
			RenderUtils.drawOutlinedBox(poseStack);
//			RenderUtils.drawCrossBox();
			RenderUtils.drawSolidBox(poseStack, 0F, 0F, 1F, 0.15F);

			MCVer.disableBlend();
			MCVer.enableTexture();
			MCVer.disableDepthTest();
			MCVer.popMatrix(poseStack);
		}
	}
}
