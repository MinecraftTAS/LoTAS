package de.pfannekuchen.lotas.mixin.accessors;

//#if MC>=11502
//$$ import java.util.Set;
//$$
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//$$
//$$ import net.minecraft.client.render.model.ModelLoader;
//$$ import net.minecraft.client.util.SpriteIdentifier;
//$$
//$$ @Mixin(ModelLoader.class)
//$$ public interface AccessorModelLoader {
//$$ 	@Accessor("SHIELD_BASE_NO_PATTERN")
//$$ 	public static void setShieldBase(SpriteIdentifier identifier) {
//$$ 		throw new AssertionError();
//$$ 	}
//$$
//$$ 	@Accessor("DEFAULT_TEXTURES")
//$$     public static Set<SpriteIdentifier> getDefaultTexture() {
//$$         throw new AssertionError();
//$$     }
//$$ }
//#endif