package com.r4l.waystone_organiser.capability;

import java.util.List;

import com.r4l.waystone_organiser.util.Folder;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.nbt.NBTTagList;

public interface IFolderSet {

	public List<Folder> getFolders();
	
	public void setFolders(List<Folder> folders);
	
	public Folder getFolder(int folder_id);
	
	public boolean addToFolder(WaystoneEntry entry, int folder_id);
	
	public boolean removeFromFolder(WaystoneEntry entry, int folder_id);
	
	public boolean removeFolder(int folder_id);
	
	public void addFolder(Folder folder);
	
	public NBTTagList writeNBT();
	
	public void writeBuf(ByteBuf buf);
	
	public int getFreeFolderId();
}
