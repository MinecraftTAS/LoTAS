package de.pfannekuchen.lotas.mixin;

import java.io.IOException;
import java.util.OptionalLong;
import java.util.Properties;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.core.utils.Timer;
import de.pfannekuchen.lotas.mixin.accessors.AccessorServerPlayerEntity;
import de.pfannekuchen.lotas.mods.AIManipMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.SavestateMod.State;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;

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
//$$ 	public void injectloadWorld(net.minecraft.client.multiplayer.ClientLevel worldClientIn, CallbackInfo ci) {
	//#else
	public void injectloadWorld(net.minecraft.client.multiplayer.MultiPlayerLevel worldClientIn, CallbackInfo ci) {
	//#endif
		isLoadingWorld = ConfigUtils.getBoolean("tools", "hitEscape") && worldClientIn != null;
	}

	/**
	 * Called after the Graphics have been initialized
	 */
	//#if MC>=11500
//$$ 	@Inject(method = "run", at = @At("HEAD"))
	//#else
	@Inject(method = "init", at = @At("TAIL"))
	//#endif
	public void loadRenderingLate(CallbackInfo ci) {
		/* Load Textures because FabricAPI has been removed */
		try {
			registerTexture("drops/apple.png", "apple.png");
			registerTexture("drops/carrot.png", "carrot.png");
			registerTexture("drops/deadbush.png", "deadbush.png");
			registerTexture("drops/diamond_ore.png", "diamond_ore.png");
			registerTexture("drops/drowned.png", "drowned.png");
			registerTexture("drops/fish.gif", "fish.gif");
			registerTexture("drops/glowstone.png", "glowstone.png");
			registerTexture("drops/gold.png", "gold.png");
			registerTexture("drops/gravel.png", "gravel.png");
			registerTexture("drops/iron.png", "iron.png");
			registerTexture("drops/leaf.png", "leaf.png");
			registerTexture("drops/plants.png", "plants.png");
			registerTexture("drops/potato.png", "potato.png");
			registerTexture("drops/sapling.png", "sapling.png");
			registerTexture("drops/sealantern.gif", "sealantern.gif");
			registerTexture("drops/sheep.png", "sheep.png");
			registerTexture("drops/spider.png", "spider.png");
			registerTexture("drops/stick.png", "stick.png");
			registerTexture("drops/wither_skeleton.png", "wither_skeleton.png");
			registerTexture("drops/zombie.png", "zombie.png");
			registerTexture("dragon/breath.png", "breath.png");
			registerTexture("dragon/flying.png", "flying.png");
			registerTexture("dragon/shooting.png", "shooting.png");
			registerTexture("heck/potion.png", "potion.png");
			
			registerTexture("drops/piglin.png", "piglin.png");
			registerTexture("drops/copper.png", "copper.png");
			
			registerTexture("gui/small_checkbox.png", "small_checkbox.png");
			registerTexture("shield/bottleshield.png", "bottleshield.png");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* Load Shields */
		LoTASModContainer.loadShieldsMCTAS();
	}
	
	private void registerTexture(String path, String file) throws IOException{
		Minecraft.getInstance().getTextureManager().register(new ResourceLocation("lotas", path), new DynamicTexture(NativeImage.read(LoTASModContainer.class.getResourceAsStream(file))));
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

		//#if MC>=11605
		//#if MC>=11800
//$$ 		// 1.18+ seedfinders will never work 
		//#else
//$$ 		if (LoTASModContainer.i != -1) {
//$$ 			if (Minecraft.getInstance().level != null) {
//$$ 				Minecraft.getInstance().getSingleplayerServer().halt(true);
//$$ 			}
//$$
//$$ 			Minecraft.getInstance().forceSetScreen(new GenericDirtMessageScreen(new net.minecraft.network.chat.TranslatableComponent("createWorld.preparing")));
//$$ 			LevelSettings levelSettings2;
//$$ 			levelSettings2 = new LevelSettings(UUID.randomUUID().toString().substring(0, 10), GameType.CREATIVE, false, Difficulty.EASY, true, new GameRules(), net.minecraft.world.level.DataPackConfig.DEFAULT);
//$$ 			Minecraft.getInstance().createLevel(UUID.randomUUID().toString().substring(0, 10), levelSettings2, net.minecraft.core.RegistryAccess.RegistryHolder.builtin(), net.minecraft.world.level.levelgen.WorldGenSettings.create(net.minecraft.core.RegistryAccess.builtin(), new Properties()).withSeed(true, OptionalLong.of(LoTASModContainer.i)));
//$$
//$$ 			LoTASModContainer.i = -1;
//$$ 			System.gc();
//$$ 		}
		//#endif
		//#endif
		
		/* Auto Strafing */
		if (player != null) {
			//#if MC>=11600
			//#if MC>=12000
//$$ 			boolean isOnGround = player.onGround();
			//#else
//$$ 			boolean isOnGround = player.isOnGround();
			//#endif
			//#else
			boolean isOnGround = player.onGround;
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
	 * Called before every single frame
	 */
	@Inject(method = "runTick", at = @At(value = "HEAD"))
	public void injectrunGameLoop(CallbackInfo ci) {
		/* Tickrate Zero Rendering */
		if (TickrateChangerMod.tickrate == 0) {
			TickrateChangerMod.timeOffset += System.currentTimeMillis() - TickrateChangerMod.timeSinceZero;
			TickrateChangerMod.timeSinceZero = System.currentTimeMillis();
		}
		
		KeybindsUtils.frameKeyEvent();
	}

	/**
	 * Handles all Keybinds
	 */
	@Inject(method = "handleKeybinds", at = @At("RETURN"), cancellable = true)
	public void keyInputEvent(CallbackInfo ci) {
		KeybindsUtils.tickKeyEvent();
	}

	/**
	 * Redirects the screen to set, whenever a screen is being set
	 */
	@Inject(method = "setScreen", at = @At(value = "HEAD"), cancellable = true)
	public void injectdisplayGuiScreen(Screen guiScreenIn, CallbackInfo ci) {
		/* Reset Tick Binds */
		if (((guiScreenIn == null) ? true : guiScreenIn instanceof PauseScreen) && SavestateMod.state == State.LOADSTATING) {
			SavestateMod.state = State.NONE;
			SavestateMod.showLoadstateDone = true;
			SavestateMod.timeTitle = System.currentTimeMillis();
			Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().forEach(player -> {
				((AccessorServerPlayerEntity) player).setSpawnInvulnerableTime(0);
			});
		}
		if (((guiScreenIn == null) ? true : guiScreenIn instanceof PauseScreen) && SavestateMod.state == State.SAVESTATING) {
			SavestateMod.state = State.NONE;
			Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().forEach(player -> {
				((AccessorServerPlayerEntity) player).setSpawnInvulnerableTime(0);
			});
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
		if(Minecraft.getInstance().getSingleplayerServer()!=null&&SavestateMod.state != State.LOADSTATING) {
			AIManipMod.save();
		}
	}
}
