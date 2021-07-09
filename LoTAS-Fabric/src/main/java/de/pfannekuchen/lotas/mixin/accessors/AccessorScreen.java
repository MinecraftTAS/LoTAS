package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.screens.Screen;


//#if MC>=11700
//$$ public interface AccessorScreen {
//$$ 	public <T extends net.minecraft.client.gui.components.events.GuiEventListener & net.minecraft.client.gui.components.Widget & net.minecraft.client.gui.narration.NarratableEntry> T addRenderableWidget(T widget);
//$$ }
//#else
@Mixin(Screen.class)
public interface AccessorScreen {
	@Invoker("addButton")
	public abstract <T extends net.minecraft.client.gui.components.AbstractWidget> T invokeAddButton(T abstractWidget);
}	
//#endif

