package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
public interface AccessorButtons {
	//#if MC>=11700
//$$ 	@Accessor("renderables")
//$$ 	public List<net.minecraft.client.gui.components.Renderable> getButtons();
	//#else
	//#if MC>=11700
//$$ 	@Accessor("renderables")
//$$ 	public List<net.minecraft.client.gui.components.Widget> getButtons();
	//#else
	@Accessor
	public List<net.minecraft.client.gui.components.AbstractWidget> getButtons();

	//#endif
	//#endif
}
