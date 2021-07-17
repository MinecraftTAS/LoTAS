package de.pfannekuchen.lotas.core.utils;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;

/**
 * Draws a potion in the gui hud that is moving with the camera
 * 
 * @author ScribbleLP
 * @since v1.0
 * @version v1.2
 */
public class PotionRenderer {

	public static float lerpX = 0f;
	public static float lerpY = 0f;

	public static float lerp(float point1, float point2, float alpha) {
		return point1 + alpha * (point2 - point1);
	}

	//#if MC>=11500
//$$ 	public static ItemStack render(com.mojang.blaze3d.vertex.PoseStack matrices) {
//$$ 		ItemStack stack = new ItemStack(Items.POTION);
//$$ 		if (stack.getItem() instanceof PotionItem) {
//$$ 			CompoundTag cp = new CompoundTag();
//$$ 			cp.putInt("CustomPotionColor", 0x4672A3);
//$$ 			stack.setTag(cp);
//$$ 		}
//$$
//$$ 		Window window = Minecraft.getInstance().window;
//$$
//$$ 		float scale = (float) (window.getGuiScale() / 3);
//$$ 		int height = window.getHeight();
//$$
//$$ 		matrices.translate(0, 0, -13);
//$$
//$$ 		double skla = 4.1;
//$$ 		if (scale == 1D) {
//$$ 			skla = 4.85;
//$$ 		} else if (scale < 1 && scale > 0.4) {
//$$ 			skla = 5.6;
//$$ 		} else if (scale < 0.4 && scale > 0.1) {
//$$ 			skla = 6.6;
//$$ 		}
//$$ 		double height2 = (height / 2 * -0.004) - skla;
//$$
//$$ 		matrices.translate(0, height2, 0);
//$$ 		matrices.scale(scale, scale, scale);
//$$
//$$ 		float scale2 = (float) (1 + (1080 - height) * 0.0015);
//$$ 		matrices.scale(scale2, scale2, scale2);
//$$
//$$ 		if (Minecraft.getInstance().player.isUnderWater()) {
//$$ 			matrices.translate(0, 0, -2.5);
//$$ 		}
//$$ 		matrices.mulPose(com.mojang.math.Vector3f.YP.rotationDegrees(180));
//$$ 		matrices.mulPose(com.mojang.math.Vector3f.ZP.rotationDegrees(10));
//$$ 		return stack;
//$$ 	}
	//#else
	public static ItemStack render() {
		ItemStack stack = new ItemStack(Items.POTION);
		if (stack.getItem() instanceof PotionItem) {
			CompoundTag cp = new CompoundTag();
			cp.putInt("CustomPotionColor", 0x4672A3);
			stack.setTag(cp);
		}

		Window window= Minecraft.getInstance().window;

		double scale=window.getGuiScale()/3;
		int height=window.getScreenHeight();

		MCVer.translated(0, 0.0f, -13);

		double skla=4.1;
		if(scale==1D) {
			skla=4.85;
		}
		else if(scale<1&&scale>0.4) {
			skla=5.6;
		}
		else if(scale<0.4&&scale>0.1) {
			skla=6.6;
		}
		double height2=(height/2*-0.004)-skla;


		MCVer.translated(0, height2, 0);
		MCVer.scaled(scale, scale, scale);

		double scale2=1+(1080-height)*0.0015;
		MCVer.scaled(scale2, scale2, scale2);

		if(Minecraft.getInstance().player.isUnderWater()) {
			MCVer.translated(0, 0, -2.5);
		}
		MCVer.rotated(180, 0, 1, 0);
		MCVer.rotated(10, 0, 0, 1);
		return stack;
	}
	//#endif

}