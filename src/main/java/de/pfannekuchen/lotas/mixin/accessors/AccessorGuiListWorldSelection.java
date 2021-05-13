package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC>=10900
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
//#else
//$$ import net.minecraft.client.gui.GuiSelectWorld;
//#endif

//#if MC>=10900
@Mixin(GuiListWorldSelection.class)
//#else
//$$ @Mixin(GuiSelectWorld.class)
//#endif
public interface AccessorGuiListWorldSelection {

	//#if MC>=10900
	@Accessor("entries")
	public List<GuiListWorldSelectionEntry> entries();
	//#endif
	
}
