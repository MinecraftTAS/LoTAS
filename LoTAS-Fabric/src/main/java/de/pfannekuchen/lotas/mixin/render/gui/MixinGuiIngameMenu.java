package de.pfannekuchen.lotas.mixin.render.gui;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.Keyboard;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;

@Mixin(GameMenuScreen.class)
public abstract class MixinGuiIngameMenu extends Screen {

	/*	
	 * Adds a few utility buttons
	 */
	
	protected MixinGuiIngameMenu(Text title) {
		super(title);
	}

	@Unique
	private static ImmutableList<Integer> glitchedButtons = ImmutableList.of(17, 18, 22);
	@Unique
	private static ImmutableList<Integer> advancedButtons = ImmutableList.of(21, 22, 30, 28, 27, 26, 29, 24, 23);
	
	public TextFieldWidget savestateName;
	public TextFieldWidget tickrateField;
	
	@Inject(method = "initWidgets", at = @At("RETURN"))
	public void injectinitGui(CallbackInfo ci) {
		
		ButtonWidget customReturnToGameButton=new ButtonWidget(this.width / 2 - 102, this.height / 4 + 24 + -16, 204, 20, I18n.translate("menu.returnToGame"), (buttonWidgetx) -> {
	         this.minecraft.openScreen((Screen)null);
	         this.minecraft.mouse.lockCursor();
	      }) {
			
			@Override
			public void playDownSound(SoundManager soundManager) {
			}
		};
		this.buttons.set(0, customReturnToGameButton);
		this.children.set(0, customReturnToGameButton);
		
		for (AbstractButtonWidget guiButton : buttons) {
			guiButton.y -= 24;	
		}
		
		buttons.get(7).y +=24;
		
		this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 96 + -16, 98, 20, I18n.translate("Savestate"), (buttonWidgetx)->{
			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				//No idea
//				savestateName = new TextFieldWidget(this.minecraft.textRenderer, this.width / 2 - 100, this.height / 4 + 96 + -16, 98, 20);
//				button.enabled = false;
//				savestateName.setFocused(true);
			} else SavestateMod.savestate(null);
		}));
    	
		ButtonWidget loadstate = new ButtonWidget(this.width / 2 + 4, this.height / 4 + 96 + -16, 98, 20, I18n.translate("Loadstate"), (buttonWidgetx)->{
			if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				/*mc.displayGuiScreen(new GuiLoadstateMenu());*/
			}
			else SavestateMod.loadstate(-1);
		});
    	loadstate.active = SavestateMod.hasSavestate();
    	this.addButton(loadstate);
		/*
		double pX = MCVer.player(Minecraft.getMinecraft()).posX;
		double pY = MCVer.player(Minecraft.getMinecraft()).posY;
		double pZ = MCVer.player(Minecraft.getMinecraft()).posZ;
		DupeMod.trackedObjects = new ArrayList<EntityItem>();
        for (EntityItem item : MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension).getEntitiesWithinAABB(EntityItem.class, MCVer.aabb(pX - 16, pY - 16, pZ - 16, pX + 16, pY + 16, pZ + 16))) {
        	DupeMod.trackedObjects.add(item);
        }
        */
		
    	addButton(new ButtonWidget(5, 15, 48, 20, "+", buttonWidgetx -> {
    		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
//				tickrateField = new GuiTextField(0, MCVer.getFontRenderer(Minecraft.getMinecraft()), 4, 15, 103, 20);
//    			buttonWidgetx.active = false;
//				tickrateField.setFocused(true);
			} else {
				TickrateChangerMod.index++;
				TickrateChangerMod.index = MCVer.clamp(TickrateChangerMod.index, 0, 11);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
        }));
        addButton(new ButtonWidget( 55, 15, 48, 20, "-", buttonWidgetx -> {
        	if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
//				tickrateField = new GuiTextField(0, MCVer.getFontRenderer(Minecraft.getMinecraft()), 4, 15, 103, 20);
//				buttonWidgetx.active = false;
//				tickrateField.setFocused(true);
			} else {
				TickrateChangerMod.index--;
				TickrateChangerMod.index = MCVer.clamp(TickrateChangerMod.index, 0, 11);
				TickrateChangerMod.updateTickrate(TickrateChangerMod.ticks[TickrateChangerMod.index]);
			}
        }));
	}
	
	@ModifyConstant(method = "render", constant = @Constant(intValue = 40))
	public int moveTextUp(int ignored) {
		return 15;
	}
	
	@Inject(method = "render", at = @At(value = "RETURN"))
	public void injectRender(CallbackInfo ci) {
		drawString(minecraft.textRenderer, "Tickrate Changer (" + TickrateChangerMod.tickrate + ")", 5, 5, 0xFFFFFF);
	}
	
}