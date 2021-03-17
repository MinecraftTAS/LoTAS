package de.pfannekuchen.lotas.duck;

import de.pfannekuchen.lotas.mixin.MixinKeyBinding;

/**
 * For whatever reason, there is no way of getting the keycode integer from a keybinding... this tries to correct that
 * @author ScribbleLP
 * @see MixinKeyBinding
 */
public interface KeyCodeDuck {
	public int getKeyCode();
}
