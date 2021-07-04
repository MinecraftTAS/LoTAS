package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screen.Screen;
//#if MC<=11605
import net.minecraft.client.gui.widget.AbstractButtonWidget;
//#endif

@Mixin(Screen.class)
public interface AccessorButtons {
	//#if MC<=11605
	@Accessor
	public List<AbstractButtonWidget> getButtons();
	//#endif
}
