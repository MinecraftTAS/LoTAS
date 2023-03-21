package de.pfannekuchen.lotas.mixin.render.binds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.GuiIngameForge;

@Mixin(GuiIngameForge.class)
public class MixinTickrateChangerUI {
	
	@Redirect(method="renderFood", at=@At(value="INVOKE", target="Lnet/minecraftforge/client/GuiIngameForge;drawTexturedModalRect(IIIIII)V", ordinal = 0), remap = false)
	public void redoRegistryAccess(GuiIngameForge gui, int x, int y, int textureX, int textureY, int width, int height) {
		int color = 0x000000;
		if (Minecraft.getMinecraft().player.isPotionActive(MobEffects.HUNGER)) {
			color = 0x12410b;
		}
		int mask = 0xFF000000;
		MCVer.fill(x + 8, y + 8, x + 1 + 8, y + 1 + 8, mask + color);
		MCVer.color4f(255, 255, 255, 255);
		
		gui.drawTexturedModalRect(x, y, textureX, textureY, width, height);
	}
}
