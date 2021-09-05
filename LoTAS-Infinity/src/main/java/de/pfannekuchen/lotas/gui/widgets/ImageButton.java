package de.pfannekuchen.lotas.gui.widgets;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;

/**
 * Button with an Image Overlayed
 * @author Pancake
 */
public class ImageButton extends Button {
	private boolean toggled;

	private ResourceLocation pic;

	public ImageButton(int x, int y, Button.OnPress action, ResourceLocation pic) {
		//#if MC>=11600
//$$ 		super(x, y, 20, 20, new net.minecraft.network.chat.TextComponent(""), action);
		//#else
		super(x, y, 20, 20, "", action);
		//#endif
		this.pic = pic;
	}

	public boolean isToggled() {
		return this.toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	//#if MC>=11600
//$$ 	public void renderButton(com.mojang.blaze3d.vertex.PoseStack matrices, int mouseX, int mouseY, float delta) {
//$$ 		super.renderButton(matrices, mouseX, mouseY, delta);
//$$ 		MCVer.stack = matrices;
	//#else
	public void renderButton(int mouseX, int mouseY, float delta) {
		super.renderButton(mouseX, mouseY, delta);
	//#endif
		MCVer.bind(Minecraft.getInstance().getTextureManager(), pic);
		MCVer.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		MCVer.blit(x, y, 0.0F, 0.0F, 20, 20, 20, 20);
	}

}
