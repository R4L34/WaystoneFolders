package com.r4l.waystone_organiser.gui;

import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.r4l.waystone_organiser.gui.buttons.BackButton;
import com.r4l.waystone_organiser.gui.buttons.PlusButton;
import com.r4l.waystone_organiser.gui.buttons.RemoveEntryButton;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.network.message.CheckMessage;
import com.r4l.waystone_organiser.network.message.DeleteMessage;
import com.r4l.waystone_organiser.network.message.SwapMessage;
import com.r4l.waystone_organiser.util.Folder;
import com.r4l.waystone_organiser.util.Utils;
import com.r4l.waystone_organiser.util.WaystoneData;

import net.blay09.mods.waystones.client.gui.GuiButtonSortWaystone;
import net.blay09.mods.waystones.client.gui.GuiButtonWaystoneEntry;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.blay09.mods.waystones.network.message.MessageTeleportToWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class EntryListGUI extends GuiScreen{
	
	private WaystoneData waystoneData;
	
	private Folder folder;
	
	private List<WaystoneEntry> entries;
	
	private int buttonCount = 0;
	
	private PlusButton btnNewEntries;
	
	private GuiButton btnPrevPage;
    private GuiButton btnNextPage;
    private int pageOffset;
    private int headerY;
    private int buttonsPerPage;
	
	public EntryListGUI (WaystoneData waystoneData, Folder folder) {
		this.waystoneData = waystoneData;
		this.folder = folder;
		entries = folder.getEntries();
	}

	    @Override
	    public void initGui() {
	        btnPrevPage = new GuiButton(buttonCount++, width / 2 - 100, height / 2 + 40, 95, 20, "Previous");
	        buttonList.add(btnPrevPage);

	        btnNextPage = new GuiButton(buttonCount++, width / 2 + 5, height / 2 + 40, 95, 20, "Next");
	        buttonList.add(btnNextPage);
	        
	        btnNewEntries = new PlusButton(buttonCount++, width / 2 + 150, height / 2 - 30);
			buttonList.add(btnNewEntries);
			
			BackButton btnGoBack = new BackButton(buttonCount++, new FolderListGUI(waystoneData));
			buttonList.add(btnGoBack);
			
			//Removing waystones if they're not on the global list
			//Have to use this instead of a for loop since i remove value from an ArrayList
			 entries.removeIf(entry -> {
			    if (!Utils.containsWaystone(waystoneData.entries, entry)) {
			    	PacketHandler.channel.sendToServer(new DeleteMessage(folder.getId(), entry));
			        return true;
			    }
			    return false;
			});
			
	        updateList();
	    }

	    public void updateList() {
	    	buttonCount = 10;
	        final int maxContentHeight = (int) (height * 0.8f);
	        final int headerHeight = 40;
	        final int footerHeight = 25;
	        final int entryHeight = 25;
	        final int maxButtonsPerPage = (maxContentHeight - headerHeight - footerHeight) / entryHeight;

	        buttonsPerPage = Math.max(4, Math.min(maxButtonsPerPage, entries.size()));
	        final int contentHeight = headerHeight + buttonsPerPage * entryHeight + footerHeight;
	        headerY = height / 2 - contentHeight / 2;

	        btnPrevPage.enabled = pageOffset > 0;
	        btnNextPage.enabled = pageOffset < (entries.size() - 1) / buttonsPerPage;

	        buttonList.removeIf(button -> button instanceof GuiButtonWaystoneEntry || button instanceof GuiButtonSortWaystone || button instanceof RemoveEntryButton);

	        int y = headerHeight;
	        for (int i = 0; i < buttonsPerPage; i++) {
	            int entryIndex = pageOffset * buttonsPerPage + i;
	            if (entryIndex >= 0 && entryIndex < entries.size()) {
	                GuiButtonWaystoneEntry btnWaystone = new GuiButtonWaystoneEntry(buttonCount++, width / 2 - 100, headerY + y, entries.get(entryIndex), waystoneData.warpMode);
	                if (Utils.compareWaystones(entries.get(entryIndex), waystoneData.fromWaystone)) {
	                    btnWaystone.enabled = false;
	                }

	                GuiButtonSortWaystone sortUp = new GuiButtonSortWaystone(buttonCount++, width / 2 - 110, headerY + y + 2, btnWaystone, -1);
	                if (entryIndex == 0) {
	                    sortUp.visible = false;
	                }

	                GuiButtonSortWaystone sortDown = new GuiButtonSortWaystone(buttonCount++, width / 2 - 110, headerY + y + 11, btnWaystone, 1);
	                if (entryIndex == entries.size() - 1) {
	                    sortDown.visible = false;
	                }
	                
	                RemoveEntryButton btnRemoveEntry = new RemoveEntryButton(buttonCount++, width / 2 + 100, headerY + y, entries.get(entryIndex));
	                
	                
	                buttonList.add(btnWaystone);
	                buttonList.add(sortUp);
	                buttonList.add(sortDown);
	                buttonList.add(btnRemoveEntry);

	                y += 22;
	            }
	        }

	        btnPrevPage.y = headerY + headerHeight + buttonsPerPage * 22 + (entries.size() > 0 ? 10 : 0);
	        btnNextPage.y = headerY + headerHeight + buttonsPerPage * 22 + (entries.size() > 0 ? 10 : 0);
	    }

	    @Override
	    protected void actionPerformed(GuiButton button) {
	        if (button == btnNextPage) {
	            pageOffset = GuiScreen.isShiftKeyDown() ? (entries.size() - 1) / buttonsPerPage : pageOffset + 1;
	            updateList();
	        } else if (button == btnPrevPage) {
	            pageOffset = GuiScreen.isShiftKeyDown() ? 0 : pageOffset - 1;
	            updateList();
	        } else if (button instanceof GuiButtonWaystoneEntry) {
	        	WaystoneEntry entry = ((GuiButtonWaystoneEntry) button).getWaystone();
	        	
	        	
	        	//Checking if Waystone exists. If not deleting it on client and server.
	        	//Utils.checkWaystone(entry, folder.getId(), waystoneData.folders);
	        	PacketHandler.channel.sendToServer(new CheckMessage(entry, folder.getId()));
	        	
	        	//It has checking system built in.
	        	//Teleporting
	            NetworkHandler.channel.sendToServer(new MessageTeleportToWaystone(((GuiButtonWaystoneEntry) button).getWaystone(), waystoneData.warpMode, waystoneData.hand, waystoneData.fromWaystone));
	            mc.displayGuiScreen(null);
	        } else if (button instanceof GuiButtonSortWaystone) {
	            WaystoneEntry waystoneEntry = ((GuiButtonSortWaystone) button).getWaystone();
	            int index = entries.indexOf(waystoneEntry);
	            int sortDir = ((GuiButtonSortWaystone) button).getSortDir();
	            int otherIndex = index + sortDir;
	            if (GuiScreen.isShiftKeyDown()) {
	                otherIndex = sortDir == -1 ? 0 : entries.size() - 1;
	            }
	            if (index == -1 || otherIndex < 0 || otherIndex >= entries.size()) {
	                return;
	            }

	            Collections.swap(entries, index, otherIndex);
	            
	            //Send swap
	            PacketHandler.channel.sendToServer(new SwapMessage(folder.getId(), index, otherIndex));
	            updateList();
	        } else if (button == btnNewEntries) {
	        	mc.displayGuiScreen(new AddEntriesGUI(waystoneData, folder));
	        } else if (button instanceof RemoveEntryButton) {
	        	WaystoneEntry waystoneEntry = ((RemoveEntryButton) button).getWaystone();
	        	entries.remove(waystoneEntry);
	        	PacketHandler.channel.sendToServer(new DeleteMessage(folder.getId(), waystoneEntry));
	        	updateList();
	        }
	    }
	    
	    
	    @Override
	    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	        drawWorldBackground(0);
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        GL11.glColor4f(1f, 1f, 1f, 1f);
	        drawCenteredString(fontRenderer, "Folder: " + folder.getName(), width / 2, height / 2 - 85, 0xFFFFFF);
	    }
	
}
