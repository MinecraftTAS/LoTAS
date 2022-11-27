package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.world.storage.SaveHandler;

@Mixin(SaveHandler.class)
/**
 * On rare occasions, savestating fails due to the level.dat being
 * 
 * @author Scribble
 *
 */
public class MixinSaveHandler {
	@Inject(method = "saveWorldInfoWithPlayer", at = @At(value = "INVOKE", target = "Ljava/io/File;exists()Z", ordinal = 0), cancellable = true)
	public void cancelLevelDataCreation(CallbackInfo ci) {
		if(SavestateMod.isLoading) {
			ci.cancel();
		}
	}

//	@Shadow
//	private File worldDirectory;
//	
//	@Redirect(method = "saveWorldInfoWithPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompressedStreamTools;writeCompressed(Lnet/minecraft/nbt/NBTTagCompound;Ljava/io/OutputStream;)V"))
//	public void redirect_saveWorldInfoWithPlayer(NBTTagCompound compound, OutputStream outputStream) {
//		if (SavestateMod.isSaving) {
//			File file = new File(this.worldDirectory, "level.dat");
//			if (file.exists()) {
//				file.delete();
//			}
//			try {
//				outputStream = new FileOutputStream(file);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//		if(!SavestateMod.isLoading) {
//			System.out.println("Writing");
//			try {
//				CompressedStreamTools.writeCompressed(compound, outputStream);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
