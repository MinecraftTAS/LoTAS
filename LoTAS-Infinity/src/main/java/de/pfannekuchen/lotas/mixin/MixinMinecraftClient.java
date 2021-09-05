package de.pfannekuchen.lotas.mixin;

import java.io.IOException;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.NativeImage;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.core.utils.Keyboard;
import de.pfannekuchen.lotas.core.utils.Timer;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Changes to the Minecraft Classes
 * @author Pancake, ScribbleLP
 */
@Mixin(Minecraft.class)
public class MixinMinecraftClient {

	@Shadow
	private Screen screen;

	@Shadow
	private LevelRenderer levelRenderer;

	/** Index of the Tickrate Slider stored while Tick Advancing */
	private int save;

	@Shadow
	private int rightClickDelay;

	@Shadow
	private LocalPlayer player;

	@Shadow
	private Options options;

	/** Whether the Player was on Ground last tick */
	@Unique
	public boolean wasOnGround;

	/** Whether the World is loading */
	@Unique
	public boolean isLoadingWorld;

	/**
	 * Called when a load is being loaded.
	 */
	@Inject(method = "setLevel", at = @At("HEAD"))
	//#if MC>=11500
	public void injectloadWorld(net.minecraft.client.multiplayer.ClientLevel worldClientIn, CallbackInfo ci) {
	//#else
//$$ 	public void injectloadWorld(net.minecraft.client.multiplayer.MultiPlayerLevel worldClientIn, CallbackInfo ci) {
	//#endif
		isLoadingWorld = ConfigUtils.getBoolean("tools", "hitEscape") && worldClientIn != null;
	}

