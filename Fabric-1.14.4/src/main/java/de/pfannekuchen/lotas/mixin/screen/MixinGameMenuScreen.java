package de.pfannekuchen.lotas.mixin.screen;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.dupemod.DupeMod;
import de.pfannekuchen.lotas.gui.AIManipulationScreen;
import de.pfannekuchen.lotas.gui.DragonManipulationScreen;
import de.pfannekuchen.lotas.gui.LoadstateScreen;
import de.pfannekuchen.lotas.gui.LootManipulationScreen;
import de.pfannekuchen.lotas.gui.SpawnManipulationScreen;
import de.pfannekuchen.lotas.savestate.SavestateMod;
import de.pfannekuchen.lotas.utils.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.world.dimension.DimensionType;

@Mixin(GameMenuScreen.class)
public abstract class MixinGameMenuScreen extends Screen {
	protected MixinGameMenuScreen(Text title) { super(title); }

	public TextFieldWidget savestateName;
	
	@Inject(at = @At("RETURN"), method = "init")
	public void addCustomButtons(CallbackInfo ci) {
		// Move Buttons higher
		for (AbstractButtonWidget guiButton : buttons) {
			guiButton.y -= 24;
		}
		
		buttons.get(7).y += 24;
		
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
		
		this.addButton(new ButtonWidget((width / 4) * 0 + 1, height - 20, width / 4 - 2, 20, "Manipulate Drops", btn -> {
			this.minecraft.openScreen(new LootManipulationScreen((GameMenuScreen) (Object) this));
		}));
		this.addButton(new ButtonWidget((width / 4) * 1 + 2, height - 20, width / 4 - 2, 20, "Manipulate Dragon", btn -> {
			this.minecraft.openScreen(new DragonManipulationScreen((GameMenuScreen) (Object) this));
		})).active = MinecraftClient.getInstance().getServer().getWorld(DimensionType.THE_END).getAliveEnderDragons().size() >= 1;
		this.addButton(new ButtonWidget((width / 4) * 2 + 3, height - 20, width / 4 - 2, 20, "Manipulate Spawning", btn -> {
			this.minecraft.openScreen(new SpawnManipulationScreen());
		}));
		this.addButton(new ButtonWidget((width / 4) * 3 + 4, height - 20, width / 4 - 4, 20, "Manipulate AI", btn -> {
			this.minecraft.openScreen(new AIManipulationScreen());
		}));
		this.addButton(new ButtonWidget(5, 55, 98, 20, "Save Items",btn -> {
			DupeMod.save(minecraft);
			btn.active = false;
		}));
		this.addButton(new ButtonWidget(5, 75, 98, 20, "Load Items", btn -> {
			DupeMod.load(minecraft);
			btn.active = false;
		}));
	}
	
	@Inject(method = "render", at = @At("TAIL"))
	public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		
		if (getClass().getSimpleName().contains("GameMenuScreen")) {
			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				this.buttons.get(8).setMessage("\u00A76Name Savestate");
				this.buttons.get(9).setMessage("\u00A76Choose State");
			} else {
				this.buttons.get(8).setMessage("Savestate");
				this.buttons.get(9).setMessage("Loadstate");
			}	
		}
		
		drawCenteredString(minecraft.textRenderer, "Hold Shift to access more features", width / 2, this.height / 4 + 126, 0xFFFFFF);
	
		if (savestateName != null) savestateName.render(mouseX, mouseX, partialTicks);
		
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
		
		drawString(minecraft.textRenderer, "Tickrate Changer (" + "X" + ")", 5, 5, 0xFFFFFF);
		minecraft.textRenderer.drawWithShadow("Tickjump", 10, 105, 0xFFFFFF);
		minecraft.textRenderer.drawWithShadow("Duping", 10, 45, 0xFFFFFF);
		int w = width - 5;
		minecraft.textRenderer.drawWithShadow("Tracked Items Delay: ", w - minecraft.textRenderer.getStringWidth("Tracked Items Delay: ") - 1, 10, 0xFFFFFFFF);
	}
	
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (savestateName != null) {
			savestateName.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (savestateName != null) {
			boolean focused = savestateName.isFocused();
			if (keyCode == GLFW.GLFW_KEY_ENTER && focused) {
				if (savestateName.getText().isEmpty()) 
					SavestateMod.savestate(null);
				else 
					SavestateMod.savestate(savestateName.getText());
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (savestateName != null) 
			savestateName.charTyped(typedChar, keyCode);
		
		return super.charTyped(typedChar, keyCode);
	}
	
}
