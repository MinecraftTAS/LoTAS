package com.minecrafttas.lotas.mods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import com.minecrafttas.lotas.mixin.client.accessors.AccessorMinecraft;
import com.minecrafttas.lotas.system.ModSystem.Mod;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.Camera;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Overlay including 3D elements for TASing 
 * @author Pancake
 */
public class Overlay extends Mod {
	
	/**
	 * !! PROOF OF CONCEPT !!
	 * this overlay is just a proof of concept for the ndi source and will be reworked in the future
	 */
	
	private DebugScreenOverlay screen;
	
	/**
	 * Initializes the overlay
	 */
	public Overlay() {
		super(new ResourceLocation("lotas", "overlay"));
		
	}
	
	@Override
	protected void onClientsideTick() {
		if (this.mc.level != null && this.screen == null)
			this.screen = new DebugScreenOverlay(this.mc);
	}
	
	@Override
	protected void onClientsideDisconnect() {
		this.screen = null;
	}
	
	@Override
	protected void onClientsidePostRender() {
		if (this.screen != null) this.screen.render();
		if (this.mc.level == null) return;
		GameRenderer rend = mc.gameRenderer;
		Camera camera = rend.getMainCamera();
		EntityRenderDispatcher erd = mc.getEntityRenderDispatcher();
		float partialTicks = ((AccessorMinecraft) mc).timer().partialTick;
		
		Lighting.turnOn();
		
		try {
			Method m = rend.getClass().getDeclaredMethod("setupCamera", float.class);
			m.setAccessible(true);
			m.invoke(rend, partialTicks);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		erd.setRenderHitBoxes(true);
		
		GL11.glRotated(camera.getXRot(), 1f, 0f, 0f);
		GL11.glRotated(camera.getYRot(), 0f, 1f, 0f);

        for (Entity entity : mc.level.entitiesForRendering()) {
        	if (entity instanceof Player) continue;
        	
        	xOff = camera.getPosition().x;
        	yOff = camera.getPosition().y;
        	zOff = camera.getPosition().z;
        	
            double d = Mth.lerp((double)partialTicks, entity.xOld, entity.x);
            double e = Mth.lerp((double)partialTicks, entity.yOld, entity.y);
            double g = Mth.lerp((double)partialTicks, entity.zOld, entity.z);
            float h = Mth.lerp(partialTicks, entity.yRotO, entity.yRot);
        	
        	renderHitbox(entity, xOff - d, e - yOff, zOff - g, h, partialTicks);
        }

		erd.setRenderHitBoxes(false);
        
	}
	
	private double xOff;
	private double yOff;
	private double zOff;
	
	private void renderHitbox(Entity entity, double d, double e, double f, float g, float h) {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        float i = entity.getBbWidth() / 2.0f;
        AABB aABB = entity.getBoundingBox();
        LevelRenderer.renderLineBox(aABB.minX - entity.x + d, aABB.minY - entity.y + e, aABB.minZ - entity.z + f, aABB.maxX - entity.x + d, aABB.maxY - entity.y + e, aABB.maxZ - entity.z + f, 1.0f, 1.0f, 1.0f, 1.0f);
        if (entity instanceof EnderDragon) {
            for (EnderDragonPart enderDragonPart : ((EnderDragon)entity).getSubEntities()) {
                double j = (enderDragonPart.x - enderDragonPart.xo) * (double)h;
                double k = (enderDragonPart.y - enderDragonPart.yo) * (double)h;
                double l = (enderDragonPart.z - enderDragonPart.zo) * (double)h;
                AABB aABB2 = enderDragonPart.getBoundingBox();
                LevelRenderer.renderLineBox(aABB2.minX - this.xOff + j, aABB2.minY - this.yOff + k, aABB2.minZ - this.zOff + l, aABB2.maxX - this.xOff + j, aABB2.maxY - this.yOff + k, aABB2.maxZ - this.zOff + l, 0.25f, 1.0f, 0.0f, 1.0f);
            }
        }
        if (entity instanceof LivingEntity) {
            LevelRenderer.renderLineBox(d - (double)i, e + (double)entity.getEyeHeight() - (double)0.01f, f - (double)i, d + (double)i, e + (double)entity.getEyeHeight() + (double)0.01f, f + (double)i, 1.0f, 0.0f, 0.0f, 1.0f);
        }
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        Vec3 vec3 = entity.getViewVector(h);
        vec3 = vec3.reverse();
        bufferBuilder.begin(3, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(d, e + (double)entity.getEyeHeight(), f).color(0, 0, 255, 255).endVertex();
        bufferBuilder.vertex(d + vec3.x * 2.0, e + (double)entity.getEyeHeight() + vec3.y * 2.0, f + vec3.z * 2.0).color(0, 0, 255, 255).endVertex();
        tesselator.end();
        GlStateManager.enableTexture();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }
	
}
