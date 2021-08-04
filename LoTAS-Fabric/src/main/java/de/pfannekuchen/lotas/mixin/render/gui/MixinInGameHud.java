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
import net.minecraft.resources.ResourceLocation;

@Mixin(Gui.class)
public abstract class MixinInGameHud {
	
	ResourceLocation potion = new ResourceLocation("lotas:heck/potion.png");
	@Shadow
	private int screenWidth;
	@Shadow
	private int screenHeight;
	@Shadow
	private Minecraft minecraft;
	
	@Inject(method="renderExperienceBar", at=@At(value="HEAD"))
	public void mixinRenderExperienceBar(CallbackInfo ci) {
		MCVer.bind(minecraft.getTextureManager(), potion);
		int m = (this.screenWidth / 2)-6;
        int n = this.screenHeight - 31 - 19;
        GL11.glEnable(GL11.GL_BLEND);
		MCVer.color4f(1, 1, 1, 0.3F);
		int skale=20;
		MCVer.blit(m-3, n, 0F, 0F, skale, skale, skale, skale);
        GL11.glDisable(GL11.GL_BLEND);
        
	}
}
