package de.pfannekuchen.lotas.core.utils;

//#if MC>=10900
import net.minecraft.util.math.Vec3d;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorMinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
//#endif

/**
 * Calculation Utils for calculating projectile trails
 * @author Wurstv7
 * @since v2.0
 * @version v2.0
 */
public class TrajectoriesCalculator {

	//#if MC>=10900
	/**
	 * Method that calculates where a projectile is going to land.
	 * @return Returns a Vector3 where the Projectile will land
	 */
	public static Vec3d calculate() {
		
		EntityPlayerSP player = MCVer.player(Minecraft.getMinecraft());
		
		// check if player is holding item
		ItemStack stack = player.inventory.getCurrentItem();
		if(stack == null)
			return null;
		
		boolean usingBow = stack.getItem() instanceof ItemBow;
		
		// calculate starting position
		double arrowPosX = player.lastTickPosX
			+ (player.posX - player.lastTickPosX) * ((AccessorMinecraftClient) Minecraft.getMinecraft()).timer().renderPartialTicks
			- Math.cos((float)Math.toRadians(player.rotationYaw)) * 0.16F;
		double arrowPosY = player.lastTickPosY
			+ (player.posY - player.lastTickPosY)
				* ((AccessorMinecraftClient) Minecraft.getMinecraft()).timer().renderPartialTicks
			+ player.getEyeHeight() - 0.1;
		double arrowPosZ = player.lastTickPosZ
			+ (player.posZ - player.lastTickPosZ)
				* ((AccessorMinecraftClient) Minecraft.getMinecraft()).timer().renderPartialTicks
			- Math.sin((float)Math.toRadians(player.rotationYaw)) * 0.16F;
		
		// calculate starting motion
		float arrowMotionFactor = usingBow ? 1F : 0.4F;
		float yaw = (float)Math.toRadians(player.rotationYaw);
		float pitch = (float)Math.toRadians(player.rotationPitch);
		double arrowMotionX =
			-Math.sin(yaw) * Math.cos(pitch) * arrowMotionFactor;
		double arrowMotionY = -Math.sin(pitch) * arrowMotionFactor;
		double arrowMotionZ =
			Math.cos(yaw) * Math.cos(pitch) * arrowMotionFactor;
		double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX
			+ arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
		arrowMotionX /= arrowMotion;
		arrowMotionY /= arrowMotion;
		arrowMotionZ /= arrowMotion;
		if(usingBow)
		{
			float bowPower = (72000 - player.getItemInUseCount()) / 20F;
			bowPower = (bowPower * bowPower + bowPower * 2F) / 3F;
			
			if(bowPower > 1F || bowPower <= 0.1F)
				bowPower = 1F;
			
			bowPower *= 3F;
			arrowMotionX *= bowPower;
			arrowMotionY *= bowPower;
			arrowMotionZ *= bowPower;
			
		}else
		{
			arrowMotionX *= 1.5D;
			arrowMotionY *= 1.5D;
			arrowMotionZ *= 1.5D;
		}
		
		double gravity =
			usingBow ? 0.05D : stack.getItem() instanceof ItemPotion ? 0.4D
				: stack.getItem() instanceof ItemFishingRod ? 0.15D : 0.03D;
		Vec3d playerVector = new Vec3d(player.posX,
			player.posY + player.getEyeHeight(), player.posZ);
		
		for(int i = 0; i < 1000; i++) {
			
			arrowPosX += arrowMotionX * 0.1;
			arrowPosY += arrowMotionY * 0.1;
			arrowPosZ += arrowMotionZ * 0.1;
			arrowMotionX *= 0.999D;
			arrowMotionY *= 0.999D;
			arrowMotionZ *= 0.999D;
			arrowMotionY -= gravity * 0.1;
			
			if(MCVer.world(Minecraft.getMinecraft()).rayTraceBlocks(playerVector,
				new Vec3d(arrowPosX, arrowPosY, arrowPosZ)) != null)
				break;
		}
		
		return new Vec3d(arrowPosX, arrowPosY, arrowPosZ);
	}
	//#endif
	
}
