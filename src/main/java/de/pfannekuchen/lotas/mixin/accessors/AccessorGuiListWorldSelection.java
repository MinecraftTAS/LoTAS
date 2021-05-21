package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC>=10900
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
@Mixin(GuiListWorldSelection.class)
//#else
//$$ import net.minecraft.client.gui.GuiSelectWorld;
//$$ @Mixin(GuiSelectWorld.class)
//#endif
public interface AccessorGuiListWorldSelection {

	//#if MC>=10900
	@Accessor("entries")
	public List<GuiListWorldSelectionEntry> entries();
	//#endif
	
}
