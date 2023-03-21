package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.LoadstateScreen;
import de.pfannekuchen.lotas.mods.SavestateMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Mixin(DeathScreen.class)
public class MixinDeathScreen extends Screen{
	
	protected MixinDeathScreen(Component component) {
		super(component);
	}

	@Inject(method = "init", at = @At(value = "RETURN"))
	public void inject_init(CallbackInfo ci) {
		Button loadstateButton = constructLoadstateButton();
		loadstateButton.active = SavestateMod.hasSavestate();
		MCVer.addButton((DeathScreen)(Object)this, loadstateButton);
	}
	
	private Button constructLoadstateButton() {
		//#if MC>=11903
//$$ 		return net.minecraft.client.gui.components.Button.builder(net.minecraft.network.chat.Component.literal("Loadstate"), btn -> {
//$$ 			if (Screen.hasShiftDown())
//$$ 				Minecraft.getInstance().setScreen(new LoadstateScreen());
//$$ 			else
//$$ 				SavestateMod.loadstate(-1);
//$$ 		}).width(98).bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20).build();
		//#else
		return MCVer.Button(this.width / 2 - 100, this.height / 4 + 120, 200, 20, "Loadstate", btn -> {
		if (Screen.hasShiftDown())
			Minecraft.getInstance().setScreen(new LoadstateScreen());
		else
			SavestateMod.loadstate(-1);
		});
		//#endif
	}
}
