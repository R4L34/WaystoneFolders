package com.r4l.waystone_organiser.network.message;

import java.util.ArrayList;
import java.util.List;

import com.r4l.waystone_organiser.capability.entries.EntryProvider;
import com.r4l.waystone_organiser.capability.entries.IEntry;
import com.r4l.waystone_organiser.capability.folders.FolderProvider;
import com.r4l.waystone_organiser.capability.folders.IFolder;
import com.r4l.waystone_organiser.network.OrganiserPacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerToClient implements IMessage {

	private List<String> folders;
	private List<String> entries;
	
	private int folders_count;
	private int entry_count;
	
	
	public ServerToClient() {}
	
	public ServerToClient(EntityPlayer server_player) {
		this.folders = server_player.getCapability(FolderProvider.FOLDER_CAP, null).getFolders();
		this.folders_count = folders.size();
		
		this.entries = server_player.getCapability(EntryProvider.ENTRY_CAP, null).getEntries();
		this.entry_count = entries.size();
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
	
	public static class Handler implements IMessageHandler<ServerToClient, IMessage> {

		@Override
		public IMessage onMessage(ServerToClient message, MessageContext ctx) {
			OrganiserPacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
					Minecraft mc = Minecraft.getMinecraft();
					
					IFolder old_folders = mc.player.getCapability(FolderProvider.FOLDER_CAP, null);
				    List<String> new_folders = message.getFolders();   
				    
				    IEntry old_entries = mc.player.getCapability(EntryProvider.ENTRY_CAP, null);
				    List<String> new_entries = message.getEntries();
				    
				    old_folders.setFolders(new_folders);
				    old_entries.setEntries(new_entries);

			});
			return null;
		}
	}

}