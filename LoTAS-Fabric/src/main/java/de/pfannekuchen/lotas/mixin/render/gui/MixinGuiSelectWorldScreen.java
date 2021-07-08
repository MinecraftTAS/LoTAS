package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.network.chat.Component;

/**
 * Adds Utility Buttons to the World Selection Screen
 * @author Pancake
 */
@Mixin(SelectWorldScreen.class)
public abstract class MixinGuiSelectWorldScreen extends Screen {

	protected MixinGuiSelectWorldScreen(Component title) {
		super(title);
	}

	private SmallCheckboxWidget widget;
	@Shadow
	private WorldSelectionList list;

	@Inject(at = @At("TAIL"), method = "init")
	public void injectinit(CallbackInfo ci) {
		/*
		 * TODO: Fucking fix this mess
		 * addButton(MCVer.Button(2, 2, 98, 20, "Seed List", button -> {
			Minecraft.getInstance().setScreen(new SeedListScreen());
		}));*/
		addButton(widget = new SmallCheckboxWidget(width - 160, 4, "Open ESC when joining world", ConfigUtils.getBoolean("tools", "hitEscape"), b -> {
			ConfigUtils.setBoolean("tools", "hitEscape", widget.isChecked());
			ConfigUtils.save();
		}));
	}

}
