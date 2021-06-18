package de.pfannekuchen.lotas.core.utils;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
//#if MC>=11502
//$$ import net.minecraft.client.util.math.MatrixStack;
//#if MC<=11605
//$$ import net.minecraft.client.util.math.Vector3f;
//#else
//$$ import net.minecraft.util.math.Vec3f;
//#endif
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
		ClientPlayerEntity entityplayersp = MinecraftClient.getInstance().player;
		float f22 = entityplayersp.prevPitch - entityplayersp.prevPitch;
		float f221 = entityplayersp.prevYaw - entityplayersp.prevYaw;
		f22 = MathHelper.clamp(f22, -16, 16);
		f221 = MathHelper.clamp(f221, -16, 16);

		lerpX = lerp(f22, lerpX, .8f);
		lerpY = lerp(f221, lerpY, .8f);

		GlStateManager.translated(lerpY * .025, lerpX * -.025, 0);

		ItemStack stack = new ItemStack(Items.POTION);
		if (stack.getItem() instanceof PotionItem) {
			CompoundTag cp = new CompoundTag();
			cp.putInt("CustomPotionColor", 0x4672A3);
			stack.setTag(cp);
		}
		float f = (float) 1.0F;
		float f1 = f / (float) stack.getMaxUseTime();

		float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float) Math.PI) * 0.1F);
		GlStateManager.translatef(0.12F, f2 - 4.5f, -4.0F);

		float f3 = 1.0F - (float) Math.pow((double) f1, 27.0D);
		int i = 1;
		if(entityplayersp.isSubmergedInWater()) {
			GlStateManager.translatef(0,0.2F,-1.1F);
		}
		GlStateManager.translatef(f3 * 0.6F * (float) i, f3 - 0.5f, f3 * 0.0F - 3F);
		GlStateManager.rotatef((float) i * f3 * 90.0F, 0.0F, 2.0F, 0.0F);
		GlStateManager.rotatef(45, 2.0F, -0.5F, -1.0F);

		return stack;
	}
	//#else
//$$ 		public static ItemStack render(MatrixStack matrices) {
//$$ 			ClientPlayerEntity entityplayersp = MinecraftClient.getInstance().player;
//$$ 			float f22 = entityplayersp.prevPitch - entityplayersp.prevPitch;
//$$ 			float f221 = entityplayersp.prevYaw - entityplayersp.prevYaw;
//$$ 			f22 = MathHelper.clamp(f22, -16, 16);
//$$ 			f221 = MathHelper.clamp(f221, -16, 16);
//$$
//$$ 			lerpX = lerp(f22, lerpX, .8f);
//$$ 			lerpY = lerp(f221, lerpY, .8f);
//$$
//$$ 			ItemStack stack = new ItemStack(Items.POTION);
//$$ 			if (stack.getItem() instanceof PotionItem) {
				//#if MC<=11700
//$$ 				NbtCompound cp = new NbtCompound();
				//#else
//$$ 				CompoundTag cp = new CompoundTag();
				//#endif
//$$ 				cp.putInt("CustomPotionColor", 0x4672A3);
//$$ 				stack.setTag(cp);
//$$ 			}
//$$ 			float f = (float) 1.0F;
//$$ 			float f1 = f / (float) stack.getMaxUseTime();
//$$
//$$ 			float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float) Math.PI) * 0.1F);
//$$
//$$ 			if(entityplayersp.isSubmergedInWater()) {
//$$ 				matrices.translate(0,0.2F,-1.1F);
//$$ 			}
//$$ 			matrices.translate(0.75, -3.6, -6);
//$$ 			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
//$$ 			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(40.0F));
//$$
//$$ 			return stack;
//$$ 		}
	//#endif
}