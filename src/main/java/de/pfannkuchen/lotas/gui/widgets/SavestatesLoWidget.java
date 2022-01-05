package de.pfannkuchen.lotas.gui.widgets;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannkuchen.lotas.ClientLoTAS;
import de.pfannkuchen.lotas.LoTAS;
import de.pfannkuchen.lotas.mods.SavestateMod.State;
import de.pfannkuchen.lotas.util.LoTASHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * Dupe Mod Widget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class SavestatesLoWidget extends WindowLoWidget {
	
	/**
	 * Initializes a Tickrate Changer Widget
	 */
	public SavestatesLoWidget() {
		super("savestatewidget", new TextComponent("Savestates"), .237, .037);
	}

	@Override
	protected void init() {
		addWidget(new ButtonLoWidget(true, 0.002, (LoTAS.savestatemod.getStateCount()*0.1)+0.04, 0.233, () -> {
			ClientLoTAS.loscreenmanager.toggleLoTASMenu(this.mc);
			LoTAS.savestatemod.requestState(0, -1, "no name given", LoTASHelper.takeScreenshot(this.mc, 256, 144));
		}, new TextComponent("Savestate")));
		super.init();
	}
	
	@Override
	protected void render(PoseStack stack, double curX, double curY) {
		super.render(stack, curX, curY);
		// Render the savestate panels
		this.windowHeight = (LoTAS.savestatemod.getStateCount()*0.1)+0.083;
		for (int i = 0; i < LoTAS.savestatemod.getStateCount(); i++) {
			State s = LoTAS.savestatemod.getSavestateInfo(i);
			// Render Box
			fill(stack, this.x+0.001, this.y+0.035+(i*0.1), this.x+0.235, this.y+0.1+(i*0.1)+0.035, (i % 2) == 0 ? 0xff1b1c21 : 0xff0a0a0b);
//			fill(stack, this.x+0.011, this.y+0.045+(i*0.1), this.x+0.09, this.y+0.09+(i*0.1)+0.035, (i % 2) != 0 ? 0xff1b1c21 : 0xff0a0a0b);
			// Render Info
			draw(stack, new TextComponent(s.getName()), this.x+0.1, this.y+(i*0.1)+0.045, 20, 0xff8f8f8f, false);
			draw(stack, new TextComponent(new SimpleDateFormat().format(Date.from(Instant.ofEpochSecond(s.getTimestamp())))), this.x+0.1, this.y+(i*0.1)+0.065, 20, 0xff8f8f8f, false);
			// Render Button
			fill(stack, this.x+0.095, this.y+0.095+(i*0.1), this.x+0.16, this.y+0.09+0.035+(i*0.1), (i % 2) != 0 ? 0xff1b1c21 : 0xff0a0a0b);
			fill(stack, this.x+0.164, this.y+0.095+(i*0.1), this.x+0.23, this.y+0.09+0.035+(i*0.1), (i % 2) != 0 ? 0xff1b1c21 : 0xff0a0a0b);
			this.draw(stack, new TextComponent("Loadstate"), this.x+0.095+0.0065, this.y+0.1+(i*0.1), 20, 0xff149b5b, false);
			this.draw(stack, new TextComponent("Deletestate"), this.x+0.095+0.0724, this.y+0.1+(i*0.1), 20, 0xff149b5b, false);
			// Render Image
		}
		
	}
	
}
