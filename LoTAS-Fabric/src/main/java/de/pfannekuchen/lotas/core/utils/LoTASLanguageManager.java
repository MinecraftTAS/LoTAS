package de.pfannekuchen.lotas.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import de.pfannekuchen.lotas.mixin.accessors.AccessorLanguage;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

/**
 * Appends language files either from the resources under assets/lotas/lang or from a resourcepack to the Locale.
 * @author Scribble
 *
 */
public class LoTASLanguageManager{
	
	private String currentCode;
	
	public LoTASLanguageManager(String startCode) {
		currentCode = startCode;
	}
	
	
	//#if MC>=11601
//$$ 	@SuppressWarnings("unchecked")
//$$ 	public net.minecraft.client.resources.language.ClientLanguage reload(net.minecraft.client.resources.language.ClientLanguage locale) {
	//#else
	public void reload(net.minecraft.client.resources.language.Locale locale) {
	//#endif
		InputStream langfile = getFromResourceManager(); // First check if a resourcepack is loaded and if it has a language file
		
		if(langfile == null) {
			langfile = getFromResources();	// If that fails, load data from the resources
		}
		if(langfile!=null) {
			//#if MC>=11601
//$$ 			Map<String, String> oldMap = ((AccessorLanguage)locale).getStorage(); // getStorage is immutiable so we have to make it mutable again
//$$ 			Map<String, String> map = new HashMap<>(oldMap);
//$$ 			Language.loadFromJson(langfile, map::put);	// Append to the locale. This also uses vanilla patterns in the translation files
//$$ 			((AccessorLanguage)locale).setStorage(ImmutableMap.copyOf((Map)map)); // Update the storage of the current language file
			//#else
			((AccessorLanguage)locale).runAppendFrom(langfile);	// Append to the locale. This also uses vanilla patterns in the translation files
			//#endif
		}
		//#if MC>=11601
//$$ 		return locale;
		//#endif
	}
	
	public void setLanguage(String code) {
		this.currentCode = code;
	}
	
	private InputStream getFromResources() {
		InputStream resource = getClass().getResourceAsStream("/assets/lotas/lang/"+currentCode+".json");
		return resource;
	}
	
	private InputStream getFromResourceManager() {
		ResourceLocation location = new ResourceLocation("lotas", "lang/"+currentCode+".json");
		ResourceManager manager = Minecraft.getInstance().getResourceManager();
		//#if MC>=11900
//$$ 		if(!manager.getResource(location).isPresent()) {
		//#else
		if(!manager.hasResource(location)) {
		//#endif
			return null;
		}
		Resource res = null;
		//#if MC<11900
		try {
		//#endif
			//#if MC>=11900
//$$ 			res = manager.getResource(location).orElse(null);
			//#else
			res = manager.getResource(location);
		} catch (IOException e) {
			return null;
		}
			//#endif

		if (res != null) {
			//#if MC>=11900
//$$ 			try {
//$$ 				return res.open();
//$$ 			} catch (IOException e) {
//$$ 				return null;
//$$ 			}
			//#else
			return res.getInputStream();
			//#endif
		}
		return null;
	}
}
