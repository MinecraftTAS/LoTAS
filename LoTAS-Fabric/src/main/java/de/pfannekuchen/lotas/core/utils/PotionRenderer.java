package de.pfannekuchen.lotas.core.utils;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.Window;
//#if MC>=11502
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
//#if MC>=11700
//$$ import net.minecraft.util.math.Vec3f;
//#else
import net.minecraft.client.util.math.Vector3f;
//#endif
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
//#if MC<=11605
import net.minecraft.nbt.CompoundTag;
//#else
//$$ import net.minecraft.nbt.NbtCompound;
//#endif
import net.minecraft.util.math.MathHelper;

public class PotionRenderer {

	public static float lerpX = 0f;
	public static float lerpY = 0f;

	public static float lerp(float point1, float point2, float alpha) {
		return point1 + alpha * (point2 - point1);
	}

	//#if MC<=11404
	public static ItemStack render() {
		ItemStack stack = new ItemStack(Items.POTION);
		if (stack.getItem() instanceof PotionItem) {
			CompoundTag cp = new CompoundTag();
			cp.putInt("CustomPotionColor", 0x4672A3);
			stack.setTag(cp);
		}
//		float f = (float) 1.0F;
//		float f1 = f / (float) stack.getMaxUseTime();

		Window window= MinecraftClient.getInstance().window;

		double scale=window.getScaleFactor()/3;
		int height=window.getHeight();

		GlStateManager.translated(0, 0, -13);

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


		GlStateManager.translated(0, height2, 0);
		GlStateManager.scaled(scale, scale, scale);

		double scale2=1+(1080-height)*0.0015;
		GlStateManager.scaled(scale2, scale2, scale2);

		if(MinecraftClient.getInstance().player.isSubmergedInWater()) {
			GlStateManager.translated(0, 0, -2.5);
		}
		GlStateManager.rotated(180, 0, 1 ,0);
		GlStateManager.rotated(10, 0, 0 ,1);
		return stack;
	}
	//#else
//$$ 		public static ItemStack render(MatrixStack matrices) {
//$$
//$$ 			ItemStack stack = new ItemStack(Items.POTION);
//$$ 			if (stack.getItem() instanceof PotionItem) {
				//#if MC>=11700
//$$ 				NbtCompound cp = new NbtCompound();
				//#else
//$$ 				CompoundTag cp = new CompoundTag();
				//#endif
//$$ 				cp.putInt("CustomPotionColor", 0x4672A3);
//$$ 				stack.setTag(cp);
//$$ 			}
//$$
//$$ 			Window window= MinecraftClient.getInstance().window;           
//$$
//$$ 			float scale=(float) (window.getScaleFactor()/3);               
//$$ 			int height=window.getHeight();                                 
//$$
//$$ 			matrices.translate(0, 0, -13);                                 
//$$
//$$ 			double skla=4.1;                                               
//$$ 			if(scale==1D) {                                                
//$$ 				skla=4.85;                                                 
//$$ 			}                                                              
//$$ 			else if(scale<1&&scale>0.4) {                                  
//$$ 				skla=5.6;                                                  
//$$ 			}                                                              
//$$ 			else if(scale<0.4&&scale>0.1) {                                
//$$ 				skla=6.6;                                                  
//$$ 			}                                                              
//$$ 			double height2=(height/2*-0.004)-skla;                         
//$$
//$$
//$$ 			matrices.translate(0, height2, 0);                             
//$$ 			matrices.scale(scale, scale, scale);                           
//$$
//$$ 			float scale2=(float) (1+(1080-height)*0.0015);                 
//$$ 			matrices.scale(scale2, scale2, scale2);                        
//$$
//$$ 			if(MinecraftClient.getInstance().player.isSubmergedInWater()) {
//$$ 				matrices.translate(0, 0, -2.5);                     
//$$ 			}                                                              
//$$
			//#if MC>=11700
//$$ 			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
//$$ 			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(10)); 
			//#else
//$$ 			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));                        
//$$ 			matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(10));                           
			//#endif
//$$
//$$ 			return stack;
//$$ 		}
	//#endif
}