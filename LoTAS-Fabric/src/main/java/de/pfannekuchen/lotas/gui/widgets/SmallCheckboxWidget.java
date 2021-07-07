package de.pfannekuchen.lotas.gui.widgets;

import java.util.function.Consumer;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SmallCheckboxWidget extends AbstractButton {
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/small_checkbox.png");
	boolean checked;
	Consumer<SmallCheckboxWidget> action;

	public SmallCheckboxWidget(int x, int y, String message, boolean checked) {
		//#if MC>=11600
//$$ 		super(x, y, 11, 11, new net.minecraft.network.chat.TextComponent(message));
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
	
	//#if MC>=11600
//$$ 	@Override public void renderButton(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void renderButton(int mouseX, int mouseY, float delta) {
	//#endif
		Minecraft minecraftClient = Minecraft.getInstance();
		minecraftClient.getTextureManager().bind(TEXTURE);
		MCVer.blit(this.x, this.y, 0.0F, 0.0F, 11, this.height, 11, 11);
		//#if MC>=11600
//$$ 		this.renderBg(MCVer.stack, minecraftClient, mouseX, mouseY);
//$$ 		MCVer.drawShadow(this.getMessage().getString(), this.x + 16, this.y + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
		//#else
		this.renderBg(minecraftClient, mouseX, mouseY);
		drawString(Minecraft.getInstance().font, this.getMessage(), this.x + 16, this.y + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
		//#endif
		if (isChecked()) {
			MCVer.disableDepthTest();
			MCVer.drawShadow("x", this.x + 3, this.y + 1, 0xFFFFFF);
		}
	}

	public void silentPress(boolean f) {
		this.checked = f;
	}
}