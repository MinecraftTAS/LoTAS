package de.pfannekuchen.lotas.core.utils;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Draws a potion in the gui hud that is moving with the camera
 * @author Pancake
 * @since v1.0
 * @version v1.2
 */
public class PotionRenderingUtils {

//	/** Temporary variable used for lerping */
//	private static float lerpX = 0f;
//	/** Temporary variable used for lerping */
//	private static float lerpY = 0f;
//	/**
//	 * Linear Interpolation between two points using alpha
//	 * @param point1 Starting point
//	 * @param point2 Target point
//	 * @param alpha Lerp-factor
//	 * @return Returns a Lerp'ed string
//	 */
//	private static float lerp(float point1, float point2, float alpha){ return point1 + alpha * (point2 - point1);}
	
	/**
	 * Moves the potion over the hotbar to then render it
	 * @return ItemStack to render over the hotbar
	 */
	public static ItemStack renderPotion() {
		
//		EntityPlayerSP entityplayersp = MCVer.player(Minecraft.getMinecraft());
//        float f22 = entityplayersp.prevRenderArmPitch - entityplayersp.renderArmPitch;
//        float f221 = entityplayersp.prevRenderArmYaw - entityplayersp.renderArmYaw;
//        f22 = MCVer.clamp(f22, -16, 16);
//        f221 = MCVer.clamp(f221, -16, 16);
//
//        lerpX = lerp(f22, lerpX, .8f);
//        lerpY = lerp(f221, lerpY, .8f);
//
//        GlStateManager.translate(lerpY * .012, lerpX * -.012, 0);

		ItemStack stack = new ItemStack(MCVer.getItem("POTION"));
		if (stack.getItem() instanceof ItemPotion) {
			NBTTagCompound cp = new NBTTagCompound();
			//#if MC>=11102
			cp.setInteger("CustomPotionColor", 0x546980);
			//#else
//$$ 			cp.setString("Potion", "minecraft:slowness");
//$$ 			cp.setBoolean("enchoff", true);
			//#endif
			stack.setTagCompound(cp);
		}
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaled = new ScaledResolution(mc);

		double scale = scaled.getScaleFactor();
		int height = mc.displayHeight;
		
		GlStateManager.translate(0, 0, -13);

		double skla=4.1;
		if(scale==3) {
			skla=4.85;
		}
		else if(scale==2) {
			skla=5.6;
		}
		else if(scale==1) {
			skla=6.6;
		}
		double height2=(height/2*-0.004)-skla;


		GlStateManager.translate(0, height2, 0);
		GlStateManager.scale(scale/3, scale/3, scale/3);

		double scale2=1+(1080-height)*0.0015;
		GlStateManager.scale(scale2, scale2, scale2);
		
		IBlockState block = MCVer.world(mc).getBlockState(MCVer.player(mc).getPosition().add(0, 1, 0));
		if (block.getBlock()==Blocks.WATER) {
			GlStateManager.translate(0, 0, -2.5);
		}
		//#if MC>=11102
		GlStateManager.rotate(180, 0, 1 ,0);
		GlStateManager.rotate(10, 0, 0 ,1);
		//#else
//$$ 		GlStateManager.rotate(10, 0, 0 ,-1);
		//#endif
		
        //#if MC>=10900
//        float f = (float)1.0F;
//        float f1 = f / (float)stack.getMaxItemUseDuration();
//
//        GlStateManager.translate(-.75F, 0.15f, 0f);
//
//        float f2 = MCVer.abs((float) (StrictMath.cos(f / 4.0F * (float)Math.PI) * 0.1F));
//        GlStateManager.translate(0.0F, f2 - 4.5f, -4.0F);
//
//        float f3 = 1.0F - (float)Math.pow((double)f1, 27.0D);
//        int i = 1;
//        GlStateManager.translate(f3 * 0.6F * (float)i, f3 - 0.5f, f3 * 0.0F -3F);
//        GlStateManager.rotate((float)i * f3 * 90.0F, 0.0F, 2.0F, 0.0F);
//        GlStateManager.rotate(45, 2.0F, -0.5F, -1.0F);
        return stack;
        //#else
        //$$         GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        //$$         GlStateManager.translate(0.0F, 1 * -0.6F, 0.0F);
        //$$         GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        //$$         float f = MCVer.sin(1 * 1 * (float)Math.PI);
        //$$         float f1 = MCVer.sin(MCVer.sqrt(1) * (float)Math.PI);
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
