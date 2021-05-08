package de.pfannekuchen.lotas.core.utils;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

/**
 * Draws a potion in the gui hud that is moving with the camera
 * 
 * @see MixinPotionRenderer
 * @author Pancake
 *
 */
public class PotionRenderingUtils {

	private static float lerpX = 0f;
	private static float lerpY = 0f;
	
	private static float lerp(float point1, float point2, float alpha){ return point1 + alpha * (point2 - point1);}
	
	public static ItemStack renderPotion() {
        EntityPlayerSP entityplayersp = MCVer.player(Minecraft.getMinecraft());
        float f22 = entityplayersp.prevRenderArmPitch - entityplayersp.renderArmPitch;
        float f221 = entityplayersp.prevRenderArmYaw - entityplayersp.renderArmYaw;
        //#if MC>=11100
        f22 = MathHelper.clamp(f22, -16, 16);
        f221 = MathHelper.clamp(f221, -16, 16);
        //#else
        //$$ f22 = MathHelper.clamp_float(f22, -16, 16);
        //$$ f221 = MathHelper.clamp_float(f221, -16, 16);
        //#endif
        
        lerpX = lerp(f22, lerpX, .8f);
        lerpY = lerp(f221, lerpY, .8f);
        
        GlStateManager.translate(lerpY * .025, lerpX * -.025, 0);
		
		ItemStack stack = new ItemStack(Items.POTIONITEM);
		if (stack.getItem() instanceof ItemPotion) {
			NBTTagCompound cp = new NBTTagCompound();
			cp.setInteger("CustomPotionColor", 0x546980);
			stack.setTagCompound(cp);
		}
		float f = (float)1.0F;
        float f1 = f / (float)stack.getMaxItemUseDuration();

        GlStateManager.translate(-.75F, 0.15f, 0f);
        
        float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F);
        GlStateManager.translate(0.0F, f2 - 4.5f, -4.0F);

        float f3 = 1.0F - (float)Math.pow((double)f1, 27.0D);
        int i = 1;
        GlStateManager.translate(f3 * 0.6F * (float)i, f3 - 0.5f, f3 * 0.0F -3F);
        GlStateManager.rotate((float)i * f3 * 90.0F, 0.0F, 2.0F, 0.0F);
        GlStateManager.rotate(45, 2.0F, -0.5F, -1.0F);
        return stack;
	}
}
