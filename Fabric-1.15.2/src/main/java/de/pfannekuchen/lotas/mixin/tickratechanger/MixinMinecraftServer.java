package de.pfannekuchen.lotas.mixin.tickratechanger;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.pfannekuchen.lotas.tickratechanger.TickrateChanger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;

/**
 * Binds the Minecraft Server to the tickrate of the client
 * @author ScribbleLP
 */
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer{
	
	@Shadow
	private long timeReference;

	private long offset = 0;
	private long currentTime = 0;
	
	//================Tickrates other than 0================
	/**
	 * Replaces all 50L values in MinecraftServer.run() to the desired milliseconds per tick which is obtained by
	 * @param ignored the value that was originally used, in this case 50L
	 * @return Milliseconds per tick
	 */
	@ModifyConstant(method = "run", constant = @Constant(longValue = 50L))
    private long serverTickWaitTime(long ignored) {
		if (!isTickrateZero()) {
			return 1000 / TickrateChanger.tickrateServer;
		} else {
			return 50L;
		}
    }
	
	//================Tickrate 0================
	
	/*
	 * These mixins exploit some weirdness in the run() method of the MinecraftServer. (As always,) I don't fully understand it, or if there is a better way,
	 * but this code is probably the least intrusive way of making tickrate 0 work.
	 * 
	 * How vanilla works:
	 * (You should take a look at the MinecraftServer now)
	 * 
	 * Util.getMeasuringTimeMs() is the current time in milliseconds. For this example we will use 1000
	 * 
	 * timeReference is the targeted millisecond time for the next tick
	 * 
	 * lines 622-628: Here the server checks if the server is running behind on ticks and tries to correct that by adding more than one tick to the timeReference (L.626)
	 * 
	 * L.630: The time reference is increased by one tick. Let's say if the current time was 1000 the time reference is 1050.
	 * 
	 * L.638: Here is the tick function of the server with a big difference compared to the 1.12.2 version. A "shouldKeepTicking" function that basically waits until the
	 * Util.getMeasuringTimeMs() is smaller than the time reference.
	 * So in our case if the current time is 1000 and the time Reference 1050, the shouldKeepTicking returns false until the current time is 1051 then the tick is ended and the time Reference increases again.
	 * 
	 * Problem: Idk about you, but I don't like screwing with the system time, in the following mixins, I am redirecting all Util.getMeasuringTimeMs() in the run() and shouldKeepTicking() and return the things I want
	 * 
	 */
	
	
	/**
	 * Redirects all Util.getMeasureTimeMs() in the run method of the minecraft server and returns {@link #getCurrentTime()}
	 * @return
	 */
	@Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J"))
	public long redirectGetMeasuringTimeMsInRun() {
		return getCurrentTime();
	}
	/**
	 * Redirects all Util.getMeasureTimeMs() in the shouldKeepTicking method of the minecraft server and returns {@link #getCurrentTime()}
	 * @return
	 */
	@Redirect(method = "shouldKeepTicking", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J"))
	public long redirectGetMeasuringTimeMsInShouldKeepTicking() {
		return getCurrentTime();
	}
	
	/**
	 * After a while i hated writing TickrateChanger.tickrate==0...
	 * @return
	 */
	private boolean isTickrateZero() {
		return TickrateChanger.tickrateServer==0;
	}
	
	/**
	 * Returns the time dependant on if the current tickrate is tickrate 0
	 * @return In tickrates>0 the vanilla time - offset or the current time in tickrate 0
	 */
	private long getCurrentTime() {
		if (!isTickrateZero()||TickrateChanger.advanceClient) {
			currentTime = Util.getMeasuringTimeMs(); //Set the current time that will be returned if the player decides to activate tickrate 0
			return Util.getMeasuringTimeMs() - offset;	//Returns the Current time - offset which was set while tickrate 0 was active
		} else {
			offset = Util.getMeasuringTimeMs() - currentTime;	//Creating the offset from the measured time and the stopped time
			this.timeReference = currentTime + 50L;
			/* Without this, the time reference would still increase by every tick in vanilla, 
			meaning that if you stop tickrate 0, the time reference would be like nothing ever happened. 
			The server realises this and just catches up with the ticks.
			Now the time reference is always one tick ahead of the current time, tricking shouldKeepTicking in forever catching up to one tick, creating a loop.
			And if we unpause this, the offset is applied  */
			
			return currentTime;
		}
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	public void injectrunTick(BooleanSupplier supplier, CallbackInfo ci) {
		TickrateChanger.ticksPassedServer++;
		TickrateChanger.resetAdvanceServer();
	}
}
