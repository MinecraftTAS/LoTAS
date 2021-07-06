package de.pfannekuchen.lotas.mixin.render.gui;

import java.awt.Color;
import java.time.Duration;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.base.Predicates;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.core.utils.Keyboard;
import de.pfannekuchen.lotas.gui.AIManipulationScreen;
import de.pfannekuchen.lotas.gui.DragonManipulationScreen;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.LoadstateScreen;
import de.pfannekuchen.lotas.gui.SpawnManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Mixin(GameMenuScreen.class)
public abstract class MixinGuiIngameMenu extends Screen {
	protected MixinGuiIngameMenu(Text title) {
		super(title);
	}

	public TextFieldWidget savestateName;
	public TextFieldWidget tickrateField;

	SmallCheckboxWidget fw = null; // do not pay attention

	@Inject(at = @At("RETURN"), method = "init")
	public void addCustomButtons(CallbackInfo ci) {
		// Move Buttons higher
		//#if MC>=11700
//$$ 	for (net.minecraft.client.gui.Drawable drawable : drawables)
//$$ 		((ButtonWidget) drawable).y -= 24;
		//#else 
		for (net.minecraft.client.gui.widget.AbstractButtonWidget guiButton : buttons) 
			guiButton.y -= 24;
		//#endif 
		
		//#if MC>=11700
//$$ 		((ButtonWidget)drawables.get(7)).y += 24;
		//#else 
		buttons.get(7).y += 24;
		//#endif 
		
		MCVer.addButton(this, new NewButtonWidget(this.width / 2 - 102, this.height / 4 + 48 + -16 + 24 + 24, 98, 20, "Savestate", btn -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				//#if MC>=11601
//$$ 							    savestateName = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, LiteralText.EMPTY);
				//#else
				savestateName = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, "");
				//#endif
				btn.active = false;
				setFocused(savestateName);
			} else
				SavestateMod.savestate(null);
		}));
		((NewButtonWidget) MCVer.addButton(this, new NewButtonWidget(this.width / 2 + 4, this.height / 4 + 48 + -16 + 24 + 24, 98, 20, "Loadstate", btn -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
				MinecraftClient.getInstance().openScreen(new LoadstateScreen());
			else
				SavestateMod.loadstate(-1);
		}))).active = SavestateMod.hasSavestate();
		MCVer.addButton(this, new NewButtonWidget(5, 15, 48, 20, "+", b -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				//#if MC>=11601
//$$ 					    tickrateField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 4, 15, 103, 20, LiteralText.EMPTY);
				//#else
				tickrateField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 4, 15, 103, 20, "");
				//#endif
				b.active = false;
				setFocused(b);
			} else {
				TickrateChangerMod.index++;
				TickrateChangerMod.index = MathHelper.clamp(TickrateChangerMod.index, 1, 10);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		}));
		MCVer.addButton(this, new NewButtonWidget(55, 15, 48, 20, "-", b -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				//#if MC>=11601
//$$ 					    tickrateField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 4, 15, 103, 20, LiteralText.EMPTY);
				//#else
				tickrateField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 4, 15, 103, 20, "");
				//#endif
				b.active = false;
				setFocused(b);
			} else {
				TickrateChangerMod.index--;
				TickrateChangerMod.index = MathHelper.clamp(TickrateChangerMod.index, 1, 10);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
		}));
		MCVer.addButton(this, new NewButtonWidget((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, "Manipulate Drops", btn -> {
			MinecraftClient.getInstance().openScreen(new DropManipulationScreen((GameMenuScreen) (Object) this));
		}));

		//#if MC>=11601
//$$ 		((NewButtonWidget) MCVer.addButton(this, new NewButtonWidget((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> {
//$$ 			MinecraftClient.getInstance().openScreen(new DragonManipulationScreen((GameMenuScreen) (Object) this));
//$$ 		}))).active = MinecraftClient.getInstance().getServer().getWorld(World.END).getAliveEnderDragons().size() >= 1;
		//#else
		((NewButtonWidget) MCVer.addButton(this, new NewButtonWidget((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> {
			MinecraftClient.getInstance().openScreen(new DragonManipulationScreen((GameMenuScreen) (Object) this));
		}))).active = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().size() >= 1;
		//#endif
		MCVer.addButton(this, new NewButtonWidget((width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, "Manipulate Spawning", btn -> {
			MinecraftClient.getInstance().openScreen(new SpawnManipulationScreen());
		}));
		MCVer.addButton(this, new NewButtonWidget((width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, "Manipulate AI", btn -> {
			MinecraftClient.getInstance().openScreen(new AIManipulationScreen());
		})).active=!MinecraftClient.getInstance().getServer().getWorld(MinecraftClient.getInstance().player.dimension).getEntities(MobEntity.class, MinecraftClient.getInstance().player.getBoundingBox().expand(64, 64, 64), Predicates.alwaysTrue()).isEmpty();
		MCVer.addButton(this, new NewButtonWidget(5, 55, 98, 20, "Save Items", btn -> {
			DupeMod.save(MinecraftClient.getInstance());
			btn.active = false;
		}));
		MCVer.addButton(this, new NewButtonWidget(5, 75, 98, 20, "Load Items", btn -> {
			DupeMod.load(MinecraftClient.getInstance());
			btn.active = false;
		}));
		MCVer.addButton(this, new NewButtonWidget(37, 115, 66, 20, "Jump ticks", btn -> {
			TickrateChangerMod.ticksToJump = (int) TickrateChangerMod.ticks[TickrateChangerMod.ji];
			btn.active = false;
			//#if MC>=11601
//$$ 								btn.setMessage(new LiteralText("Jumping..."));
			//#else
			btn.setMessage("Jumping...");
			//#endif
		}));

		MCVer.addButton(this, new NewButtonWidget(5, 115, 30, 20, TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t", btn -> {
			TickrateChangerMod.ji++;
			if (TickrateChangerMod.ji > 10)
				TickrateChangerMod.ji = 1;
			//#if MC>=11700
//$$ 			clearChildren();
			//#else
			buttons.clear();
			//#endif
			init();
		}));
		MCVer.addButton(this, new SmallCheckboxWidget(2, height - 20 - 15, "Avoid taking damage", !ConfigUtils.getBoolean("tools", "takeDamage"), b -> {
			ConfigUtils.setBoolean("tools", "takeDamage", !b.isChecked());
			ConfigUtils.save();
		}));

		final SmallCheckboxWidget tw = (SmallCheckboxWidget) MCVer.addButton(this, new SmallCheckboxWidget(2, height - 32 - 15, "Drop towards me", ConfigUtils.getBoolean("tools", "manipulateVelocityTowards"), b -> {
			ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", b.isChecked());
			if (b.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityAway", false);
				fw.silentPress(false);
			}
			ConfigUtils.save();
		}));
		fw = (SmallCheckboxWidget) MCVer.addButton(this, new SmallCheckboxWidget(2, height - 44 - 15, "Drop away from me", ConfigUtils.getBoolean("tools", "manipulateVelocityAway"), b -> {
			ConfigUtils.setBoolean("tools", "manipulateVelocityAway", b.isChecked());
			if (b.isChecked()) {
				ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", false);
				tw.silentPress(false);
			}
			ConfigUtils.save();
		}));
		MCVer.addButton(this, new SmallCheckboxWidget(2, height - 56 - 15, "Optimize Explosions", ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance"), b -> {
			ConfigUtils.setBoolean("tools", "manipulateExplosionDropChance", b.isChecked());
			ConfigUtils.save();
		}));
		MCVer.addButton(this, new SmallCheckboxWidget(2, height - 68 - 15, "Right Auto Clicker", ConfigUtils.getBoolean("tools", "rAutoClicker"), b -> {
			ConfigUtils.setBoolean("tools", "rAutoClicker", b.isChecked());
			ConfigUtils.save();
		}));
		MCVer.addButton(this, new NewButtonWidget(this.width / 2 - 102, this.height / 4 + 144 + -16, 204, 20, "Reset Timer", btn -> {
			Timer.ticks = -1;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
		}));

	}

	public ButtonWidget getButton(int index) {
		//#if MC>=11700
//$$ 		return ((ButtonWidget) drawables.get(index));
		//#else
		return (ButtonWidget) this.buttons.get(index);
		//#endif
	}
	
	@Inject(method = "render", at = @At("TAIL"))
	//#if MC>=11601
//$$ 		public void drawScreen(net.minecraft.client.util.math.MatrixStack matrices, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
//$$ 			MCVer.matrixStack = matrices;
	//#else
	public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		//#endif
		if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			MCVer.setMessage(getButton(8), "\u00A76Name Savestate");
			MCVer.setMessage(getButton(9), "\u00A76Choose State");
			MCVer.setMessage(getButton(10),"\u00A76Custom");
			MCVer.setMessage(getButton(11),"\u00A76Tickrate");
		} else {
			MCVer.setMessage(getButton(8), "Savestate");
			MCVer.setMessage(getButton(9),"Loadstate");
			MCVer.setMessage(getButton(10),"+");
			MCVer.setMessage(getButton(11),"-");
		}
		MCVer.drawCenteredString(this, "Hold Shift to access more features", width / 2, this.height / 4 + 152, 0xFFFFFF);
		if (savestateName != null) MCVer.render(savestateName, mouseX, mouseX, partialTicks);
		if (tickrateField != null) MCVer.render(tickrateField, mouseX, mouseX, partialTicks);

		if (SavestateMod.showSavestateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showSavestateDone = false;
				MCVer.getButton(this, 9).active=SavestateMod.hasSavestate();
				return;
			}
			MCVer.drawCenteredString(this, "\u00A76Savestate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		} else if (SavestateMod.showLoadstateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showLoadstateDone = false;
				return;
			}
			MCVer.drawCenteredString(this, "\u00A76Loadstate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		}
		MCVer.drawStringWithShadow("Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);
		int i = 18;
		MCVer.drawStringWithShadow("Tickjump", 10, 105, 0xFFFFFF);
		if (getButton(i).active == false) {
			MCVer.drawStringWithShadow("Tickjump is ready,", 8, 137, 0xFFFFFF);
			MCVer.drawStringWithShadow("press ESC to continue", 8, 147, 0xFFFFFF);
		}

		MCVer.drawStringWithShadow("Duping", 10, 45, 0xFFFFFF);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (savestateName != null)
			savestateName.mouseClicked(mouseX, mouseY, mouseButton);
		if (tickrateField != null)
			tickrateField.mouseClicked(mouseX, mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (savestateName != null) {
			savestateName.keyPressed(keyCode, scanCode, modifiers);
			boolean focused = savestateName.isFocused();
			if (keyCode == GLFW.GLFW_KEY_ENTER && focused) {
				if (savestateName.getText().isEmpty())
					SavestateMod.savestate(null);
				else
					SavestateMod.savestate(savestateName.getText());
			}
		}
		if (tickrateField != null) {
			tickrateField.keyPressed(keyCode, scanCode, modifiers);
			boolean focused = tickrateField.isFocused();
			if (keyCode == GLFW.GLFW_KEY_ENTER && focused) {
				if (!tickrateField.getText().isEmpty())
					TickrateChangerMod.updateTickrate(Float.parseFloat(tickrateField.getText()));
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (savestateName != null)
			savestateName.charTyped(typedChar, keyCode);
		if (tickrateField != null)
			tickrateField.charTyped(typedChar, keyCode);
		return super.charTyped(typedChar, keyCode);
	}

}
