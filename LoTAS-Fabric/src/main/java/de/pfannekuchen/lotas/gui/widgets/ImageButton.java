package de.pfannekuchen.lotas.gui.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;

public class ImageButton extends Button {
	private boolean toggled;

	private ResourceLocation pic;

	public ImageButton(int x, int y, Button.OnPress action, ResourceLocation pic) {
		super(x, y, 20, 20, I18n.get(""), action);
		this.pic = pic;
	}

	public boolean isToggled() {
		return this.toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	public void renderButton(int mouseX, int mouseY, float delta) {
		super.renderButton(mouseX, mouseY, delta);
		Minecraft.getInstance().getTextureManager().bind(pic);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiComponent.blit(x, y, 0.0F, 0.0F, 20, 20, 20, 20);
	}

}
