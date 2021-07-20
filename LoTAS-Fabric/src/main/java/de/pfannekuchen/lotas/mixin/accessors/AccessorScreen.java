package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import de.pfannekuchen.lotas.core.utils.AccessorScreen2;
import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
//#if MC>=11700
//$$ public class AccessorScreen implements AccessorScreen2{
//$$
//$$
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
//$$ }
//#else
public interface AccessorScreen {
	@Invoker("addButton")
	public abstract <T extends net.minecraft.client.gui.components.AbstractWidget> T invokeAddButton(T abstractWidget);
}	
//#endif

