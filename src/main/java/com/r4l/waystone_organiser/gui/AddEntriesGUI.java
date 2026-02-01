package com.r4l.waystone_organiser.gui;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.r4l.waystone_organiser.gui.buttons.AddEntryButton;
import com.r4l.waystone_organiser.gui.buttons.BackButton;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.network.message.AddMessage;
import com.r4l.waystone_organiser.util.Folder;
import com.r4l.waystone_organiser.util.Utils;
import com.r4l.waystone_organiser.util.WaystoneData;

import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class AddEntriesGUI extends GuiScreen{
	
	private WaystoneData waystoneData;
	
	private Folder folder;
	
	private List<WaystoneEntry> entries;
	
	private GuiButton btnPrevPage;
    private GuiButton btnNextPage;
	
	private int buttonCount = 0;
	
    private int pageOffset;
    
    private int headerY;
    
    private int buttonsPerPage;
    
    private final int headerHeight = 40;
	
	public AddEntriesGUI (WaystoneData waystoneData, Folder folder) {
		this.waystoneData = waystoneData;
		this.folder = folder;
		entries = Arrays.asList(waystoneData.entries);
	}
	
	
	@Override
    public void initGui() {
        btnPrevPage = new GuiButton(buttonCount++, width / 2 - 100, height / 2 + 40, 95, 20, "Previous");
        buttonList.add(btnPrevPage);

        btnNextPage = new GuiButton(buttonCount++, width / 2 + 5, height / 2 + 40, 95, 20, "Next");
        buttonList.add(btnNextPage);
        
        BackButton btnGoBack = new BackButton(buttonCount++, new EntryListGUI(waystoneData, folder));
		buttonList.add(btnGoBack);

        updateList();
    }
	
	
	
	public void updateList() {
		buttonCount = 10;
		final int maxContentHeight = (int) (height * 0.8f);
        final int footerHeight = 25;
        final int entryHeight = 25;
        final int maxButtonsPerPage = (maxContentHeight - headerHeight - footerHeight) / entryHeight;

        buttonsPerPage = Math.max(4, Math.min(maxButtonsPerPage, entries.size()));
        final int contentHeight = headerHeight + buttonsPerPage * entryHeight + footerHeight;
        headerY = height / 2 - contentHeight / 2;

        btnPrevPage.enabled = pageOffset > 0;
        btnNextPage.enabled = pageOffset < (entries.size() - 1) / buttonsPerPage;
        
        buttonList.removeIf(button -> button instanceof AddEntryButton);
        int y = headerHeight;
        for (int i = 0; i < buttonsPerPage; i++) {
            int entryIndex = pageOffset * buttonsPerPage + i;
            if (entryIndex >= 0 && entryIndex < entries.size()) {
            	if(!Utils.containsWaystone(folder.getEntries(), entries.get(entryIndex))) {
            		AddEntryButton btnEntry = new AddEntryButton(buttonCount++, width / 2 + 100, headerY + y, entries.get(entryIndex));
            		buttonList.add(btnEntry);
            	}
                y += 22;
            }
        }
        
        btnPrevPage.y = headerY + headerHeight + buttonsPerPage * 22 + (entries.size() > 0 ? 10 : 0);
        btnNextPage.y = headerY + headerHeight + buttonsPerPage * 22 + (entries.size() > 0 ? 10 : 0);
        updateStringList(buttonsPerPage);
    }
	
	
	public void updateStringList(int entriesPerPage) {
		int y = headerHeight;
		for(int i = 0; i < entriesPerPage; i++) {
			int entryIndex = pageOffset * entriesPerPage + i;
			if(entryIndex >= 0 && entryIndex < entries.size()) {
				drawCenteredString(fontRenderer, entries.get(entryIndex).getName(), width / 2, headerY + y, 0xFFFFFF);
				y += 22;
			}
		}
		
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button){
		if(button == btnNextPage) {
			pageOffset++;
			updateList();
		} else if(button == btnPrevPage) {
			pageOffset--;
			updateList();
		} else if(button instanceof AddEntryButton) {
			WaystoneEntry entry = ((AddEntryButton)button).getWaystone();
			folder.addEntry(entry);
			PacketHandler.channel.sendToServer(new AddMessage(folder.getId(), entry));
			updateList();
		}
	}
	
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawWorldBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glColor4f(1, 1, 1, 1);
		drawCenteredString(fontRenderer, "Add Entries For: " + folder.getName(), width / 2, height / 2 - 85, 0xFFFFFF);
		updateStringList(buttonsPerPage);
	}
	
	
	

}
