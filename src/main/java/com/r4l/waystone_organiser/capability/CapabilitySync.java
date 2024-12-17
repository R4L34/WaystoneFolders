package com.r4l.waystone_organiser.capability;

import com.r4l.waystone_organiser.capability.entries.EntryProvider;
import com.r4l.waystone_organiser.capability.entries.IEntry;
import com.r4l.waystone_organiser.capability.folders.FolderProvider;
import com.r4l.waystone_organiser.capability.folders.IFolder;
import com.r4l.waystone_organiser.network.OrganiserPacketHandler;
import com.r4l.waystone_organiser.network.message.ServerToClient;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilitySync {
	
	 @SubscribeEvent
	 public void EntityJoinWorldEvent(EntityJoinWorldEvent event) {
		 	Entity entity = event.getEntity();
	        if (entity.world.isRemote || !(entity instanceof EntityPlayerMP)) return;
	        EntityPlayer player = (EntityPlayer) entity;

	        OrganiserPacketHandler.channel.sendTo(new ServerToClient(player), (EntityPlayerMP) player);

	}
	
	
	@SubscribeEvent
    public void onPlayerClone(Clone event)
    {
        EntityPlayer player = event.getEntityPlayer();
        
        IFolder folder = player.getCapability(FolderProvider.FOLDER_CAP, null);
        IFolder old_folder = event.getOriginal().getCapability(FolderProvider.FOLDER_CAP, null);
        
        IEntry entry = player.getCapability(EntryProvider.ENTRY_CAP, null);
        IEntry old_entry = event.getOriginal().getCapability(EntryProvider.ENTRY_CAP, null);
        

        folder.setFolders(old_folder.getFolders());
        entry.setEntries(old_entry.getEntries());
    }

}
