package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.pfannekuchen.lotas.duck.KeyCodeDuck;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Mixin(KeyBinding.class)
public class MixinKeyBinding implements KeyCodeDuck{
	
	@Shadow
	private InputUtil.KeyCode keyCode;

	@Override
	public int getKeyCode() {
		return this.keyCode.getKeyCode();
	}
	
}
