package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC>=10900
@Mixin(net.minecraft.client.gui.GuiListWorldSelection.class)
//#else
//$$ @Mixin(net.minecraft.client.gui.GuiSelectWorld.class)
//#endif
public interface AccessorGuiListWorldSelection {

	//#if MC>=10900
	@Accessor("entries")
	public List<net.minecraft.client.gui.GuiListWorldSelectionEntry> entries();
	//#endif
	
}
