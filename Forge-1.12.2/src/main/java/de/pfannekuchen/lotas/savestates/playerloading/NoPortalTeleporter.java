package de.pfannekuchen.lotas.savestates.playerloading;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class NoPortalTeleporter implements ITeleporter{
	public NoPortalTeleporter() {
	}
	@Override
	public void placeEntity(World world, Entity entity, float yaw) {
	}
	@Override
	public boolean isVanilla() {
		return false;
	}

}
