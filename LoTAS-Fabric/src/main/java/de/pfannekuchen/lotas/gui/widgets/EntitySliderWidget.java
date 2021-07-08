package de.pfannekuchen.lotas.gui.widgets;

import java.util.List;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.mods.SpawnManipMod.EntityOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.server.level.ServerLevel;
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
		//#if MC>=11600
//$$ 		super(xPos, yPos, width, height, net.minecraft.network.chat.TextComponent.EMPTY);
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

	//#if MC>=11600
//$$ 	@Override protected void renderBg(com.mojang.blaze3d.vertex.PoseStack stack, Minecraft client, int mouseX, int mouseY) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override protected void renderBg(Minecraft client, int mouseX, int mouseY) {
	//#endif
		client.getTextureManager().bind(WIDGETS_LOCATION);
		MCVer.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i = (this.isHovered() ? 2 : 1) * 20;
		MCVer.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)), this.y, 0, 46 + i, 4, 20);
		MCVer.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.visible) {
			if (this.isMouseDown) {
				this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);

				if (this.sliderPosition < 0.0F) {
					this.sliderPosition = 0.0F;
				}

				if (this.sliderPosition > 1.0F) {
					this.sliderPosition = 1.0F;
				}
				MCVer.setMessage(this, entities.get((int) Math.round(sliderPosition * (max - min) + min)).title);
			}

         	MCVer.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			MCVer.blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)), this.y, 0, 66, 4, 20);
			MCVer.blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
		}
		return true;
	}

	@Override
	protected int getYImage(boolean isHovered) {
		return 0;
	}

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
			this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);

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

	public ItemStack addEnchants(ItemStack item, EnchantmentInstance[] enchants) {
		for (EnchantmentInstance enchantmentData : enchants) {
			item.enchant(enchantmentData.enchantment, enchantmentData.level);
		}
		return item;
	}
}