package de.pfannekuchen.lotas.mixin.accessors;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EditBox.class)
public interface AccessorTextFieldWidget {
	@Accessor("textColorUneditable")
	public int getUneditableColor();
}
