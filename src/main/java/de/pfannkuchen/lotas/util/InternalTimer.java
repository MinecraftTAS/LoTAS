package de.pfannkuchen.lotas.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * For animations to work properly, lotas has an internal timer.
 * @author Pancake
 */
@Environment(EnvType.CLIENT)
public class InternalTimer {
	public float partialTick;
	public float tickDelta;
	private long lastMs = System.currentTimeMillis();
	private final float msPerTick = 50F;

	public int advanceTime(long l) {
		this.tickDelta = (l - this.lastMs) / this.msPerTick;
		this.lastMs = l;
		this.partialTick += this.tickDelta;
		int i = (int) this.partialTick;
		this.partialTick -= i;
		return i;
	}
}
