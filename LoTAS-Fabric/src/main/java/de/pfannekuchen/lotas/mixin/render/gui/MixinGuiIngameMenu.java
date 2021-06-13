package de.pfannekuchen.lotas.mixin.render.gui;

import java.awt.Color;
import java.time.Duration;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.core.utils.ConfigUtils;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.core.utils.Keyboard;
import de.pfannekuchen.lotas.gui.AIManipulationScreen;
import de.pfannekuchen.lotas.gui.DragonManipulationScreen;
import de.pfannekuchen.lotas.gui.DropManipulationScreen;
import de.pfannekuchen.lotas.gui.LoadstateScreen;
import de.pfannekuchen.lotas.gui.SpawnManipulationScreen;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import de.pfannekuchen.lotas.mods.DupeMod;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.DimensionType;

@Mixin(GameMenuScreen.class)
public abstract class MixinGuiIngameMenu extends Screen {
	protected MixinGuiIngameMenu(Text title) { super(title); }

	public TextFieldWidget savestateName;
	public TextFieldWidget tickrateField;
	
	 SmallCheckboxWidget fw = null; // do not pay attention
	
	@Inject(at = @At("RETURN"), method = "init")
	public void addCustomButtons(CallbackInfo ci) {
		// Move Buttons higher
		for (AbstractButtonWidget guiButton : buttons) {
			guiButton.y -= 24;
		}
		
		buttons.get(7).y += 24;
		
		boolean glitched=ConfigUtils.getBoolean("ui", "glitchedMode");
		
		boolean advanced=ConfigUtils.getBoolean("ui", "advancedMode");
		
		this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 48 + -16 +24 +24, 98, 20, "Savestate", btn -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				savestateName = new TextFieldWidget(minecraft.textRenderer, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, "");
				btn.active = false;
				setFocused(savestateName);
			} else SavestateMod.savestate(null);
		}));
    	this.addButton(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 48 + -16+24+24, 98, 20, "Loadstate", btn -> {
    		if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) minecraft.openScreen(new LoadstateScreen());
			else SavestateMod.loadstate(-1);
    	})).active = SavestateMod.hasSavestate();
        addButton(new ButtonWidget(5, 15, 48, 20, "+", b -> {
        	if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				tickrateField = new TextFieldWidget(minecraft.textRenderer, 4, 15, 103, 20, "");
				b.active = false;
				setFocused(b);
			} else {
	            TickrateChangerMod.index++;
	            TickrateChangerMod.index = MathHelper.clamp(TickrateChangerMod.index, 1, 10);
	            TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
        }));
        addButton(new ButtonWidget( 55, 15, 48, 20, "-", b -> {
        	if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				tickrateField = new TextFieldWidget(minecraft.textRenderer, 4, 15, 103, 20, "");
				b.active = false;
				setFocused(b);
			} else {
	            TickrateChangerMod.index--;
	            TickrateChangerMod.index = MathHelper.clamp(TickrateChangerMod.index, 1, 10);
	            TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
        }));
		this.addButton(new ButtonWidget((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, "Manipulate Drops", btn -> {
			this.minecraft.openScreen(new DropManipulationScreen((GameMenuScreen) (Object) this));
		}));
		
		if (advanced) {
			this.addButton(new ButtonWidget((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> {
				this.minecraft.openScreen(new DragonManipulationScreen((GameMenuScreen) (Object) this));
			})).active = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().size() >= 1;
			this.addButton(new ButtonWidget((width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, "Manipulate Spawning", btn -> {
				this.minecraft.openScreen(new SpawnManipulationScreen());
			}));

			this.addButton(new ButtonWidget((width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, "Manipulate AI", btn -> {
				this.minecraft.openScreen(new AIManipulationScreen());
			}));
		}
		if(glitched) {
			this.addButton(new ButtonWidget(5, 55, 98, 20, "Save Items",btn -> {
				DupeMod.save(minecraft);
				btn.active = false;
			}));
			this.addButton(new ButtonWidget(5, 75, 98, 20, "Load Items", btn -> {
				DupeMod.load(minecraft);
				btn.active = false;
			}));
		}
        if(advanced) {
	        addButton(new ButtonWidget(37, 115, 66, 20, "Jump ticks", btn -> {
				TickrateChangerMod.ticksToJump = (int) TickrateChangerMod.ticks[TickrateChangerMod.ji];
				btn.active = false;
				btn.setMessage("Jumping...");
	        }));
        
			addButton(new ButtonWidget(5, 115, 30, 20, TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t", btn -> {
				TickrateChangerMod.ji++;
				if (TickrateChangerMod.ji > 10)
					TickrateChangerMod.ji = 1;
				buttons.clear();
				init();
			}));
        }
        if(advanced&&glitched) {
	        addButton(new SmallCheckboxWidget(2, height - 20 - 15, "Avoid taking damage", !ConfigUtils.getBoolean("tools", "takeDamage"), b -> {
	            ConfigUtils.setBoolean("tools", "takeDamage", !b.isChecked());
	            ConfigUtils.save();
	        }));
        }
       
        if(advanced) {
	        final SmallCheckboxWidget tw = addButton(new SmallCheckboxWidget(2, height - 32 - 15, "Drop towards me", ConfigUtils.getBoolean("tools", "manipulateVelocityTowards"), b -> {
	            ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", b.isChecked());
	            if (b.isChecked()) {
	                ConfigUtils.setBoolean("tools", "manipulateVelocityAway", false);
	                fw.silentPress(false);
	            }
	            ConfigUtils.save();
	        }));
	        fw = addButton(new SmallCheckboxWidget(2, height - 44 - 15, "Drop away from me", ConfigUtils.getBoolean("tools", "manipulateVelocityAway"), b -> {
	            ConfigUtils.setBoolean("tools", "manipulateVelocityAway", b.isChecked());
	            if (b.isChecked()) {
	                ConfigUtils.setBoolean("tools", "manipulateVelocityTowards", false);
	                tw.silentPress(false);
	            }
	            ConfigUtils.save();
	        }));
	        addButton(new SmallCheckboxWidget(2, height - 56 - 15 , "Optimize Explosions", ConfigUtils.getBoolean("tools", "manipulateExplosionDropChance"), b -> {
	            ConfigUtils.setBoolean("tools", "manipulateExplosionDropChance", b.isChecked());
	            ConfigUtils.save();
	        }));
	        addButton(new SmallCheckboxWidget(2, height - 68 - 15 , "Right Auto Clicker", ConfigUtils.getBoolean("tools", "rAutoClicker"), b -> {
	            ConfigUtils.setBoolean("tools", "rAutoClicker", b.isChecked());
	            ConfigUtils.save();
	        }));
        }
		addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 144 + -16, 204, 20, "Reset Timer", btn -> {
			Timer.ticks = -1;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
		}));
        
	}
	
	@Inject(method = "render", at = @At("TAIL"))
	public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		
		boolean glitched=ConfigUtils.getBoolean("ui", "glitchedMode");
		
		boolean advanced=ConfigUtils.getBoolean("ui", "advancedMode");
		
		if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			this.buttons.get(8).setMessage("\u00A76Name Savestate");
			this.buttons.get(9).setMessage("\u00A76Choose State");
			this.buttons.get(10).setMessage("\u00A76Custom");
			this.buttons.get(11).setMessage("\u00A76Tickrate");
		} else {
			this.buttons.get(8).setMessage("Savestate");
			this.buttons.get(9).setMessage("Loadstate");
			this.buttons.get(10).setMessage("+");
			this.buttons.get(11).setMessage("-");
		}
		if(advanced) drawCenteredString(minecraft.textRenderer, "Hold Shift to access more features", width / 2, this.height / 4 + 152, 0xFFFFFF);
	
		if (savestateName != null) savestateName.render(mouseX, mouseX, partialTicks);
		if (tickrateField != null) tickrateField.render(mouseX, mouseX, partialTicks);
		
		if (SavestateMod.showSavestateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showSavestateDone = false;
				return;
			}
			drawCenteredString(minecraft.textRenderer, "\u00A76Savestate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		} else if (SavestateMod.showLoadstateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showLoadstateDone = false;
				return;
			}
			drawCenteredString(minecraft.textRenderer, "\u00A76Loadstate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
		}
		
		drawString(minecraft.textRenderer, "Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);
		
		if (advanced) {
			int i=glitched? 18 : 16;
			minecraft.textRenderer.drawWithShadow("Tickjump", 10, 105, 0xFFFFFF);
			if (buttons.get(i).active == false) {
				minecraft.textRenderer.drawWithShadow("Tickjump is ready,", 8, 137, 0xFFFFFF);
				minecraft.textRenderer.drawWithShadow("press ESC to continue", 8, 147, 0xFFFFFF);
			}
		}
		if (glitched) minecraft.textRenderer.drawWithShadow("Duping", 10, 45, 0xFFFFFF);
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
