package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
//#if MC>=11700
//$$ public class AccessorScreen2 implements AccessorScreen{
//#else
public class AccessorScreen2 {
//#endif

	//#if MC>=11700
//$$ 	private List<net.minecraft.client.gui.components.Widget> renderables;
//$$
//$$ 	private List<net.minecraft.client.gui.components.events.GuiEventListener> children;
//$$
//$$ 	private List<net.minecraft.client.gui.narration.NarratableEntry> narratables;
//$$
//$$ 	@Override
//$$ 	public <T extends net.minecraft.client.gui.components.events.GuiEventListener & net.minecraft.client.gui.components.Widget & net.minecraft.client.gui.narration.NarratableEntry> T addRenderableWidget(T widget) {
//$$ 		this.renderables.add((net.minecraft.client.gui.components.Widget) widget);
//$$ 		this.children.add(widget);
//$$ 		this.narratables.add((net.minecraft.client.gui.narration.NarratableEntry) widget);
//$$ 		return widget;
//$$ 	}
//#endif
}
