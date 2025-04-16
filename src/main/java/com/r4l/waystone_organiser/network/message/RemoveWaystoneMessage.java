package com.r4l.waystone_organiser.network.message;

import com.r4l.waystone_organiser.network.OrganiserPacketHandler;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RemoveWaystoneMessage {
	public static class ClientToServer implements IMessage{
		
		private WaystoneEntry entry;

		public ClientToServer () {}
		
		public ClientToServer (WaystoneEntry entry) {
			this.entry = entry;
		}
		
		@Override
		public void fromBytes(ByteBuf buf) {
			entry = WaystoneEntry.read(buf);
			
		}

		@Override
		public void toBytes(ByteBuf buf) {
			entry.write(buf);
			
		}
		
		public WaystoneEntry getWaystone () {
			return this.entry;
		}
		
		
		public static class Handler implements IMessageHandler<ClientToServer, IMessage> {
			 @Override
			    public IMessage onMessage(ClientToServer message, MessageContext ctx) {
			    	EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			    	//
			    	OrganiserPacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
			    		WaystoneManager.removePlayerWaystone(serverPlayer, message.getWaystone());
		    			WaystoneManager.sendPlayerWaystones(serverPlayer);
			    	});
					return null;
			 }
		}
		
	}
}
