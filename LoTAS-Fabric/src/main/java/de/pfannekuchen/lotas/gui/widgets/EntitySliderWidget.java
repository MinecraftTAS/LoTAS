package de.pfannekuchen.lotas.gui.widgets;

import java.util.HashMap;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.mods.SpawnManipMod.EntityOptions;
import net.minecraft.client.MinecraftClient;
//#if MC>=11700
//$$ import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
//$$ import net.minecraft.client.gui.widget.PressableWidget;
//#endif
//#if MC<=11605
import net.minecraft.client.gui.widget.AbstractButtonWidget;
//#endif
//#if MC>=11601
//$$ import net.minecraft.text.LiteralText;
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.enchantment.Enchantment;
//#if MC<=11502
import net.minecraft.enchantment.InfoEnchantment;
//#endif
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;



//#if MC>=11700
//$$ public class EntitySliderWidget extends PressableWidget {
//#else
public class EntitySliderWidget extends AbstractButtonWidget {
//#endif

	public List<EntityOptions> entities;

	private float sliderPosition = 1.0F;
	public boolean isMouseDown;
	private final String name;
	private final float min;
	private final float max;
	

	public EntitySliderWidget(int xPos, int yPos, List<EntityOptions> ent, int width, int height, PressAction c) {
		//#if MC>=11601
//$$ 				super(xPos, yPos, width, height, new LiteralText(""));
		//#else
		super(xPos, yPos, width, height, "");
		//#endif
		this.name = "Entity";
		this.entities = ent;
		this.min = 0;
		this.max = ent.size() - 1;
		this.sliderPosition = -min / (max - min);
		//#if MC>=11601
//$$ 				this.setMessage(new LiteralText(ent.get(0).title));
		//#else
		this.setMessage(ent.get(0).title);
		//#endif
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
		//#if MC>=11601
//$$ 			    this.setMessage(new LiteralText(this.getDisplayString()));
		//#else
		this.setMessage(this.getDisplayString());
		//#endif
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
//$$ 	@Override
//$$ 	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//$$ 		super.renderButton(matrices, mouseX, mouseY, delta);
		//#if MC>=11700
//$$ 		MinecraftClient.getInstance().getTextureManager().bindTexture(WIDGETS_TEXTURE);
//$$ 		com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		//#else
//$$ 		MinecraftClient.getInstance().getTextureManager().bindTexture(WIDGETS_LOCATION);
//$$ 		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//#endif
//$$ 		int i = (this.isHovered() ? 2 : 1) * 20;
		//#if MC>=11601
//$$ 		this.drawTexture(matrices, this.x + (int) (this.sliderPosition * (double) (this.width - 8)), this.y, 0, 46 + i, 4, 20);
//$$ 		this.drawTexture(matrices, this.x + (int) (this.sliderPosition * (double) (this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
		//#endif
//$$ 	}
	//#else
	@Override
	protected void renderBg(MinecraftClient client, int mouseX, int mouseY) {
		client.getTextureManager().bindTexture(WIDGETS_LOCATION);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i = (this.isHovered() ? 2 : 1) * 20;
		this.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)), this.y, 0, 46 + i, 4, 20);
		this.blit(this.x + (int) (this.sliderPosition * (double) (this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
	}
	//#endif

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
				//#if MC>=11601
//$$ 					setMessage(new LiteralText(entities.get((int) Math.round(sliderPosition * (max - min) + min)).title));
				//#else
				setMessage(entities.get((int) Math.round(sliderPosition * (max - min) + min)).title);
				//#endif
			}

			//#if MC>=11700
//$$ 			com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         	//#else
         	GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
         	//#endif
			//#if MC<=11502
			blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)), this.y, 0, 66, 4, 20);
			blit(this.x + (int) (this.sliderPosition * (float) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
			//#endif
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
		//#if MC>=11601
//$$ 				this.setMessage(new LiteralText(this.getDisplayString()));
		//#else
		this.setMessage(this.getDisplayString());
		//#endif
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
			//#if MC>=11601
//$$ 			this.setMessage(new LiteralText(entities.get((int) Math.round(sliderPosition * (max - min) + min)).title));
			//#else
			this.setMessage(entities.get((int) Math.round(sliderPosition * (max - min) + min)).title);
			//#endif
			this.isMouseDown = true;
			return true;
		} else {
			return false;
		}
	}

	public LivingEntity getEntity(ServerWorld world) {
		return entities.get((int) Math.round(sliderPosition * (max - min) + min)).entity;
	}

	//#if MC>=11601
//$$ 		public ItemStack addEnchants(ItemStack item,  HashMap<Enchantment, Integer> enchs) {
//$$ 		    enchs.entrySet().forEach(entry->{
//$$ 		        item.addEnchantment(entry.getKey(), entry.getValue());
//$$ 		    });;
//$$ 			return item;
//$$ 		}
	//#else
	public ItemStack addEnchants(ItemStack item, InfoEnchantment[] enchants) {
		for (InfoEnchantment enchantmentData : enchants) {
			item.addEnchantment(enchantmentData.enchantment, enchantmentData.level);
		}
		return item;
	}
	//#endif

	//#if MC>=11700
//$$ 	/**
//$$ 	 * Fired when the mouse button is released. Equivalent of
//$$ 	 * MouseListener.mouseReleased(MouseEvent e).
//$$ 	 */
//$$ 	@Override
//$$ 	public boolean mouseReleased(double mouseX, double mouseY, int button) {
//$$ 		this.isMouseDown = false;
//$$ 		return super.mouseReleased(mouseX, mouseY, button);
//$$ 	}
//$$
//$$ 	@Override
//$$ 	public void appendNarrations(NarrationMessageBuilder builder) {
//$$ 		// TODO Auto-generated method stub
//$$
//$$ 	}
//$$
//$$ 	@Override
//$$ 	public void onPress() {
//$$ 		// TODO Auto-generated method stub
//$$
//$$ 	}
	//#endif
}