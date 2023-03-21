package de.pfannekuchen.lotas.gui.widgets;

import java.util.List;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mods.SpawnManipMod.EntityOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

/**
 * Slider which can choose between Entities instead of values
 * @author Pancake
 */
public class EntitySliderWidget extends AbstractWidget {

	public List<EntityOptions> entities;

	private float sliderPosition = 1.0F;
	public boolean isMouseDown;
	private final String name;
	private final float min;
	private final float max;
	

	public EntitySliderWidget(int xPos, int yPos, List<EntityOptions> ent, int width, int height, OnPress c) {
		//#if MC>=11601
		//#if MC>=11900
//$$ 		super(xPos, yPos, width, height, net.minecraft.network.chat.Component.empty());
		//#else
//$$ 		super(xPos, yPos, width, height, net.minecraft.network.chat.TextComponent.EMPTY);
		//#endif
		//#else
		super(xPos, yPos, width, height, "");
		//#endif
		this.name = "Entity";
		this.entities = ent;
		this.min = 0;
		this.max = ent.size() - 1;
		this.sliderPosition = -min / (max - min);
		MCVer.setMessage(this, ent.get(0).title);
	}

	/**
	 * Gets the value of the slider.
	 * 
	 * @return A value that will under normal circumstances be between the slider's
	 *         {@link #min} and {@link #max} values, unless it was manually set out
	 *         of that range.
	 */
	public float getSliderValue() {
		return this.min + (this.max - this.min) * this.sliderPosition;
	}

	/**
	 * Sets the slider's value, optionally notifying the associated
	 * {@linkplain GuiPageButtonList.GuiResponder responder} of the change.
	 */
	public void setSliderValue(float value) {
		this.sliderPosition = (value - this.min) / (this.max - this.min);
		MCVer.setMessage(this, this.getDisplayString());
	}

	/**
	 * Gets the slider's position.
	 * 
	 * @return The position of the slider, which will under normal circumstances be
	 *         between 0 and 1, unless it was manually set out of that range.
	 */
	public float getSliderPosition() {
		return this.sliderPosition;
	}

	private String getDisplayString() {
		return this.name + ": " + this.getSliderValue();
	}

	//#if MC>=11601
	//#if MC>=11904
//$$ 	@Override
//$$ 	public void renderWidget(com.mojang.blaze3d.vertex.PoseStack stack, int x, int y, float partial) {
//$$ 		Minecraft client = Minecraft.getInstance();
//$$ 		renderButton(stack, x, y, partial);
	//#else
//$$ 	@Override protected void renderBg(com.mojang.blaze3d.vertex.PoseStack stack, Minecraft client, int mouseX, int mouseY) {
	//#endif
//$$ 		MCVer.stack = stack;
	//#else
	@Override protected void renderBg(Minecraft client, int mouseX, int mouseY) {
	//#endif
		MCVer.bind(client.getTextureManager(), WIDGETS_LOCATION);
		MCVer.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//#if MC>=11800
//$$ 		int i = (this.isHovered ? 2 : 1) * 20;
		//#else
		int i = (this.isHovered() ? 2 : 1) * 20;
		//#endif
		//#if MC>=11903
//$$ 		MCVer.blit(this.getX() + (int) (this.sliderPosition * (double) (this.width - 8)), this.getY(), 0, 46 + i, 4, 20);
//$$ 		MCVer.blit(this.getX() + (int) (this.sliderPosition * (double) (this.width - 8)) + 4, this.getY(), 196, 46 + i, 4, 20);
		//#else
		MCVer.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)), this.y, 0, 46 + i, 4, 20);
		MCVer.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
		//#endif
	}
	
	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.visible) {
			if (this.isMouseDown) {
				//#if MC>=11903
//$$ 				this.sliderPosition = (float) (mouseX - (this.getX() + 4)) / (float) (this.width - 8);
				//#else
				this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
				//#endif

				if (this.sliderPosition < 0.0F) {
					this.sliderPosition = 0.0F;
				}

				if (this.sliderPosition > 1.0F) {
					this.sliderPosition = 1.0F;
				}
				MCVer.setMessage(this, entities.get((int) Math.round(sliderPosition * (max - min) + min)).title);
			}

         	MCVer.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			//#if MC>=11903
//$$ 			MCVer.blit(this.getX() + (int) (this.sliderPosition * (float) (this.width - 8)), this.getY(), 0, 66, 4, 20);
//$$ 			MCVer.blit(this.getX() + (int) (this.sliderPosition * (float) (this.width - 8)) + 4, this.getY(), 196, 66, 4, 20);
			//#else
			MCVer.blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)), this.y, 0, 66, 4, 20);
			MCVer.blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
			//#endif
		}
		return true;
	}

	//#if MC>=11904
//$$ 	@Override
//$$ 	public int getY() {
//$$ 		return super.getY();
//$$ 	}
	//#else
	@Override
	protected int getYImage(boolean isHovered) {
		return 0;
	}
	//#endif

	/**
	 * Sets the position of the slider and notifies the associated
	 * {@linkplain GuiPageButtonList.GuiResponder responder} of the change
	 */
	public void setSliderPosition(float position) {
		this.sliderPosition = position;
		MCVer.setMessage(this, this.getDisplayString());
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int delta) {
		if (super.mouseClicked(mouseX, mouseY, delta)) {
			//#if MC>=11903
//$$ 			this.sliderPosition = (float) (mouseX - (this.getX() + 4)) / (float) (this.width - 8);
			//#else
			this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
			//#endif

			if (this.sliderPosition < 0.0F) {
				this.sliderPosition = 0.0F;
			}

			if (this.sliderPosition > 1.0F) {
				this.sliderPosition = 1.0F;
			}
			MCVer.setMessage(this, entities.get((int) Math.round(sliderPosition * (max - min) + min)).title);
			this.isMouseDown = true;
			return true;
		} else {
			return false;
		}
	}

	public LivingEntity getEntity(ServerLevel world) {
		return entities.get((int) Math.round(sliderPosition * (max - min) + min)).entity;
	}

	public void updateManipList(List<EntityOptions> manipList) {
		entities=manipList;
	}

	//#if MC>=11903
//$$ 	@Override
//$$ 	protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput var1) {
//$$ 	}
	//#else
	//#if MC>=11700
//$$ 	@Override
//$$ 	public void updateNarration(net.minecraft.client.gui.narration.NarrationElementOutput narrationElementOutput) {
//$$ 	}
	//#endif
	//#endif

	//#if MC>=11904
//$$ 	public void renderButton(com.mojang.blaze3d.vertex.PoseStack poseStack, int i, int j, float f) {
//$$ 		Minecraft minecraft = Minecraft.getInstance();
//$$ 		Font font = minecraft.font;
//$$ 		com.mojang.blaze3d.systems.RenderSystem.setShader(GameRenderer::getPositionTexShader);
//$$ 		com.mojang.blaze3d.systems.RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
//$$ 		com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
//$$ 		int k = this.getYImage(this.isHoveredOrFocused());
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableBlend();
//$$ 		com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
//$$ 		com.mojang.blaze3d.systems.RenderSystem.enableDepthTest();
//$$ 		this.blit(poseStack, this.getX(), this.getY(), 0, 46 + k * 20, this.width / 2, this.height);
//$$ 		this.blit(poseStack, this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
//$$ 		int l = this.active ? 16777215 : 10526880;
//$$ 		drawCenteredString(poseStack, font, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, l | Mth.ceil(this.alpha * 255.0F) << 24);
//$$ 	}
//$$
//$$ 	protected int getYImage(boolean bl) {
//$$ 		return 0;
//$$ 	}
	//#endif
}