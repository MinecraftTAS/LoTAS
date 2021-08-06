package de.pfannekuchen.lotas.mixin.render.gui;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;

@Mixin(GuiIngameForge.class)
public abstract class MixinInGameHud {
	
	ResourceLocation potion = new ResourceLocation("lotas:misc/potion.png");
	
	@Inject(method="renderExperience", at=@At(value="HEAD"), remap = false)
	public void mixinRenderExperienceBar(CallbackInfo ci) {
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
		Minecraft.getMinecraft().getTextureManager().bindTexture(potion);
		int m = (scaledresolution.getScaledWidth() / 2)-6;
        int n = scaledresolution.getScaledHeight() - 31 - 19;
        GL11.glEnable(GL11.GL_BLEND);
		int skale=20;
		GL11.glColor4f(1, 1, 1, 0.3F);
		Gui.drawModalRectWithCustomSizedTexture(m-3, n, 0F, 0F, skale, skale, skale, skale);
        GL11.glDisable(GL11.GL_BLEND);
        
	}
}
