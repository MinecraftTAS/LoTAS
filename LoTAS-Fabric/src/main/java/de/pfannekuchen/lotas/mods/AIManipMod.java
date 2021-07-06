package de.pfannekuchen.lotas.mods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Forces a selected entity to walk to a target
 * @author ScribbleLP
 *
 */
public class AIManipMod {
	private MinecraftClient mc = MinecraftClient.getInstance();
	private static Vec3d target;

	private int selectedIndex = 0;
	private List<MobEntity> entities;

	private static MobEntity selectedEntity;
	
	private static List<AiJob> jobQueue = new ArrayList<AiJob>();
	
	private final Direction orientation;
	
	public AIManipMod() {
		orientation =  mc.player.getHorizontalFacing();
		//#if MC>=11601
		//#if MC>=11605
//$$ 		//Getting the entities in the world
//$$ 		entities = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().getEntitiesByClass(MobEntity.class, MinecraftClient.getInstance().player.getBoundingBox().expand(64, 64, 64), Predicates.alwaysTrue()); 
		//#else
//$$ 		entities = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().getEntities(MobEntity.class, MinecraftClient.getInstance().player.getBoundingBox().expand(64, 64, 64), Predicates.alwaysTrue()); 
		//#endif
		//#else
		entities = mc.getServer().getWorld(MinecraftClient.getInstance().player.dimension).getEntities(MobEntity.class, mc.player.getBoundingBox().expand(64, 64, 64), Predicates.alwaysTrue());
		//#endif
		sortEntities();
		
		selectedEntity= entities.get(selectedIndex);
		target=mc.player.getPos();
	}

	public static void tick() {
		for (AiJob job : new ArrayList<>(jobQueue)) {
			job.timeoutTick();
			if(job.isFinished()) {
				jobQueue.remove(job);
			}else {
				MoveControl control = job.entity.getMoveControl();
				Vec3d target=job.target;
				control.moveTo(target.x, target.y, target.z, 1);
			}
		}
	}
	
	public void selectNext() {
		if(hasNext()) {
			selectedIndex++;
			
			selectedEntity=entities.get(selectedIndex);
		}
	}
	
	public boolean hasNext() {
		return entities.size()>=selectedIndex+2;
	}

	public void selectPrevious() {
		if(hasPrevious()) {
			selectedIndex--;
			selectedEntity=entities.get(selectedIndex);
		}
	}
	
	public boolean hasPrevious() {
		return entities.size()>0&&selectedIndex-1>=0;
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
	
	public void confirm() {
		addJob(selectedEntity, target);
	}
	
	public static Vec3d getSelectedEntityPos() {
		if(selectedEntity!=null) {
			return selectedEntity.getPos();
		}else {
			return null;
		}
	}
	
	public void setTarget(Vec3d target) {
		AIManipMod.target = target;
	}
	
	public void setTargetToPlayer() {
		target=mc.player.getPos();
	}
	
	public void setTargetToEntity() {
		target=selectedEntity.getPos();
	}
	
	public static Vec3d getTargetPos() {
		return target;
	}
	
	public boolean contains(MobEntity entity) {
		for (AiJob job:jobQueue) {
			if(job.entity.equals(entity)) {
				return true;
			}
		}
		return false;
	}
	
	public static MobEntity getSelectedEntity() {
		return selectedEntity;
	}
	
	public void quickDebug() {
		if(!entities.isEmpty()) {
			addJob(entities.get(0), target);
		}
	}

	public void addJob(MobEntity entity, Vec3d target) {
		if(entity!=null) {
			if(contains(entity)) return;
			jobQueue.add(new AiJob(entity, target));
		}
	}

	/**
	 * Sorts entities by distance from the player
	 */
	private void sortEntities() {
		if(entities.isEmpty()) return;
		entities.sort(new Comparator<MobEntity>() {
			Vec3d player = mc.player.getPos();

			@Override
			public int compare(MobEntity o1, MobEntity o2) {
				return (int) (player.distanceTo(o1.getPos()) - player.distanceTo(o2.getPos()));
			}
		});
	}
	
	private class AiJob {

		final MobEntity entity;

		final Vec3d target;
		
		Vec3d prevPos;
		
		int timeouttimer=0;

		public AiJob(MobEntity entity, Vec3d target) {
			this.entity = entity;
			this.target=target;
		}

		/**
		 * Checks if the entity is at the target or if it's stuck
		 * @return
		 */
		public boolean isFinished() {
			double distance=target.distanceTo(entity.getPos());
			if (distance < 1) {
				return true;
			}else if(timeouttimer==180) {
				return true;
			}else if(!entity.isAlive()) {
				return true;
			}else if(entity.squaredDistanceTo(mc.player.getPos())>500) {
				return true;
			}else {
				return false;
			}
		}
		
		/**
		 * If an entity is stuck, the timeouttimer will increase
		 */
		public void timeoutTick() {
			if(prevPos==null) {
				Vec3d pos = entity.getPos();
				prevPos=pos;
			}else {
				Vec3d pos = entity.getPos();
				double distance=prevPos.distanceTo(pos);
				if(distance<15) {
					timeouttimer++;
				}else {
					timeouttimer=0;
				}
			}
		}
	}
}
