package de.pfannekuchen.lotas.core.utils;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;

import de.pfannekuchen.lotas.core.MCVer;

/**
 * Rendering Utils to render boxes.
 * Not commented.
 * @since v1.0
 * @version v1.0
 * @author Wurstv6
 */
public enum RenderUtils {
	;

	private static final AABB DEFAULT_AABB = new AABB(0, 0, 0, 1, 1, 1);

	public static void applyRenderOffset() {
		Vec3 camPos = getCameraPos();
		MCVer.translated(-camPos.x, -camPos.y, -camPos.z);
	}

	public static void applyRegionalRenderOffset() {
		applyCameraRotationOnly();

		Vec3 camPos = getCameraPos();
		BlockPos blockPos = getCameraBlockPos();

		int regionX = (blockPos.getX() >> 9) * 512;
		int regionZ = (blockPos.getZ() >> 9) * 512;

		MCVer.translated(regionX - camPos.x, -camPos.y, regionZ - camPos.z);
	}

	public static void applyRegionalRenderOffset(ChunkAccess chunk) {
		applyCameraRotationOnly();

		Vec3 camPos = getCameraPos();

		int regionX = (chunk.getPos().getMinBlockX() >> 9) * 512;
		int regionZ = (chunk.getPos().getMinBlockZ() >> 9) * 512;

		GL11.glTranslated(regionX - camPos.x, -camPos.y, regionZ - camPos.z);
	}

	public static void applyCameraRotationOnly() {
		Camera camera = MCVer.getBlockEntityDispatcher().camera;
		MCVer.rotated((int) Mth.wrapDegrees(camera.getXRot()), 1, 0, 0);
		MCVer.rotated((int) Mth.wrapDegrees(camera.getYRot() + 180.0), 0, 1, 0);
	}

	public static Vec3 getCameraPos() {
		return MCVer.getBlockEntityDispatcher().camera.getPosition();
	}

	public static BlockPos getCameraBlockPos() {
		return MCVer.getBlockEntityDispatcher().camera.getBlockPosition();
	}

	public static void drawSolidBox(float r, float g, float b, float a) {
		drawSolidBox(DEFAULT_AABB, r, g, b, a);
	}

	public static void drawSolidBox(AABB bb, float r, float g, float b, float a) {
		Tesselator tesselator = Tesselator.getInstance();
	    BufferBuilder bufferBuilder = tesselator.getBuilder();
	    //#if MC>=1170
	    com.mojang.math.Matrix4f matrix = MCVer.stack.last().pose();
	    com.mojang.blaze3d.systems.RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionColorShader);
	    bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
	    bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();

		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
		
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
		
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
		
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
		
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
	    bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
		
		bufferBuilder.end();
		
