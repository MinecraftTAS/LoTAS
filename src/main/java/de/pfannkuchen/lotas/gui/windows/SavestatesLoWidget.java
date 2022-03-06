package de.pfannkuchen.lotas.gui.windows;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.gui.widgets.ButtonLoWidget;
import de.pfannkuchen.lotas.gui.widgets.TextFieldLoWidget;
import de.pfannkuchen.lotas.gui.widgets.WindowLoWidget;
import de.pfannkuchen.lotas.mods.SavestateMod.State;
import de.pfannkuchen.lotas.util.LoTASHelper;
import de.pfannkuchen.lotas.util.ResourceManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;

/**
 * Savestate window lowidget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class SavestatesLoWidget extends WindowLoWidget {

	private static int offset;
	
	/*
	 * Savestate button for later realigning
	 */
	private ButtonLoWidget savestateBtn;

	/*
	 * Savestate name text field for later realigning
	 */
	private TextFieldLoWidget savestateText;
	
	/**
	 * Name of the savestate
	 */
	private String savestateName = "no name given";
	
	/**
	 * Initializes a tickrate changer widget
	 */
	public SavestatesLoWidget() {
		super("savestatewidget", new TextComponent("Savestates"), .237, .047);
	}

	@Override
	protected void init() {
		this.addWidget(this.savestateBtn = new ButtonLoWidget(true, 0.002, LoTAS.savestatemod.getStateCount()*0.1+0.04, 0.233, () -> {
			ClientLoTAS.loscreenmanager.setScreen(null);
			LoTAS.savestatemod.requestState(0, -1, this.savestateName, LoTASHelper.takeScreenshot(this.mc, 256, 144));
		}, new TextComponent("Savestate")));
		this.addWidget(this.savestateText = new TextFieldLoWidget(true, .02, LoTAS.savestatemod.getStateCount()*0.1+0.104, .197, s -> {
			if (s.length() > 20)
				s = s.substring(0, 20);
			this.savestateName = s;
		}, "no name given"));
		super.init();
	}
	
	@Override
	protected void scroll(double scroll) {
		SavestatesLoWidget.offset -= scroll;
		SavestatesLoWidget.offset = Mth.clamp(SavestatesLoWidget.offset, 0, (LoTAS.savestatemod.getStateCount() - 6 < 0) ? 0 : LoTAS.savestatemod.getStateCount() - 6);
		super.scroll(scroll);
	}
	
	@Override
	protected void click(double curX, double curY, int button) {
		// Handle loadstate and deletestate buttons
		for (int i = 0; i < Math.min(LoTAS.savestatemod.getStateCount(), 6); i++) {
			if (curX > this.x+0.095 && curY > this.y+0.095+i*0.1 && curX < this.x+0.16 && curY < this.y+0.09+0.035+i*0.1) {
				
			}
			if (curX > this.x+0.164 && curY > this.y+0.095+i*0.1 && curX < this.x+0.23 && curY < this.y+0.09+0.035+i*0.1) {
				ClientLoTAS.loscreenmanager.setScreen(null);
				LoTAS.savestatemod.requestState(2, i, null, null);
			}
		}
		super.click(curX, curY, button);
	}

	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		if (!this.active) return;
		super.render(stack, curX, curY);
		stack.pushPose();
		if (this.animationProgress != 6) {
			stack.translate(
					this.vertical ? (this.leftORight ? -1000 : 1000) + this.ease(this.animationProgress, 0, 1, 6)*(this.leftORight ? +1000 : -1000) : 0,
							this.horizontal ? (this.topOBottom ? -1000 : 1000) + this.ease(this.animationProgress, 0, 1, 6)*(this.topOBottom ? +1000 : -1000) : 0,
									0);
		}
		// Render the savestate panels
		this.windowHeight = Math.min(LoTAS.savestatemod.getStateCount(), 6)*0.1+0.153;
		this.savestateBtn.y = Math.min(LoTAS.savestatemod.getStateCount(), 6)*0.1+0.04;
		this.savestateText.y = Math.min(LoTAS.savestatemod.getStateCount(), 6)*0.1+0.104;
		for (int i = 0; i < Math.min(LoTAS.savestatemod.getStateCount(), 6); i++) {
			State s = LoTAS.savestatemod.getSavestateInfo(i+offset);
			if (s == null) continue;
			// Render box
			this.fill(stack, this.x+0.001, this.y+0.035+i*0.1, this.x+0.235, this.y+0.1+i*0.1+0.035, i % 2 == 0 ? 0xff1b1c21 : 0xff0a0a0b);
			// Render info
			this.draw(stack, new TextComponent(s.getName()), this.x+0.1, this.y+i*0.1+0.045, 20, 0xff8f8f8f, false);
			this.draw(stack, new TextComponent(new SimpleDateFormat().format(Date.from(Instant.ofEpochSecond(s.getTimestamp())))), this.x+0.1, this.y+i*0.1+0.065, 20, 0xff8f8f8f, false);
			// Render button
			if (curX > this.x+0.095 && curY > this.y+0.095+i*0.1 && curX < this.x+0.16 && curY < this.y+0.09+0.035+i*0.1) {
				this.fill(stack, this.x+0.095, this.y+0.095+i*0.1, this.x+0.16, this.y+0.09+0.035+i*0.1, 0xff149b5b);
				this.draw(stack, new TextComponent("Loadstate"), this.x+0.095+0.0065, this.y+0.1+i*0.1, 20, i % 2 != 0 ? 0xff1b1c21 : 0xff0a0a0b, false);
			} else {
				this.fill(stack, this.x+0.095, this.y+0.095+i*0.1, this.x+0.16, this.y+0.09+0.035+i*0.1, i % 2 != 0 ? 0xff1b1c21 : 0xff0a0a0b);
				this.draw(stack, new TextComponent("Loadstate"), this.x+0.095+0.0065, this.y+0.1+i*0.1, 20, 0xff149b5b, false);
			}
			
			if (curX > this.x+0.164 && curY > this.y+0.095+i*0.1 && curX < this.x+0.23 && curY < this.y+0.09+0.035+i*0.1) {
				this.fill(stack, this.x+0.164, this.y+0.095+i*0.1, this.x+0.23, this.y+0.09+0.035+i*0.1, 0xff149b5b);
				this.draw(stack, new TextComponent("Deletestate"), this.x+0.095+0.0724, this.y+0.1+i*0.1, 20, i % 2 != 0 ? 0xff1b1c21 : 0xff0a0a0b, false);
			} else {
				this.fill(stack, this.x+0.164, this.y+0.095+i*0.1, this.x+0.23, this.y+0.09+0.035+i*0.1, i % 2 != 0 ? 0xff1b1c21 : 0xff0a0a0b);
				this.draw(stack, new TextComponent("Deletestate"), this.x+0.095+0.0724, this.y+0.1+i*0.1, 20, 0xff149b5b, false);
			}
			// Render image
			/*if (this.mc.hasSingleplayerServer()) { // Savestate textures are not supported at all. /////// on multiplayer servers
				if (s.texture != null) {
					RenderSystem.setShaderTexture(0, s.texture);
					this.render(stack, this.x+0.011, this.y+0.045+i*0.1, 0.079, 0.079, 0, 0);
				}
			} else {*/
				RenderSystem.setShaderTexture(0, ResourceManager.UNSUPPORTED);
				this.render(stack, this.x+0.011, this.y+0.045+i*0.1, 0.079, 0.079, 0, 0);
			/*}*/
		}
		stack.popPose();
	}

}
