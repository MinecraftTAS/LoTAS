package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.utils.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

	private boolean isLoadingWorld;
	
	@Inject(method = "joinWorld", at = @At("HEAD"))
	public void injectloadWorld(ClientWorld worldClientIn, CallbackInfo ci) {
		isLoadingWorld = ConfigManager.getBoolean("tools", "hitEscape") && worldClientIn != null;
	}
	
    @ModifyVariable(method = "openScreen", at = @At("STORE"), index = 1, ordinal = 0)
    public Screen changeGuiScreen(Screen screenIn) {
		if (isLoadingWorld && screenIn == null) {
			isLoadingWorld = false;
			return new GameMenuScreen(false);
		}
		return screenIn;
    }
	
}
