package de.pfannekuchen.lotas.mixin.render.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
//#if MC>=10900
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraftforge.fml.client.config.GuiCheckBox;
@Mixin(GuiWorldSelection.class)
public abstract class MixinGuiWorldSelection extends GuiScreen {
	@Shadow
	private GuiListWorldSelection selectionList;
//#else
//$$ import net.minecraft.world.storage.SaveFormatComparator;
//$$ import net.minecraftforge.fml.client.config.GuiCheckBox;
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
	
	@Inject(at = @At("HEAD"), method = "initGui")
	public void injectinitGui(CallbackInfo ci) {
		this.buttonList.add(new GuiCheckBox(17, width - 17 - MCVer.getFontRenderer(mc).getStringWidth("Open ESC when joining world"), 4, "Open ESC when joining world", ConfigUtils.getBoolean("tools", "hitEscape")));
	}
	
	@Inject(at = @At("HEAD"), method = "actionPerformed")
	public void injectactionPerformed(GuiButton button, CallbackInfo ci) {
		switch (button.id) {
			case 17:
				ConfigUtils.setBoolean("tools", "hitEscape", ((GuiCheckBox) button).isChecked());
				ConfigUtils.save();
				break;
			default:
				break;
		}
	}
	
}
