/*
 * These mixins exploit some weirdness in the runServer() method of the MinecraftServer. (As always,) I don't fully understand it, or if there is a better way,
 * but this code is probably the least intrusive way of making tickrate 0 work.
 *
 * How vanilla works:
 *
 * Util.getMillis() is the current time in milliseconds. For this example we will use 1000
 *
 * nextTickTime is the targeted millisecond time for the next tick
 *
 *		if (l > 2000L && this.nextTickTime - this.lastOverloadWarning >= 15000L) {
 *     		long m = l / 50L;
 *			LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", (Object)l, (Object)m);
 *			this.nextTickTime += m * 50L;
 *			this.lastOverloadWarning = this.nextTickTime;
 *		}
 *
 * Here the server checks if the server is running behind on ticks and tries to correct that by adding more than one tick to the nextTickTime
 *
 *		this.nextTickTime += 50L;
 *
 * The nextTickTime is increased by one tick. Let's say if the current time was 1000, then nextTickTime is 1050.
 *
 *		this.tickServer(this::haveTime);
 *
 * Here is the tick function of the server with a big difference compared to the 1.12.2 version. A "haveTime" function that basically waits until the
 * Util.getMillis() is smaller than the nextTickTime.
 * So in our case if the current time is 1000 and the nextTickTime 1050, the haveTime returns false until the current time is 1051, then the tick is ended and the nextTickTime increases again.
 *
 * Problem: Idk about you, but I don't like screwing with the system time, in the following mixins, I am redirecting all Util.getMillis() in the run() and haveTime() and return the things I want
 */

package com.minecrafttas.lotas.mixin.tickratechanger;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecrafttas.lotas.mods.TickAdvance;
import com.minecrafttas.lotas.mods.TickrateChanger;

import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;

/**
 * Slows down the Minecraft Server
 * @author Scribble
 */
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

	@Shadow
	private long nextTickTime;

	private long offset = 0;
	private long currentTime = 0;

	/**
	 * Replaces all 50L values in MinecraftServer.runServer() to the desired milliseconds
	 * per tick which is obtained by the tickratechanger
	 * @param ignored the value that was originally used, in this case 50L
	 * @return Milliseconds per tick
	 */
	@ModifyConstant(method = "runServer", constant = @Constant(longValue = 50L)) // @RunServer;
	private long serverTickWaitTime(long ignored) {
		return (long) TickrateChanger.instance.getMsPerTick();
	}

	/**
	 * Redirects all Util.getMillis() in the run method of the minecraft server and
	 * returns {@link #getCurrentTime()}
	 * @return Modified measuring time
	 */
	@Redirect(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J")) // @RunServer;
	public long redirectGetMillis() {
		return this.getCurrentTime();
	}

	/**
	 * Redirects all Util.getMillis() in the shouldKeepTicking method of the
	 * minecraft server and returns {@link #getCurrentTime()}
	 * @return Modified measuring time
	 */
	@Redirect(method = "haveTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
	public long redirectGetMillisInHaveTime() {
		return this.getCurrentTime();
	}

	/**
	 * Returns the time dependant on if the current tickrate is tickrate 0
	 * @return In tickrates > 0 the vanilla time - offset else the current time in tickrate 0
	 */
	private long getCurrentTime() {
		if (!TickAdvance.instance.isTickadvanceEnabled() || TickAdvance.instance.shouldTickServer) {
			this.currentTime = Util.getMillis(); // Set the current time that will be returned if the player decides to activate
													// tickrate 0
			return Util.getMillis() - this.offset; // Returns the Current time - offset which was set while tickrate 0 was active
		}
		this.offset = Util.getMillis() - this.currentTime; // Creating the offset from the measured time and the stopped time
		this.nextTickTime = this.currentTime + 50L;
		// Without this, the time reference would still increase by every tick in
		// vanilla, meaning that if you stop tickrate 0, the time reference would be
		// like nothing ever happened. The server realises this and just catches up with
		// the ticks. Now the time reference is always one tick ahead of the current
		// time, tricking shouldKeepTicking in forever catching up to one tick, creating
		// a loop. And if we unpause this, the offset is applied.

		return this.currentTime;
	}

	/**
	 * Resets the Tick Advance status
	 * @param supplier Parameters
	 * @param ci Callback Info
	 */
	@Inject(method = "tickServer", at = @At("HEAD"))
	public void injectrunTick(BooleanSupplier supplier, CallbackInfo ci) {
		TickAdvance.instance.shouldTickServer = false;
	}

}
