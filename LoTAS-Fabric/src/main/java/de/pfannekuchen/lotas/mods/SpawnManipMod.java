package de.pfannekuchen.lotas.mods;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class SpawnManipMod {
	
	private MinecraftClient mc = MinecraftClient.getInstance();
	
	static Vec3d target;
	
	private final Direction orientation;
	
	public SpawnManipMod() {
		orientation =  mc.player.getHorizontalFacing();
		target=mc.player.getPos();
	}
	
	public void debugSpawn() {
		for(int i=0; i<3;i++) {
			changeTargetForward();
		}
		ServerWorld world=mc.getServer().getWorld(mc.player.dimension);
		Entity entity = new SkeletonEntity(EntityType.SKELETON, world);
		entity.updatePosition(target.x, target.y, target.z);
		((MobEntity)entity).initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnType.NATURAL, null, null);
		world.spawnEntity(entity);
	}
	
	public void changeTargetForward() {
		switch(orientation) {
		case NORTH:
			target=target.add(0, 0, -1);
			break;
		case EAST:
			target=target.add(1, 0, 0);
			break;
		case SOUTH:
			target=target.add(0, 0, 1);
			break;
		case WEST:
			target=target.add(-1, 0, 0);
			break;
		}
	}
	
	public void changeTargetBack() {
		switch(orientation) {
		case NORTH:
			target=target.add(0, 0, 1);
			break;
		case EAST:
			target=target.add(-1, 0, 0);
			break;
		case SOUTH:
			target=target.add(0, 0, -1);
			break;
		case WEST:
			target=target.add(1, 0, 0);
			break;
		}
	}
	
	public void changeTargetLeft() {
		switch(orientation) {
		case NORTH:
			target=target.add(-1, 0, 0);
			break;
		case EAST:
			target=target.add(0, 0, -1);
			break;
		case SOUTH:
			target=target.add(1, 0, 0);
			break;
		case WEST:
			target=target.add(0, 0, 1);
			break;
		}
	}
	
	public void changeTargetRight() {
		switch(orientation) {
		case NORTH:
			target=target.add(1, 0, 0);
			break;
		case EAST:
			target=target.add(0, 0, 1);
			break;
		case SOUTH:
			target=target.add(-1, 0, 0);
			break;
		case WEST:
			target=target.add(0, 0, -1);
			break;
		}
	}
	
	public void changeTargetUp() {
		target=target.add(0, 1, 0);
	}
	
	public void changeTargetDown() {
		target=target.add(0, -1, 0);
	}
}
