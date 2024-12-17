package com.r4l.waystone_organiser.network.message;

import java.util.ArrayList;
import java.util.List;

import com.r4l.waystone_organiser.capability.entries.EntryProvider;
import com.r4l.waystone_organiser.capability.entries.IEntry;
import com.r4l.waystone_organiser.capability.folders.FolderProvider;
import com.r4l.waystone_organiser.capability.folders.IFolder;
import com.r4l.waystone_organiser.network.OrganiserPacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientToServer implements IMessage {

	private List<String> folders;
	private List<String> entries;
	
	private int folders_count;
	private int entry_count;
	
	public ClientToServer () {}
	
	public ClientToServer (IFolder folder, IEntry entry) {
		this.folders = folder.getFolders();
		this.folders_count = folder.size();
		
		this.entries = entry.getEntries();
		this.entry_count = entry.size();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		folders = new ArrayList<>();
		entries = new ArrayList<>();
		
		folders_count = buf.readInt();
		entry_count = buf.readInt();
		
		for (int i = 0; i < folders_count; i++) {
			folders.add(ByteBufUtils.readUTF8String(buf));
		}
		
		for (int i = 0; i < entry_count; i++) {
			entries.add(ByteBufUtils.readUTF8String(buf));
		}
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(folders_count);
		buf.writeInt(entry_count);
		for (int i = 0; i < folders_count; i++) {
			ByteBufUtils.writeUTF8String(buf, folders.get(i));
		}
		for (int i = 0; i < entry_count; i++) {
			ByteBufUtils.writeUTF8String(buf, entries.get(i));
		}
		
	}
	
	public List<String> getFolders (){
		return this.folders;
	}
	
	public List<String> getEntries() {
		return this.entries;
	}
	
	public static class Handler implements IMessageHandler<ClientToServer, IMessage> {

	    @Override
	    public IMessage onMessage(ClientToServer message, MessageContext ctx) {
	    	EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
	    	OrganiserPacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
				
		    	IFolder old_folders = serverPlayer.getCapability(FolderProvider.FOLDER_CAP, null);
			    List<String> new_folders = message.getFolders();
			    
			    IEntry old_entries = serverPlayer.getCapability(EntryProvider.ENTRY_CAP, null);
			    List<String> new_entries = message.getEntries();
			    
			    old_folders.setFolders(new_folders);
			    old_entries.setEntries(new_entries);
				
			});
			return null;

	    }
	}

}