package de.pfannekuchen.lotas.mods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
//#if MC>=10900
import net.minecraft.util.math.Vec3d;
//#else
//$$ import net.minecraft.util.Vec3;
//#endif

/**
 * Forces a selected entity to walk to a target
 * @author ScribbleLP
 */
public class AIManipMod {
	private Minecraft mc = Minecraft.getMinecraft();
	private static Vec target;

	private int selectedIndex = 0;
	private List<EntityLiving> entities;

	private static EntityLiving selectedEntity;
	
	private static List<AiJob> jobQueue = new ArrayList<AiJob>();
	
	private final EnumFacing orientation;
	
	public AIManipMod() {
		orientation = MCVer.player(mc).getHorizontalFacing();
		entities = MCVer.world(mc.getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension).getEntitiesWithinAABB(EntityLiving.class, MCVer.expandBy32(MCVer.player(Minecraft.getMinecraft()).getEntityBoundingBox()));
		sortEntities();
		
		selectedEntity= entities.get(selectedIndex);
		target=new Vec(MCVer.player(mc).getPositionVector());
	}

	public static boolean isEntityInRange() {
		Minecraft mc=Minecraft.getMinecraft();
		return !MCVer.world(mc.getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension).getEntitiesWithinAABB(EntityLiving.class, MCVer.expandBy32(MCVer.player(Minecraft.getMinecraft()).getEntityBoundingBox())).isEmpty();
	}
	
	public static void tick() {
		for (AiJob job : new ArrayList<>(jobQueue)) {
			job.timeoutTick();
			if(job.isFinished()) {
				jobQueue.remove(job);
			}else {
				PathNavigate control = job.entity.getNavigator();
				Vec target=job.target;
				control.tryMoveToXYZ(target.x, target.y, target.z, 1);
				job.entity.getMoveHelper().setMoveTo(target.x, target.y, target.z, job.entity.getAIMoveSpeed());
				
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
			target=target.addVector(0, 0, -1);
			break;
		case EAST:
			target=target.addVector(1, 0, 0);
			break;
		case SOUTH:
			target=target.addVector(0, 0, 1);
			break;
		case WEST:
			target=target.addVector(-1, 0, 0);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetBack() {
		switch(orientation) {
		case NORTH:
			target=target.addVector(0, 0, 1);
			break;
		case EAST:
			target=target.addVector(-1, 0, 0);
			break;
		case SOUTH:
			target=target.addVector(0, 0, -1);
			break;
		case WEST:
			target=target.addVector(1, 0, 0);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetLeft() {
		switch(orientation) {
		case NORTH:
			target=target.addVector(-1, 0, 0);
			break;
		case EAST:
			target=target.addVector(0, 0, -1);
			break;
		case SOUTH:
			target=target.addVector(1, 0, 0);
			break;
		case WEST:
			target=target.addVector(0, 0, 1);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetRight() {
		switch(orientation) {
		case NORTH:
			target=target.addVector(1, 0, 0);
			break;
		case EAST:
			target=target.addVector(0, 0, 1);
			break;
		case SOUTH:
			target=target.addVector(-1, 0, 0);
			break;
		case WEST:
			target=target.addVector(0, 0, -1);
			break;
		default:
			break;
		}
	}
	
	public void changeTargetUp() {
		target=target.addVector(0, 1, 0);
	}
	
	public void changeTargetDown() {
		target=target.addVector(0, -1, 0);
	}
	
	public void confirm() {
		addJob(selectedEntity, target);
	}
	
	public static Vec getSelectedEntityPos() {
		if(selectedEntity!=null) {
			return new Vec(selectedEntity.getPositionVector());
		}else {
			return null;
		}
	}
	
	public void setTarget(Vec target) {
		AIManipMod.target = target;
	}
	
	public void setTargetToPlayer() {
		target=new Vec(MCVer.player(mc).getPositionVector());
	}
	
	public void setTargetToEntity() {
		target=new Vec(selectedEntity.getPositionVector());
	}
	
	public static Vec getTargetPos() {
		return target;
	}
	
	public boolean contains(EntityLiving entity) {
		for (AiJob job:jobQueue) {
			if(job.entity.equals(entity)) {
				return true;
			}
		}
		return false;
	}
	
	public static EntityLiving getSelectedEntity() {
		return selectedEntity;
	}
	
	public void quickDebug() {
		if(!entities.isEmpty()) {
			addJob(entities.get(0), target);
		}
	}

	public void addJob(EntityLiving entity, Vec target) {
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
		entities.sort(new Comparator<EntityLiving>() {
			Vec player = new Vec(MCVer.player(mc).getPositionVector());

			@Override
			public int compare(EntityLiving o1, EntityLiving o2) {
				return (int) (player.distanceTo(o1.getPositionVector()) - player.distanceTo(o2.getPositionVector()));
			}
		});
	}
	
	private class AiJob {

		final EntityLiving entity;

		final Vec target;
		
		Vec prevPos;
		
		int timeouttimer=0;

		public AiJob(EntityLiving entity, Vec target) {
			this.entity = entity;
			this.target=target;
		}

		/**
		 * Checks if the entity is at the target or if it's stuck
		 * @return
		 */
		public boolean isFinished() {
			double distance=target.distanceTo(entity.getPositionVector());
			if (distance < 1) {
				return true;
			}else if(timeouttimer==180) {
				return true;
			}else if(!entity.isEntityAlive()) {
				return true;
			}else if(entity.getDistanceSq(MCVer.player(mc).getPosition())>500) {
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
				Vec pos = new Vec(entity.getPositionVector());
				prevPos=pos;
			}else {
				Vec pos = new Vec(entity.getPositionVector());
				double distance=prevPos.distanceTo(pos);
				if(distance<15) {
					timeouttimer++;
				}else {
					timeouttimer=0;
				}
			}
		}
	}
	
	public static class Vec {
		
		public double x;
		public double y;
		public double z;
		
		public Vec(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		//#if MC>=10900
		public double distanceTo(Vec3d positionVector) {
		//#else
		//$$ public double distanceTo(Vec3 positionVector) {
		//#endif
			return distanceTo(new Vec(positionVector));
		}

		public Vec addVector(int i, int j, int k) {
			x += i;
			y += j;
			z += k;
			return this;
		}

		public double distanceTo(Vec positionVector) {
			//#if MC>=10900
			return new Vec3d(positionVector.x, positionVector.y, positionVector.z).distanceTo(new Vec3d(x, y, z));
			//#else
			//$$ return new Vec3(positionVector.x, positionVector.y, positionVector.z).distanceTo(new Vec3(x, y, z));
			//#endif
		}

		//#if MC>=10900
		public Vec(Vec3d positionVector) {
		//#else
		//$$ public Vec(Vec3 positionVector) {	
		//#endif
			//#if MC>=11202
			this.x = positionVector.x;
			this.y = positionVector.y;
			this.z = positionVector.z;
			//#else
//$$ 			this.x = positionVector.xCoord;
//$$ 			this.y = positionVector.yCoord;
//$$ 			this.z = positionVector.zCoord;
			//#endif
		}
		
	}
}
