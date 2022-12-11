package de.pfannkuchen.lotas.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
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
	 * @param tooltip tooltip
	 * @param toggled Is toggled or not
	 */
	public CustomImageButton(int i, int j, int k, int l, int m, int n, int o, ResourceLocation streaming, int p, int q, OnPress onPress, Tooltip tooltip, boolean toggled) {
		super(i + 2, j + 2, k - 4, l - 4, m, n, o, streaming, p, q, b -> {
		}, Component.empty());
		setTooltip(tooltip);
		this.isToggled = toggled;
		// Trigger the onPress differently
		this.internalButton = Button.builder(Component.empty(), b -> {
			this.isToggled = !this.isToggled;
			onPress.onPress(CustomImageButton.this);
		}).bounds(i, j, k, l).build();
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
		this.internalButton.render(poseStack, this.isToggled ? this.internalButton.getX() + 1 : i, this.isToggled ? this.internalButton.getY() + 1 : j, f);
		super.render(poseStack, i, j, f);
	}

}
