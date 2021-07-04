package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import de.pfannekuchen.lotas.core.CheekyScreenDuck;
import net.minecraft.client.gui.screen.Screen;

@Mixin(Screen.class)
//#if MC>=11700
//$$ public abstract class AccessorScreen implements CheekyScreenDuck {
//$$ 	@Shadow
//$$ 	private List<net.minecraft.client.gui.Drawable> drawables;
//$$ 	@Shadow
//$$ 	private List<net.minecraft.client.gui.Element> children;
//$$ 	@Shadow
//$$ 	private List<net.minecraft.client.gui.Selectable> selectables;
//$$
//$$ 	@Override
//$$ 	public <T extends net.minecraft.client.gui.Element & net.minecraft.client.gui.Drawable & net.minecraft.client.gui.Selectable> T addDrawableCheekyChild(T drawable) {
//$$ 		this.drawables.add(drawable);
//$$ 		this.children.add(drawable);
//$$ 	    this.selectables.add((net.minecraft.client.gui.Selectable)drawable);
//$$ 	    return drawable;
//$$ 	}
//$$
//#else
public interface AccessorScreen {
	@Invoker("addButton")
	public net.minecraft.client.gui.widget.AbstractButtonWidget invokeaddButton(net.minecraft.client.gui.widget.AbstractButtonWidget drawable);
//#endif

}
