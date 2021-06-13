package de.pfannekuchen.lotas.mixin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.core.utils.Keyboard;
import de.pfannekuchen.lotas.core.utils.TextureYoinker;
import de.pfannekuchen.lotas.gui.ChallengeMenuScreen;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import de.pfannekuchen.lotas.taschallenges.ChallengeLoader;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
	
	@Shadow
	private Screen currentScreen;
	
	@Shadow
	private WorldRenderer worldRenderer;

	private int save;
	
	@Shadow
	private int itemUseCooldown;

	@Shadow
	private ClientPlayerEntity player;

	@Shadow
	private GameOptions options; 

	@Unique
	public boolean wasOnGround;
	
	@Unique
	public boolean isLoadingWorld;
	
	@Inject(method = "joinWorld", at = @At("HEAD"))
	public void injectloadWorld(ClientWorld worldClientIn, CallbackInfo ci) {
		isLoadingWorld = ConfigUtils.getBoolean("tools", "hitEscape") && worldClientIn != null;
		
		if (ChallengeLoader.startTimer) {
			ChallengeLoader.startTimer = false;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
			Timer.ticks = 1;
			Timer.running = true;
		}
	}
	
	//#if MC>=11502
//$$ 	@Inject(method = "run", at = @At("HEAD"))
	//#else
	@Inject(method = "init", at = @At("TAIL"))
	//#endif
	public void loadRenderingLate(CallbackInfo ci) {
		LoTASModContainer.loadShields();
		try {
			//#if MC>=11502
//$$ 			BufferedReader stream = new BufferedReader(new InputStreamReader(new URL("http://mgnet.work/taschallenges/maps1.15.2.txt").openStream()));
			//#else
			BufferedReader stream = new BufferedReader(new InputStreamReader(new URL("http://mgnet.work/taschallenges/maps1.14.4.txt").openStream()));
			//#endif
			int maps = Integer.parseInt(stream.readLine().charAt(0) + "");
			for (int i = 0; i < maps; i++) {
				ChallengeMap map = new ChallengeMap();

				map.displayName = stream.readLine();
				map.name = stream.readLine();
				map.description = stream.readLine();
				map.map = new URL("http://mgnet.work/taschallenges/" + stream.readLine());
				int board = Integer.parseInt(stream.readLine().charAt(0) + "");
				map.leaderboard = new String[board];
				for (int j = 0; j < board; j++) {
					map.leaderboard[j] = stream.readLine();
				}

				map.resourceLoc = TextureYoinker.download(map.name, new URL("http://mgnet.work/taschallenges/" + map.name + ".png").openStream());

				LoTASModContainer.maps.add(map);

				stream.readLine(); // Empty
			}
			stream.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@Inject(method = "tick", at = @At(value="HEAD"))
	public void injectrunTick(CallbackInfo ci) {
		if (ConfigUtils.getBoolean("tools", "rAutoClicker")) itemUseCooldown = 0;
		
		TickrateChangerMod.show = !TickrateChangerMod.show;
		if ((currentScreen == null ? true : Timer.allowed.contains(currentScreen.getClass().getSimpleName().toLowerCase())) && Timer.running) Timer.ticks++;
		
		if (KeybindsUtils.shouldSavestate) {
			KeybindsUtils.shouldSavestate = false;
			try {
				if (ChallengeLoader.map == null)
					SavestateMod.savestate(null);
				else 
					ChallengeLoader.reload();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (player != null) {
			if (player.onGround && !wasOnGround && KeybindsUtils.holdStrafeKeybind.isPressed()) {
				 player.yaw += 45;
				 KeyBinding.setKeyPressed(options.keyRight.getDefaultKeyCode(), false);
			} else if (!player.onGround && wasOnGround && KeybindsUtils.holdStrafeKeybind.isPressed()) {
				player.yaw -= 45;
				KeyBinding.setKeyPressed(options.keyRight.getDefaultKeyCode(), true);
			}
			wasOnGround = player.onGround;
		}
		
		if (KeybindsUtils.shouldLoadstate) {
			KeybindsUtils.shouldLoadstate = false;
			/*try {
				if (ChallengeMap.currentMap != null) ChallengeLoader.reload();
				else */if (SavestateMod.hasSavestate()) SavestateMod.loadstate(-1);
			/*} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		if (TickrateChangerMod.advanceClient) {
			TickrateChangerMod.resetAdvanceClient();
		}
		
		if (TickrateChangerMod.ticksToJump != -1 && MinecraftClient.getInstance().currentScreen instanceof GameMenuScreen == false) {
			TickrateChangerMod.ticksToJump--;
			if (TickrateChangerMod.ticksToJump == 0) {
				TickrateChangerMod.ticksToJump = -1;
				if (currentScreen == null && MinecraftClient.getInstance().player != null) MinecraftClient.getInstance().openScreen(new GameMenuScreen(true));
			}
		}
	}
	
	@Unique
	private void move(float strafe, float up, float forward, float friction) {
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
	
	@Inject(method = "render", at = @At(value = "HEAD"))
	public void injectrunGameLoop(CallbackInfo ci) {

		if (TickrateChangerMod.tickrate == 0) {
			TickrateChangerMod.timeOffset += System.currentTimeMillis() - TickrateChangerMod.timeSinceZero;
			TickrateChangerMod.timeSinceZero = System.currentTimeMillis();
		}

		if (KeybindsUtils.toggleAdvanceKeybind.wasPressed() && TickrateChangerMod.advanceClient == false && !KeybindsUtils.isFreecaming && currentScreen == null) { 
    		if (TickrateChangerMod.tickrate > 0) {
    			save = TickrateChangerMod.index;
    			TickrateChangerMod.updateTickrate(0);
    			TickrateChangerMod.index = 0;
    		} else {
    			TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[save]);
    			TickrateChangerMod.index = save;
    		}
    	}
		
		if (TickrateChangerMod.tickrate == 0 && KeybindsUtils.advanceTicksKeybind.wasPressed() && !KeybindsUtils.isFreecaming) {
			TickrateChangerMod.advanceTick();
		}
		boolean flag = false;
		if (KeybindsUtils.increaseTickrateKeybind.wasPressed()) {
			flag = true;
			TickrateChangerMod.index++;
		} else if (KeybindsUtils.decreaseTickrateKeybind.wasPressed()) {
			flag = true;
			TickrateChangerMod.index--;
		}
		if (flag) {
			TickrateChangerMod.index = MCVer.clamp(TickrateChangerMod.index, 0, 11);
			TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
		}

		if (TickrateChangerMod.tickrate == 0 && currentScreen == null && Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			((MinecraftClient) (Object) this).openScreen(new GameMenuScreen(true));
			TickrateChangerMod.updateTickrate(KeybindsUtils.savedTickrate);
    		KeybindsUtils.isFreecaming = false; MinecraftClient.getInstance().player.noClip = false;
			worldRenderer.reload();
		}
		
		//Controls for freecam
    	if (KeybindsUtils.isFreecaming) {
    		if (options.keyForward.isPressed()) {
    			move(0.0F, 0.0F, 0.91F, 1.0F);
    		} 
    		if (options.keyBack.isPressed()) {
    			move(0.0F, 0.0F, -0.91F, 1.0F);
	   		} 
    		if (options.keyLeft.isPressed()) {
    			move(0.91F, 0.0F, 0.0F, 1.0F);
			} 
    		if (options.keyRight.isPressed()) {
    			move(-0.91F, 0.0F, 0.0F, 1.0F);
			} 
    		if (options.keyJump.isPressed()) {
    			move(0.0F, 0.92F, 0.0F, 1.0F);
			} 
    		if (options.keySneak.isPressed()) {
    			move(0.0F, -0.92F, 0.0F, 1.0F);
			}
    	}
    	
    	if (KeybindsUtils.toggleFreecamKeybind.wasPressed() && MinecraftClient.getInstance().currentScreen == null) {
    		if (KeybindsUtils.isFreecaming) {
    			KeybindsUtils.isFreecaming = false; MinecraftClient.getInstance().player.noClip = false;
    			worldRenderer.reload();
				TickrateChangerMod.updateTickrate(KeybindsUtils.savedTickrate);
			} else {
				KeybindsUtils.isFreecaming = true; MinecraftClient.getInstance().player.noClip = true;
				KeybindsUtils.savedTickrate = (int)TickrateChangerMod.tickrate;
				TickrateChangerMod.updateTickrate(0);
			}
		}
	}
	
	@Inject(method = "handleInputEvents", at = @At("RETURN"), cancellable = true)
	public void keyInputEvent(CallbackInfo ci) {
		KeybindsUtils.keyEvent();
	}
	
	@Inject(method = "openScreen", at = @At(value = "HEAD"))
	public void injectdisplayGuiScreen(Screen guiScreenIn, CallbackInfo ci) {
		if (((guiScreenIn == null) ? true : guiScreenIn instanceof GameMenuScreen) && SavestateMod.isLoading) {
			SavestateMod.isLoading = false;
	        SavestateMod.showLoadstateDone = true;
	        SavestateMod.timeTitle = System.currentTimeMillis();
		}
		if (isLoadingWorld && guiScreenIn == null) {
			isLoadingWorld = false;
			MinecraftClient.getInstance().openScreen(ChallengeLoader.map == null ? new GameMenuScreen(true) : new ChallengeMenuScreen());
			ci.cancel();
		}
		if (guiScreenIn == null && (((MinecraftClient) (Object) this).player != null)) {
			if (SavestateMod.applyVelocity) {
				SavestateMod.applyVelocity = false;
				ClientPlayerEntity player=((MinecraftClient) (Object) this).player;
				player.setVelocity(SavestateMod.motionX, SavestateMod.motionY, SavestateMod.motionZ);
			}
		}

	}
}
