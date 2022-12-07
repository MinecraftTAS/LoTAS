package de.pfannekuchen.lotas.mixin.render.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.gui.widgets.ChallengeMapEntryWidget;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.gui.GuiButton;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

//#if MC>=10900
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
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
				ChallengeMapEntryWidget entry = new ChallengeMapEntryWidget(selectionList, map, width);
				entry.loc = new ResourceLocation("maps", map.resourceLoc);
				try {
					@SuppressWarnings("unchecked")
					java.util.List<GuiListWorldSelectionEntry> i = (java.util.List<GuiListWorldSelectionEntry>) getFinal(selectionList, GuiListWorldSelection.class.getDeclaredField("field_186799_w"));
					i.add(entry);
					setFinal(selectionList, GuiListWorldSelection.class.getDeclaredField("field_186799_w"), i);
				} catch (Exception e) { }
			}
			//#else
//$$ 			field_146638_t = ((GuiSelectWorld) (Object) this).new List(mc);
			//#endif
		}
	}
	
	@Inject(at = @At("HEAD"), method = "initGui")
	public void injectinitGui(CallbackInfo ci) {
		this.buttonList.add(new GuiCheckBox(17, width - 17 - MCVer.getFontRenderer(mc).getStringWidth("Open ESC when joining world"), 4, "Open ESC when joining world", ConfigUtils.getBoolean("tools", "hitEscape")));
		this.buttonList.add(new GuiCheckBox(18, width - 17 - MCVer.getFontRenderer(mc).getStringWidth("Open ESC when joining world"), 16, "Show TAS Challenge Maps", ConfigUtils.getBoolean("tools", "hideMaps")));
	}
	
	// Workaround cuz of Mixin
	@Unique private static void setFinal(Object instance, Field field, Object newValue) {
		try {
			field.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

			field.set(instance, newValue);
		} catch (Exception e) { }
	}
	// 2. Workaround
	@Unique private static Object getFinal(Object instance, Field field) {
		try {
			field.setAccessible(true);
			return field.get(instance);
		} catch (Exception e) { }
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Inject(at = @At("HEAD"), method = "actionPerformed")
	public void injectactionPerformed(GuiButton button, CallbackInfo ci) {
		switch (button.id) {
			case 17:
				ConfigUtils.setBoolean("tools", "hitEscape", ((GuiCheckBox) button).isChecked());
				ConfigUtils.save();
				break;
			case 18:
				ConfigUtils.setBoolean("tools", "hideMaps", ((GuiCheckBox) button).isChecked());
				ConfigUtils.save();
				//#if MC>=10900
				try {
					java.util.List<GuiListWorldSelectionEntry> i = (java.util.List<GuiListWorldSelectionEntry>) getFinal(selectionList, GuiListWorldSelection.class.getDeclaredField("field_186799_w"));
					i.clear();
					setFinal(selectionList, GuiListWorldSelection.class.getDeclaredField("field_186799_w"), i);
					if (((GuiCheckBox) button).isChecked()) {
						selectionList.refreshList();
						for (ChallengeMap map : LoTASModContainer.maps) {
							ChallengeMapEntryWidget entry = new ChallengeMapEntryWidget(selectionList, map, width);
							entry.loc = new ResourceLocation("maps", map.resourceLoc);
							i = (java.util.List<GuiListWorldSelectionEntry>) getFinal(selectionList, GuiListWorldSelection.class.getDeclaredField("field_186799_w"));
							i.add(entry);
							setFinal(selectionList, GuiListWorldSelection.class.getDeclaredField("field_186799_w"), i);
						}
					} else {
						selectionList.refreshList();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//#else
//$$ 				if (((GuiCheckBox) button).isChecked()) {
//$$ 					field_146638_t = new ChallengeMapEntryWidget((GuiSelectWorld) (Object) this, width);
//$$ 				} else {
//$$ 					field_146638_t = ((GuiSelectWorld) (Object) this).new List(mc);
//$$ 				}
				//#endif
			default:
				
				break;
		}
	}
	
}
