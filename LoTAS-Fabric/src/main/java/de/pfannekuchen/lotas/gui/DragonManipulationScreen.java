package de.pfannekuchen.lotas.gui;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonSittingPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonHoldingPatternPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonLandingApproachPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonStrafePlayerPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.level.dimension.DimensionType;
import com.mojang.blaze3d.platform.GlStateManager;

/**
 * Draws a gui screen where the player can select the next dragon phase the dragon will execute. All buttons are disabled if there is no dragon available
 * @author Pancake
 */
@SuppressWarnings("serial")
public class DragonManipulationScreen extends Screen {

	Screen here;

	static final ResourceLocation DRAGONGIF = new ResourceLocation("lotas", "dragon/flying.png");
	static final ResourceLocation DRAGONGIF2 = new ResourceLocation("lotas", "dragon/breath.png");
	static final ResourceLocation DRAGONGIF3 = new ResourceLocation("lotas", "dragon/shooting.png");

	static HashMap<String, EnderDragonPhase<?>> phases = new HashMap<String, EnderDragonPhase<?>>();

	static HashMap<String, String> translation = new HashMap<String, String>() {
		{
			put("HoldingPatternPhase", "Ender Dragon is flying through the air");
			put("StrafePlayerPhase", "Ender Dragon is shooting at you");
			put("LandingApproachPhase", "Ender Dragon trying to land");
			put("LandingPhase", "Ender Dragon is landing");
			put("TakeoffPhase", "Ender Dragon is taking off from the Portal");
			put("SittingFlamingPhase", "Ender Dragon is flaming at the Portal");
			put("SittingScanningPhase", "Ender Dragon is turning at the Portal");
			put("SittingAttackingPhase", "Ender Dragon is attacking you at the Portal");
			put("ChargingPlayerPhase", "Ender Dragon currently charging you");
			put("DyingPhase", "Ender Dragon is dying");
			put("HoverPhase", "Ender Dragon is hovering over the Portal");
		}
	};

	DragonPhaseInstance dragonPhase;

	Button action1;
	Button action2;
	Button action3;

	public DragonManipulationScreen(Screen screen) {
		super(new TextComponent("Dragon Manipulator Screen"));
		EnderDragon dragon = Minecraft.getInstance().getSingleplayerServer().getLevel(DimensionType.THE_END).getDragons().get(0);
		dragonPhase = dragon.getPhaseManager().getCurrentPhase();
		here = screen;

		phases = new HashMap<String, EnderDragonPhase<?>>();
		phases.put("Stop shooting at the Player", EnderDragonPhase.HOLDING_PATTERN);
		phases.put("Shoot at the Player", EnderDragonPhase.STRAFE_PLAYER);
		phases.put("Try to land", EnderDragonPhase.LANDING_APPROACH);
		phases.put("Takeoff", EnderDragonPhase.TAKEOFF);
		phases.put("Start Flaming", EnderDragonPhase.SITTING_FLAMING);
		phases.put("Turn", EnderDragonPhase.SITTING_SCANNING);
		phases.put("Cancel Landing", EnderDragonPhase.HOLDING_PATTERN);
		phases.put("Cancel Landing and shoot at Player", EnderDragonPhase.STRAFE_PLAYER);
	}

	@Override
	public void init() {
		action1 = new Button(this.width / 3 * 0 + 5, height / 8, this.width / 3 - 10, 20, "Phase1", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			EnderDragon dragon = Minecraft.getInstance().getSingleplayerServer().getLevel(DimensionType.THE_END).getDragons().get(0);
			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			dragonPhase = dragon.getPhaseManager().getCurrentPhase();
		});
		action2 = new Button(this.width / 3 * 1 + 5, height / 8, this.width / 3 - 10, 20, "Phase2", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			EnderDragon dragon = Minecraft.getInstance().getSingleplayerServer().getLevel(DimensionType.THE_END).getDragons().get(0);
			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			dragonPhase = dragon.getPhaseManager().getCurrentPhase();
		});
		action3 = new Button(this.width / 3 * 2 + 5, height / 8, this.width / 3 - 10, 20, "Phase3", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			EnderDragon dragon = Minecraft.getInstance().getSingleplayerServer().getLevel(DimensionType.THE_END).getDragons().get(0);
			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			dragonPhase = dragon.getPhaseManager().getCurrentPhase();
		});

		if (dragonPhase instanceof DragonHoldingPatternPhase) {
			action1.setMessage("Try to land");
			action2.setMessage("Shoot at the Player");
			action3.setMessage("");
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof DragonLandingApproachPhase) {
			action1.setMessage("Cancel Landing");
			action2.setMessage("Cancel Landing and shoot at Player");
			action3.setMessage("");
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof DragonStrafePlayerPhase) {
			action1.setMessage("Stop shooting at the Player");
			action2.setMessage("Try to land");
			action3.setMessage("");
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof AbstractDragonSittingPhase) {
			action1.setMessage("Takeoff");
			action2.setMessage("Turn");
			action3.setMessage("Start Flaming");
			action1.active = true;
			action2.active = true;
			action3.active = true;
		} else {
			action1.setMessage("");
			action2.setMessage("");
			action3.setMessage("");
			action1.active = false;
			action2.active = false;
			action3.active = false;
		}
		addButton(action1);
		addButton(action2);
		addButton(action3);
		addButton(new Button(this.width / 2 - 155, this.height - 29, 300, 20, I18n.get("gui.done"), btn -> {
			Minecraft.getInstance().setScreen(here);
		}));
		super.init();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		GlStateManager.enableTexture();

		drawCenteredString(Minecraft.getInstance().font, translation.get(dragonPhase.getClass().getSimpleName()), width / 2, 10, 0xFFFFFF);

		minecraft.getTextureManager().bind(DRAGONGIF);
		GuiComponent.blit(width / 28 * 3, height / 19 * 2, 0, 0, width / 28 * 23, height / 19 * 17, width / 28 * 23, height / 19 * 17);
		super.render(mouseX, mouseY, partialTicks);
	}

}
