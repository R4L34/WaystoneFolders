package com.r4l.waystone_organiser.network.message;

import com.r4l.waystone_organiser.capability.FolderSet;
import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.network.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncMessage {
	
	public static class ClientToServer implements IMessage {

		private IFolderSet folders;
		
		public ClientToServer () {}
		
		public ClientToServer (EntityPlayer player) {
			this.folders = player.getCapability(FolderSet.Provider.FOLDER_CAP, null);
		}
		
		public ClientToServer (IFolderSet folders) {
			this.folders = folders;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			folders = FolderSet.readBuf(buf);
		}

		@Override
		public void toBytes(ByteBuf buf) {
			folders.writeBuf(buf);
		}
		
		public IFolderSet getFolders (){
			return this.folders;
		}
		
		public static class Handler implements IMessageHandler<ClientToServer, IMessage> {

		    @Override
		    public IMessage onMessage(ClientToServer message, MessageContext ctx) {
		    	EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		    	PacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
		    		IFolderSet folders = serverPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
					folders.setFolders(message.getFolders().getFolders());
				});
				return null;
		    }
		}
	}
	
	
	public static class ServerToClient implements IMessage {

		private IFolderSet folders;
		
		public ServerToClient () {}
		
		public ServerToClient (EntityPlayer player) {
			this.folders = player.getCapability(FolderSet.Provider.FOLDER_CAP, null);
		}
		
		public ServerToClient (IFolderSet folders) {
			this.folders = folders;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			folders = FolderSet.readBuf(buf);
		}

		@Override
		public void toBytes(ByteBuf buf) {
			folders.writeBuf(buf);
		}
		
		public IFolderSet getFolders (){
			return this.folders;
		}
		
		public static class Handler implements IMessageHandler<ServerToClient, IMessage> {

		    @Override
		    public IMessage onMessage(ServerToClient message, MessageContext ctx) {
		    	PacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
		    		EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
					IFolderSet folders = clientPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
					folders.setFolders(message.getFolders().getFolders());
				});
				return null;
		    }
		}
	}
	
}
