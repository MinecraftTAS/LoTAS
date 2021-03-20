package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;

@Mixin(I18n.class)
public class MixinI18n {
	
	@Shadow
	private static Locale i18nLocale;
	
	/**
	 * @author Pancake
	 * @reason Remove CopyOf
	 * @param translateKey
	 * @param parameters
	 * @return
	 */
	@Overwrite
    public static String format(String translateKey, Object... parameters) {
        return translateKey == "selectWorld.newWorld.copyOf" ? (String) parameters[0] : i18nLocale.formatMessage(translateKey, parameters);
    }
			 
	
}
