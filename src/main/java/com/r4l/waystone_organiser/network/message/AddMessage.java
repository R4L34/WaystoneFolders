package com.r4l.waystone_organiser.network.message;

import com.r4l.waystone_organiser.capability.FolderSet;
import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.util.Folder;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AddMessage implements IMessage{
	
	private int folder_id;
	
	private Folder folder;
	
	private WaystoneEntry entry;
	
	public AddMessage () {}
	
	public AddMessage (Folder folder) {
		this.folder_id = -1;
		this.folder = folder;
	}
	
	public AddMessage (int folder_id, WaystoneEntry entry) {
		this.folder_id = folder_id;
		this.entry = entry;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		folder_id = buf.readInt();
		if (folder_id == -1) {
			folder = new Folder(ByteBufUtils.readUTF8String(buf), buf.readInt());
			return;
		}
		entry = WaystoneEntry.read(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(folder_id);
		//Write folder
		if (folder_id == -1) {
			//since new folder is always empty we will just send it's name and id
			ByteBufUtils.writeUTF8String(buf, folder.getName());
			buf.writeInt(folder.getId());
			return;
		}
		//or
		//Write entry
		entry.write(buf);
	}

	public Folder getFolder() {
		return folder;
	}

	public WaystoneEntry getEntry() {
		return entry;
	}
	
	public int getFolderId() {
		return folder_id;
	}

	public static class Handler implements IMessageHandler<AddMessage, IMessage> {

		@Override
		public IMessage onMessage(AddMessage message, MessageContext ctx) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
	    	PacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
	    		// Add Folder
	    		if (message.getFolderId() == -1) {
	    			IFolderSet folders = serverPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
	    			folders.addFolder(message.getFolder());
	    		} else {
	    		//Add entry
	    			IFolderSet folders = serverPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
	    			folders.addToFolder(message.getEntry(), message.getFolderId());
	    		}
	    		
			});
			return null;
		}
		
	}
}
