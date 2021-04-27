package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.utils.Hotkeys;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin(DeathScreen.class)
public abstract class MixinDeathScreen extends Screen {
	
	protected MixinDeathScreen(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("RETURN"))
	public void injectinitGui(CallbackInfo ci) {
        addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120, 200, 20, "Loadstate", btn -> {
        	minecraft.openScreen(new GameMenuScreen(true));
        	Hotkeys.shouldLoadstate = true;
        }));
	}
	
}
