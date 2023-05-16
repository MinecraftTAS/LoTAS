package de.pfannekuchen.lotas.gui.widgets;

import java.util.function.Consumer;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Small Checkbox Widget from 1.12.2
 * @author Pancake
 */
public class SmallCheckboxWidget extends AbstractButton {
	
	//#if MC>=11903
//$$ 	private static final ResourceLocation TEXTURE = new ResourceLocation("lotas", "gui/small_checkbox.png");
	//#else
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/small_checkbox.png");
	//#endif
	private boolean checked;
	Consumer<SmallCheckboxWidget> action;

	public SmallCheckboxWidget(int x, int y, String message, boolean checked) {
		//#if MC>=11601
//$$ 		super(x, y, 11, 11, MCVer.literal(message));
		//#else
		super(x, y, 11, 11, message);
		//#endif
		this.checked = checked;
	}

	public SmallCheckboxWidget(int x, int y, String message, boolean checked, Consumer<SmallCheckboxWidget> action) {
		this(x, y, message, checked);
		this.action = action;
	}

	@Override
	public void onPress() {
		this.checked = !this.checked;
		if (action != null)
			action.accept(this);
	}

	public boolean isChecked() {
		return this.checked;
	}
	
	//#if MC>=11904
//$$ 	@Override
	//#if MC>=12000
//$$ 	protected void renderScrollingString(net.minecraft.client.gui.GuiGraphics poseStack, Font font, int i, int j) {
	//#else
//$$ 	protected void renderScrollingString(com.mojang.blaze3d.vertex.PoseStack poseStack, Font font, int i, int j) {
	//#endif
//$$ 	}
	//#endif
	
	//#if MC>=11903
	//#if MC>=11904
//$$ 	@Override
	//#if MC>=12000
//$$ 	public void render(net.minecraft.client.gui.GuiGraphics stack, int mouseX, int mouseY, float delta) {
	//#else
//$$ 	public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
	//#endif
//$$ //		super.render(stack, mouseX, mouseY, delta);
	//#else
//$$ 	@Override public void renderButton(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
	//#endif
//$$ 		MCVer.stack = stack;
//$$ 		Minecraft minecraftClient = Minecraft.getInstance();
//$$ 		MCVer.bind(minecraftClient.getTextureManager(), TEXTURE);
//$$ 		MCVer.blit(this.getX(), this.getY(), 0.0F, 0.0F, 11, this.height, 11, 11);
		//#if MC<11904
//$$ 		this.renderBg(MCVer.stack, minecraftClient, mouseX, mouseY);
		//#endif
//$$ 		MCVer.drawShadow(this.getMessage().getString(), this.getX() + 16, this.getY() + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
//$$ 		if (isChecked()) {
//$$ 			MCVer.disableDepthTest();
//$$ 			MCVer.drawShadow("x", this.getX() + 3, this.getY() + 1, 0xFFFFFF);
//$$ 		}
//$$ 	}
	//#else
	//#if MC>=11601
//$$ 	@Override public void renderButton(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void renderButton(int mouseX, int mouseY, float delta) {
	//#endif
		Minecraft minecraftClient = Minecraft.getInstance();
		MCVer.bind(minecraftClient.getTextureManager(), TEXTURE);
		MCVer.blit(this.x, this.y, 0.0F, 0.0F, 11, this.height, 11, 11);
		//#if MC>=11601
//$$ 		this.renderBg(MCVer.stack, minecraftClient, mouseX, mouseY);
//$$ 		MCVer.drawShadow(this.getMessage().getString(), this.x + 16, this.y + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
		//#else
		this.renderBg(minecraftClient, mouseX, mouseY);
		drawString(minecraftClient.font, this.getMessage(), this.x + 16, this.y + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
		//#endif
		if (isChecked()) {
			MCVer.disableDepthTest();
			MCVer.drawShadow("x", this.x + 3, this.y + 1, 0xFFFFFF);
		}
	}
	//#endif

	public void silentPress(boolean f) {
		this.checked = f;
	}

	//#if MC>=11903
//$$ 	@Override
//$$ 	protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput var1) {
//$$
//$$ 	}
	//#else
	//#if MC>=11700
//$$ 	@Override
//$$ 	public void updateNarration(net.minecraft.client.gui.narration.NarrationElementOutput narrationElementOutput) {
//$$
//$$ 	}
	//#endif
	//#endif
	
}