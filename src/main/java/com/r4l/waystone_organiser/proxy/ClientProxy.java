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
import com.r4l.waystone_organiser.reference.functions.WaystoneFunctions;

public class ClientProxy extends CommonProxy {

	private MainButton buttonWarp;
	
	@SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiWaystoneList) {
            buttonWarp = new MainButton();
            event.getButtonList().add(buttonWarp);
        }
    }
	

	@SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Pre event) throws IllegalArgumentException, IllegalAccessException { 
		
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
        	
    		
        } else if (event.getButton() instanceof GuiButtonRemoveWaystone) {
        	//Synchronise with the server
        	EntityPlayer playerSP = Minecraft.getMinecraft().player;
        	IEntry entry = playerSP.getCapability(EntryProvider.ENTRY_CAP, null);
        	GuiButtonRemoveWaystone button = (GuiButtonRemoveWaystone) event.getButton();
        	
        	String name = WaystoneFunctions.getUniqueWaystone(button.getWaystone());
			
			for(int i = 0; i < entry.size(); i++) {
				if (WaystoneFunctions.getUniqueWaystone(entry.get(i)).equals(name)) {
					entry.remove(i);
				}
			}
        }
    }	
	}
	
	
	
	
	
	
	
	
	
/*
	
	@SubscribeEvent
	public void onKeyInput (KeyInputEvent event){
		WaystoneEntry w1 = new WaystoneEntry("name1", 0, new BlockPos(0, 1, 9), false);
		WaystoneEntry w2 = new WaystoneEntry("name2", 0, new BlockPos(22, 33, 5), false);
		WaystoneEntry w3 = new WaystoneEntry("name3", 1, new BlockPos(0, 1, 9), false);
		WaystoneEntry w4 = new WaystoneEntry("name4", 1, new BlockPos(22, 33, 5), false);
		WaystoneEntry w5 = new WaystoneEntry("name5", 0, new BlockPos(98, 0, 98), false);
		WaystoneEntry w6 = new WaystoneEntry("name6", 0, new BlockPos(14, 0, 88), false);
		WaystoneEntry[] entries = {w1, w2, w3, w4, w5, w6};

		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = this.player;
		EntityPlayer playerSP = mc.player;
        IFolder folders = player.getCapability(FolderProvider.FOLDER_CAP, null);
        int folders_size = folders.size() - 1;
        
        IEntry Ientries = player.getCapability(EntryProvider.ENTRY_CAP, null);
        int Ientries_size = Ientries.size() - 1;

        
		if (Keybinds.GUI_KEY.isPressed())
		{
			mc.displayGuiScreen(new FoldersListGUI(player, entries, WarpMode.WAYSTONE, EnumHand.OFF_HAND, null));
			
		} 
		
		
		
		//Technical Shit
		else if (Keybinds.ADD_CAP_KEY.isPressed()) {
			//
			
		} else if (Keybinds.REMOVE_CAP_KEY.isPressed()) {
			Ientries.setEntries(new ArrayList<String>());
		} else if (Keybinds.SHOW_ALL_CAP_KEY.isPressed()) {
			for(int i = 0; i < Ientries.size(); i++) {
	        	String message =  Ientries.get(i);
	        	playerSP.sendMessage(new TextComponentString(message));
	        }
		}
	}
	
		
		
		*/


