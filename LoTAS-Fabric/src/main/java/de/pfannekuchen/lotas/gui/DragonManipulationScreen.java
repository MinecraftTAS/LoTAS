package de.pfannekuchen.lotas.gui;

import java.util.HashMap;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
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
		EnderDragon dragon = MCVer.getCurrentLevel().getDragons().get(0);
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

	public String getButtonMessage(Button btn) {
		//#if MC>=11600
//$$ 		return btn.getMessage().getString();
		//#else
		return btn.getMessage();
		//#endif
	}
	
	@Override
	public void init() {
		action1 = MCVer.Button(this.width / 3 * 0 + 5, height / 8, this.width / 3 - 10, 20, "Phase1", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			EnderDragon dragon = MCVer.getCurrentLevel().getDragons().get(0);
			dragon.getPhaseManager().setPhase(phases.get(getButtonMessage(btn)));
			dragonPhase = dragon.getPhaseManager().getCurrentPhase();
		});
		action2 = MCVer.Button(this.width / 3 * 1 + 5, height / 8, this.width / 3 - 10, 20, "Phase2", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			EnderDragon dragon = MCVer.getCurrentLevel().getDragons().get(0);
			dragon.getPhaseManager().setPhase(phases.get(getButtonMessage(btn)));
			dragonPhase = dragon.getPhaseManager().getCurrentPhase();
		});
		action3 = MCVer.Button(this.width / 3 * 2 + 5, height / 8, this.width / 3 - 10, 20, "Phase3", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			EnderDragon dragon = MCVer.getCurrentLevel().getDragons().get(0);
			dragon.getPhaseManager().setPhase(phases.get(getButtonMessage(btn)));
			dragonPhase = dragon.getPhaseManager().getCurrentPhase();
		});

		if (dragonPhase instanceof DragonHoldingPatternPhase) {
			MCVer.setMessage(action1, "Try to land");
			MCVer.setMessage(action2, "Shoot at the Player");
			MCVer.setMessage(action3, "");
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof DragonLandingApproachPhase) {
			MCVer.setMessage(action1, "Cancel Landing");
			MCVer.setMessage(action2, "Cancel Landing and shoot at Player");
			MCVer.setMessage(action3, "");
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof DragonStrafePlayerPhase) {
			MCVer.setMessage(action1, "Stop shooting at the Player");
			MCVer.setMessage(action2, "Try to land");
			MCVer.setMessage(action3, "");
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof AbstractDragonSittingPhase) {
			MCVer.setMessage(action1, "Takeoff");
			MCVer.setMessage(action2, "Turn");
			MCVer.setMessage(action3, "Start Flaming");
			action1.active = true;
			action2.active = true;
			action3.active = true;
		} else {
			MCVer.setMessage(action1, "");
			MCVer.setMessage(action2, "");
			MCVer.setMessage(action3, "");
			action1.active = false;
			action2.active = false;
			action3.active = false;
		}
		addButton(action1);
		addButton(action2);
		addButton(action3);
		addButton(MCVer.Button(this.width / 2 - 155, this.height - 29, 300, 20, I18n.get("gui.done"), btn -> {
			Minecraft.getInstance().setScreen(here);
		}));
		super.init();
	}

	//#if MC>=11600
//$$ 	@Override public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float partialTicks) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void render(int mouseX, int mouseY, float partialTicks) {
	//#endif
		
		MCVer.renderBackground(this);
		MCVer.enableTexture();

		MCVer.drawCenteredString(this, translation.get(dragonPhase.getClass().getSimpleName()), width / 2, 10, 0xFFFFFF);

		minecraft.getTextureManager().bind(DRAGONGIF);
		MCVer.blit(width / 28 * 3, height / 19 * 2, 0, 0, width / 28 * 23, height / 19 * 17, width / 28 * 23, height / 19 * 17);
		for(int k = 0; k < this.buttons.size(); ++k) {
			MCVer.render(((AbstractWidget)this.buttons.get(k)), mouseX, mouseY, partialTicks);
		}
	}

}
