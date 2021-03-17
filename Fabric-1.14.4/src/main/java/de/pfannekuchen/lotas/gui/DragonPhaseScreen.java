package de.pfannekuchen.lotas.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.Phase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.text.LiteralText;
import rlog.RLogAPI;

public class DragonPhaseScreen extends Screen {

    Screen here;

    private static List<PhaseType<? extends Phase>> dragonPhase = Arrays.asList(PhaseType.CHARGING_PLAYER, PhaseType.STRAFE_PLAYER, PhaseType.LANDING_APPROACH, PhaseType.LANDING, PhaseType.TAKEOFF, PhaseType.SITTING_ATTACKING, PhaseType.SITTING_FLAMING, PhaseType.SITTING_SCANNING, PhaseType.DYING, PhaseType.HOVER);
    private static List<String> phaseNames = Arrays.asList("Charging Player", "Strafe Player", "Landing Approach", "Landing", "Takeoff", "Sitting Attacking", "Sitting Flaming", "Sitting Scanning", "Dying", "Hover");
    private List<EnderDragonEntity> dragons = new ArrayList<>();

    public DragonPhaseScreen(Screen screen) {
        super(new LiteralText("Dragon Phase Manipulator"));
        here = screen;
    }

    @Override
    public void init() {

        int i = 0;

        int widthOfBtn = (int) (this.width * .95f / 2);
        int heightOfBtn = 20;

        int spaceInBetween = (int) (this.width * .05f / 3);

        for (PhaseType<? extends Phase> s : dragonPhase) {

            int row = (int) Math.floor(i / 2);
            int column = i % 2;

            addButton(new ButtonWidget(column * (widthOfBtn + spaceInBetween) + spaceInBetween, row * 25 + 20, widthOfBtn, heightOfBtn, phaseNames.get(i), b -> {
                dragons.forEach(d -> {
                    d.getPhaseManager().setPhase(s);
                    RLogAPI.logDebug("[DragonPhase] Updated Dragon Phase");
                });
            }));

            i++;
        }

        minecraft.getServer().getWorld(minecraft.player.dimension).getEntities(EntityType.ENDER_DRAGON, Predicates.alwaysTrue()).forEach(c -> {
            if (c instanceof EnderDragonEntity) {
                dragons.add((EnderDragonEntity) c);
                RLogAPI.logDebug("[DragonPhase] Ender Dragon was found.");
            }
        });
        if (dragons.size() == 0) {
            for (AbstractButtonWidget btn : buttons) {
                btn.active = false;
            }
        }

        addButton(new ButtonWidget(this.width / 2 - 155, this.height - 29, 300, 20, "Done", b -> {
            minecraft.openScreen(here);
        }));
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground(0);
        super.render(mouseX, mouseY, partialTicks);
    }

}
