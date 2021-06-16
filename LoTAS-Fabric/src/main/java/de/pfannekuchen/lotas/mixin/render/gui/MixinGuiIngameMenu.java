package de.pfannekuchen.lotas.mixin.render.gui;

import java.awt.Color;
import java.time.Duration;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.realmsclient.dto.RealmsServer.WorldType;

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
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//$$ import net.minecraft.util.registry.RegistryKey;
//#endif
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
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
		
		this.addButton(new NewButtonWidget(this.width / 2 - 102, this.height / 4 + 48 + -16 +24 +24, 98, 20, "Savestate", btn -> {
			if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			    //#if MC>=11601
//$$ 			    savestateName = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, LiteralText.EMPTY);
                //#else
                savestateName = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20, "");
                //#endif
				btn.active = false;
				setFocused(savestateName);
			} else SavestateMod.savestate(null);
		}));
    	this.addButton(new NewButtonWidget(this.width / 2 + 4, this.height / 4 + 48 + -16+24+24, 98, 20, "Loadstate", btn -> {
    		if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) MinecraftClient.getInstance().openScreen(new LoadstateScreen());
			else SavestateMod.loadstate(-1);
    	})).active = SavestateMod.hasSavestate();
        addButton(new NewButtonWidget(5, 15, 48, 20, "+", b -> {
        	if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
        	    //#if MC>=11601
        //$$ 	    tickrateField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 4, 15, 103, 20, LiteralText.EMPTY);
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
        addButton(new NewButtonWidget( 55, 15, 48, 20, "-", b -> {
        	if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
        	    //#if MC>=11601
        //$$ 	    tickrateField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 4, 15, 103, 20, LiteralText.EMPTY);
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
		this.addButton(new NewButtonWidget((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, "Manipulate Drops", btn -> {
			MinecraftClient.getInstance().openScreen(new DropManipulationScreen((GameMenuScreen) (Object) this));
		}));
		
		if (advanced) {
		    //#if MC>=11601
//$$             this.addButton(new NewButtonWidget((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> {
//$$                 MinecraftClient.getInstance().openScreen(new DragonManipulationScreen((GameMenuScreen) (Object) this));
//$$             })).active = MinecraftClient.getInstance().getServer().getWorld(World.END).getAliveEnderDragons().size() >= 1;
            //#else
                       this.addButton(new NewButtonWidget((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> { MinecraftClient.getInstance().openScreen(new DragonManipulationScreen((GameMenuScreen) (Object) this)); })).active = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().size() >= 1;
            //#endif
			this.addButton(new NewButtonWidget((width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, "Manipulate Spawning", btn -> {
			    MinecraftClient.getInstance().openScreen(new SpawnManipulationScreen());
			}));

			this.addButton(new NewButtonWidget((width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, "Manipulate AI", btn -> {
			    MinecraftClient.getInstance().openScreen(new AIManipulationScreen());
			}));
		}
		if(glitched) {
			this.addButton(new NewButtonWidget(5, 55, 98, 20, "Save Items",btn -> {
				DupeMod.save(MinecraftClient.getInstance());
				btn.active = false;
			}));
			this.addButton(new NewButtonWidget(5, 75, 98, 20, "Load Items", btn -> {
				DupeMod.load(MinecraftClient.getInstance());
				btn.active = false;
			}));
		}
        if(advanced) {
	        addButton(new NewButtonWidget(37, 115, 66, 20, "Jump ticks", btn -> {
				TickrateChangerMod.ticksToJump = (int) TickrateChangerMod.ticks[TickrateChangerMod.ji];
				btn.active = false;
				//#if MC>=11601
//$$ 				btn.setMessage(new LiteralText("Jumping..."));
                //#else
                btn.setMessage("Jumping...");
                //#endif
	        }));
        
			addButton(new NewButtonWidget(5, 115, 30, 20, TickrateChangerMod.ticks[TickrateChangerMod.ji] + "t", btn -> {
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
		addButton(new NewButtonWidget(this.width / 2 - 102, this.height / 4 + 144 + -16, 204, 20, "Reset Timer", btn -> {
			Timer.ticks = -1;
			Timer.startTime = Duration.ofMillis(System.currentTimeMillis());
		}));
        
	}
	
	@Inject(method = "render", at = @At("TAIL")) 
	//#if MC>=11601
//$$ 	public void drawScreen(MatrixStack matrices, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
    //#else
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
    //#endif
		boolean glitched=ConfigUtils.getBoolean("ui", "glitchedMode");
		
		boolean advanced=ConfigUtils.getBoolean("ui", "advancedMode");
		
		//#if MC>=11601
//$$         if (Keyboard.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
//$$             this.buttons.get(8).setMessage(new LiteralText("\u00A76Name Savestate"));
//$$             this.buttons.get(9).setMessage(new LiteralText("\u00A76Choose State"));
//$$             this.buttons.get(10).setMessage(new LiteralText("\u00A76Custom"));
//$$             this.buttons.get(11).setMessage(new LiteralText("\u00A76Tickrate"));
//$$         } else {
//$$             this.buttons.get(8).setMessage(new LiteralText("Savestate"));
//$$             this.buttons.get(9).setMessage(new LiteralText("Loadstate"));
//$$             this.buttons.get(10).setMessage(new LiteralText("+"));
//$$             this.buttons.get(11).setMessage(new LiteralText("-"));
//$$         }
//$$         if(advanced) drawCenteredString(matrices, MinecraftClient.getInstance().textRenderer, "Hold Shift to access more features", width / 2, this.height / 4 + 152, 0xFFFFFF);
//$$         if (savestateName != null) savestateName.render(matrices, mouseX, mouseX, partialTicks);
//$$         if (tickrateField != null) tickrateField.render(matrices, mouseX, mouseX, partialTicks);
        //#else
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
       if(advanced) drawCenteredString(MinecraftClient.getInstance().textRenderer, "Hold Shift to access more features", width / 2, this.height / 4 + 152, 0xFFFFFF);
       if (savestateName != null) savestateName.render(mouseX, mouseX, partialTicks);
       if (tickrateField != null) tickrateField.render(mouseX, mouseX, partialTicks);
        //#endif
		
		if (SavestateMod.showSavestateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showSavestateDone = false;
				return;
			}
			//#if MC>=11601
//$$ 			drawCenteredString(matrices, MinecraftClient.getInstance().textRenderer, "\u00A76Savestate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
            //#else
            drawCenteredString(MinecraftClient.getInstance().textRenderer, "\u00A76Savestate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
            //#endif
		} else if (SavestateMod.showLoadstateDone) {
			long timeSince = System.currentTimeMillis() - SavestateMod.timeTitle;
			if (timeSince >= 1800) {
				SavestateMod.showLoadstateDone = false;
				return;
			}
			//#if MC>=11601
//$$ 			drawCenteredString(matrices, MinecraftClient.getInstance().textRenderer, "\u00A76Loadstate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
            //#else
            drawCenteredString(MinecraftClient.getInstance().textRenderer, "\u00A76Loadstate successful...", width / 2, 20, new Color(1F, 1F, 1F, 1F - (timeSince / 2000F)).getRGB());
            //#endif
		}
		//#if MC>=11601
//$$         drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, "Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);
//$$         if (advanced) {
//$$             int i=glitched? 18 : 16;
//$$             MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "Tickjump", 10, 105, 0xFFFFFF);
//$$             if (buttons.get(i).active == false) {
//$$                 MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "Tickjump is ready,", 8, 137, 0xFFFFFF);
//$$                 MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "press ESC to continue", 8, 147, 0xFFFFFF);
//$$             }
//$$         }
//$$         if (glitched) MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, "Duping", 10, 45, 0xFFFFFF);
        //#else
       drawString(MinecraftClient.getInstance().textRenderer, "Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);
       if (advanced) {
           int i=glitched? 18 : 16;
           MinecraftClient.getInstance().textRenderer.drawWithShadow("Tickjump", 10, 105, 0xFFFFFF);
           if (buttons.get(i).active == false) {
               MinecraftClient.getInstance().textRenderer.drawWithShadow("Tickjump is ready,", 8, 137, 0xFFFFFF);
               MinecraftClient.getInstance().textRenderer.drawWithShadow("press ESC to continue", 8, 147, 0xFFFFFF);
           }
       }
       if (glitched) MinecraftClient.getInstance().textRenderer.drawWithShadow("Duping", 10, 45, 0xFFFFFF);
        //#endif
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
