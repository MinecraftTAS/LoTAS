package de.pfannkuchen.lotas.gui.widgets;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import de.pfannkuchen.lotas.LoTAS;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.TextComponent;

/**
 * Dupe Mod Widget
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class SavestatesLoWidget extends WindowLoWidget {
	
	// Callbacks
	private Callable<Integer> countCallback;
	private Runnable onSave;
	private Consumer<Integer> onLoad;
	private Consumer<Integer> onDelete;
	// Buttons
	private SliderLoWidget slider;
	private ButtonLoWidget load;
	private ButtonLoWidget delete;
	private ButtonLoWidget save;
	// Data
	private int stateCount;
	private int currentState;
	
	/**
	 * Initializes a Tickrate Changer Widget
	 */
	public SavestatesLoWidget(Callable<Integer> countCallback, Runnable onSave, Consumer<Integer> onLoad, Consumer<Integer> onDelete, int currentState) {
		super("savestatewidget", new TextComponent("Savestates"), .3, .135);
		this.countCallback = countCallback;
		this.onSave = onSave;
		this.onLoad = onLoad;
		this.onDelete = onDelete;
		this.currentState = currentState;
	}

	@Override
	protected void init() {
		try { this.stateCount = this.countCallback.call(); } catch (Exception e) { /* This cannot be thrown */ }
		addWidget(slider = new SliderLoWidget(true, 0.01, 0.04, 0.28, 1, progress -> {
			int state = (int) (progress * (stateCount-1));
			if (state == this.stateCount-1) {
				this.load.value = new TextComponent("Loadstate");
				this.delete.active = false;
				this.save.active = true;
			} else {
				this.load.value = new TextComponent("Load this state");
				this.delete.active = true;
				this.save.active = false;
			}
			return new TextComponent(LoTAS.savestatemod.getSavestateInfo(state));
		}, this.stateCount == -1 ? new TextComponent("No savestate available") : new TextComponent(LoTAS.savestatemod.getSavestateInfo(this.stateCount-1))));
		addWidget(this.load = new ButtonLoWidget(true, 0.005, 0.085, 0.14, () -> this.onLoad.accept((int) (this.slider.progress * (this.stateCount-1))) /* Add a small callback to obtain the selected index too.*/, new TextComponent("Loadstate")));
		addWidget(this.delete = new ButtonLoWidget(this.currentState == this.stateCount-1 ? false : true, 0.155, 0.085, 0.14, () -> this.onDelete.accept((int) (this.slider.progress * (this.stateCount-1))) /* Add a small callback to obtain the selected index too.*/, new TextComponent("Delete this state")));
		addWidget(this.save = new ButtonLoWidget(this.currentState == this.stateCount-1 ? true : false, 0.155, 0.085, 0.14, this.onSave /* Call directly */, new TextComponent("Savestate")));
		super.init();
	}
	
}
