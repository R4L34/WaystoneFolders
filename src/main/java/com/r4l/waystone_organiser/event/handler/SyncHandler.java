package com.r4l.waystone_organiser.event.handler;

import com.r4l.waystone_organiser.capability.FolderSet;
import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.network.message.SyncMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SyncHandler {

	

	@SubscribeEvent
	 public void onJoinWorld(EntityJoinWorldEvent event) {
		 	Entity entity = event.getEntity();
	        if (entity.world.isRemote || !(entity instanceof EntityPlayerMP)) return;
	        EntityPlayerMP player = (EntityPlayerMP) entity;

	        PacketHandler.channel.sendTo(new SyncMessage.ServerToClient(player), player);
	}
	
	
	@SubscribeEvent
   public void onPlayerClone(PlayerEvent.Clone event)
   {
       EntityPlayer player = event.getEntityPlayer();
       
       IFolderSet folder = player.getCapability(FolderSet.Provider.FOLDER_CAP, null);
       IFolderSet old_folder = event.getOriginal().getCapability(FolderSet.Provider.FOLDER_CAP, null);
       

       folder.setFolders(old_folder.getFolders());
   }
	
}
