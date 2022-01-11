package de.pfannkuchen.lotas.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * A VANILLA custom image button with a background
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class CustomImageButton extends ImageButton {

	private Button internalButton;
	public boolean isToggled;

	/**
	 * Creates a new button with a given image
	 * @param i X position
	 * @param j Y position
	 * @param k Width
	 * @param l Height
	 * @param m UVX
	 * @param n UVY
	 * @param o
	 * @param streaming Resource location
	 * @param p uv end X
	 * @param q uv end Y
	 * @param onPress press action
	 * @param onTooltip hover action
	 * @param toggled Is toggled or not
	 */
	public CustomImageButton(int i, int j, int k, int l, int m, int n, int o, ResourceLocation streaming, int p, int q, OnPress onPress, OnTooltip onTooltip, boolean toggled) {
		super(i+2, j+2, k-4, l-4, m, n, o, streaming, p, q, b -> {}, onTooltip, TextComponent.EMPTY);
		this.isToggled = toggled;
		// Trigger the onPress differently
		this.internalButton = new Button(i, j, k, l, TextComponent.EMPTY, b -> {
			this.isToggled = !this.isToggled;
			onPress.onPress(CustomImageButton.this);
		});
	}

	/**
	 * Click internal button too
	 */
	@Override
	public boolean mouseClicked(double d, double e, int i) {
		this.internalButton.mouseClicked(d, e, i);
		return super.mouseClicked(d, e, i);
	}

	/**
	 * Renders the background button
	 */
	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		this.internalButton.render(poseStack, this.isToggled ? this.internalButton.x+1 : i, this.isToggled ? this.internalButton.y+1 : j, f);
		super.render(poseStack, i, j, f);
	}

}
