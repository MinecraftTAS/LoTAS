package de.pfannekuchen.lotas.gui;

import java.util.HashMap;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractSittingPhase;
import net.minecraft.entity.boss.dragon.phase.HoldingPatternPhase;
import net.minecraft.entity.boss.dragon.phase.LandingApproachPhase;
import net.minecraft.entity.boss.dragon.phase.Phase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.boss.dragon.phase.StrafePlayerPhase;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

/**
 * Draws a gui screen where the player can select the next dragon phase the dragon will execute. All buttons are disabled if there is no dragon available
 * @author Pancake
 */
@SuppressWarnings("serial")
public class DragonManipulationScreen extends Screen {

	Screen here;

	static final Identifier DRAGONGIF = new Identifier("lotas", "dragon/flying.png");
	static final Identifier DRAGONGIF2 = new Identifier("lotas", "dragon/breath.png");
	static final Identifier DRAGONGIF3 = new Identifier("lotas", "dragon/shooting.png");

	static HashMap<String, PhaseType<?>> phases = new HashMap<String, PhaseType<?>>();

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

	Phase dragonPhase;

	ButtonWidget action1;
	ButtonWidget action2;
	ButtonWidget action3;

	public DragonManipulationScreen(Screen screen) {
		super(new LiteralText("Dragon Manipulator Screen"));
		EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().get(0);
		dragonPhase = dragon.getPhaseManager().getCurrent();
		here = screen;

		phases = new HashMap<String, PhaseType<?>>();
		phases.put("Stop shooting at the Player", PhaseType.HOLDING_PATTERN);
		phases.put("Shoot at the Player", PhaseType.STRAFE_PLAYER);
		phases.put("Try to land", PhaseType.LANDING_APPROACH);
		phases.put("Takeoff", PhaseType.TAKEOFF);
		phases.put("Start Flaming", PhaseType.SITTING_FLAMING);
		phases.put("Turn", PhaseType.SITTING_SCANNING);
		phases.put("Cancel Landing", PhaseType.HOLDING_PATTERN);
		phases.put("Cancel Landing and shoot at Player", PhaseType.STRAFE_PLAYER);
	}

	@Override
	public void init() {
		action1 = new NewButtonWidget(this.width / 3 * 0 + 5, height / 8, this.width / 3 - 10, 20, "Phase1", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().get(0);
			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			dragonPhase = dragon.getPhaseManager().getCurrent();
		});
		action2 = new NewButtonWidget(this.width / 3 * 1 + 5, height / 8, this.width / 3 - 10, 20, "Phase2", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().get(0);
			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			dragonPhase = dragon.getPhaseManager().getCurrent();
		});
		action3 = new NewButtonWidget(this.width / 3 * 2 + 5, height / 8, this.width / 3 - 10, 20, "Phase3", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().get(0);
			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			dragonPhase = dragon.getPhaseManager().getCurrent();
		});

		if (dragonPhase instanceof HoldingPatternPhase) {
			action1.setMessage("Try to land");
			action2.setMessage("Shoot at the Player");
			action3.setMessage("");
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof LandingApproachPhase) {
			action1.setMessage("Cancel Landing");
			action2.setMessage("Cancel Landing and shoot at Player");
			action3.setMessage("");
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof StrafePlayerPhase) {
			action1.setMessage("Stop shooting at the Player");
			action2.setMessage("Try to land");
			action3.setMessage("");
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof AbstractSittingPhase) {
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
		addButton(new NewButtonWidget(this.width / 2 - 155, this.height - 29, 300, 20, I18n.translate("gui.done"), btn -> {
			minecraft.openScreen(here);
		}));
		super.init();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		GlStateManager.enableTexture();

		drawCenteredString(MinecraftClient.getInstance().textRenderer, translation.get(dragonPhase.getClass().getSimpleName()), width / 2, 10, 0xFFFFFF);

		minecraft.getTextureManager().bindTexture(DRAGONGIF);
		DrawableHelper.blit(width / 28 * 3, height / 19 * 2, 0, 0, width / 28 * 23, height / 19 * 17, width / 28 * 23, height / 19 * 17);
		super.render(mouseX, mouseY, partialTicks);
	}

}
