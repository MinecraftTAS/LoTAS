package de.pfannekuchen.lotas.mixin;

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
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.util.Mth;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {

	@Shadow
	private Screen screen;

	@Shadow
	private LevelRenderer levelRenderer;

	private int save;

	@Shadow
	private int rightClickDelay;

	@Shadow
	private LocalPlayer player;

	@Shadow
	private Options options;

	@Unique
	public boolean wasOnGround;

	@Unique
	public boolean isLoadingWorld;

	@Inject(method = "setLevel", at = @At("HEAD"))
	//#if MC>=11500
//$$ 	public void injectloadWorld(net.minecraft.client.multiplayer.ClientLevel worldClientIn, CallbackInfo ci) {
	//#else
	public void injectloadWorld(net.minecraft.client.multiplayer.MultiPlayerLevel worldClientIn, CallbackInfo ci) {
	//#endif
		isLoadingWorld = ConfigUtils.getBoolean("tools", "hitEscape") && worldClientIn != null;
	}

	//#if MC>=11500
//$$ 	@Inject(method = "run", at = @At("HEAD"))
	//#else
	@Inject(method = "init", at = @At("TAIL"))
	//#endif
	public void loadRenderingLate(CallbackInfo ci) {
		LoTASModContainer.loadShields();
		KeybindsUtils.registerKeybinds();
	}

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void injectrunTick(CallbackInfo ci) {
		if (ConfigUtils.getBoolean("tools", "rAutoClicker"))
			rightClickDelay = 0;

		TickrateChangerMod.show = !TickrateChangerMod.show;
		if ((screen == null ? true : Timer.allowed.contains(screen.getClass().getSimpleName().toLowerCase())) && Timer.running)
			Timer.ticks++;

		if (KeybindsUtils.shouldSavestate) {
			KeybindsUtils.shouldSavestate = false;
			SavestateMod.savestate(null);
		}

		if (player != null) {
			//#if MC>=11600
//$$ 			boolean isOnGround = player.isOnGround();
			//#else
			boolean isOnGround = player.onGround;
			//#endif
			if (isOnGround && !wasOnGround && KeybindsUtils.holdStrafeKeybind.isDown()) {
				player.yRot += 45;
				KeyMapping.set(options.keyRight.getDefaultKey(), false);
			} else if (!isOnGround && wasOnGround && KeybindsUtils.holdStrafeKeybind.isDown()) {
				player.yRot -= 45;
				KeyMapping.set(options.keyRight.getDefaultKey(), true);
			}
			wasOnGround = isOnGround;
		}

		if (KeybindsUtils.shouldLoadstate) {
			KeybindsUtils.shouldLoadstate = false;
			/*try {
				if (ChallengeMap.currentMap != null) ChallengeLoader.reload();
				else */if (SavestateMod.hasSavestate())
				SavestateMod.loadstate(-1);
			/*} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		if (TickrateChangerMod.advanceClient) {
			TickrateChangerMod.resetAdvanceClient();
		}

		if (TickrateChangerMod.ticksToJump != -1 && Minecraft.getInstance().screen instanceof PauseScreen == false) {
			TickrateChangerMod.ticksToJump--;
			if (TickrateChangerMod.ticksToJump == 0) {
				TickrateChangerMod.ticksToJump = -1;
				if (screen == null && Minecraft.getInstance().player != null)
					Minecraft.getInstance().setScreen(new PauseScreen(true));
			}
		}
	}

	@Unique
	private void move(float strafe, float up, float forward, float friction) {
		float f = strafe * strafe + up * up + forward * forward;
		if (f >= 1.0E-4F) {
			f = Mth.sqrt(f);
			if (f < 1.0F)
				f = 1.0F;
			f = friction / f;
			strafe = strafe * f;
			up = up * f;
			forward = forward * f;
			float f1 = Mth.sin(player.yRot * 0.017453292F);
			float f2 = Mth.cos(player.yRot * 0.017453292F);
			MCVer.setXYZ(player, MCVer.getX(player) + (strafe * f2 - forward * f1), MCVer.getY(player) + up, MCVer.getZ(player) + (forward * f2 + strafe * f1));
		}
	}

	@Inject(method = "runTick", at = @At(value = "HEAD"))
	public void injectrunGameLoop(CallbackInfo ci) {

		if (TickrateChangerMod.tickrate == 0) {
			TickrateChangerMod.timeOffset += System.currentTimeMillis() - TickrateChangerMod.timeSinceZero;
			TickrateChangerMod.timeSinceZero = System.currentTimeMillis();
		}

		if (KeybindsUtils.toggleAdvanceKeybind.consumeClick() && TickrateChangerMod.advanceClient == false && !KeybindsUtils.isFreecaming && screen == null) {
			if (TickrateChangerMod.tickrate > 0) {
				save = TickrateChangerMod.index;
				TickrateChangerMod.updateTickrate(0);
				TickrateChangerMod.index = 0;
			} else {
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[save]);
				TickrateChangerMod.index = save;
			}
		}

		if (TickrateChangerMod.tickrate == 0 && KeybindsUtils.advanceTicksKeybind.consumeClick() && !KeybindsUtils.isFreecaming) {
			TickrateChangerMod.advanceTick();
		}
		boolean flag = false;
		if (KeybindsUtils.increaseTickrateKeybind.consumeClick()) {
			flag = true;
			TickrateChangerMod.index++;
		} else if (KeybindsUtils.decreaseTickrateKeybind.consumeClick()) {
			flag = true;
			TickrateChangerMod.index--;
		}
		if (flag) {
			TickrateChangerMod.index = Mth.clamp(TickrateChangerMod.index, 0, 11);
			TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
		}

		if (TickrateChangerMod.tickrate == 0 && screen == null && Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			((Minecraft) (Object) this).setScreen(new PauseScreen(true));
			TickrateChangerMod.updateTickrate(KeybindsUtils.savedTickrate);
			KeybindsUtils.isFreecaming = false;
			Minecraft.getInstance().player.noPhysics = false;
			levelRenderer.allChanged();
		}

		//Controls for freecam
		if (KeybindsUtils.isFreecaming) {
			if (options.keyUp.isDown()) {
				move(0.0F, 0.0F, 0.91F, 1.0F);
			}
			if (options.keyDown.isDown()) {
				move(0.0F, 0.0F, -0.91F, 1.0F);
			}
			if (options.keyLeft.isDown()) {
				move(0.91F, 0.0F, 0.0F, 1.0F);
			}
			if (options.keyRight.isDown()) {
				move(-0.91F, 0.0F, 0.0F, 1.0F);
			}
			if (options.keyJump.isDown()) {
				move(0.0F, 0.92F, 0.0F, 1.0F);
			}
			//#if MC>=11500
//$$ 			if (options.keyShift.isDown()) {
			//#else
			if (options.keySneak.isDown()) {
			//#endif
				move(0.0F, -0.92F, 0.0F, 1.0F);
			}
		}

		if (KeybindsUtils.toggleFreecamKeybind.consumeClick() && Minecraft.getInstance().screen == null) {
			if (KeybindsUtils.isFreecaming) {
				KeybindsUtils.isFreecaming = false;
				Minecraft.getInstance().player.noPhysics = false;
				levelRenderer.allChanged();
				TickrateChangerMod.updateTickrate(KeybindsUtils.savedTickrate);
			} else {
				KeybindsUtils.isFreecaming = true;
				Minecraft.getInstance().player.noPhysics = true;
				KeybindsUtils.savedTickrate = (int) TickrateChangerMod.tickrate;
				TickrateChangerMod.updateTickrate(0);
			}
		}
	}

	@Inject(method = "handleKeybinds", at = @At("RETURN"), cancellable = true)
	public void keyInputEvent(CallbackInfo ci) {
		KeybindsUtils.keyEvent();
	}

	@Inject(method = "setScreen", at = @At(value = "HEAD"), cancellable = true)
	public void injectdisplayGuiScreen(Screen guiScreenIn, CallbackInfo ci) {
		if (((guiScreenIn == null) ? true : guiScreenIn instanceof PauseScreen) && SavestateMod.isLoading) {
			SavestateMod.isLoading = false;
			SavestateMod.showLoadstateDone = true;
			SavestateMod.timeTitle = System.currentTimeMillis();
		}
		if (isLoadingWorld && guiScreenIn == null) {
			isLoadingWorld = false;
			Minecraft.getInstance().setScreen(new PauseScreen(true));
			ci.cancel();
		}
		if (guiScreenIn == null && (((Minecraft) (Object) this).player != null)) {
			if (SavestateMod.applyVelocity) {
				SavestateMod.applyVelocity = false;
				LocalPlayer player = ((Minecraft) (Object) this).player;
				player.setDeltaMovement(SavestateMod.motionX, SavestateMod.motionY, SavestateMod.motionZ);
			}
		}

	}
}
