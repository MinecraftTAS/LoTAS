package de.pfannekuchen.lotas.savestates.motion;

import de.pfannekuchen.lotas.savestates.motion.ClientMotionServer.Saver;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MotionPacketHandler implements IMessageHandler<MotionPacket, IMessage>{

	@Override
	public IMessage onMessage(MotionPacket message, MessageContext ctx) {
		if(ctx.side.isServer()) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(()->{
				ClientMotionServer.getMotion().put(ctx.getServerHandler().player, new Saver(message.x, message.y, message.z, message.rx, message.ry, message.rz));
			});
		}
		return null;
	}

}
