package de.pfannekuchen.lotas.gui;

import net.minecraft.client.gui.GuiScreen;

//#if MC>=10900
import java.io.IOException;
import java.util.HashMap;

import com.google.common.base.Predicates;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseHoldingPattern;
import net.minecraft.entity.boss.dragon.phase.PhaseLandingApproach;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseSittingBase;
import net.minecraft.entity.boss.dragon.phase.PhaseStrafePlayer;
import net.minecraft.util.ResourceLocation;
@SuppressWarnings("serial")
public class GuiDragonManipulation extends GuiScreen {

    GuiScreen here;

    static final ResourceLocation DRAGONGIF = new ResourceLocation("lotas", "dragon/flying.png");
    static final ResourceLocation DRAGONGIF2 = new ResourceLocation("lotas", "dragon/breath.png");
    static final ResourceLocation DRAGONGIF3 = new ResourceLocation("lotas", "dragon/shooting.png");

    static HashMap<String, PhaseList<?>> phases = new HashMap<String, PhaseList<?>>();

    static HashMap<String, String> translation = new HashMap<String, String>() {
    	{
    		put("PhaseHoldingPattern", "Ender Dragon is flying through the air");
    		put("PhaseStrafePlayer", "Ender Dragon is shooting at you");
    		put("PhaseLandingApproach", "Ender Dragon trying to land");
    		put("PhaseLanding", "Ender Dragon is landing");
    		put("PhaseTakeoff", "Ender Dragon is taking off from the Portal");
    		put("PhaseSittingFlaming", "Ender Dragon is flaming at the Portal");
    		put("PhaseSittingScanning", "Ender Dragon is turning at the Portal");
    		put("PhaseSittingAttacking", "Ender Dragon is attacking you at the Portal");
    		put("PhaseChargingPlayer", "Ender Dragon currently charging you");
    		put("PhaseDying", "Ender Dragon is dying");
    		put("PhaseHover", "Ender Dragon is hovering over the Portal");
    	}
    };

    IPhase dragonPhase;

    GuiButton action1;
    GuiButton action2;
    GuiButton action3;

	public GuiDragonManipulation(GuiScreen screen) {
		EntityDragon dragon = MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension).getEntities(EntityDragon.class, Predicates.alwaysTrue()).get(0);
		dragonPhase = dragon.getPhaseManager().getCurrentPhase();
		here = screen;

		phases = new HashMap<String, PhaseList<?>>();
		phases.put("Stop shooting at the Player", PhaseList.HOLDING_PATTERN);
		phases.put("Shoot at the Player", PhaseList.STRAFE_PLAYER);
		phases.put("Try to land", PhaseList.LANDING_APPROACH);
		phases.put("Takeoff", PhaseList.TAKEOFF);
		phases.put("Start Flaming", PhaseList.SITTING_FLAMING);
		phases.put("Turn", PhaseList.SITTING_SCANNING);
		phases.put("Cancel Landing", PhaseList.HOLDING_PATTERN);
		phases.put("Cancel Landing and shoot at Player", PhaseList.STRAFE_PLAYER);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void initGui() {
		action1 = new GuiButton(0, this.width / 3 * 0 + 5, height / 8, this.width / 3 - 10, 20, "Phase1");
		action2 = new GuiButton(1, this.width / 3 * 1 + 5, height / 8, this.width / 3 - 10, 20, "Phase2");
		action3 = new GuiButton(2, this.width / 3 * 2 + 5, height / 8, this.width / 3 - 10, 20, "Phase3");

		if (dragonPhase instanceof PhaseHoldingPattern) {
			action1.displayString = "Try to land";
			action2.displayString = "Shoot at the Player";
			action3.displayString = "";
			action1.enabled = true;
			action2.enabled = true;
			action3.enabled = false;
		} else if (dragonPhase instanceof PhaseLandingApproach) {
			action1.displayString = "Cancel Landing";
			action2.displayString = "Cancel Landing and shoot at Player";
			action3.displayString = "";
			action1.enabled = true;
			action2.enabled = true;
			action3.enabled = false;
		} else if (dragonPhase instanceof PhaseStrafePlayer) {
			action1.displayString = "Stop shooting at the Player";
			action2.displayString = "Try to land";
			action3.displayString = "";
			action1.enabled = true;
			action2.enabled = true;
			action3.enabled = false;
		} else if (dragonPhase instanceof PhaseSittingBase) {
			action1.displayString = "Takeoff";
			action2.displayString = "Turn";
			action3.displayString = "Start Flaming";
			action1.enabled = true;
			action2.enabled = true;
			action3.enabled = true;
		} else {
			action1.displayString = "";
			action2.displayString = "";
			action3.displayString = "";
			action1.enabled = false;
			action2.enabled = false;
			action3.enabled = false;
		}

		this.buttonList.add(action1);
		this.buttonList.add(action2);
		this.buttonList.add(action3);
        this.buttonList.add(new GuiButton(2000, this.width / 2 - 155, this.height - 29, 300, 20, I18n.format("gui.done")));
        super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        GlStateManager.enableTexture2D();

        drawCenteredString(MCVer.getFontRenderer(mc), translation.get(dragonPhase.getClass().getSimpleName()), width / 2, 10, 0xFFFFFF);

		mc.getTextureManager().bindTexture(DRAGONGIF);
		drawModalRectWithCustomSizedTexture(width / 28 * 3, height / 19 * 2, 0, 0, width / 28 * 23, height / 19 * 17, width / 28 * 23, height / 19 * 17);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 2000) mc.displayGuiScreen(here);
		else {
			action1.enabled = false;
			action2.enabled = false;
			action3.enabled = false;

			EntityDragon dragon = MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension).getEntities(EntityDragon.class, Predicates.alwaysTrue()).get(0);
			dragon.getPhaseManager().setPhase(phases.get(button.displayString));
			dragonPhase = dragon.getPhaseManager().getCurrentPhase();
		}
	}

}
//#else
//$$ public class GuiDragonManipulation extends GuiScreen {	
//$$ 	public GuiDragonManipulation(GuiScreen screen) {
//$$
//$$ 	}
//$$ }
//#endif