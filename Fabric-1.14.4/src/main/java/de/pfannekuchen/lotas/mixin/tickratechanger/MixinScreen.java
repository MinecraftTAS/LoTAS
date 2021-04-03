package de.pfannekuchen.lotas.mixin.tickratechanger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.ConfigManager;
import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

@Mixin(Screen.class)
public class MixinScreen {

    @Inject(method = "<init>(Lnet/minecraft/text/Text;)V", at = @At("RETURN"))
    void init(CallbackInfo info) {
        if (!TickrateChanger.exceptions.contains(((Object) this).getClass()) && ((Object) this).getClass() != null) {
            if (((Object) this).getClass() != InventoryScreen.class || !MinecraftClient.getInstance().interactionManager.hasCreativeInventory()) {
                if (TickrateChanger.tickrate != 0 && !TickrateChanger.isScreenBlocking && ConfigManager.getBoolean("tools", "tickrateZeroGui")) {
                    TickrateChanger.tickrateSaved = TickrateChanger.tickrate;
                    TickrateChanger.updateTickrate(0);
                    TickrateChanger.whatScreenIsCausingBlock = (Screen) (Object) this;
                    TickrateChanger.isScreenBlocking = true;
                }
            }
        }
    }
    
}
