package de.pfannekuchen.lotas.mixin.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.gui.GuiSeedsMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraftforge.fml.client.config.GuiCheckBox;

@Mixin(GuiSelectWorld.class)
public abstract class RedoGuiWorldSelection extends GuiScreen {

	/*
	 *
	 * This Code adds a CheckBox whether you want to auto-open the IngameMenu when
	 * entering a world to the top right corner of the world selection screen.
	 *
	 */

	@Shadow
	private GuiSelectWorld.List field_146638_t;

	@Shadow
	private int selectedIndex;
	
	@Shadow
	private java.util.List<SaveFormatComparator> field_146639_s;

	@Inject(at = @At("HEAD"), method = "initGui")
	public void injectinitGui(CallbackInfo ci) {
		this.buttonList.add(new GuiButton(16, 5, 5, 98, 20, "Seeds"));
		this.buttonList
				.add(new GuiCheckBox(17, width - 17 - mc.fontRendererObj.getStringWidth("Open ESC when joining world"),
						4, "Open ESC when joining world", ConfigManager.getBoolean("tools", "hitEscape")));
	}

	@Inject(at = @At("HEAD"), method = "actionPerformed")
	public void injectactionPerformed(GuiButton button, CallbackInfo ci) {
		switch (button.id) {
		case 16:
			Minecraft.getMinecraft().displayGuiScreen(new GuiSeedsMenu());
			break;
		case 17:
			ConfigManager.setBoolean("tools", "hitEscape", ((GuiCheckBox) button).isChecked());
			ConfigManager.save();
			break;
		default:
			
			break;
		}
	}

	@Shadow
	public abstract void func_146627_h();

}
