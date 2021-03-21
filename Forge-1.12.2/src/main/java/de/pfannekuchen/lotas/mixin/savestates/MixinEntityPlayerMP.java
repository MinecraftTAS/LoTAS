package de.pfannekuchen.lotas.mixin.savestates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.savestates.motion.ClientMotionServer;
import de.pfannekuchen.lotas.savestates.motion.ClientMotionServer.Saver;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

@Mixin(EntityPlayerMP.class)
public class MixinEntityPlayerMP {
		
		@Inject(method="writeEntityToNBT", at = @At(value="RETURN"))
		public void writeClientMotion(NBTTagCompound compound, CallbackInfo ci) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			ClientMotionServer.Saver saver=ClientMotionServer.getMotion().get((EntityPlayerMP)(Object)this);
			if(saver!=null) {
	            nbttagcompound.setDouble("x", saver.getClientX());
	            nbttagcompound.setDouble("y", saver.getClientY());
	            nbttagcompound.setDouble("z", saver.getClientZ());
	            nbttagcompound.setFloat("RelativeX", saver.getClientrX());
	            nbttagcompound.setFloat("RelativeY", saver.getClientrY());
	            nbttagcompound.setFloat("RelativeZ", saver.getClientrZ());
	            compound.setTag("clientMotion", nbttagcompound);
			}
		}
		
		@Inject(method = "readEntityFromNBT", at = @At(value="RETURN"))
		public void readClientMotion(NBTTagCompound compound, CallbackInfo ci) {
			NBTTagCompound nbttagcompound = compound.getCompoundTag("clientMotion");
			
			double clientmotionX = nbttagcompound.getDouble("x");
			double clientmotionY = nbttagcompound.getDouble("y");
			double clientmotionZ = nbttagcompound.getDouble("z");
			float clientmotionrX = nbttagcompound.getFloat("RelativeX");
			float clientmotionrY = nbttagcompound.getFloat("RelativeY");
			float clientmotionrZ = nbttagcompound.getFloat("RelativeZ");
			
			ClientMotionServer.Saver saver=new Saver(clientmotionX,clientmotionY,clientmotionZ, clientmotionrX,clientmotionrY,clientmotionrZ);
			ClientMotionServer.getMotion().put((EntityPlayerMP)(Object)this, saver);
			
		}
}
