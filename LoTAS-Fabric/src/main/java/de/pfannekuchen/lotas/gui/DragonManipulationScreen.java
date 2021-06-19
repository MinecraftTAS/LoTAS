package de.pfannekuchen.lotas.gui;

import java.util.HashMap;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractSittingPhase;
import net.minecraft.entity.boss.dragon.phase.HoldingPatternPhase;
import net.minecraft.entity.boss.dragon.phase.LandingApproachPhase;
import net.minecraft.entity.boss.dragon.phase.Phase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.boss.dragon.phase.StrafePlayerPhase;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
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
		//#if MC>=11601
//$$ 		EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(World.END).getAliveEnderDragons().get(0);
		//#else
		EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().get(0);
		//#endif
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

			//#if MC>=11601
//$$ 			EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(World.END).getAliveEnderDragons().get(0);
			//#else
			EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().get(0);
			//#endif
			//#if MC>=11601
//$$ 			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			//#else
			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			//#endif
			dragonPhase = dragon.getPhaseManager().getCurrent();
		});
		action2 = new NewButtonWidget(this.width / 3 * 1 + 5, height / 8, this.width / 3 - 10, 20, "Phase2", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			//#if MC>=11601
//$$ 			EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(World.END).getAliveEnderDragons().get(0);
			//#else
			EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().get(0);
			//#endif
			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			dragonPhase = dragon.getPhaseManager().getCurrent();
		});
		action3 = new NewButtonWidget(this.width / 3 * 2 + 5, height / 8, this.width / 3 - 10, 20, "Phase3", btn -> {
			action1.active = false;
			action2.active = false;
			action3.active = false;

			//#if MC>=11601
//$$ 			EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(World.END).getAliveEnderDragons().get(0);
			//#else
			EnderDragonEntity dragon = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().get(0);
			//#endif
			dragon.getPhaseManager().setPhase(phases.get(btn.getMessage()));
			dragonPhase = dragon.getPhaseManager().getCurrent();
		});

		if (dragonPhase instanceof HoldingPatternPhase) {
			//#if MC>=11601
//$$ 			action1.setMessage(new LiteralText("Try to land"));
//$$ 			action2.setMessage(new LiteralText("Shoot at the Player"));
//$$ 			action3.setMessage(new LiteralText(""));
			//#else
			action1.setMessage("Try to land");
			action2.setMessage("Shoot at the Player");
			action3.setMessage("");
			//#endif
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof LandingApproachPhase) {
			//#if MC>=11601
//$$ 			action1.setMessage(new LiteralText("Cancel Landing"));
//$$ 			action2.setMessage(new LiteralText("Cancel Landing and shoot at Player"));
//$$ 			action3.setMessage(new LiteralText(""));
			//#else
			action1.setMessage("Cancel Landing");
			action2.setMessage("Cancel Landing and shoot at Player");
			action3.setMessage("");
			//#endif
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof StrafePlayerPhase) {
			//#if MC>=11601
//$$ 			action1.setMessage(new LiteralText("Stop shooting at the Player"));
//$$ 			action2.setMessage(new LiteralText("Try to land"));
//$$ 			action3.setMessage(new LiteralText(""));
			//#else
			action1.setMessage("Stop shooting at the Player");
			action2.setMessage("Try to land");
			action3.setMessage("");
			//#endif
			action1.active = true;
			action2.active = true;
			action3.active = false;
		} else if (dragonPhase instanceof AbstractSittingPhase) {
			//#if MC>=11601
//$$ 			action1.setMessage(new LiteralText("Takeoff"));
//$$ 			action2.setMessage(new LiteralText("Turn"));
//$$ 			action3.setMessage(new LiteralText("Start Flaming"));
			//#else
			action1.setMessage("Takeoff");
			action2.setMessage("Turn");
			action3.setMessage("Start Flaming");
			//#endif
			action1.active = true;
			action2.active = true;
			action3.active = true;
		} else {
			//#if MC>=11601
//$$ 			action1.setMessage(new LiteralText(""));
//$$ 			action2.setMessage(new LiteralText(""));
//$$ 			action3.setMessage(new LiteralText(""));
			//#else
			action1.setMessage("");
			action2.setMessage("");
			action3.setMessage("");
			//#endif
			action1.active = false;
			action2.active = false;
			action3.active = false;
		}
		//#if MC>=11700
//$$ 		addDrawable(action1);
//$$ 		addDrawable(action2);
//$$ 		addDrawable(action3);
//$$ 		addDrawable(new NewButtonWidget(this.width / 2 - 155, this.height - 29, 300, 20, I18n.translate("gui.done"), btn -> {
//$$ 			MinecraftClient.getInstance().openScreen(here);
//$$ 		}));
		//#else
		addButton(action1);
		addButton(action2);
		addButton(action3);
		addButton(new NewButtonWidget(this.width / 2 - 155, this.height - 29, 300, 20, I18n.translate("gui.done"), btn -> {
			MinecraftClient.getInstance().openScreen(here);
		}));
		//#endif
		super.init();
	}

	//#if MC>=11601
//$$ 	@Override
//$$ 	public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
//$$ 		renderBackground(matrices);
		//#if MC>=11700
//$$ 		GlStateManager._enableTexture();
//$$
//$$ 		drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, translation.get(dragonPhase.getClass().getSimpleName()), width / 2, 10, 0xFFFFFF);
		//#else
//$$ 		GlStateManager.enableTexture();
//$$
//$$ 		drawCenteredString(matrices, MinecraftClient.getInstance().textRenderer, translation.get(dragonPhase.getClass().getSimpleName()), width / 2, 10, 0xFFFFFF);
		//#endif
//$$ 		MinecraftClient.getInstance().getTextureManager().bindTexture(DRAGONGIF);
//$$ 		DrawableHelper.drawTexture(matrices, width / 28 * 3, height / 19 * 2, 0, 0, width / 28 * 23, height / 19 * 17, width / 28 * 23, height / 19 * 17);
//$$ 		super.render(matrices, mouseX, mouseY, partialTicks);
//$$ 	}
	//#else
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		GlStateManager.enableTexture();

		drawCenteredString(MinecraftClient.getInstance().textRenderer, translation.get(dragonPhase.getClass().getSimpleName()), width / 2, 10, 0xFFFFFF);

		minecraft.getTextureManager().bindTexture(DRAGONGIF);
		DrawableHelper.blit(width / 28 * 3, height / 19 * 2, 0, 0, width / 28 * 23, height / 19 * 17, width / 28 * 23, height / 19 * 17);
		super.render(mouseX, mouseY, partialTicks);
	}
	//#endif

}
