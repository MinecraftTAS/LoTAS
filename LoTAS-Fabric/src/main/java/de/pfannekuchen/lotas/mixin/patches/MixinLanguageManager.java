package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.LoTASModContainer;
import de.pfannekuchen.lotas.core.utils.LoTASLanguageManager;
import net.minecraft.client.resources.language.LanguageManager;

@Mixin(LanguageManager.class)
public class MixinLanguageManager {
	
	//#if MC<11601
	@Shadow @org.spongepowered.asm.mixin.Final
	private static net.minecraft.client.resources.language.Locale LOCALE;
	//#endif
	
	
	@Shadow
	private String currentCode;
	
	
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	public void inject_Init(String string, CallbackInfo ci) {
		LoTASModContainer.languageManager = new LoTASLanguageManager(string);
	}
	
	//#if MC>=11601
//$$ 	@ModifyArg(method = "onResourceManagerReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/language/I18n;setLanguage(Lnet/minecraft/locale/Language;)V"), index = 0)
//$$ 	public net.minecraft.locale.Language inject_onResourceManagerReload(net.minecraft.locale.Language clientLanguage) {
//$$  		LoTASModContainer.languageManager.reload((net.minecraft.client.resources.language.ClientLanguage) clientLanguage);
//$$  		return clientLanguage;
//$$ 	}
	//#else
	@Inject(method = "onResourceManagerReload", at = @At(value = "RETURN"))
	public void inject_onResourceManagerReload(CallbackInfo ci) {
		LoTASModContainer.languageManager.reload(LOCALE);
	}
	//#endif
	
	
	@Inject(method = "setSelected", at = @At("RETURN"))
	public void inject_setSelected(CallbackInfo ci) {
		LoTASModContainer.languageManager.setLanguage(currentCode);
	}
}
