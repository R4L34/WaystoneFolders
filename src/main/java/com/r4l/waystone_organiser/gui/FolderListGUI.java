package com.r4l.waystone_organiser.gui;

import org.lwjgl.opengl.GL11;

import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.gui.buttons.BackButton;
import com.r4l.waystone_organiser.gui.buttons.ChangeNameButton;
import com.r4l.waystone_organiser.gui.buttons.DeleteButton;
import com.r4l.waystone_organiser.gui.buttons.FolderButton;
import com.r4l.waystone_organiser.gui.buttons.PlusButton;
import com.r4l.waystone_organiser.gui.buttons.SortButton;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.network.message.SwapMessage;
import com.r4l.waystone_organiser.util.Folder;
import com.r4l.waystone_organiser.util.SortDirection;
import com.r4l.waystone_organiser.util.WaystoneData;

import net.blay09.mods.waystones.client.gui.GuiWaystoneList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.Collections;

public class FolderListGUI extends GuiScreen {
	
	private IFolderSet folders;
	
	private WaystoneData waystoneData;
	
	private GuiButton btnPrevPage;
	
    private GuiButton btnNextPage;
    
    private PlusButton btnNewFolder;
    
    private int pageOffset;
    
    private int headerY;
    
    private int buttonsPerPage;
    
    private int buttonCount = 0;
	
	public FolderListGUI (WaystoneData data) {
		this.folders = data.folders;
		waystoneData = data;
	}
	
	
	@Override
    public void initGui() {
        btnPrevPage = new GuiButton(buttonCount++, width / 2 - 100, height / 2 + 40, 95, 20, "Previous");
        buttonList.add(btnPrevPage);

        btnNextPage = new GuiButton(buttonCount++, width / 2 + 5, height / 2 + 40, 95, 20, "Next" );
        buttonList.add(btnNextPage);
        
        btnNewFolder = new PlusButton(buttonCount++, width / 2 + 170, height / 2 - 30);
		buttonList.add(btnNewFolder);
		
		BackButton btnGoBack = new BackButton(buttonCount++, new GuiWaystoneList(waystoneData.entries, waystoneData.warpMode, waystoneData.hand, waystoneData.fromWaystone));
		buttonList.add(btnGoBack);

        updateList();
    }
	
	
	public void updateList() {
		buttonCount = 10;
		final int maxContentHeight = (int) (height * 0.8f);
        final int headerHeight = 40;
        final int footerHeight = 25;
        final int entryHeight = 25;
        final int maxButtonsPerPage = (maxContentHeight - headerHeight - footerHeight) / entryHeight;

        buttonsPerPage = Math.max(4, Math.min(maxButtonsPerPage, folders.getFolders().size()));
        final int contentHeight = headerHeight + buttonsPerPage * entryHeight + footerHeight;
        headerY = height / 2 - contentHeight / 2;

        btnPrevPage.enabled = pageOffset > 0;
        btnNextPage.enabled = pageOffset < (folders.getFolders().size() - 1) / buttonsPerPage;
        
        buttonList.removeIf(button -> button instanceof FolderButton || button instanceof SortButton ||  button instanceof DeleteButton || button instanceof ChangeNameButton);
        int y = headerHeight;
        for (int i = 0; i < buttonsPerPage; i++) {
            int entryIndex = pageOffset * buttonsPerPage + i;
            if (entryIndex >= 0 && entryIndex < folders.getFolders().size()) {
            	//FolderButton
            	FolderButton btnFolder = new FolderButton(buttonCount++, width / 2 - 100, headerY + y, folders.getFolders().get(entryIndex));
            	//SortButtons
            	SortButton sortUp = new SortButton(buttonCount++, width / 2 - 110, headerY + y + 2, headerY + y, entryIndex, SortDirection.UP);
                if (entryIndex == 0) {
                    sortUp.visible = false;
                }
                SortButton sortDown = new SortButton(buttonCount++, width / 2 - 110, headerY + y + 11, headerY + y, entryIndex, SortDirection.DOWN);
                if (entryIndex == folders.getFolders().size() - 1) {
                    sortDown.visible = false;
                }
                //DeleteButtons
                DeleteButton btnDeleteFolder = new DeleteButton(buttonCount++, width / 2 - 100  + 220, headerY + y, folders.getFolders().get(entryIndex).getId());
                
                //ChangeNameButtons
                ChangeNameButton btnChangeName = new ChangeNameButton(buttonCount++, width / 2 - 100  + 200, headerY + y, folders.getFolders().get(entryIndex).getId());
                
                
                buttonList.add(btnFolder);
                buttonList.add(sortUp);
                buttonList.add(sortDown);
                buttonList.add(btnDeleteFolder);
                buttonList.add(btnChangeName);
            	
                y += 22;
            }
        }
        
        btnPrevPage.y = headerY + headerHeight + buttonsPerPage * 22 + (folders.getFolders().size() > 0 ? 10 : 0);
        btnNextPage.y = headerY + headerHeight + buttonsPerPage * 22 + (folders.getFolders().size() > 0 ? 10 : 0);
    }

	
		@Override
	    protected void actionPerformed(GuiButton button) {
	 		if(button == btnNextPage) {
				pageOffset++;
				updateList();
			} else if(button == btnPrevPage) {
				pageOffset--;
				updateList();
			}else if (button instanceof SortButton) {
				SortButton btn = (SortButton) button;
	            
	            int index = btn.getIndex();
	            int sortDir = btn.getSortDir().toInt();
	            int otherIndex = index + sortDir;
	            if (index == -1 || otherIndex < 0 || otherIndex >= folders.getFolders().size()) {
	                return;
	            }
	            
	            Collections.swap(folders.getFolders(), index, otherIndex);
	            PacketHandler.channel.sendToServer(new SwapMessage(index, otherIndex));

	            updateList();
	        } else if (button == btnNewFolder) {
	        	mc.displayGuiScreen(new FolderNameGUI(waystoneData));
	        } else if (button instanceof DeleteButton) {
	        	int folder_id = ((DeleteButton)button).getFolderId();
	        	mc.displayGuiScreen(new ConfirmDeleteGUI(waystoneData, folder_id));
	        } else if (button instanceof ChangeNameButton) {
	        	int folder_id = ((ChangeNameButton)button).getFolderId();
	        	mc.displayGuiScreen(new FolderNameGUI(waystoneData, folder_id));
	        } else if (button instanceof FolderButton) {
	        	Folder folder = ((FolderButton) button).getFolder();
	        	mc.displayGuiScreen(new EntryListGUI(waystoneData, folder));
	        }
	 		
	 		
	 	}
	
	
	
	 @Override
	    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	        drawWorldBackground(0);
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        GL11.glColor4f(1f, 1f, 1f, 1f);
	        drawCenteredString(fontRenderer, "Folders", width / 2, height / 2 - 85, 0xFFFFFF);
	    }

}
