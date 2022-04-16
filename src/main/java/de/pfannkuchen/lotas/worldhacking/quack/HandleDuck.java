package de.pfannkuchen.lotas.worldhacking.quack;

import de.pfannkuchen.lotas.worldhacking.CustomRespawnPacket;

public interface HandleDuck {
	
	public abstract void handleCustomPacket(CustomRespawnPacket packet);
}
