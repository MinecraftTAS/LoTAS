package de.pfannekuchen.lotas.mixin;

import java.io.IOException;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.savestate.SavestateMod;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import de.pfannekuchen.lotas.utils.ConfigManager;
import de.pfannekuchen.lotas.utils.Hotkeys;
import de.pfannekuchen.lotas.utils.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

	private boolean isLoadingWorld;
	@Shadow public int itemUseCooldown;
	
	@Inject(method = "joinWorld", at = @At("HEAD"))
	public void injectloadWorld(ClientWorld worldClientIn, CallbackInfo ci) {
		isLoadingWorld = ConfigManager.getBoolean("tools", "hitEscape") && worldClientIn != null;
	}
	
	@Inject(method = "tick", at = @At(value="HEAD"))
	public void injectrunTick(CallbackInfo ci) {
		if (ConfigManager.getBoolean("tools", "lAutoClicker")) itemUseCooldown = 0;
		
		if (Hotkeys.shouldLoadstate) {
			Hotkeys.shouldLoadstate = false;
			SavestateMod.loadstate(-1);
		} else if (Hotkeys.shouldSavestate) {
			Hotkeys.shouldSavestate = false;
			SavestateMod.savestate(null);
		}
		
		TickrateChanger.show = !TickrateChanger.show;
		
		TickrateChanger.timeOffset = Util.getMeasuringTimeMs();
		TickrateChanger.ticksPassed++;
		if (TickrateChanger.advanceClient) {
			TickrateChanger.resetAdvanceClient();
		}
		if ((currentScreen == null ? true : Timer.allowed.contains(currentScreen.getClass().getSimpleName().toLowerCase())) && Timer.running) Timer.ticks++;
		if (TickrateChanger.ticksToJump != -1 && MinecraftClient.getInstance().currentScreen instanceof GameMenuScreen == false) {
			TickrateChanger.ticksToJump--;
			if (TickrateChanger.ticksToJump == 0) {
				TickrateChanger.ticksToJump = -1;
				MinecraftClient.getInstance().openScreen(new GameMenuScreen(true));
			}
		}
	}
	
	private void moveRelatively(float strafe, float up, float forward, float friction) {
        float f = strafe * strafe + up * up + forward * forward;
        if (f >= 1.0E-4F)
        {
            f = MathHelper.sqrt(f);
            if (f < 1.0F) f = 1.0F;
            f = friction / f;
            strafe = strafe * f;
            up = up * f;
            forward = forward * f;
            float f1 = MathHelper.sin(player.yaw * 0.017453292F);
            float f2 = MathHelper.cos(player.yaw * 0.017453292F);
            player.x += (double)(strafe * f2 - forward * f1);
            player.y += (double)up;
            player.z += (double)(forward * f2 + strafe * f1);
        }
	}
	
	@Shadow
	public GameOptions options;
	@Shadow
	public ClientPlayerEntity player;
	@Shadow
	public WorldRenderer worldRenderer;
	
	public int save;
	
    @Inject(method = "render", at = @At(value="HEAD"))
    public void injectrunGameLoop(CallbackInfo ci) throws IOException {
    	if (TickrateChanger.tickrate == 0) {
    		TickrateChanger.timeOffset += System.currentTimeMillis() - TickrateChanger.timeSinceZero;
    		TickrateChanger.timeSinceZero = System.currentTimeMillis();
    	}
    	
    	if (TickrateChanger.tickrate == 0 && Hotkeys.advance.wasPressed() && !Hotkeys.isFreecaming) {
    		TickrateChanger.advanceTick();
    	}
    	if (TickrateChanger.tickrate == 0 && currentScreen == null && Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
    		((MinecraftClient) (Object) this).openScreen(new GameMenuScreen(true));
    		TickrateChanger.updateTickrate(Hotkeys.savedTickrate);
    		Hotkeys.isFreecaming = false;
    		worldRenderer.reload();
    	}
    	
    	//Controls for freecam
    	if (Hotkeys.isFreecaming) {
    		if (options.keyForward.isPressed()) {
    			moveRelatively(0.0F, 0.0F, 0.91F, 1.0F);
    		} 
    		if (options.keyBack.isPressed()) {
    			moveRelatively(0.0F, 0.0F, -0.91F, 1.0F);
	   		} 
    		if (options.keyLeft.isPressed()) {
	   			moveRelatively(0.91F, 0.0F, 0.0F, 1.0F);
			} 
    		if (options.keyRight.isPressed()) {
				moveRelatively(-0.91F, 0.0F, 0.0F, 1.0F);
			} 
    		if (options.keyJump.isPressed()) {
				moveRelatively(0.0F, 0.92F, 0.0F, 1.0F);
			} 
    		if (options.keySneak.isPressed()) {
				moveRelatively(0.0F, -0.92F, 0.0F, 1.0F);
			}
    	}
    	
    	if (Hotkeys.freecam.wasPressed() && MinecraftClient.getInstance().currentScreen == null) {
    		if (Hotkeys.isFreecaming) {
    			Hotkeys.isFreecaming = false;
    			worldRenderer.reload();
				TickrateChanger.updateTickrate(Hotkeys.savedTickrate);
			} else {
				Hotkeys.isFreecaming = true;
				Hotkeys.savedTickrate = (int)TickrateChanger.tickrate;
				TickrateChanger.updateTickrate(0);
			}
		}
    	
    	else if (Hotkeys.zero.wasPressed() && TickrateChanger.advanceClient == false && !Hotkeys.isFreecaming && currentScreen == null) { 
    		if (TickrateChanger.tickrate > 0) {
    			save = TickrateChanger.index;
    			TickrateChanger.updateTickrate(0);
    			TickrateChanger.index = 0;
    		} else {
    			TickrateChanger.updateTickrate(TickrateChanger.ticks[save]);
    			TickrateChanger.index = save;
    		}
    	}
    }

	@Inject(method = "handleInputEvents", at = @At("HEAD"))
	public void keyPressedEvent(CallbackInfo ci) {
		try {
			Hotkeys.keyEvent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Shadow @Nullable 
	public Screen currentScreen;
	
	@ModifyVariable(method = "openScreen", at = @At("STORE"), index = 1, ordinal = 0)
    public Screen changeGuiScreen(Screen screenIn) {
		if (isLoadingWorld && screenIn == null) {
			isLoadingWorld = false;
			return new GameMenuScreen(true);
		}
		return screenIn;
    }
	
}
