package com.r4l.waystone_organiser.proxy;

import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.client.gui.GuiButtonRemoveWaystone;
import net.blay09.mods.waystones.client.gui.GuiWaystoneList;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

import com.r4l.waystone_organiser.capability.entries.EntryProvider;
import com.r4l.waystone_organiser.capability.entries.IEntry;
import com.r4l.waystone_organiser.gui.FoldersListGUI;
import com.r4l.waystone_organiser.gui.buttons.MainButton;
import com.r4l.waystone_organiser.network.OrganiserPacketHandler;
import com.r4l.waystone_organiser.network.message.RemoveWaystoneMessage;
import com.r4l.waystone_organiser.reference.functions.WaystoneFunctions;

public class ClientProxy extends CommonProxy {

	private MainButton buttonWarp;
	
	@SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiWaystoneList) {
            buttonWarp = new MainButton(event.getGui().width, event.getGui().height);
            event.getButtonList().add(buttonWarp);
        }
    }
	

	@SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) throws IllegalArgumentException, IllegalAccessException { 
		
        if (event.getButton() instanceof MainButton) {
        	
        	WaystoneEntry[] entries = null;
        	WarpMode warpMode = null;
        	EnumHand hand = null;
        	WaystoneEntry fromWaystone = null;
        	
        	GuiWaystoneList gui = (GuiWaystoneList) event.getGui();
        	Minecraft mc = Minecraft.getMinecraft();
    		
    		Field[] gui_fields = gui.getClass().getDeclaredFields();
    		
    		
    		
    		for(Field field : gui_fields) {
    			
    			if (field.getName().equals("entries")) {
    				field.setAccessible(true);
    				entries = (WaystoneEntry[]) field.get(gui);
    			}else if (field.getName().equals("warpMode")) {
    				field.setAccessible(true);
    				warpMode = (WarpMode) field.get(gui);
    			} else if (field.getName().equals("hand")) {
    				field.setAccessible(true);
    				hand = (EnumHand) field.get(gui);
    			} else if (field.getName().equals("fromWaystone")) {
    				field.setAccessible(true);
    				fromWaystone = (WaystoneEntry) field.get(gui);
    			}
    		}
    		
    		mc.displayGuiScreen(new FoldersListGUI(Minecraft.getMinecraft().player, entries, warpMode, hand, fromWaystone));
        	
    		
        }
    }
	
	
	@SubscribeEvent
    public void onDeleteWaystone(GuiScreenEvent.ActionPerformedEvent.Pre event) throws IllegalArgumentException, IllegalAccessException {
		
        if (event.getButton() instanceof GuiButtonRemoveWaystone) {
        	//Deleting it from all folders
			 GuiButtonRemoveWaystone button = (GuiButtonRemoveWaystone) event.getButton();
			 GuiWaystoneList gui = (GuiWaystoneList) event.getGui();

		     EntityPlayer playerSP = Minecraft.getMinecraft().player;
		     IEntry entry = playerSP.getCapability(EntryProvider.ENTRY_CAP, null);
		        	
		     String name = WaystoneFunctions.getUniqueWaystone(button.getWaystone());
					
		     for(int i = 0; i < entry.size(); i++) {
		    	 if (WaystoneFunctions.getUniqueWaystone(entry.get(i)).equals(name)) {
					entry.remove(i);
		    	 }
		     }

			 
			 //Bugfix
			 WaystoneEntry[] entries = null;
			 WaystoneEntry[] new_entries = null;
			 Field[] gui_fields = gui.getClass().getDeclaredFields();
			 
			 for(Field field : gui_fields) {
	    			
	    			if (field.getName().equals("entries")) {
	    				field.setAccessible(true);
	    				
	    				entries = (WaystoneEntry[]) field.get(gui);
	    				new_entries = new WaystoneEntry[entries.length - 1];
	    				int count = 0;
	    				for (int i = 0; i < entries.length; i++) {
	    					if (!button.getWaystone().equals(entries[i])) {
	    						new_entries[i - count] = entries[i];
	    					} else {
	    						count ++;
	    					}
	    					
	    				}
	    				field.set(gui, new_entries);
	    				
	    			}
			 }	
			 OrganiserPacketHandler.channel.sendToServer(new RemoveWaystoneMessage.ClientToServer(button.getWaystone()));
			 gui.updateList();
			 event.setCanceled(true);
		 }
	}
	
}

