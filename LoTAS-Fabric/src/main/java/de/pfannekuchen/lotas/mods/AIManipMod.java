package de.pfannekuchen.lotas.mods;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Predicates;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

/**
 * Forces a selected entity to walk to a target
 * @author ScribbleLP
 */
public class AIManipMod {
	private Minecraft mc = Minecraft.getInstance();
	private static Vec3 target;

	private int selectedIndex = 0;
	private List<Mob> entities;

	private static Mob selectedEntity;
	
	private static List<AiJob> jobQueue = new ArrayList<AiJob>();
	
	private final Direction orientation;
	
	public AIManipMod() {
		orientation =  mc.player.getDirection();
		entities = MCVer.getCurrentLevel().getEntitiesOfClass(Mob.class, mc.player.getBoundingBox().inflate(64, 64, 64), Predicates.alwaysTrue());
		sortEntities();
		
		selectedEntity = entities.get(selectedIndex);
		target = mc.player.position();
	}

	public static boolean isEntityInRange() {
		Minecraft mc=Minecraft.getInstance();
		return !MCVer.getCurrentLevel().getEntitiesOfClass(Mob.class, mc.player.getBoundingBox().inflate(64, 64, 64), Predicates.alwaysTrue()).isEmpty();
	}
	
	public static void tick() {
		for (AiJob job : new ArrayList<>(jobQueue)) {
			job.timeoutTick();
			if(job.isFinished()) {
				jobQueue.remove(job);
			}else {
				MoveControl control = job.entity.getMoveControl();
				Vec3 target=job.target;
				control.setWantedPosition(target.x, target.y, target.z, 1);
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
		default:
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
		default:
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
		default:
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
		default:
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
	
	public static Vec3 getSelectedEntityPos() {
		if(selectedEntity!=null) {
			return selectedEntity.position();
		}else {
			return null;
		}
	}
	
	public void setTarget(Vec3 target) {
		AIManipMod.target = target;
	}
	
	public void setTargetToPlayer() {
		target=mc.player.position();
	}
	
	public void setTargetToEntity() {
		target=selectedEntity.position();
	}
	
	public static Vec3 getTargetPos() {
		return target;
	}
	
	public boolean contains(Mob entity) {
		for (AiJob job:jobQueue) {
			if(job.entity.equals(entity)) {
				return true;
			}
		}
		return false;
	}
	
	public static Mob getSelectedEntity() {
		return selectedEntity;
	}
	
	public void quickDebug() {
		if(!entities.isEmpty()) {
			addJob(entities.get(0), target);
		}
	}

	public void addJob(Mob entity, Vec3 target) {
		if(entity!=null) {
			if(contains(entity)) return;
			jobQueue.add(new AiJob(entity, target));
		}
	}
	
	public void save() {
		File file=new File(Minecraft.getInstance().gameDirectory, MCVer.getCurrentWorldFolder()+"aijobs.txt");
		List<String> aijobs=new ArrayList<>();
		
		if(jobQueue.isEmpty()&&file.exists()) {
			file.delete();
		}
		
		jobQueue.forEach(job->{
			aijobs.add(job.toString());
		});
		
		try {
			FileUtils.writeLines(file, aijobs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		jobQueue.clear();
	}
	
	public void read() {
		File file=new File(Minecraft.getInstance().gameDirectory, MCVer.getCurrentWorldFolder()+"aijobs.txt");
		jobQueue.clear();
		if(!file.exists()) {
			return;
		}
		List<String> aijobs=new ArrayList<>();
		try {
			aijobs=FileUtils.readLines(file, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(aijobs.isEmpty()) return;
		
		aijobs.forEach(line->{
			jobQueue.add(fromString(this, line));
		});
	}

	/**
	 * Sorts entities by distance from the player
	 */
	private void sortEntities() {
		if(entities.isEmpty()) return;
		entities.sort(new Comparator<Mob>() {
			Vec3 player = mc.player.position();

			@Override
			public int compare(Mob o1, Mob o2) {
				return (int) (player.distanceTo(o1.position()) - player.distanceTo(o2.position()));
			}
		});
	}
	
	public static AiJob fromString(AIManipMod manip, String line) {
		String[] split=line.split(",");
		Mob entity=(Mob) MCVer.getCurrentLevel().getEntity(UUID.fromString(split[0]));
		double x = Double.parseDouble(split[1]);
		double y = Double.parseDouble(split[2]);
		double z = Double.parseDouble(split[3]);
		double px = Double.parseDouble(split[4]);
		double py = Double.parseDouble(split[5]);
		double pz = Double.parseDouble(split[6]);
		
		Vec3 target=new Vec3(x, y, z);
		Vec3 prevPos=new Vec3(px, py, pz);
		
		return manip.new AiJob(entity, target, prevPos);
	}
	
	private class AiJob {

		final Mob entity;

		final Vec3 target;
		
		Vec3 prevPos;
		
		int timeouttimer=0;

		public AiJob(Mob entity, Vec3 target) {
			this.entity = entity;
			this.target=target;
		}
		
		public AiJob(Mob entity, Vec3 target, Vec3 prevPos) {
			this.entity = entity;
			this.target=target;
			this.prevPos=prevPos;
		}

		/**
		 * Checks if the entity is at the target or if it's stuck
		 * @return
		 */
		public boolean isFinished() {
			double distance=target.distanceTo(entity.position());
			if (distance < 1) {
				return true;
			}else if(timeouttimer==180) {
				return true;
			}else if(!entity.isAlive()) {
				return true;
			}else if(entity.distanceToSqr(mc.player.position())>500) {
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
				Vec3 pos = entity.position();
				prevPos=pos;
			}else {
				Vec3 pos = entity.position();
				double distance=prevPos.distanceTo(pos);
				if(distance<15) {
					timeouttimer++;
				}else {
					timeouttimer=0;
				}
			}
		}
		
		@Override
		public String toString() {
			return String.format("%s,%d,%d,%d,%d,%d,%d", entity.getStringUUID(), target.x, target.y, target.z, prevPos.x, prevPos.y, prevPos.z);
		}
	}
}
