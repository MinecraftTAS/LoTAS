package de.pfannekuchen.lotas.gui.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.brigadier.LiteralMessage;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class ImageButton extends ButtonWidget {
	private boolean toggled;

	private Identifier pic;

	public ImageButton(int x, int y, ButtonWidget.PressAction action, Identifier pic) {
		//#if MC>=11601
//$$ 		super(x, y, 20, 20, new LiteralText(""), action);
		//#else
		super(x, y, 20, 20, I18n.translate(""), action);
		//#endif
		this.pic = pic;
	}

	public boolean isToggled() {
		return this.toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	public void renderButton(int mouseX, int mouseY, float delta) {
		//#if MC>=11601
//$$ 		super.renderButton(null, mouseX, mouseY, delta);
		//#else
		super.renderButton(mouseX, mouseY, delta);
		//#endif
		MinecraftClient.getInstance().getTextureManager().bindTexture(pic);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		//#if MC>=11601
//$$ 		drawTexture(null, x, y, 0.0F, 0.0F, 20, 20, 20, 20);
		//#else
		DrawableHelper.blit(x, y, 0.0F, 0.0F, 20, 20, 20, 20);
		//#endif
	}

}
