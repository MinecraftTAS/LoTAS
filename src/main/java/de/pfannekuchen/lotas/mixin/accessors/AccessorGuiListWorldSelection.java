package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;

@Mixin(GuiListWorldSelection.class)
public interface AccessorGuiListWorldSelection {

	@Accessor("entries")
	public List<GuiListWorldSelectionEntry> entries();
	
}