	/**
	 * Called after the Graphics have been initialized
	 */
	//#if MC>=11500
	@Inject(method = "run", at = @At("HEAD"))
	//#else
//$$ 	@Inject(method = "init", at = @At("TAIL"))
	//#endif
	public void loadRenderingLate(CallbackInfo ci) {
		/* Load Textures because FabricAPI has been removed */
		try {
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/apple.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("apple.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/carrot.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("carrot.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/deadbush.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("deadbush.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/diamond_ore.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("diamond_ore.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/drowned.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("drowned.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/fish.gif"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("fish.gif"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/glowstone.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("glowstone.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/gold.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("gold.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/gravel.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("gravel.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/iron.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("iron.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/leaf.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("leaf.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/plants.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("plants.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/potato.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("potato.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/sapling.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("sapling.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/sealantern.gif"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("sealantern.gif"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/sheep.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("sheep.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/spider.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("spider.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/stick.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("stick.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/wither_skeleton.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("wither_skeleton.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/zombie.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("zombie.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "dragon/breath.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("breath.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "dragon/flying.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("flying.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "dragon/shooting.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("shooting.png"))));
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "heck/potion.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("potion.png"))));
			
			Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", "drops/piglin.png"), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream("piglin.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* Load Shields and Register Keymappings */
		LoTASModContainer.loadShields();
	}

	/**
	 * Called before a tick passes
	 */
	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void injectrunTick(CallbackInfo ci) {
		/* Reset the Right Mouse Delay when the Auto Clicker is enabled */
		if (ConfigUtils.getBoolean("tools", "rAutoClicker"))
			rightClickDelay = 0;

		/* Timer and Tick Indicator */
		TickrateChangerMod.show = !TickrateChangerMod.show;
		if ((screen == null ? true : Timer.allowed.contains(screen.getClass().getSimpleName().toLowerCase())) && Timer.running)
			Timer.ticks++;

		/* Savestates when requested */
		if (KeybindsUtils.shouldSavestate) {
			KeybindsUtils.shouldSavestate = false;
			SavestateMod.savestate(null);
		}

		/* Auto Strafing */
		if (player != null) {
			//#if MC>=11600
 			boolean isOnGround = player.isOnGround();
			//#else
//$$			boolean isOnGround = player.onGround;
			//#endif
			if (isOnGround && !wasOnGround && KeybindsUtils.holdStrafeKeybind.isDown()) {
				//#if MC>=11700
//$$ 				player.setYRot(player.getYRot() + 45);
				//#else
				player.yRot += 45;
				//#endif
				KeyMapping.set(options.keyRight.getDefaultKey(), false);
			} else if (!isOnGround && wasOnGround && KeybindsUtils.holdStrafeKeybind.isDown()) {
				//#if MC>=11700
//$$ 				player.setYRot(player.getYRot() - 45);
				//#else
				player.yRot -= 45;
				//#endif
				KeyMapping.set(options.keyRight.getDefaultKey(), true);
			}
			wasOnGround = isOnGround;
		}

		/* Loadstate when requested */
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
		
		/* Tick Advance - Post */
		if (TickrateChangerMod.advanceClient) {
			TickrateChangerMod.resetAdvanceClient();
		}

		/* Tick Jumping */
		if (TickrateChangerMod.ticksToJump != -1 && Minecraft.getInstance().screen instanceof PauseScreen == false) {
			TickrateChangerMod.ticksToJump--;
			if (TickrateChangerMod.ticksToJump == 0) {
				TickrateChangerMod.ticksToJump = -1;
				if (screen == null && Minecraft.getInstance().player != null)
					Minecraft.getInstance().setScreen(new PauseScreen(true));
			}
		}
		if(player!=null) {
			LoTASModContainer.hud.tick();
		}
	}
 
	/**
	 * Moves the Player when in Freecam mode
	 * @param strafe Direction
	 * @param up to
	 * @param forward move
	 * @param friction to
	 */
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
			//#if MC>=11700
//$$ 			float f1 = Mth.sin(player.getYRot() * 0.017453292F);
//$$ 			float f2 = Mth.cos(player.getYRot() * 0.017453292F);
			//#else
			float f1 = Mth.sin(player.yRot * 0.017453292F);
			float f2 = Mth.cos(player.yRot * 0.017453292F);
			//#endif
			MCVer.setXYZ(player, MCVer.getX(player) + (strafe * f2 - forward * f1), MCVer.getY(player) + up, MCVer.getZ(player) + (forward * f2 + strafe * f1));
		}
	}

	/**
	 * Called before every single frame
	 */
	@Inject(method = "runTick", at = @At(value = "HEAD"))
	public void injectrunGameLoop(CallbackInfo ci) {
		/* Tickrate Zero Rendering */
		if (TickrateChangerMod.tickrate == 0) {
			TickrateChangerMod.timeOffset += System.currentTimeMillis() - TickrateChangerMod.timeSinceZero;
			TickrateChangerMod.timeSinceZero = System.currentTimeMillis();
		}

		/* Tickrate Zero Toggle */
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

		/* Tickrate Zero */
		if (TickrateChangerMod.tickrate == 0 && KeybindsUtils.advanceTicksKeybind.consumeClick() && !KeybindsUtils.isFreecaming) {
			TickrateChangerMod.advanceTick();
		}
		/* Tickrate Changer */
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

		/* Escape using Freecam disables Freecam */
		if (TickrateChangerMod.tickrate == 0 && screen == null && Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			((Minecraft) (Object) this).setScreen(new PauseScreen(true));
			TickrateChangerMod.updateTickrate(KeybindsUtils.savedTickrate);
			KeybindsUtils.isFreecaming = false;
			Minecraft.getInstance().player.noPhysics = false;
			levelRenderer.allChanged();
		}

		/* Freecam Movement */
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
			if (options.keyShift.isDown()) {
			//#else
//$$ 			if (options.keySneak.isDown()) {
			//#endif
				move(0.0F, -0.92F, 0.0F, 1.0F);
			}
		}

		/* Freecam Toggle */
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

	/**
	 * Handles all Keybinds
	 */
	@Inject(method = "handleKeybinds", at = @At("RETURN"), cancellable = true)
	public void keyInputEvent(CallbackInfo ci) {
		KeybindsUtils.keyEvent();
	}

	/**
	 * Redirects the screen to set, whenever a screen is being set
	 */
	@Inject(method = "setScreen", at = @At(value = "HEAD"), cancellable = true)
	public void injectdisplayGuiScreen(Screen guiScreenIn, CallbackInfo ci) {
		/* Reset Tick Binds */
		if (((guiScreenIn == null) ? true : guiScreenIn instanceof PauseScreen) && SavestateMod.isLoading) {
			SavestateMod.isLoading = false;
			SavestateMod.showLoadstateDone = true;
			SavestateMod.timeTitle = System.currentTimeMillis();
		}
		/* Auto-Pause */
		if (isLoadingWorld && guiScreenIn == null) {
			isLoadingWorld = false;
			Minecraft.getInstance().setScreen(new PauseScreen(true));
			ci.cancel();
		}
		/* Loadstate Velocity Reseting */
		if (guiScreenIn == null && (((Minecraft) (Object) this).player != null)) {
			if (SavestateMod.applyVelocity) {
				SavestateMod.applyVelocity = false;
				LocalPlayer player = ((Minecraft) (Object) this).player;
				player.setDeltaMovement(SavestateMod.motionX, SavestateMod.motionY, SavestateMod.motionZ);
			}
			AIManipMod.read();
		}

	}
	
	@Inject(method = "Lnet/minecraft/client/Minecraft;clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At(value = "HEAD"))
	public void injectClearLevel(CallbackInfo ci) {
		if(Minecraft.getInstance().getSingleplayerServer()!=null&&!SavestateMod.isLoading) {
			AIManipMod.save();
		}
	}
}
