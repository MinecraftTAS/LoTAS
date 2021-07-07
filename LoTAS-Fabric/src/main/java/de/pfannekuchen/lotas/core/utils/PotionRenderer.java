package de.pfannekuchen.lotas.core.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;

public class PotionRenderer {

	public static float lerpX = 0f;
	public static float lerpY = 0f;

	public static float lerp(float point1, float point2, float alpha) {
		return point1 + alpha * (point2 - point1);
	}

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

		if(Minecraft.getInstance().player.isUnderWater()) {
			GlStateManager.translated(0, 0, -2.5);
		}
		GlStateManager.rotated(180, 0, 1 ,0);
		GlStateManager.rotated(10, 0, 0 ,1);
		return stack;
	}
}