package de.pfannekuchen.lotas.mixin.accessors;

import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreateWorldScreen.class)
public interface AccessorCreateWorldScreen {
	@Accessor("initSeed")
	void setSeed(String seed);
	@Accessor("commands")
	void setCheatsEnabled(boolean cheatsEnabled);
}
