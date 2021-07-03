package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.screen.Screen;

@Mixin(Screen.class)
public interface AccessorScreen {

	//#if MC>=11700
//$$ 	@Invoker
//$$ 	public net.minecraft.client.gui.Drawable addDrawableChild(net.minecraft.client.gui.Drawable drawable);
	//#else
	@Invoker("addButton")
	public net.minecraft.client.gui.widget.AbstractButtonWidget invokeaddButton(net.minecraft.client.gui.widget.AbstractButtonWidget drawable);
	//#endif
	
}
