package de.pfannekuchen.lotas.gui;

import static net.minecraft.entity.boss.dragon.phase.PhaseList.CHARGING_PLAYER;
import static net.minecraft.entity.boss.dragon.phase.PhaseList.DYING;
import static net.minecraft.entity.boss.dragon.phase.PhaseList.HOVER;
import static net.minecraft.entity.boss.dragon.phase.PhaseList.LANDING;
import static net.minecraft.entity.boss.dragon.phase.PhaseList.LANDING_APPROACH;
import static net.minecraft.entity.boss.dragon.phase.PhaseList.SITTING_ATTACKING;
import static net.minecraft.entity.boss.dragon.phase.PhaseList.SITTING_FLAMING;
import static net.minecraft.entity.boss.dragon.phase.PhaseList.SITTING_SCANNING;
import static net.minecraft.entity.boss.dragon.phase.PhaseList.STRAFE_PLAYER;
import static net.minecraft.entity.boss.dragon.phase.PhaseList.TAKEOFF;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseBase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import rlog.RLogAPI;

/**
 * Draws a gui screen where the player can select the next dragon phase the dragon will execute. All buttons are disabled if there is no dragon available
 * @author Pancake
 */
public class GuiDragonPhase extends GuiScreen {
	
    GuiScreen here;
	
    private static List<PhaseList<? extends PhaseBase>> dragonPhase = Arrays.asList(CHARGING_PLAYER, STRAFE_PLAYER, LANDING_APPROACH, LANDING, TAKEOFF, SITTING_ATTACKING, SITTING_FLAMING, SITTING_SCANNING, DYING, HOVER);
    private static List<String> phaseNames = Arrays.asList("Charging Player", "Strafe Player", "Landing Approach", "Landing", "Takeoff", "Sitting Attacking", "Sitting Flaming", "Sitting Scanning", "Dying", "Hover");
    private static List<EntityDragon> dragons;
    
	public GuiDragonPhase(GuiScreen screen) {
		here = screen;
	}
	
	@Override
	public void initGui() {
		
        int i = 0;
        
        int widthOfBtn = (int) (this.width * .95f / 2);
        int heightOfBtn = 20;
        
        int spaceInBetween = (int) (this.width * .05f / 3);
        
        for (@SuppressWarnings("unused") PhaseList<? extends PhaseBase> s : dragonPhase) {
        	
        	int row = (int) Math.floor(i / 2);
        	int column = i % 2;
        	
        	this.buttonList.add(new GuiButton(i, column * (widthOfBtn + spaceInBetween) + spaceInBetween, row * 25 + 20, widthOfBtn, heightOfBtn, phaseNames.get(i)));
        	
            i++;
        }
		
        RLogAPI.logDebug("[DragonPhase] Searching for Ender Dragon");
        dragons = mc.getIntegratedServer().getWorld(mc.player.dimension).getEntities(EntityDragon.class, e -> !e.isDead);
        if (dragons.size() == 0) {
        	RLogAPI.logDebug("[DragonPhase] Didn't find a dragon");
        	for (GuiButton btn : buttonList) {
				btn.enabled = false;
			}
        } else {
        	RLogAPI.logDebug("[DragonPhase] Ender Dragon was found.");
        }
        this.buttonList.add(new GuiButton(2000, this.width / 2 - 155, this.height - 29, 300, 20, I18n.format("gui.done")));
        super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 2000) mc.displayGuiScreen(here);
		else {
			PhaseList<? extends PhaseBase> phase = dragonPhase.get(button.id);
	        dragons.forEach(e -> {
	        	e.getPhaseManager().setPhase(phase);
	        });
	        RLogAPI.logDebug("[DragonPhase] Updated Dragon Phase");
		} 
	}
	
}
