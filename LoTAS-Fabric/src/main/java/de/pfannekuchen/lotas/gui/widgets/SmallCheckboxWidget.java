package de.pfannekuchen.lotas.gui.widgets;

import java.util.function.Consumer;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
//#if MC>=11700
//$$ import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
//#endif
//#if MC<=11605
import net.minecraft.client.gui.widget.AbstractButtonWidget;
//#else
//$$ import net.minecraft.client.gui.widget.PressableWidget;
//#endif
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

//#if MC>=11700
//$$ public class SmallCheckboxWidget extends PressableWidget {
//#else
public class SmallCheckboxWidget extends AbstractButtonWidget {
//#endif
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
		//#if MC>=11700
//$$ 		GlStateManager._enableDepthTest();
//$$ 		TextRenderer textRenderer = minecraftClient.textRenderer;
//$$ 		com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
//$$ 		GlStateManager._enableBlend();
		//#else
		GlStateManager.enableDepthTest();
		TextRenderer textRenderer = minecraftClient.textRenderer;
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		GlStateManager.enableBlend();
		//#endif
		
		//#if MC>=11502
		//#if MC>=11601
		//#if MC>=11700
//$$ 			GlStateManager._blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SrcFactor.ONE.value, GlStateManager.DstFactor.ZERO.value);
//$$ 			GlStateManager._blendFunc(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value);
		//#else
//$$ 			GlStateManager.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.field_22545, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.field_22528, GlStateManager.SrcFactor.ONE.field_22545, GlStateManager.DstFactor.ZERO.field_22528);
//$$ 			GlStateManager.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA.field_22545, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.field_22528);
		//#endif
		//#else
//$$ 		GlStateManager.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SrcFactor.ONE.value, GlStateManager.DstFactor.ZERO.value);
//$$ 		GlStateManager.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value);
		//#endif
		//#else
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		//#endif
		//#if MC>=11601
		//#if MC>=11700
//$$ 			drawTexture(matrices, this.x, this.y, 0.0F, 0.0F, 11, this.height, 11, 11);
//$$ 			this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
//$$ 			this.drawStringWithShadow(matrices, textRenderer, this.getMessage().getString(), this.x + 16, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
//$$ 			if (isChecked()) this.drawStringWithShadow(matrices, textRenderer, "x", this.x + 3, this.y + 1, 0xFFFFFF);
		//#else
//$$ 			drawTexture(matrices, this.x, this.y, 0.0F, 0.0F, 11, this.height, 11, 11);
//$$ 			this.renderBg(matrices, minecraftClient, mouseX, mouseY);
//$$ 			this.drawStringWithShadow(matrices, textRenderer, this.getMessage().getString(), this.x + 16, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
//$$ 			if (isChecked()) this.drawStringWithShadow(matrices, textRenderer, "x", this.x + 3, this.y + 1, 0xFFFFFF);
		//#endif
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
	//#if MC>=11700
//$$ 	@Override
//$$ 	public void appendNarrations(NarrationMessageBuilder builder) {
//$$ 		// TODO Auto-generated method stub
//$$
//$$ 	}
	//#endif
}