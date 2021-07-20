package de.pfannekuchen.lotas.core.utils;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.gui.screens.Screen;

//#if MC>=11700
//$$ public interface AccessorScreen2 {
//$$ 	public <T extends net.minecraft.client.gui.components.events.GuiEventListener & net.minecraft.client.gui.components.Widget & net.minecraft.client.gui.narration.NarratableEntry> T addRenderableWidget(T widget);
//#else
public class AccessorScreen2 {
//#endif
}
