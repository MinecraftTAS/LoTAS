package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.GuiSeedList;
import de.pfannekuchen.lotas.gui.widgets.ChallengeMapEntryWidget;
import de.pfannekuchen.lotas.mixin.accessors.AccessorGuiListWorldSelection;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

//#if MC>=10900
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiWorldSelection;
@Mixin(GuiWorldSelection.class)
public abstract class MixinGuiWorldSelection extends GuiScreen {
	@Shadow
	private GuiListWorldSelection selectionList;
//#else
//$$ import net.minecraft.world.storage.SaveFormatComparator;
//$$ import net.minecraft.client.gui.GuiSelectWorld;
//$$ @Mixin(GuiSelectWorld.class)
//$$ public abstract class MixinGuiWorldSelection extends GuiScreen {
//$$ 	@Shadow
//$$ 	private GuiSelectWorld.List field_146638_t;
//$$ 	@Shadow
//$$ 	private int selectedIndex;
//$$ 	@Shadow
//$$ 	private java.util.List<SaveFormatComparator> field_146639_s;
//#endif
	
	@Inject(at = @At("RETURN"), method = "initGui")
	public void injectinitGui2(CallbackInfo ci) {
		if (!ConfigUtils.getBoolean("tools", "hideMaps")) {
			//#if MC>=10900
			for (ChallengeMap map : LoTASModContainer.maps) {
				ChallengeMapEntryWidget entry = new ChallengeMapEntryWidget(selectionList, map);
				entry.loc = new ResourceLocation("maps", map.resourceLoc);
				((AccessorGuiListWorldSelection) selectionList).entries().add(entry);
			}
			//#else
//$$ 			field_146638_t = new ChallengeMapEntryWidget((GuiSelectWorld) (Object) this);
			//#endif
		}
	}
	
	@Inject(at = @At("HEAD"), method = "initGui")
	public void injectinitGui(CallbackInfo ci) {
		this.buttonList.add(new GuiButton(16, 5, 5, 98, 20, "Seeds"));
		this.buttonList.add(new GuiCheckBox(17, width - 17 - MCVer.getFontRenderer(mc).getStringWidth("Open ESC when joining world"), 4, "Open ESC when joining world", ConfigUtils.getBoolean("tools", "hitEscape")));
		this.buttonList.add(new GuiCheckBox(18, width - 17 - MCVer.getFontRenderer(mc).getStringWidth("Open ESC when joining world"), 16, "Show TAS Challenge Maps", !ConfigUtils.getBoolean("tools", "hideMaps")));
	}
	
	@Inject(at = @At("HEAD"), method = "actionPerformed")
	public void injectactionPerformed(GuiButton button, CallbackInfo ci) {
		switch (button.id) {
			case 16:
				Minecraft.getMinecraft().displayGuiScreen(new GuiSeedList());
				break;
			case 17:
				ConfigUtils.setBoolean("tools", "hitEscape", ((GuiCheckBox) button).isChecked());
				ConfigUtils.save();
				break;
			case 18:
				ConfigUtils.setBoolean("tools", "hideMaps", !((GuiCheckBox) button).isChecked());
				ConfigUtils.save();
				
				//#if MC>=10900
				((AccessorGuiListWorldSelection) selectionList).entries().clear();
				if (((GuiCheckBox) button).isChecked()) {
					selectionList.refreshList();
					for (ChallengeMap map : LoTASModContainer.maps) {
						ChallengeMapEntryWidget entry = new ChallengeMapEntryWidget(selectionList, map);
						entry.loc = new ResourceLocation("maps", map.resourceLoc);
						((AccessorGuiListWorldSelection) selectionList).entries().add(entry);
					}
				} else {
					selectionList.refreshList();
				}
				//#else
//$$ 				if (((GuiCheckBox) button).isChecked()) {
//$$ 					field_146638_t = new ChallengeMapEntryWidget((GuiSelectWorld) (Object) this);
//$$ 				} else {
//$$ 					field_146638_t = ((GuiSelectWorld) (Object) this).new List(mc);
//$$ 				}
				//#endif
			default:
				
				break;
		}
	}
	
}
