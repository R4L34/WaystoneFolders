package com.r4l.waystone_organiser.network.message;

import com.r4l.waystone_organiser.capability.FolderSet;
import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.util.Folder;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RenameMessage implements IMessage {
	
	private String new_name;
	
	private int folder_id;
	
	public RenameMessage () {}
	
	public RenameMessage (String new_name, int folder_id) {
		this.new_name = new_name;
		this.folder_id = folder_id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		new_name = ByteBufUtils.readUTF8String(buf);
		folder_id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, new_name);
		buf.writeInt(folder_id);
	}

	public String getName() {
		return new_name;
	}

	public int getFolderId() {
		return folder_id;
	}
	
	
	public static class Handler implements IMessageHandler<RenameMessage, IMessage> {

		@Override
		public IMessage onMessage(RenameMessage message, MessageContext ctx) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
	    	PacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
	    		IFolderSet folders = serverPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
	    		Folder folder = folders.getFolder(message.getFolderId());
	    		folder.setName(message.getName());
			});
			return null;
		}
		
	}

}
