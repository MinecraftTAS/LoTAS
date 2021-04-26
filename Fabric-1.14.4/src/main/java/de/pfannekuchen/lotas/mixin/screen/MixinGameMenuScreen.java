package de.pfannekuchen.lotas.mixin.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.gui.DragonManipulationScreen;
import de.pfannekuchen.lotas.gui.LootManipulationScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.world.dimension.DimensionType;

@Mixin(GameMenuScreen.class)
public abstract class MixinGameMenuScreen extends Screen {
	protected MixinGameMenuScreen(Text title) { super(title); }

	@Inject(at = @At("RETURN"), method = "init")
	public void addCustomButtons(CallbackInfo ci) {
		this.addButton(new ButtonWidget((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, "Manipulate Drops", btn -> {
			this.minecraft.openScreen(new LootManipulationScreen((GameMenuScreen) (Object) this));
		}));
		this.addButton(new ButtonWidget((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> {
			this.minecraft.openScreen(new DragonManipulationScreen((GameMenuScreen) (Object) this));
		})).active = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().size() >= 1;
	}
	
	
}
