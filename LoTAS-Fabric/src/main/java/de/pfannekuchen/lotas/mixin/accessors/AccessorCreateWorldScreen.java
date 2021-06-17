package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;

@Mixin(CreateWorldScreen.class)
public interface AccessorCreateWorldScreen {
	//#if MC<=11502
	@Accessor("seed")
	void setSeed(String seed);
	@Accessor("cheatsEnabled")
	void setCheatsEnabled(boolean cheatsEnabled);
	//#endif
}
