package de.pfannkuchen.lotas.gui.api;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * LoScreen is an abstract class representing a second screen with a custom api rendered after the normal gui (Short: interactable overlay). 
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public abstract class LoScreen {

	public Minecraft mc;
	public abstract void update(double width, double height);
	public abstract void render(PoseStack stack, double curX, double curY);
	public abstract void drag(double prevCurX, double prevCurY, double curX, double curY);
	public abstract void click(double curX, double curY, int button);
	
}
