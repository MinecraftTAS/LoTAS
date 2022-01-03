package de.pfannkuchen.lotas.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * A VANILLA Custom Image Button with a background
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class CustomImageButton extends ImageButton {

	private Button internalButton;
	public boolean isToggled;
	
	/**
	 * Creates a new button with a given image
	 * FIXME: no i will not figure out what these parameters do..
	 * @param i
	 * @param j
	 * @param k
	 * @param l
	 * @param m
	 * @param n
	 * @param o
	 * @param streaming
	 * @param p
	 * @param q
	 * @param object
	 */
	public CustomImageButton(int i, int j, int k, int l, int m, int n, int o, ResourceLocation streaming, int p, int q, OnPress onPress, OnTooltip onTooltip, boolean toggled) {
		super(i+2, j+2, k-4, l-4, m, n, o, streaming, p, q, b -> {}, onTooltip, TextComponent.EMPTY);
		isToggled = toggled;
		// Trigger the onPress differenently
		internalButton = new Button(i, j, k, l, TextComponent.EMPTY, b -> {
			isToggled = !isToggled;
			onPress.onPress(CustomImageButton.this);
		});
	}
	
	/**
	 * Click internal button too
	 */
	@Override
	public boolean mouseClicked(double d, double e, int i) {
		internalButton.mouseClicked(d, e, i);
		return super.mouseClicked(d, e, i);
	}
	
	/**
	 * Renders the background button
	 */
	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		internalButton.render(poseStack, isToggled ? internalButton.x+1 : i, isToggled ? internalButton.y+1 : j, f);
		super.render(poseStack, i, j, f);
	}

}
