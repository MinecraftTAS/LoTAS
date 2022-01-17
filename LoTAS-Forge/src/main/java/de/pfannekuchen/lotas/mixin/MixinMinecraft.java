package de.pfannekuchen.lotas.mixin;

import java.io.IOException;
import java.time.Duration;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.gui.GuiChallengeIngameMenu;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeLoader;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

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
	
	private boolean isLoadingWorld;
	
	@Shadow
	private boolean isGamePaused;
	@Unique
	public boolean wasOnGround = false;

	private boolean once;
	
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
		
		AIManipMod.tick();
		if (MCVer.player((Minecraft) (Object) this) != null) LoTASModContainer.hud.tick();
		
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
		
		if (EventUtils.Timer.running) {
			if (currentScreen == null) EventUtils.Timer.ticks++;
			else if (EventUtils.Timer.allowed.contains(currentScreen.getClass().getSimpleName().toLowerCase())) EventUtils.Timer.ticks++;
		}
		
				
		if (TickrateChangerMod.ticksToJump != -1 && Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu == false) {
			TickrateChangerMod.ticksToJump--;
			if (TickrateChangerMod.ticksToJump == 0) {
				TickrateChangerMod.ticksToJump = -1;
				if (currentScreen == null && MCVer.player((Minecraft) (Object) this) != null) Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			}
		}
		
		if (MCVer.player((Minecraft) (Object) this) != null) {
			if (MCVer.player((Minecraft) (Object) this).onGround && !wasOnGround && KeybindsUtils.holdStrafeKeybind.isKeyDown()) {
				 MCVer.player((Minecraft) (Object) this).rotationYaw += 45;
				 KeyBinding.setKeyBindState(32, false);
			} else if (!MCVer.player((Minecraft) (Object) this).onGround && wasOnGround && KeybindsUtils.holdStrafeKeybind.isKeyDown()) {
				MCVer.player((Minecraft) (Object) this).rotationYaw -= 45;
				KeyBinding.setKeyBindState(32, true);
			}
			wasOnGround = MCVer.player((Minecraft) (Object) this).onGround;
		}
	}
	
    @Inject(method = "runGameLoop", at = @At(value="HEAD"))
    public void injectrunGameLoop(CallbackInfo ci) throws IOException {
    	//Time offset
    	if (TickrateChangerMod.tickrate == 0) {
    		TickrateChangerMod.timeOffset += System.currentTimeMillis() - TickrateChangerMod.timeSinceZero;
    		TickrateChangerMod.timeSinceZero = System.currentTimeMillis();
    	}
    	
    	KeybindsUtils.frameKeyEvent();
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
		if (guiScreenIn == null && MCVer.player((Minecraft) (Object) this) != null) {
			if (SavestateMod.applyVelocity) {
				SavestateMod.applyVelocity = false;
				MCVer.player((Minecraft) (Object) this).motionX = SavestateMod.motionX;
				MCVer.player((Minecraft) (Object) this).motionY = SavestateMod.motionY;
				MCVer.player((Minecraft) (Object) this).motionZ = SavestateMod.motionZ;
			}
			if(!once) {
				once=true;
				AIManipMod.read();
			}
		}

	}
	//#if MC>10900
	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;runTickMouse()V"))
	//#else
//$$ 	@Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;fireKeyInput()V", remap = false))
	//#endif
	public void injectAtRunTickKebindings(CallbackInfo ci) {
		/* Handle all keybinds */
		try {
			KeybindsUtils.tickKeyEvent(); // Trigger the KeybindsUtils method to handle most of the keybinds
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	@Inject(method = "Lnet/minecraft/client/Minecraft;loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
	public void injectLoadLevel(WorldClient world, String string, CallbackInfo ci) {
		if(world==null&&Minecraft.getMinecraft().getIntegratedServer()!=null&&!SavestateMod.isLoading) {
			AIManipMod.save();
		}else if(world!=null) {
			once=false;
		}
	}
	//#if MC>=10900
	@ModifyConstant(method = "runTickMouse", constant = @Constant(longValue = 200L))
	//#else
//$$ 	@ModifyConstant(method = "runTick", constant = @Constant(longValue = 200L))
	//#endif
	public long fixMouseWheel(long twohundredLong) {
		return (long) Math.max(4000F / TickrateChangerMod.tickrate, 200L);
	}
}
