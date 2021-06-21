package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.SeedListScreen;
import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin(SelectWorldScreen.class)
public abstract class MixinGuiSelectWorldScreen extends Screen {

	protected MixinGuiSelectWorldScreen(Text title) {
		super(title);
	}

	private SmallCheckboxWidget widget;
	@Shadow
	private WorldListWidget levelList;

	@Inject(at = @At("TAIL"), method = "init")
	public void injectinit(CallbackInfo ci) {
		//#if MC>=11700
//$$ 		this.addDrawableChild(new NewButtonWidget(2, 2, 98, 20, "Seed List", button -> {
		//#else
		this.addButton(new NewButtonWidget(2, 2, 98, 20, "Seed List", button -> {
		//#endif
			MinecraftClient.getInstance().openScreen(new SeedListScreen());
		}));
		//#if MC>=11700
//$$ 		this.addDrawableChild(widget = new SmallCheckboxWidget(width - 160, 4, "Open ESC when joining world", ConfigUtils.getBoolean("tools", "hitEscape"), b -> {
		//#else
		this.addButton(widget = new SmallCheckboxWidget(width - 160, 4, "Open ESC when joining world", ConfigUtils.getBoolean("tools", "hitEscape"), b -> {
		//#endif
			ConfigUtils.setBoolean("tools", "hitEscape", widget.isChecked());
			ConfigUtils.save();
		}));
	}

}
