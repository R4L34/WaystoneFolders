package com.r4l.waystone_organiser.event.handler;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.network.message.BugfixMessage;
import com.r4l.waystone_organiser.util.Utils;

import net.blay09.mods.waystones.client.gui.GuiButtonRemoveWaystone;
import net.blay09.mods.waystones.client.gui.GuiWaystoneList;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DeleteEntryBugfix {
	
	@SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Pre event) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		
		if (event.getButton() instanceof GuiButtonRemoveWaystone) {
			 GuiButtonRemoveWaystone button = (GuiButtonRemoveWaystone) event.getButton();
			 GuiWaystoneList gui = (GuiWaystoneList) event.getGui();
			 
			 WaystoneEntry[] entries = Utils.getField("entries", gui, WaystoneEntry[].class);
			 WaystoneEntry[] new_entries = new WaystoneEntry[entries.length - 1];
				int count = 0;
				for (int i = 0; i < entries.length; i++) {
					if (!Utils.compareWaystones(button.getWaystone(), entries[i])) {
						new_entries[i - count] = entries[i];
					} else {
						count ++;
					}
				}
				
			Utils.setField("entries", gui, new_entries);
			PacketHandler.channel.sendToServer(new BugfixMessage(button.getWaystone()));
			gui.updateList();
			event.setCanceled(true);
		 }
	}

}