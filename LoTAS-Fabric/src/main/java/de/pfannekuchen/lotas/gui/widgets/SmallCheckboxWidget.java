package de.pfannekuchen.lotas.gui.widgets;

import java.util.function.Consumer;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SmallCheckboxWidget extends AbstractPressableButtonWidget {
	private static final Identifier TEXTURE = new Identifier("textures/gui/small_checkbox.png");
	boolean checked;
	Consumer<SmallCheckboxWidget> action;

	public SmallCheckboxWidget(int x, int y, String message, boolean checked) {
		//#if MC>=11601
//$$ 		super(x, y, 11, 11, new LiteralText(message));
		//#else
		super(x, y, 11, 11, message);
		//#endif
		this.checked = checked;
	}

	public SmallCheckboxWidget(int x, int y, String message, boolean checked, Consumer<SmallCheckboxWidget> action) {
		//#if MC>=11601
//$$ 		super(x, y, 11, 11, new LiteralText(message));
		//#else
		super(x, y, 11, 11, message);
		//#endif
		this.checked = checked;
		this.action = action;
	}

	public void onPress() {
		this.checked = !this.checked;
		if (action != null)
			action.accept(this);
	}

	public boolean isChecked() {
		return this.checked;
	}

	//#if MC>=11601
//$$ 	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
	//#else
	public void renderButton(int mouseX, int mouseY, float delta) {
		//#endif
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		minecraftClient.getTextureManager().bindTexture(TEXTURE);
		GlStateManager.enableDepthTest();
		TextRenderer textRenderer = minecraftClient.textRenderer;
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		GlStateManager.enableBlend();
		//#if MC>=11502
		//#if MC>=11601
//$$ 			GlStateManager.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.field_22545, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.field_22528, GlStateManager.SrcFactor.ONE.field_22545, GlStateManager.DstFactor.ZERO.field_22528);
//$$ 			GlStateManager.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA.field_22545, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.field_22528);
		//#else
//$$ 		GlStateManager.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SrcFactor.ONE.value, GlStateManager.DstFactor.ZERO.value);
//$$ 		GlStateManager.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value);
		//#endif
		//#else
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		//#endif
		//#if MC>=11601
//$$ 			drawTexture(matrices, this.x, this.y, 0.0F, 0.0F, 11, this.height, 11, 11);
//$$ 			this.renderBg(matrices, minecraftClient, mouseX, mouseY);
//$$ 			this.drawStringWithShadow(matrices, textRenderer, this.getMessage().getString(), this.x + 16, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
//$$ 			if (isChecked()) this.drawStringWithShadow(matrices, textRenderer, "x", this.x + 3, this.y + 1, 0xFFFFFF);
		//#else
		blit(this.x, this.y, 0.0F, 0.0F, 11, this.height, 11, 11);
		this.renderBg(minecraftClient, mouseX, mouseY);
		this.drawString(textRenderer, this.getMessage(), this.x + 16, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
		if (isChecked())
			this.drawString(textRenderer, "x", this.x + 3, this.y + 1, 0xFFFFFF);
		//#endif
	}

	public void silentPress(boolean f) {
		this.checked = f;
	}
}