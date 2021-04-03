package de.pfannekuchen.lotas.mixin.tickratechanger;

import java.io.IOException;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.ConfigManager;
import de.pfannekuchen.lotas.duck.TickDuck;
import de.pfannekuchen.lotas.gui.AcceptanceScreen;
import de.pfannekuchen.lotas.hotkeys.Hotkeys;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import work.mgnet.identifier.Client;

@Mixin(MinecraftClient.class)
public class InjectMinecraftClient implements TickDuck{

	@Shadow public int itemUseCooldown;
	
	@Inject(method = "tick", at = @At(value="HEAD"))
	public void injectrunTick(CallbackInfo ci) {
		if (ConfigManager.getBoolean("tools", "rAutoClicker")) itemUseCooldown = 0;
		TickrateChanger.timeOffset = Util.getMeasuringTimeMs();
		TickrateChanger.ticksPassed++;
		if (TickrateChanger.advanceClient) {
			TickrateChanger.resetAdvanceClient();
		}
		TickrateChanger.showTickIndicator = !TickrateChanger.showTickIndicator;
		if ((currentScreen == null ? true : Timer.allowed.contains(currentScreen.getClass().getSimpleName().toLowerCase())) && Timer.running) Timer.ticks++;
		if (TickrateChanger.ticksToJump != -1 && MinecraftClient.getInstance().currentScreen instanceof GameMenuScreen == false) {
			TickrateChanger.ticksToJump--;
			if (TickrateChanger.ticksToJump == 0) {
				TickrateChanger.ticksToJump = -1;
				MinecraftClient.getInstance().openScreen(new GameMenuScreen(true));
			}
		}
	}
	
	@Inject(method = "openScreen", at = @At("HEAD"), cancellable = true)
	public void injectopenScreen(Screen screenIn, CallbackInfo ci) {
		if (screenIn instanceof TitleScreen && ConfigManager.getBoolean("hidden", "acceptedDataSending") == false) {
			((MinecraftClient) (Object) this).openScreen(new AcceptanceScreen());
			ci.cancel();
		} else if (screenIn instanceof TitleScreen) {
			Client.main(null);
			if (ConfigManager.getBoolean("tools", "saveTickrate")) TickrateChanger.updateTickrate(TickrateChanger.ticks[ConfigManager.getInt("hidden", "tickrate")]); 
		}
	}

	@Inject(method = "openScreen", at = @At("TAIL"))
	public void endOfOpenScreen(Screen screenIn, CallbackInfo ci) {
        if (TickrateChanger.isScreenBlocking && screenIn != TickrateChanger.whatScreenIsCausingBlock) {
            TickrateChanger.updateTickrate((int) TickrateChanger.tickrateSaved);
            TickrateChanger.whatScreenIsCausingBlock = null;
            TickrateChanger.isScreenBlocking = false;
        }
	}
	
	
	@Inject(method = "render", at = @At(value="HEAD"))
    public void injectrender(CallbackInfo ci) throws IOException {
    	if (TickrateChanger.tickrate == 0 && Hotkeys.advance.wasPressed()) {
    		TickrateChanger.advanceTick();
    	}
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
		
		if (Hotkeys.freecam.wasPressed() && currentScreen == null) {
			if (Hotkeys.isFreecaming) {
				Hotkeys.isFreecaming = false;
				worldRenderer.reload();
				TickrateChanger.updateTickrate(Hotkeys.savedTickrate);
			} else {
				Hotkeys.isFreecaming = true;
				Hotkeys.savedTickrate = (int)TickrateChanger.tickrate;
				TickrateChanger.updateTickrate(0);
			}
		} else if (Hotkeys.zero.wasPressed() && TickrateChanger.advanceClient == false && !Hotkeys.isFreecaming) {
    		if (TickrateChanger.tickrate > 0) {
    			TickrateChanger.updateTickrate(0);
    			TickrateChanger.index=0;
    		} else {
    			TickrateChanger.updateTickrate(20);
    			TickrateChanger.index=6;
    		}
    	}


    }

	private void moveRelatively(float strafe, float up, float forward, float friction) {
		float f = strafe * strafe + up * up + forward * forward;
		if (f >= 1.0E-4F)  {
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


	@Inject(method = "handleInputEvents", at = @At("HEAD"))
	public void keyPressedEvent(CallbackInfo ci) {
		TickrateChanger.onInput();
		Hotkeys.keyEvent();
	}
	
	@Shadow @Final
	private RenderTickCounter renderTickCounter;

	@Shadow @Nullable public Screen currentScreen;

	@Shadow @Final private MinecraftClientGame game;

	@Shadow public GameOptions options;

	@Shadow public ClientPlayerEntity player;

	@Shadow public WorldRenderer worldRenderer;

	@Override
	public float getTickTime() {
		return ((TickDuck)renderTickCounter).getTickTime();
	}

	@Override
	public void setTickTime(float ticksPerSecond) {
		((TickDuck)renderTickCounter).setTickTime(ticksPerSecond);
	}

	@Inject(method = "startIntegratedServer(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/world/level/LevelInfo;)V", at = @At("TAIL"))
	private void startIntegratedServer(CallbackInfo info) {
		MinecraftClient client = MinecraftClient.getInstance();
		new Thread(() -> {
			while (client.getServer() == null || client.world == null || client.player == null) {
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			TickrateChanger.updateTickrate(0);
		}).start();
	}
}
