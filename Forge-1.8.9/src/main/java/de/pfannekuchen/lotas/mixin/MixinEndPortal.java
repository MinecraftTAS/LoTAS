package de.pfannekuchen.lotas.mixin;

import java.io.File;
import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEndPortal extends EntityPlayer {
	
	public MixinEndPortal(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
	}

	@Inject(at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/server/management/ServerConfigurationManager;transferPlayerToDimension(Lnet/minecraft/entity/player/EntityPlayerMP;I)V"), method = "travelToDimension", cancellable = true)
	public void injectHere(int dimensionIn, CallbackInfo ci) {
		System.out.println("fck");
		if (dimensionIn == 1 && ChallengeLoader.map != null) {
			setGameType(GameType.SPECTATOR);
			Timer.running = false;
			GuiIngame chat = Minecraft.getMinecraft().ingameGUI;
			chat.getChatGUI().clearChatMessages();
			chat.getChatGUI().printChatMessage(new ChatComponentText("You have completed: \u00A76" + ChallengeLoader.map.displayName + "\u00A7f! Your Time is: " + Timer.getCurrentTimerFormatted()));
			chat.getChatGUI().printChatMessage(new ChatComponentText("Please submit your \u00A7craw \u00A7fvideo to \u00A77#new-misc-things \u00A7f on the Minecraft TAS Discord Server."));
			ChallengeLoader.map = null;
			
            try {
            	Field h = Minecraft.getMinecraft().getClass().getDeclaredField("field_71469_aa");
            	h.setAccessible(true);
            	h.set(Minecraft.getMinecraft(), new AnvilSaveConverter(new File(Minecraft.getMinecraft().mcDataDir, "saves")));
            } catch (Exception e) {
    			e.printStackTrace();
    		}
			
			ci.cancel();
			return;
		}
		if (dimensionIn == 1 && dimension == 0 && ((EntityPlayerMP) (Object) this).theItemInWorldManager.getGameType() == GameType.SPECTATOR) {
			ci.cancel();
			return;
		}
	}
	
}
