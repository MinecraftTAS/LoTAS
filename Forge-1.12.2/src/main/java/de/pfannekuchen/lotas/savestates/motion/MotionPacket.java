package de.pfannekuchen.lotas.savestates.motion;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MotionPacket implements IMessage{
	double x=0;
	double y=0;
	double z=0;
	float rx=0;
	float ry=0;
	float rz=0;
	public MotionPacket() {
	}
	public MotionPacket(double x, double y, double z, float moveForward, float moveVertical, float moveStrafe) {
		this.x=x;
		this.y=y;
		this.z=z;
		this.rx=moveForward;
		this.ry=moveVertical;
		this.rz=moveStrafe;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		x=buf.readDouble();
		y=buf.readDouble();
		z=buf.readDouble();
		rx=buf.readFloat();
		ry=buf.readFloat();
		rz=buf.readFloat();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(rx);
		buf.writeFloat(ry);
		buf.writeFloat(rz);
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public float getRx() {
		return rx;
	}
	public float getRy() {
		return ry;
	}
	public float getRz() {
		return rz;
	}
}
