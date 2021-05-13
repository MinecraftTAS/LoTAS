package de.pfannekuchen.lotas.mixin;

import java.io.IOException;
import java.time.Duration;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.gui.GuiChallengeIngameMenu;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeLoader;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
//#if MC>=10900
import net.minecraft.util.math.MathHelper;
//#else
//$$ import net.minecraft.util.MathHelper;
//#endif

@Mixin(Minecraft.class)
public class MixinMinecraft {

	/*
	 * This Mixin automatically pauses the Game when entering a World and 
	 * makes Tickrate 0 work
	 */
	
	@Shadow
	public int rightClickDelayTimer;
	
	@Shadow
	public GameSettings gameSettings;
	
	@Shadow
    public GuiScreen currentScreen;
	
	private int save = 6;
	
	private int das = 0;
	
	private boolean isLoadingWorld;
	
	@Shadow
	//#if MC>=11100
	public EntityPlayerSP player;
	//#else
//$$ 	public EntityPlayerSP thePlayer;
	//#endif
	
	@Shadow
	private boolean isGamePaused;
	
	@Inject(method = "Lnet/minecraft/client/Minecraft;loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
	public void injectloadWorld(WorldClient worldClientIn, String loadingMessage, CallbackInfo ci) {
		isLoadingWorld = ConfigUtils.getBoolean("tools", "hitEscape") && worldClientIn != null;
		
		if (ChallengeLoader.startTimer) {
			ChallengeLoader.startTimer = false;
			EventUtils.Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
			EventUtils.Timer.ticks = 1;
			EventUtils.Timer.running = true;
		}
	}
	
	@Inject(method = "updateDisplay", cancellable = true, at = @At("HEAD"))
	public void redoUpdateDisplay(CallbackInfo ci) {
		if (SavestateMod.isLoading) ci.cancel();
	}
	
	@Inject(method = "runTick", at = @At(value="HEAD"))
	public void injectrunTick(CallbackInfo ci) {
		if (ConfigUtils.getBoolean("tools", "lAutoClicker")) rightClickDelayTimer = 0;
    	
		TickrateChangerMod.show = !TickrateChangerMod.show;
		
		if (KeybindsUtils.shouldSavestate) {
			KeybindsUtils.shouldSavestate = false;
			SavestateMod.savestate(null);
		}
		
		if (KeybindsUtils.shouldLoadstate) {
			KeybindsUtils.shouldLoadstate = false;
			try {
				if (ChallengeMap.currentMap != null) ChallengeLoader.reload();
				else if (SavestateMod.hasSavestate()) SavestateMod.loadstate(-1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (TickrateChangerMod.advanceClient) {
			TickrateChangerMod.resetAdvanceClient();
		}
		
		TickrateChangerMod.ticksPassed++;
		if (EventUtils.Timer.running) {
			if (currentScreen == null) EventUtils.Timer.ticks++;
			else if (EventUtils.Timer.allowed.contains(currentScreen.getClass().getSimpleName().toLowerCase())) EventUtils.Timer.ticks++;
		}
		
		if (TickrateChangerMod.ticksToJump != -1 && Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu == false) {
			TickrateChangerMod.ticksToJump--;
			if (TickrateChangerMod.ticksToJump == 0) {
				TickrateChangerMod.ticksToJump = -1;
				Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			}
		}
	}
	
	private void moveRelatively(float strafe, float up, float forward, float friction) {
        float f = strafe * strafe + up * up + forward * forward;
        if (f >= 1.0E-4F)
        {
        	//#if MC>=11100
            f = MathHelper.sqrt(f);
            //#else
            //$$ f = MathHelper.sqrt_float(f);
            //#endif
            if (f < 1.0F) f = 1.0F;
            f = friction / f;
            strafe = strafe * f;
            up = up * f;
            forward = forward * f;
            //#if MC>=11100
            float f1 = MathHelper.sin(player.rotationYaw * 0.017453292F);
            float f2 = MathHelper.cos(player.rotationYaw * 0.017453292F);
            player.posX += (double)(strafe * f2 - forward * f1);
            player.posY += (double)up;
            player.posZ += (double)(forward * f2 + strafe * f1);
            //#else
            //$$ float f1 = MathHelper.sin(thePlayer.rotationYaw * 0.017453292F);
            //$$ float f2 = MathHelper.cos(thePlayer.rotationYaw * 0.017453292F);
            //$$ thePlayer.posX += (double)(strafe * f2 - forward * f1);
            //$$ thePlayer.posY += (double)up;
            //$$ thePlayer.posZ += (double)(forward * f2 + strafe * f1);
            //#endif
        }
	}
	
    @Inject(method = "runGameLoop", at = @At(value="HEAD"))
    public void injectrunGameLoop(CallbackInfo ci) throws IOException {
    	das--;
    	
    	if (TickrateChangerMod.tickrate == 0) {
    		TickrateChangerMod.timeOffset += System.currentTimeMillis() - TickrateChangerMod.timeSinceZero;
    		TickrateChangerMod.timeSinceZero = System.currentTimeMillis();
    	}
    	
    	if (TickrateChangerMod.tickrate == 0 && Keyboard.isKeyDown(KeybindsUtils.advanceTicksKeybind.getKeyCode()) && das <= 0 && !KeybindsUtils.isFreecaming) {
    		TickrateChangerMod.advanceTick();
    		das = 15;
    	}
    	if (TickrateChangerMod.tickrate == 0 && currentScreen == null && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
    		((Minecraft) (Object) this).displayGuiScreen(new GuiIngameMenu());
    		TickrateChangerMod.updateTickrate(KeybindsUtils.savedTickrate);
    		KeybindsUtils.isFreecaming = false; MCVer.player((Minecraft) (Object) this).noClip = false;
    		Minecraft.getMinecraft().renderGlobal.loadRenderers();
    	}
    	
    	//Controls for freecam
    	if (KeybindsUtils.isFreecaming) {
    		if (Keyboard.isKeyDown(gameSettings.keyBindForward.getKeyCode())) {
    			moveRelatively(0.0F, 0.0F, 0.91F, 1.0F);
    		} 
    		if (Keyboard.isKeyDown(gameSettings.keyBindBack.getKeyCode())) {
    			moveRelatively(0.0F, 0.0F, -0.91F, 1.0F);
	   		} 
    		if (Keyboard.isKeyDown(gameSettings.keyBindLeft.getKeyCode())) {
	   			moveRelatively(0.91F, 0.0F, 0.0F, 1.0F);
			} 
    		if (Keyboard.isKeyDown(gameSettings.keyBindRight.getKeyCode())) {
				moveRelatively(-0.91F, 0.0F, 0.0F, 1.0F);
			} 
    		if (Keyboard.isKeyDown(gameSettings.keyBindJump.getKeyCode())) {
				moveRelatively(0.0F, 0.92F, 0.0F, 1.0F);
			} 
    		if (Keyboard.isKeyDown(gameSettings.keyBindSneak.getKeyCode())) {
				moveRelatively(0.0F, -0.92F, 0.0F, 1.0F);
			}
    	}
    	
    	if (Keyboard.isKeyDown(KeybindsUtils.toggleFreecamKeybind.getKeyCode()) && Minecraft.getMinecraft().currentScreen == null && das <= 0) {
    		das = 15;
    		if (KeybindsUtils.isFreecaming) {
    			KeybindsUtils.isFreecaming = false; MCVer.player((Minecraft) (Object) this).noClip = false;
    			Minecraft.getMinecraft().renderGlobal.loadRenderers();
				TickrateChangerMod.updateTickrate(KeybindsUtils.savedTickrate);
			} else {
				KeybindsUtils.isFreecaming = true; MCVer.player((Minecraft) (Object) this).noClip = true;
				//#if MC>=11100
				player.moveForward = 0f;
				player.moveStrafing = 0f;
				//#else
//$$ 				thePlayer.moveForward = 0f;
//$$ 				thePlayer.moveStrafing = 0f;
				//#endif
				KeybindsUtils.savedTickrate = (int)TickrateChangerMod.tickrate;
				TickrateChangerMod.updateTickrate(0);
			}
		}
    	
    	else if (Keyboard.isKeyDown(KeybindsUtils.toggleAdvanceKeybind.getKeyCode()) && das <= 0 && TickrateChangerMod.advanceClient == false && !KeybindsUtils.isFreecaming && Minecraft.getMinecraft().currentScreen == null) { 
    		if (TickrateChangerMod.tickrate > 0) {
    			save = TickrateChangerMod.index;
    			TickrateChangerMod.updateTickrate(0);
    			TickrateChangerMod.index = 0;
    		} else {
    			TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[save]);
    			TickrateChangerMod.index = save;
    		}
    		das = 15;
    	}
    }
    
    @ModifyVariable(method = "displayGuiScreen", at = @At("STORE"), index = 1, ordinal = 0)
    public GuiScreen changeGuiScreen(GuiScreen screenIn) {
    	if (ChallengeMap.currentMap != null && screenIn != null) {
    		if (screenIn instanceof GuiIngameMenu) return new GuiChallengeIngameMenu();
    	}
		if (isLoadingWorld && screenIn == null) {
			isLoadingWorld = false;
			return ChallengeMap.currentMap == null ? new GuiIngameMenu() : new GuiChallengeIngameMenu();
		}
		return screenIn;
    }
    
    @Inject(at = @At("RETURN"), method = "createDisplay")
    public void injectLogo(CallbackInfo ci) {
		Display.setTitle(Display.getTitle() + " - LoTAS");
    }
    
    @Inject(at = @At("HEAD"), method = "stopIntegratedServer")
    private static void injectstopIntegratedServer(CallbackInfo ci) {
    	if(DupeMod.items!=null)	DupeMod.items.clear();
		if(DupeMod.trackedObjects!=null) DupeMod.trackedObjects.clear();
		if(DupeMod.tileentities!=null)DupeMod.tileentities.clear();
    }
    
	@Inject(method = "displayGuiScreen", at = @At("HEAD"))
	public void injectdisplayGuiScreen(GuiScreen guiScreenIn, CallbackInfo ci) {
		if (((guiScreenIn == null) ? true : guiScreenIn instanceof GuiIngameMenu) && SavestateMod.isLoading) {
			SavestateMod.isLoading = false;
	        SavestateMod.showLoadstateDone = true;
	        SavestateMod.timeTitle = System.currentTimeMillis();
		}
		//#if MC>=11100
		if (guiScreenIn == null && player != null) {
		//#else
//$$ 		if (guiScreenIn == null && thePlayer != null) {
		//#endif
			//#if MC>=11100
			if (SavestateMod.applyVelocity) {
				SavestateMod.applyVelocity = false;
				player.motionX = SavestateMod.motionX;
				player.motionY = SavestateMod.motionY;
				player.motionZ = SavestateMod.motionZ;
			}
			//#else
//$$ 			if (SavestateMod.applyVelocity) {
//$$ 				SavestateMod.applyVelocity = false;
//$$ 				thePlayer.motionX = SavestateMod.motionX;
//$$ 				thePlayer.motionY = SavestateMod.motionY;
//$$ 				thePlayer.motionZ = SavestateMod.motionZ;
//$$ 			}
			//#endif
		}

	}
	
}
