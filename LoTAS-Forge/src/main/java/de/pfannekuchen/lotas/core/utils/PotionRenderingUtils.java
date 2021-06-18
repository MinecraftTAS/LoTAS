package de.pfannekuchen.lotas.core.utils;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
//#if MC>=10809
//$$ import net.minecraft.util.MathHelper;
//$$ import net.minecraft.init.Items;
//#endif

/**
 * Draws a potion in the gui hud that is moving with the camera
 * @author Pancake
 * @since v1.0
 * @version v1.2
 */
public class PotionRenderingUtils {

	/** Temporary variable used for lerping */
	private static float lerpX = 0f;
	/** Temporary variable used for lerping */
	private static float lerpY = 0f;
	/**
	 * Linear Interpolation between two points using alpha
	 * @param point1 Starting point
	 * @param point2 Target point
	 * @param alpha Lerp-factor
	 * @return Returns a Lerp'ed string
	 */
	private static float lerp(float point1, float point2, float alpha){ return point1 + alpha * (point2 - point1);}
	
	/**
	 * Moves the potion over the hotbar to then render it
	 * @return ItemStack to render over the hotbar
	 */
	public static ItemStack renderPotion() {
		//#if MC>=10900
		EntityPlayerSP entityplayersp = MCVer.player(Minecraft.getMinecraft());
        float f22 = entityplayersp.prevRenderArmPitch - entityplayersp.renderArmPitch;
        float f221 = entityplayersp.prevRenderArmYaw - entityplayersp.renderArmYaw;
        f22 = MCVer.clamp(f22, -16, 16);
        f221 = MCVer.clamp(f221, -16, 16);

        lerpX = lerp(f22, lerpX, .8f);
        lerpY = lerp(f221, lerpY, .8f);

        GlStateManager.translate(lerpY * .012, lerpX * -.012, 0);

		ItemStack stack = new ItemStack(MCVer.getItem("POTION"));
        if (stack.getItem() instanceof ItemPotion) {
			NBTTagCompound cp = new NBTTagCompound();
			cp.setInteger("CustomPotionColor", 0x546980);
			stack.setTagCompound(cp);
		}
		float f = (float)1.0F;
        float f1 = f / (float)stack.getMaxItemUseDuration();

        GlStateManager.translate(-.75F, 0.15f, 0f);

        float f2 = MCVer.abs((float) (StrictMath.cos(f / 4.0F * (float)Math.PI) * 0.1F));
        GlStateManager.translate(0.0F, f2 - 4.5f, -4.0F);

        float f3 = 1.0F - (float)Math.pow((double)f1, 27.0D);
        int i = 1;
        GlStateManager.translate(f3 * 0.6F * (float)i, f3 - 0.5f, f3 * 0.0F -3F);
        GlStateManager.rotate((float)i * f3 * 90.0F, 0.0F, 2.0F, 0.0F);
        GlStateManager.rotate(45, 2.0F, -0.5F, -1.0F);
        return stack;
		//#else
//$$ 		EntityPlayerSP entityplayersp = MCVer.player(Minecraft.getMinecraft());
//$$         float f22 = entityplayersp.prevRenderArmPitch - entityplayersp.renderArmPitch;
//$$         float f221 = entityplayersp.prevRenderArmYaw - entityplayersp.renderArmYaw;
//$$         f22 = MCVer.clamp(f22, -16, 16);
//$$         f221 = MCVer.clamp(f221, -16, 16);
//$$
//$$         lerpX = lerp(f22, lerpX, .8f);
//$$         lerpY = lerp(f221, lerpY, .8f);
//$$
//$$         GlStateManager.translate(lerpY * .012, lerpX * -.012, 0);
//$$
//$$ 		ItemStack stack = new ItemStack(MCVer.getItem("POTION"));
//$$         //ItemStack stack = new ItemStack(Items.POTIONITEM);
//$$         if (stack.getItem() instanceof ItemPotion) {
//$$ 			NBTTagCompound cp = new NBTTagCompound();
//$$ 			cp.setInteger("CustomPotionColor", 0x546980);
//$$ 			stack.setTagCompound(cp);
//$$ 		}
//$$
//$$         GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
//$$         GlStateManager.translate(0.0F, 1 * -0.6F, 0.0F);
//$$         GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
//$$         float f = MathHelper.sin(1 * 1 * (float)Math.PI);
//$$         float f1 = MathHelper.sin(MathHelper.sqrt_float(1) * (float)Math.PI);
//$$         GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
//$$         GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
//$$         GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
//$$         GlStateManager.scale(0.4F, 0.4F, 0.4F);
//$$         float f2 = MCVer.abs((float) (StrictMath.cos(f / 4.0F * (float)Math.PI) * 0.1F));
//$$         GlStateManager.translate(6F, -5.2f, -6F);
//$$
//$$         float f3 = 1.0F - (float)Math.pow((double)f1, 27.0D);
//$$         int i = 1;
//$$         GlStateManager.translate(f3 * 0.6F * (float)i, f3 - 0.5f, f3 * 0.0F -3F);
//$$         GlStateManager.rotate((float)i * f3 * 90.0F, 0.0F, 2.0F, 0.0F);
//$$         return stack;
        //#endif
	}
}
