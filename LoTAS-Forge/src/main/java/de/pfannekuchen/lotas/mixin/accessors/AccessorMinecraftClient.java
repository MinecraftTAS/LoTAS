package de.pfannekuchen.lotas.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

@Mixin(Minecraft.class)
public interface AccessorMinecraftClient {

	//#if MC>=11200
	@Accessor("integratedServer")
	//#else
//$$ 	@Accessor("theIntegratedServer")
	//#endif
	public void integratedServer(IntegratedServer server);
	
	@Accessor("timer")
	public Timer timer();
	
	@Accessor("integratedServerIsRunning")
	public void integratedServerIsRunning(boolean integratedServerIsRunning);
	
	@Accessor("session")
	public void session(Session session);
	
}
