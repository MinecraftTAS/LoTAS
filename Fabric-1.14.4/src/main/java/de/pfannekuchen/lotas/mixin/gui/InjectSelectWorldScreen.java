package de.pfannekuchen.lotas.mixin.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.gui.SeedListScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin(SelectWorldScreen.class)
public abstract class InjectSelectWorldScreen extends Screen {

    protected InjectSelectWorldScreen(Text title) {
        super(title);
    }


    /**
     * Adds a seed button to the top left corner of the selectworldscreen
     */
    @Inject(at = @At("HEAD"), method = "init")
    public void injectinit(CallbackInfo ci) {
        this.addButton(new ButtonWidget(2, 2, 98, 20, "Seeds", button -> {
            this.minecraft.openScreen(new SeedListScreen());
        }));
    }

}
