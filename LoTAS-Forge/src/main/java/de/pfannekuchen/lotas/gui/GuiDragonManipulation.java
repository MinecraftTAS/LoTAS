package de.pfannekuchen.lotas.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.DimensionManager;

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
    		put("PhaseHoldingPattern", I18n.format("dragonmanipgui.lotas.phase.holding"));
    		put("PhaseStrafePlayer", I18n.format("dragonmanipgui.lotas.phase.strafeplayer"));
    		put("PhaseLandingApproach", I18n.format("dragonmanipgui.lotas.phase.trylanding"));
    		put("PhaseLanding", I18n.format("dragonmanipgui.lotas.phase.landing"));
    		put("PhaseTakeoff", I18n.format("dragonmanipgui.lotas.phase.takeoff"));
    		put("PhaseSittingFlaming", I18n.format("dragonmanipgui.lotas.phase.flaming"));
    		put("PhaseSittingScanning", I18n.format("dragonmanipgui.lotas.phase.scanning"));
    		put("PhaseSittingAttacking", I18n.format("dragonmanipgui.lotas.phase.attacking"));
    		put("PhaseChargingPlayer", I18n.format("dragonmanipgui.lotas.phase.charge"));
    		put("PhaseDying", I18n.format("dragonmanipgui.lotas.phase.dying"));
    		put("PhaseHover", I18n.format("dragonmanipgui.lotas.phase.hovering"));
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
		phases.put(I18n.format("dragonmanipgui.lotas.phase.holding"), PhaseList.HOLDING_PATTERN);
		phases.put(I18n.format("dragonmanipgui.lotas.phase.strafeplayer"), PhaseList.STRAFE_PLAYER);
		phases.put(I18n.format("dragonmanipgui.lotas.phase.landing"), PhaseList.LANDING_APPROACH);
		phases.put(I18n.format("dragonmanipgui.lotas.phase.takeoff"), PhaseList.TAKEOFF);
		phases.put(I18n.format("dragonmanipgui.lotas.phase.flaming"), PhaseList.SITTING_FLAMING);
		phases.put(I18n.format("dragonmanipgui.lotas.phase.scanning"), PhaseList.SITTING_SCANNING);
		phases.put(I18n.format("dragonmanipgui.lotas.phase.cancel_landing"), PhaseList.HOLDING_PATTERN);
		phases.put(I18n.format("dragonmanipgui.lotas.phase.cancel_and_shoot"), PhaseList.STRAFE_PLAYER);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void initGui() {
		action1 = new GuiButton(0, this.width / 3 * 0 + 5, height / 8, this.width / 3 - 10, 20, I18n.format("dragonmanipgui.lotas.actionname.1"));
		action2 = new GuiButton(1, this.width / 3 * 1 + 5, height / 8, this.width / 3 - 10, 20, I18n.format("dragonmanipgui.lotas.actionname.2"));
		action3 = new GuiButton(2, this.width / 3 * 2 + 5, height / 8, this.width / 3 - 10, 20, I18n.format("dragonmanipgui.lotas.actionname.3"));

		if (dragonPhase instanceof PhaseHoldingPattern) {
			action1.displayString = I18n.format("dragonmanipgui.lotas.phase.landing");
			action2.displayString = I18n.format("dragonmanipgui.lotas.phase.strafeplayer");
			action3.displayString = "";
			action1.enabled = true;
			action2.enabled = true;
			action3.enabled = false;
		} else if (dragonPhase instanceof PhaseLandingApproach) {
			action1.displayString = I18n.format("dragonmanipgui.lotas.phase.cancel_landing");
			action2.displayString = I18n.format("dragonmanipgui.lotas.phase.cancel_and_shoot");
			action3.displayString = "";
			action1.enabled = true;
			action2.enabled = true;
			action3.enabled = false;
		} else if (dragonPhase instanceof PhaseStrafePlayer) {
			action1.displayString = I18n.format("dragonmanipgui.lotas.phase.holding");
			action2.displayString = I18n.format("dragonmanipgui.lotas.phase.landing");
			action3.displayString = "";
			action1.enabled = true;
			action2.enabled = true;
			action3.enabled = false;
		} else if (dragonPhase instanceof PhaseSittingBase) {
			action1.displayString = I18n.format("dragonmanipgui.lotas.phase.takeoff");
			action2.displayString = I18n.format("dragonmanipgui.lotas.phase.scanning");
			action3.displayString = I18n.format("dragonmanipgui.lotas.phase.flaming");
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
//$$
//$$ 	public static void chargePlayer() {
//$$ 		List<net.minecraft.entity.boss.EntityDragon> dragons = DimensionManager.getWorld(1).getEntities(net.minecraft.entity.boss.EntityDragon.class, new com.google.common.base.Predicate<net.minecraft.entity.boss.EntityDragon>() {
//$$ 			@Override
//$$ 			public boolean apply(net.minecraft.entity.boss.EntityDragon e) {
//$$ 				return !e.isDead;
//$$ 			}
//$$ 		});
//$$
//$$ 		for (int i = 0; i < dragons.size(); i++) {
//$$ 			dragons.get(i).target=Minecraft.getMinecraft().thePlayer;
//$$ 		}
//$$ 	}
//$$ }
//#endif