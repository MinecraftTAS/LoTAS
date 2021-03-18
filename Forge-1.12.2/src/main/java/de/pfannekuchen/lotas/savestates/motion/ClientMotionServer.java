package de.pfannekuchen.lotas.savestates.motion;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayerMP;

public class ClientMotionServer {
	private static Map<EntityPlayerMP, Saver> motion=Maps.<EntityPlayerMP, Saver>newHashMap();
	
	public static Map<EntityPlayerMP, Saver> getMotion() {
		return motion;
	}
	
	public static class Saver{
		double clientX;
		double clientY;
		double clientZ;
		private float clientrX;
		private float clientrY;
		private float clientrZ;
		public Saver(double x,double y,double z,float rx,float ry,float rz) {
			clientX=x;
			clientY=y;
			clientZ=z;
			clientrX=rx;
			clientrY=ry;
			clientrZ=rz;
		}
		public double getClientX() {
			return clientX;
		}
		public double getClientY() {
			return clientY;
		}
		public double getClientZ() {
			return clientZ;
		}
		public float getClientrX() {
			return clientrX;
		}
		public float getClientrY() {
			return clientrY;
		}
		public float getClientrZ() {
			return clientrZ;
		}
	}
}
