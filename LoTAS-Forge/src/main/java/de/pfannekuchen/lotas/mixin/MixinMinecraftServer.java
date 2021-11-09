package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
	
	@ModifyVariable(method = "run", at = @At("STORE"), index = 1, ordinal = 0)
	public long undoServerTicking(long in) {
		return 0;
	}
	
	@Redirect(method = "run", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V"))
	public void redoServerWait(long millis) throws InterruptedException {
		//Applying tickrate on the server side
		this.tick();
		
		if (TickrateChangerMod.tickrateServer == 0) {
			//Thread sleeping until the tickrate is something different than 0 or ticksync is correct again
			while (TickrateChangerMod.tickrateServer == 0) {
				Thread.sleep(1);
			}
		} else {
			Thread.sleep((long) Math.max(1L, (1000F / TickrateChangerMod.tickrateServer)));
		}
		TickrateChangerMod.ticksPassedServer++;
		TickrateChangerMod.resetAdvanceServer();
	}
	
	@Redirect(method = "run", at = @At(value = "FIELD", target = "Lnet/minecraft/server/MinecraftServer;worlds:[Lnet/minecraft/world/WorldServer;"))
	public WorldServer[] fixCrashDuringLoadstate(MinecraftServer server) {
		if(server.worlds.length==0) {
			System.out.println("Prevented a forge crash. You are welcome!");
			return null;
		}
		return server.worlds;
	}

	@Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;areAllPlayersAsleep(V)Z"))
	public boolean fixCrashDuringLoadstate2(WorldServer world) {
		if (world == null)
			return false;
		else
			return world.areAllPlayersAsleep();
	}
	
	@Shadow
	protected abstract void tick();
	
}