package de.pfannekuchen.lotas.mixin.accessors;

import java.io.InputStream;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

//#if MC>=11601
//$$ @Mixin(net.minecraft.client.resources.language.ClientLanguage.class)
//#else
@Mixin(net.minecraft.client.resources.language.Locale.class)
//#endif
public interface AccessorLanguage {
	//#if MC>=11601
//$$ 	@Accessor
//$$ 	public Map<String, String> getStorage();
//$$
//$$ 	@Accessor("storage") @Mutable
//$$ 	public void setStorage(Map<String, String> map);
	//#else
	@Invoker("appendFrom")
	public void runAppendFrom(InputStream stream);
	//#endif
}
