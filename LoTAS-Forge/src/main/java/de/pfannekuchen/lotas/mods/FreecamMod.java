package de.pfannekuchen.lotas.mods;

import org.lwjgl.input.Keyboard;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;

public class FreecamMod {

	private boolean freecaming;

	/**
	 * Toggles freecam mode on and off
	 * 
	 * @return If it's turned on or turned off
	 */
	public boolean toggle() {
		return toggle(!freecaming);
	}

	/**
	 * Toggles freecam to the specified value
	 * 
	 * @param enable If freebam should be enabled
	 * @return If it's turned on or turned off
	 */
	public boolean toggle(boolean enable) {
		EntityPlayerSP player = MCVer.player(Minecraft.getMinecraft());
		if (enable) { // On enable
			player.noClip = true;
			player.moveForward = 0f;
			player.moveStrafing = 0f;
			KeybindsUtils.savedTickrate = (int) TickrateChangerMod.tickrate;
			TickrateChangerMod.updateTickrate(0);
		} else { // On disable
			player.noClip = false;
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
			TickrateChangerMod.updateTickrate(KeybindsUtils.savedTickrate);
		}
		return freecaming = enable;
	}

	/**
	 * @return If freecam is enabled
	 */
	public boolean isFreecaming() {
		return freecaming;
	}

	/**
	 * Updates the client players position based on vanilla movement keybindings
	 */
	public void updatePlayer() {
		// Controls for freecam
		if (freecaming) {
			GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
			if (Keyboard.isKeyDown(gameSettings.keyBindForward.getKeyCode())) {
				moveRelatively(0.0F, 0.0F, 0.91F, 1.0F);
			}
			if (Keyboard.isKeyDown(gameSettings.keyBindBack.getKeyCode())) {
				moveRelatively(0.0F, 0.0F, -0.91F, 1.0F);
			}
			if (Keyboard.isKeyDown(gameSettings.keyBindLeft.getKeyCode())) {
				moveRelatively(0.91F, 0.0F, 0.0F, 1.0F);
			}
			if (Keyboard.isKeyDown(gameSettings.keyBindRight.getKeyCode())) {
				moveRelatively(-0.91F, 0.0F, 0.0F, 1.0F);
			}
			if (Keyboard.isKeyDown(gameSettings.keyBindJump.getKeyCode())) {
				moveRelatively(0.0F, 0.92F, 0.0F, 1.0F);
			}
			if (Keyboard.isKeyDown(gameSettings.keyBindSneak.getKeyCode())) {
				moveRelatively(0.0F, -0.92F, 0.0F, 1.0F);
			}
		}
	}

	private void moveRelatively(float strafe, float up, float forward, float friction) {
		float f = strafe * strafe + up * up + forward * forward;
		if (f >= 1.0E-4F) {
			f = MCVer.sqrt(f);
			if (f < 1.0F)
				f = 1.0F;
			f = friction / f;
			strafe = strafe * f;
			up = up * f;
			forward = forward * f;
			EntityPlayerSP player = MCVer.player(Minecraft.getMinecraft());
			float f1 = MCVer.sin(player.rotationYaw * 0.017453292F);
			float f2 = MCVer.cos(player.rotationYaw * 0.017453292F);
			player.posX += (double) (strafe * f2 - forward * f1);
			player.posY += (double) up;
			player.posZ += (double) (forward * f2 + strafe * f1);
		}
	}
}
