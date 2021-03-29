package de.pfannekuchen.lotas.mixin.gui;

import java.io.IOException;
import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.LoTASModContainer;
import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.challenges.ChallengeMap;
import de.pfannekuchen.lotas.config.ConfigManager;
import de.pfannekuchen.lotas.gui.GuiSeedsMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.world.WorldSettings;
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

	public HashMap<Integer, ChallengeMap> list = new HashMap<>();
	
	@Inject(at = @At("HEAD"), cancellable = true, method = "func_146615_e")
	public void func_146615_e(int i, CallbackInfo ci) {
		if (field_146639_s.get(selectedIndex).getSizeOnDisk() == 0) {
			ChallengeLoader.map = list.get(selectedIndex);
			try {
				ChallengeLoader.load(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ci.cancel();
		}
	}
	
	public void tryLoadExistingWorld(GuiSelectWorld selectWorldGUI, String dirName, String saveName) {
		mc.launchIntegratedServer(dirName, saveName, (WorldSettings) null);
	}

	@Inject(at = @At("RETURN"), method = "initGui")
	public void injectinitGui2(CallbackInfo ci) {
		if (!ConfigManager.getBoolean("tools", "hideMaps")) {
			for (ChallengeMap map : LoTASModContainer.maps) {
				SaveFormatComparator entry = map.getSummary();
				// entry.po = new ResourceLocation("maps", map.resourceLoc);
				field_146639_s.add(entry);
				list.put(field_146639_s.size() - 1, map);
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "initGui")
	public void injectinitGui(CallbackInfo ci) {
		this.buttonList.add(new GuiButton(16, 5, 5, 98, 20, "Seeds"));
		this.buttonList
				.add(new GuiCheckBox(17, width - 17 - mc.fontRendererObj.getStringWidth("Open ESC when joining world"),
						4, "Open ESC when joining world", ConfigManager.getBoolean("tools", "hitEscape")));
		this.buttonList
				.add(new GuiCheckBox(18, width - 17 - mc.fontRendererObj.getStringWidth("Open ESC when joining world"),
						16, "Show TAS Challenge Maps", !ConfigManager.getBoolean("tools", "hideMaps")));
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
		case 18:
			ConfigManager.setBoolean("tools", "hideMaps", !((GuiCheckBox) button).isChecked());
			ConfigManager.save();

			field_146639_s.clear();
			if (((GuiCheckBox) button).isChecked()) {
				func_146627_h();
				for (ChallengeMap map : LoTASModContainer.maps) {
					SaveFormatComparator entry = map.getSummary();
					field_146639_s.add(entry);
				}
			} else {
				func_146627_h();
			}
		default:

			break;
		}
	}

	@Shadow
	public abstract void func_146627_h();

}
