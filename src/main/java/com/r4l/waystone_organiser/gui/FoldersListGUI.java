package com.r4l.waystone_organiser.gui;

import java.util.Iterator;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.r4l.waystone_organiser.gui.buttons.BackButton;
import com.r4l.waystone_organiser.gui.buttons.ChangeNameButton;
import com.r4l.waystone_organiser.gui.buttons.DeleteButton;
import com.r4l.waystone_organiser.gui.buttons.FolderButton;
import com.r4l.waystone_organiser.gui.buttons.PlusButton;
import com.r4l.waystone_organiser.gui.buttons.SortButton;
import com.r4l.waystone_organiser.network.OrganiserPacketHandler;
import com.r4l.waystone_organiser.network.message.ClientToServer;
import com.r4l.waystone_organiser.reference.functions.FolderFunctions;
import com.r4l.waystone_organiser.capability.entries.EntryProvider;
import com.r4l.waystone_organiser.capability.entries.IEntry;
import com.r4l.waystone_organiser.capability.folders.FolderProvider;
import com.r4l.waystone_organiser.capability.folders.IFolder;

import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.client.gui.GuiWaystoneList;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class FoldersListGUI extends GuiScreen {
	
	private EntityPlayer player;
	
	private IFolder folder;
	private IEntry entry;
	private int folders_count;
	private WaystoneEntry[] waystone_entries;
	
	private GuiButton btnPrevPage;
	private GuiButton btnNextPage;
	private GuiButton btnNewFolder;
	private GuiButton btnGoBack;
	
	private int buttonCount = 4;
	
	private int pageOffset;
	
	private final WarpMode warpMode;
	private final EnumHand hand;
	private final WaystoneEntry fromWaystone;
	
	public FoldersListGUI (EntityPlayer player, WaystoneEntry[] waystone_entries, WarpMode warpMode, EnumHand hand, @Nullable WaystoneEntry fromWaystone) {
		this.player = player;
		this.folder = player.getCapability(FolderProvider.FOLDER_CAP, null);
		this.entry = player.getCapability(EntryProvider.ENTRY_CAP, null);
		this.folders_count = folder.size();
		this.waystone_entries = waystone_entries;
		
		this.warpMode = warpMode;
        this.hand = hand;
        this.fromWaystone = fromWaystone;
	}
	
	@Override
	public void initGui () {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		btnPrevPage = new GuiButton(0, width / 2 - 100, height / 2 + 40, 95, 20, "Previous Page");
		buttonList.add(btnPrevPage);

		btnNextPage = new GuiButton(1, width / 2 + 5, height / 2 + 40, 95, 20, "Next Page");
		buttonList.add(btnNextPage);
		
		btnNewFolder = new PlusButton(2, width / 2 + 170, height / 2 - 30);
		buttonList.add(btnNewFolder);
		
		btnGoBack = new BackButton(3);
		buttonList.add(btnGoBack);
		
		updateList();
	}
	
	public void updateList() {
		final int buttonsPerPage = 4;
		
		btnPrevPage.enabled = pageOffset > 0;
		btnNextPage.enabled = pageOffset < folders_count / buttonsPerPage;
		
		@SuppressWarnings("rawtypes")
		Iterator it = buttonList.iterator();
		while(it.hasNext()) {
			Object next = it.next();
			if (next instanceof FolderButton || next instanceof ChangeNameButton || next instanceof DeleteButton || next instanceof SortButton) {
				it.remove();
			}
		}
		
		
			
		int y = 0;
		int j = 0;
		for(int i = 0; i < buttonsPerPage; i++) {
			int entryIndex = pageOffset * buttonsPerPage + i;
			if(entryIndex >= 0 && entryIndex < folders_count) {
				if(j != 0) { j++; }
				FolderButton btnFolder = new FolderButton(buttonCount + j, width / 2 - 100, height / 2 - 60 + y, folder.get(entryIndex));
				j++;
				ChangeNameButton btnChangeName = new ChangeNameButton (buttonCount + j, width / 2 + 100, height / 2 - 60 + y, folder.get(entryIndex));
				j++;
				DeleteButton btnDeleteFolder = new DeleteButton(buttonCount + j, width / 2 + 120, height / 2 - 60 + y, folder.get(entryIndex));
				j++;
				SortButton sortUp = new SortButton(buttonCount + j, width / 2 - 110, height / 2 - 60 + y + 2, btnFolder, -1);
                if (entryIndex == 0) {
                    sortUp.visible = false;
                }
                j++;
                SortButton sortDown = new SortButton(buttonCount + j, width / 2 - 110, height / 2 - 60 + y + 11, btnFolder, 1);
                if (entryIndex == folders_count - 1) {
                    sortDown.visible = false;
                }
				
				
				
				buttonList.add(btnFolder);
				buttonList.add(btnChangeName);
				buttonList.add(btnDeleteFolder);
				buttonList.add(sortUp);
				buttonList.add(sortDown);
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
		} else if(button == btnNewFolder) {
			mc.displayGuiScreen(new FolderNameGUI(player, waystone_entries, warpMode, hand, fromWaystone));
		}else if(button == btnGoBack) {
			mc.displayGuiScreen(new GuiWaystoneList(waystone_entries, warpMode, hand, fromWaystone));
			//mc.displayGuiScreen(null);
		} else if(button instanceof FolderButton) {
			mc.displayGuiScreen(new FolderGUI(player, ((FolderButton)button).getId(), this.waystone_entries, warpMode, hand, fromWaystone));
		} else if(button instanceof ChangeNameButton) {
			mc.displayGuiScreen(new FolderNameGUI(player, waystone_entries, ((ChangeNameButton)button).getId(), warpMode, hand, fromWaystone));
		}else if(button instanceof DeleteButton) {
			mc.displayGuiScreen(new ConfirmDelete(player, ((DeleteButton)button).getId(), this.waystone_entries, warpMode, hand, fromWaystone));
		}else if (button instanceof SortButton) {
			int sortDir = ((SortButton) button).getSortDir();
			String folder11 = FolderFunctions.getFolderById(folder.getFolders(),((SortButton) button).getFolderId());
			
			if (sortDir == -1) {
				for(int i = 0; i < folder.size(); i++) {
					if (folder.get(i).equals(folder11)) {
						folder.swap(i, i - 1);
						break;
					}
				}	
			}else if (sortDir == 1){
				for(int i = 0; i < folder.size(); i++) {
					if (folder.get(i).equals(folder11)) {
						folder.swap(i, i + 1);
						break;
					}
				}	
			}
			OrganiserPacketHandler.channel.sendToServer(new ClientToServer(folder, entry));
			updateList();
		}
	}
		

	@Override
	public void updateScreen() {
		super.updateScreen();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawWorldBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glColor4f(1, 1, 1, 1);
		drawCenteredString(fontRenderer, "Folders", width / 2, height / 2 - 85, 0xFFFFFF);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
