package de.pfannekuchen.lotas.gui.widgets;

import java.util.function.Consumer;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SmallCheckboxWidget extends AbstractPressableButtonWidget {
    private static final Identifier TEXTURE = new Identifier("textures/gui/small_checkbox.png");
    boolean checked;
    Consumer<SmallCheckboxWidget> action;

    public SmallCheckboxWidget(int x, int y, String message, boolean checked) {
        super(x, y, 11, 11, message);
        this.checked = checked;
    }

    public SmallCheckboxWidget(int x, int y, String message, boolean checked, Consumer<SmallCheckboxWidget> action) {
        super(x, y, 11, 11, message);
        this.checked = checked;
        this.action = action;
    }

    public void onPress() {
        this.checked = !this.checked;
        if (action != null) action.accept(this);
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void renderButton(int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.enableDepthTest();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        blit(this.x, this.y, 0.0F, 0.0F, 11, this.height, 11, 11);
        this.renderBg(minecraftClient, mouseX, mouseY);
        this.drawString(textRenderer, this.getMessage(), this.x + 16, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
        if (isChecked()) this.drawString(textRenderer, "x", this.x + 3, this.y + 1, 0xFFFFFF);
    }

    public void silentPress(boolean f) {
        this.checked = f;
    }
}