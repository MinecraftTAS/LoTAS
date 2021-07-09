package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
public interface AccessorButtons {
	@Accessor
	//#if MC>=11700
//$$ 	public List<net.minecraft.client.gui.components.Widget> getButtons();
	//#else
	public List<net.minecraft.client.gui.components.AbstractWidget> getButtons();
	//#endif
}
