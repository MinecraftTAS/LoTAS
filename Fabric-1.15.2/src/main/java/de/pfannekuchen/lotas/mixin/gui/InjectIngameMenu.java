package de.pfannekuchen.lotas.mixin.gui;

import java.time.Duration;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.ConfigManager;
import de.pfannekuchen.lotas.dupemod.DupeMod;
import de.pfannekuchen.lotas.gui.AiRigScreen;
import de.pfannekuchen.lotas.gui.DragonPhaseScreen;
import de.pfannekuchen.lotas.gui.DropManipulatorScreen;
import de.pfannekuchen.lotas.gui.EntitySpawnerScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

@Mixin(GameMenuScreen.class)
public abstract class InjectIngameMenu extends Screen {

    protected InjectIngameMenu(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At("HEAD"))
    public void injectinitWidgets(CallbackInfo ci) {
        addButton(new ButtonWidget(5, 15, 48, 20, "+", b -> {
            TickrateChanger.index++;
            TickrateChanger.index = MathHelper.clamp(TickrateChanger.index, 1, 10);
            TickrateChanger.updateTickrate(TickrateChanger.ticks[TickrateChanger.index]);
        }));
        addButton(new ButtonWidget( 55, 15, 48, 20, "-", b -> {
            TickrateChanger.index--;
            TickrateChanger.index = MathHelper.clamp(TickrateChanger.index, 1, 10);
            TickrateChanger.updateTickrate(TickrateChanger.ticks[TickrateChanger.index]);
        }));
        addButton(new ButtonWidget( 5, 55, 98, 20, "Save Items", b -> {
            DupeMod.save(minecraft);
            b.active = false;
        }));
        addButton(new ButtonWidget( 5, 75, 98, 20, "Load Items", b -> {
            DupeMod.load(minecraft);
            b.active = false;
        }));
        addButton(new ButtonWidget((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, "Manipulate Drops", b -> {
            minecraft.openScreen(new DropManipulatorScreen((GameMenuScreen) (Object) this));
        }));
        addButton(new ButtonWidget((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", b -> {
            minecraft.openScreen(new DragonPhaseScreen((GameMenuScreen) (Object) this));
        }));
        addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120 + 8, 204, 20, "Reset Timer", b -> {
            Timer.ticks = 0;
            Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
        }));
        addButton(new SmallCheckboxWidget(2, height - 20 - 15, "Avoid taking damage", !ConfigManager.getBoolean("tools", "takeDamage"), b -> {
            ConfigManager.setBoolean("tools", "takeDamage", !b.isChecked());
            ConfigManager.save();
        }));

        addButton(new SmallCheckboxWidget(2, height - 32 - 15, "Drop towards me", ConfigManager.getBoolean("tools", "manipulateVelocityTowards"), b -> {
            ConfigManager.setBoolean("tools", "manipulateVelocityTowards", b.isChecked());
            if (b.isChecked()) {
                ConfigManager.setBoolean("tools", "manipulateVelocityAway", false);
                ((SmallCheckboxWidget) buttons.get(8)).silentPress(false);
            }
            ConfigManager.save();
        }));
        addButton(new SmallCheckboxWidget(2, height - 44 - 15, "Drop away from me", ConfigManager.getBoolean("tools", "manipulateVelocityAway"), b -> {
            ConfigManager.setBoolean("tools", "manipulateVelocityAway", b.isChecked());
            if (b.isChecked()) {
                ConfigManager.setBoolean("tools", "manipulateVelocityTowards", false);
                ((SmallCheckboxWidget) buttons.get(7)).silentPress(false);
            }
            ConfigManager.save();
        }));
        // DO NOT PUT ANYTHING BEFORE THIS, OR I WILL EAT ALL OF YOUR COOKIES
        
        addButton(new ButtonWidget((width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, "Manipulate Spawning", b -> {
            minecraft.openScreen(new EntitySpawnerScreen());
        }));
        addButton(new ButtonWidget((width / 4) * 3 + 4, height - 20, width / 4 - 2, 20, "Manipulate AI", b -> {
            minecraft.openScreen(new AiRigScreen());
        }));
        addButton(new SmallCheckboxWidget(2, height - 56 - 15, "Optimize Explosions", ConfigManager.getBoolean("tools", "optimizeexplosions"), b -> {
        	ConfigManager.setBoolean("tools", "optimizeexplosions", b.isChecked());
        	ConfigManager.save();
        }));
        addButton(new SmallCheckboxWidget(2, height - 68 - 15, "Toggle R Auto Clicker", ConfigManager.getBoolean("tools", "rAutoClicker"), c -> {
        	ConfigManager.setBoolean("tools", "rAutoClicker", c.isChecked());
        	ConfigManager.save();
        }));
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void injectrender(CallbackInfo ci) {
        drawString(minecraft.textRenderer, "Tickrate Changer (" + TickrateChanger.tickrate + ")", 5, 5, 0xFFFFFFFF);
        drawString(minecraft.textRenderer, "Duping", 10, 45, 0xFFFFFFFF);
    }

}
