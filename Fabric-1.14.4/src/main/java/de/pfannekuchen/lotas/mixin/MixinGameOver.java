package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin(DeathScreen.class)
public abstract class MixinGameOver extends Screen {

	protected MixinGameOver(Text title) {
		super(title);
	}

	@Shadow
	private int ticksSinceDeath;
	
	@Inject(method = "init", at = @At("RETURN"))
	public void injectinit(CallbackInfo ci) {
        addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 120, 200, 20, "Loadstate", b -> {
        	minecraft.openScreen(new GameMenuScreen(true));
        	// TODO: Loadstating
        }));
        this.buttons.get(this.buttons.size() - 1).active = false;
	}

	@Inject(method = "tick", at = @At("RETURN"))
	public void injecttick(CallbackInfo ci) {
		if (ticksSinceDeath == 20) {
			this.buttons.get(this.buttons.size() - 1).active = true;
		}
	}
}
