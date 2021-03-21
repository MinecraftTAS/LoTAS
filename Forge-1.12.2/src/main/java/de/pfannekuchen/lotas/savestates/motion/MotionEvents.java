package de.pfannekuchen.lotas.savestates.motion;

import de.pfannekuchen.lotas.LoTASModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class MotionEvents {
	public static void onTick() {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (player != null) {
			LoTASModContainer.NETWORK.sendToServer(new MotionPacket(player.motionX, player.motionY, player.motionZ, player.moveForward, player.moveVertical, player.moveStrafing));
		}
	}
}
