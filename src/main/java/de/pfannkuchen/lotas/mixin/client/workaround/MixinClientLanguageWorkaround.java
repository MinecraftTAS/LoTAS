package de.pfannkuchen.lotas.mixin.client.workaround;

//import java.io.InputStream;
//import java.util.List;
//import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;

//import de.pfannkuchen.lotas.ClientLoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.ClientLanguage;
//import net.minecraft.locale.Language;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.packs.resources.Resource;

/**
 * This workaround loads the language files for LoTAS
 * @author Pancake
 FIXME: refmap issues
 */
@Mixin(ClientLanguage.class)
@Environment(EnvType.CLIENT)
public class MixinClientLanguageWorkaround {
/*
	/**
	 * Temporary Input Stream in case LoTAS adds something to the loaded language
	 *
	@Unique
	private static InputStream stream;

	/**
	 * Tries to find the Language Code during language initialization and loads an Input Stream for the LoTAS Append
	 * @param string2 Namepsace
	 * @param string Resource
	 * @return non-modified resource location
	 *
	@Redirect(method = "loadFrom", at = @At(value = "NEW", target = "Lnet/minecraft/resources/ResourceLocation;(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
	private static ResourceLocation onLanguageLoadFromResourceLoad(String string2, String string) {
		stream = ClientLoTAS.class.getResourceAsStream("/assets/lotas/" + string);
		return new ResourceLocation(string2, string);
	}

	/**
	 * Tries to load the new Language JSON provided by {@link  #onLanguageLoadFromResourceLoad(String, String)} and add it's content to the parameter map.
	 * @param list List of resource to load from
	 * @param map Map with translations
	 *
	@Redirect(method = "loadFrom", at = @At(value = "INVOKE", target = "appendFrom"))
	private static void onLanguageLoadFromContentLoad(List<Resource> list, Map<String, String> map) {
		if (stream != null) Language.loadFromJson(stream, map::put);
		appendFrom(list, map);
	}

	/**
	 * Need to add this shadow method in order to load vanilla and lotas translations when readirecting the original appendFrom call
	 * @param list List of Resources to load translations from
	 * @param map Map with translation to fill up
	 *
	@Shadow
	private static void appendFrom(List<Resource> list, Map<String, String> map) {
		throw new AssertionError();
	}

*/

}
