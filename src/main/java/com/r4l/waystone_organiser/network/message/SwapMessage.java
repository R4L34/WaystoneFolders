package com.r4l.waystone_organiser.network.message;

import java.util.Collections;

import com.r4l.waystone_organiser.capability.FolderSet;
import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.util.Folder;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SwapMessage implements IMessage{
	
	private int folder_id;
	
	private int index1;
	
	private int index2;
	
	public SwapMessage () {}
	
	public SwapMessage (int index1, int index2) {
		this.folder_id = -1;
		this.index1 = index1;
		this.index2 = index2;
	}
	
	public SwapMessage (int folder_id, int index1, int index2) {
		this.folder_id = folder_id;
		this.index1 = index1;
		this.index2 = index2;
	}

	public int getIndex1() {
		return index1;
	}

	public int getIndex2() {
		return index2;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		folder_id = buf.readInt();
		index1 = buf.readInt();
		index2 = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(folder_id);
		buf.writeInt(index1);
		buf.writeInt(index2);
	}
	
	public int getFolderId() {
		return folder_id;
	}

	public static class Handler implements IMessageHandler<SwapMessage, IMessage> {

		@Override
		public IMessage onMessage(SwapMessage message, MessageContext ctx) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
	    	PacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
	    		//Swap folders
	    		if(message.getFolderId() == -1) {
	    			IFolderSet folders = serverPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
	    			Collections.swap(folders.getFolders(), message.getIndex1(), message.getIndex2());
	    		} else {
	    		//Swap entries
	    			IFolderSet folders = serverPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
	    			Folder folder = folders.getFolder(message.getFolderId());
	    			Collections.swap(folder.getEntries(), message.getIndex1(), message.getIndex2());
	    		}
			});
			return null;
		}
		
	}

}
