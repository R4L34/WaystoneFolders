package com.r4l.waystone_organiser.proxy;

import com.r4l.waystone_organiser.capability.entries.EntryStorage;
import com.r4l.waystone_organiser.capability.CapabilitySync;
import com.r4l.waystone_organiser.capability.entries.Entry;
import com.r4l.waystone_organiser.capability.entries.EntryHandler;
import com.r4l.waystone_organiser.capability.entries.IEntry;
import com.r4l.waystone_organiser.capability.folders.Folder;
import com.r4l.waystone_organiser.capability.folders.FolderHandler;
import com.r4l.waystone_organiser.capability.folders.FolderStorage;
import com.r4l.waystone_organiser.capability.folders.IFolder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CommonProxy  {
	
@SuppressWarnings("deprecation")
public void subscribeHandler() {
		//Register Events
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new CapabilitySync());
		MinecraftForge.EVENT_BUS.register(new FolderHandler());
		MinecraftForge.EVENT_BUS.register(new EntryHandler());	
		
		//Register Capabilities
		CapabilityManager.INSTANCE.register(IFolder.class, new FolderStorage(), Folder.class);
		CapabilityManager.INSTANCE.register(IEntry.class, new EntryStorage(), Entry.class);
	}

}