		BufferUploader.end(bufferBuilder);
	    //#else
//$$ 	    bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR);
//$$
//$$ 	    bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
//$$ 		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
//$$
//$$ 		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
//$$
//$$ 		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
//$$ 		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
//$$
//$$ 		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
//$$
//$$ 		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
//$$ 		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
//$$
//$$ 		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 	    bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
//$$ 		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
//$$
//$$ 		tesselator.end();
	    //#endif
	}

	public static void drawOutlinedBox() {
		drawOutlinedBox(DEFAULT_AABB);
	}

	public static void drawOutlinedBox(AABB bb) {
		Tesselator tesselator = Tesselator.getInstance();
	    BufferBuilder bufferBuilder = tesselator.getBuilder();
	    
	    //#if MC>=11700
//$$ 	    com.mojang.math.Matrix4f matrix = MCVer.stack.last().pose();
//$$ 	    com.mojang.blaze3d.systems.RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionColorShader);
//$$ 	    bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
//$$ 	    bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
//$$ 		bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
//$$
//$$ 		bufferBuilder.end();
//$$
//$$ 		BufferUploader.end(bufferBuilder);
	    //#else
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION_COLOR);
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();

		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		tesselator.end();
		//#endif
		
	}

	public static void drawCrossBox() {
		drawCrossBox(DEFAULT_AABB);
	}

	public static void drawCrossBox(AABB bb) {
		Tesselator tesselator = Tesselator.getInstance();
	    BufferBuilder bufferBuilder = tesselator.getBuilder();
	    //#if MC>=11700
//$$ 	    bufferBuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
	    //#else
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION_COLOR);
	    //#endif
	    bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.maxX, bb.maxY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.maxY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		                                              
		bufferBuilder.vertex(bb.maxX, bb.minY, bb.maxZ).color(1F, 1F, 1F, 1F).endVertex();
		bufferBuilder.vertex(bb.minX, bb.minY, bb.minZ).color(1F, 1F, 1F, 1F).endVertex();
		tesselator.end();
	}

	public static void drawNode(AABB bb) {
		double midX = (bb.minX + bb.maxX) / 2;
		double midY = (bb.minY + bb.maxY) / 2;
		double midZ = (bb.minZ + bb.maxZ) / 2;
		GL11.glVertex3d(midX, midY, bb.maxZ);
		GL11.glVertex3d(bb.minX, midY, midZ);

		GL11.glVertex3d(bb.minX, midY, midZ);
		GL11.glVertex3d(midX, midY, bb.minZ);

		GL11.glVertex3d(midX, midY, bb.minZ);
		GL11.glVertex3d(bb.maxX, midY, midZ);

		GL11.glVertex3d(bb.maxX, midY, midZ);
		GL11.glVertex3d(midX, midY, bb.maxZ);

		GL11.glVertex3d(midX, bb.maxY, midZ);
		GL11.glVertex3d(bb.maxX, midY, midZ);

		GL11.glVertex3d(midX, bb.maxY, midZ);
		GL11.glVertex3d(bb.minX, midY, midZ);

		GL11.glVertex3d(midX, bb.maxY, midZ);
		GL11.glVertex3d(midX, midY, bb.minZ);

		GL11.glVertex3d(midX, bb.maxY, midZ);
		GL11.glVertex3d(midX, midY, bb.maxZ);

		GL11.glVertex3d(midX, bb.minY, midZ);
		GL11.glVertex3d(bb.maxX, midY, midZ);

		GL11.glVertex3d(midX, bb.minY, midZ);
		GL11.glVertex3d(bb.minX, midY, midZ);

		GL11.glVertex3d(midX, bb.minY, midZ);
		GL11.glVertex3d(midX, midY, bb.minZ);

		GL11.glVertex3d(midX, bb.minY, midZ);
		GL11.glVertex3d(midX, midY, bb.maxZ);
	}

	public static void drawArrow(Vec3 from, Vec3 to) {
		double startX = from.x;
		double startY = from.y;
		double startZ = from.z;

		double endX = to.x;
		double endY = to.y;
		double endZ = to.z;

		GL11.glPushMatrix();

		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(startX, startY, startZ);
		GL11.glVertex3d(endX, endY, endZ);
		GL11.glEnd();

		GL11.glTranslated(endX, endY, endZ);
		GL11.glScaled(0.1, 0.1, 0.1);

		double angleX = Math.atan2(endY - startY, startZ - endZ);
		GL11.glRotated(Math.toDegrees(angleX) + 90, 1, 0, 0);

		double angleZ = Math.atan2(endX - startX, Math.sqrt(Math.pow(endY - startY, 2) + Math.pow(endZ - startZ, 2)));
		GL11.glRotated(Math.toDegrees(angleZ), 0, 0, 1);

		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(0, 2, 1);
		GL11.glVertex3d(-1, 2, 0);

		GL11.glVertex3d(-1, 2, 0);
		GL11.glVertex3d(0, 2, -1);

		GL11.glVertex3d(0, 2, -1);
		GL11.glVertex3d(1, 2, 0);

		GL11.glVertex3d(1, 2, 0);
		GL11.glVertex3d(0, 2, 1);

		GL11.glVertex3d(1, 2, 0);
		GL11.glVertex3d(-1, 2, 0);

		GL11.glVertex3d(0, 2, 1);
		GL11.glVertex3d(0, 2, -1);

		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(1, 2, 0);

		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(-1, 2, 0);

		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(0, 2, -1);

		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(0, 2, 1);
		GL11.glEnd();

		GL11.glPopMatrix();
	}
}