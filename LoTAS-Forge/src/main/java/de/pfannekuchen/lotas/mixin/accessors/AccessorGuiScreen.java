package de.pfannekuchen.lotas.mixin.accessors;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiScreen.class)
public interface AccessorGuiScreen {

	@Accessor
	public List<GuiButton> getButtonList();
}
