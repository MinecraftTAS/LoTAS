package de.pfannekuchen.lotas.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;

@Mixin(WorldBorder.class)
public abstract class MixinWorldborder {

	@Shadow
	public double startDiameter;
	@Shadow
	public double endDiameter;
	@Shadow
	public long endTime;
	@Shadow
	public long startTime;

	/**
	 * @author Pancake
	 * @reason Easy ;)
	 * @param oldSize
	 * @param newSize
	 * @param time
	 */
	@Overwrite 
    public void setTransition(double oldSize, double newSize, long time) {
		time /= (TickrateChanger.tickrateServer / 20F);
        this.startDiameter = oldSize;
        this.endDiameter = newSize;
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + time;

        for (IBorderListener iborderlistener : this.getListeners()) {
            iborderlistener.onTransitionStarted((WorldBorder) (Object) this, oldSize, newSize, time);
        }
    }

	@Shadow
	public abstract List<IBorderListener> getListeners();
	
}
