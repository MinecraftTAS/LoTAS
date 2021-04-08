package de.pfannekuchen.lotas.mixin.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.LoTASModContainer;
import de.pfannekuchen.lotas.challenges.ChallengeMap;
import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.gui.GuiSeedsMenu;
import de.pfannekuchen.lotas.gui.parts.GuiCustomMapEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

@Mixin(GuiWorldSelection.class)
public abstract class RedoGuiWorldSelection extends GuiScreen {

	/*
	 *
	 * This Code adds a CheckBox whether you want to auto-open the IngameMenu when entering a world to the top right corner of the world selection screen.
	 *
	 */
	
	@Shadow
	private GuiListWorldSelection selectionList;
	
	@Inject(at = @At("RETURN"), method = "initGui")
	public void injectinitGui2(CallbackInfo ci) {
		if (!ConfigManager.getBoolean("tools", "hideMaps")) {
			for (ChallengeMap map : LoTASModContainer.maps) {
				GuiCustomMapEntry entry = new GuiCustomMapEntry(selectionList, map);
				entry.loc = new ResourceLocation("maps", map.resourceLoc);
				selectionList.entries.add(entry);
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "initGui")
	public void injectinitGui(CallbackInfo ci) {
		this.buttonList.add(new GuiButton(6, 5, 5, 98, 20, "Seeds"));
		this.buttonList.add(new GuiCheckBox(7, width - 17 - mc.fontRendererObj.getStringWidth("Open ESC when joining world"), 4, "Open ESC when joining world", ConfigManager.getBoolean("tools", "hitEscape")));
		this.buttonList.add(new GuiCheckBox(8, width - 17 - mc.fontRendererObj.getStringWidth("Open ESC when joining world"), 16, "Show TAS Challenge Maps", !ConfigManager.getBoolean("tools", "hideMaps")));
	}
	
	@Inject(at = @At("HEAD"), method = "actionPerformed")
	public void injectactionPerformed(GuiButton button, CallbackInfo ci) {
		switch (button.id) {
			case 6:
				Minecraft.getMinecraft().displayGuiScreen(new GuiSeedsMenu());
				break;
			case 7:
				ConfigManager.setBoolean("tools", "hitEscape", ((GuiCheckBox) button).isChecked());
				ConfigManager.save();
				break;
			case 8:
				ConfigManager.setBoolean("tools", "hideMaps", !((GuiCheckBox) button).isChecked());
				ConfigManager.save();
				
				selectionList.entries.clear();
				if (((GuiCheckBox) button).isChecked()) {
					selectionList.refreshList();
					for (ChallengeMap map : LoTASModContainer.maps) {
						GuiListWorldSelectionEntry entry = new GuiCustomMapEntry(selectionList, map);
						selectionList.entries.add(entry);
					}
				} else {
					selectionList.refreshList();
				}
			default:
				
				break;
		}
	}
	
}
