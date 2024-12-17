package com.r4l.waystone_organiser.gui;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.r4l.waystone_organiser.capability.entries.EntryProvider;
import com.r4l.waystone_organiser.capability.entries.IEntry;
import com.r4l.waystone_organiser.capability.folders.FolderProvider;
import com.r4l.waystone_organiser.capability.folders.IFolder;
import com.r4l.waystone_organiser.gui.buttons.BackButton;
import com.r4l.waystone_organiser.gui.buttons.PlusButton;
import com.r4l.waystone_organiser.gui.buttons.RemoveEntryButton;
import com.r4l.waystone_organiser.gui.buttons.SortButton;
import com.r4l.waystone_organiser.gui.buttons.WaystoneButton;
import com.r4l.waystone_organiser.network.OrganiserPacketHandler;
import com.r4l.waystone_organiser.network.message.ClientToServer;
import com.r4l.waystone_organiser.reference.functions.FolderFunctions;
import com.r4l.waystone_organiser.reference.functions.WaystoneFunctions;

import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.blay09.mods.waystones.network.message.MessageTeleportToWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class FolderGUI extends GuiScreen{

	private EntityPlayer player;
	private IFolder folder;
	private IEntry entry;
	
	private String folder_name;
	private int folder_id;
	private WaystoneEntry[] waystone_entries;
	private List<WaystoneEntry> waystones_in_folder;
	private int waystones_in_folder_count;
	
	private GuiButton btnPrevPage;
	private GuiButton btnNextPage;
	private GuiButton btnNewEntries;
	private GuiButton btnGoBack;
	private GuiButton btnRemoveAll;
	
	private int buttonCount = 5;
	
	private int pageOffset;
	
	private final WarpMode warpMode;
	private final EnumHand hand;
	private final WaystoneEntry fromWaystone;
	
	
	public FolderGUI (EntityPlayer player, int folder_id, WaystoneEntry[] waystone_entries, WarpMode warpMode, EnumHand hand, @Nullable WaystoneEntry fromWaystone) {
		this.player = player;
		this.folder = player.getCapability(FolderProvider.FOLDER_CAP, null);
		this.entry = player.getCapability(EntryProvider.ENTRY_CAP, null);
		
		this.folder_name = FolderFunctions.getFolderName(FolderFunctions.getFolderById(this.folder.getFolders(), folder_id));
		this.folder_id = folder_id;
		
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
		
		btnNewEntries = new PlusButton(2, width / 2 + 150, height / 2 - 30);
		buttonList.add(btnNewEntries);
		
		btnGoBack = new BackButton(3);
		buttonList.add(btnGoBack);
		
		btnRemoveAll = new GuiButton(4, width / 2 + 130, height / 2 - 85, 75, 20, "Remove All");
		buttonList.add(btnRemoveAll);
		
		updateList();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void updateList() {
		final int buttonsPerPage = 4;
		
		this.waystones_in_folder = WaystoneFunctions.getWaystonesInFolder(entry.getEntries(), waystone_entries, folder_id);
		this.waystones_in_folder_count = waystones_in_folder.size();
		
		btnPrevPage.enabled = pageOffset > 0;
		btnNextPage.enabled = pageOffset < waystones_in_folder_count / buttonsPerPage;
		
		@SuppressWarnings("rawtypes")
		Iterator it = buttonList.iterator();
		while(it.hasNext()) {
			Object next = it.next();
			if (next instanceof WaystoneButton || next instanceof SortButton || next instanceof RemoveEntryButton) {
				it.remove();
			}
		}
		
		int y = 0;
		int j = 0;
		for(int i = 0; i < buttonsPerPage; i++) {
			int entryIndex = pageOffset * buttonsPerPage + i;
			if(entryIndex >= 0 && entryIndex < waystones_in_folder_count) {
				if(j != 0) { j++; }
				WaystoneButton btnWaystoneEntry = new WaystoneButton(buttonCount + j, width / 2 - 100, height / 2 - 60 + y, waystones_in_folder.get(entryIndex), warpMode);
				if (waystones_in_folder.get(entryIndex).equals(btnWaystoneEntry)) {
					btnWaystoneEntry.enabled = false;
                }
				j++;
				RemoveEntryButton btnRemoveEntry = new RemoveEntryButton(buttonCount + j, width / 2 + 100, height / 2 - 60 + y, waystones_in_folder.get(entryIndex), folder_id);
				j++;
				SortButton sortUp = new SortButton(buttonCount + j, width / 2 - 110, height / 2 - 60 + y + 2, btnWaystoneEntry, -1);
                if (entryIndex == 0) {
                    sortUp.visible = false;
                }
                j++;
                SortButton sortDown = new SortButton(buttonCount + j, width / 2 - 110, height / 2 - 60 + y + 11, btnWaystoneEntry, 1);
                if (entryIndex == waystones_in_folder_count - 1) {
                    sortDown.visible = false;
                }
				
				
				buttonList.add(btnWaystoneEntry);
				buttonList.add(btnRemoveEntry);
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
		} else if(button == btnGoBack) {
			mc.displayGuiScreen(new FoldersListGUI(player, waystone_entries, warpMode, hand, fromWaystone));
		}else if(button == btnNewEntries) {
			mc.displayGuiScreen(new AddEntriesGUI(player, folder_id, waystone_entries, warpMode, hand, fromWaystone));
		} else if(button instanceof WaystoneButton) {
			NetworkHandler.channel.sendToServer(new MessageTeleportToWaystone(((WaystoneButton) button).getWaystone(),  warpMode, hand, fromWaystone));
			mc.displayGuiScreen(null);
		}else if (button instanceof SortButton) {
			int sortDir = ((SortButton) button).getSortDir();
			int global_index = 0;
			int global_index_plus = 0;
			int global_index_minus = 0;
			
			String entry1 = WaystoneFunctions.createUniqueWaystone(((SortButton) button).getWaystone(), folder_id);
			String entry1plus = null;
			String entry1minus = null;
			for (int i = 0; i < waystones_in_folder_count; i++) {
				String waystone = WaystoneFunctions.createUniqueWaystone(waystones_in_folder.get(i), folder_id);
				if (waystone.equals(entry1)) {
					if (i == 0) { entry1plus = WaystoneFunctions.createUniqueWaystone(waystones_in_folder.get(i + 1), folder_id);}
					else if (i == waystones_in_folder_count - 1) { entry1minus = WaystoneFunctions.createUniqueWaystone(waystones_in_folder.get(i - 1), folder_id);}
					else {
						entry1plus = WaystoneFunctions.createUniqueWaystone(waystones_in_folder.get(i + 1), folder_id);
						entry1minus = WaystoneFunctions.createUniqueWaystone(waystones_in_folder.get(i - 1), folder_id);
					}
				}
			}
			
			for(int i = 0; i < entry.size(); i++) {
				if (entry.get(i).equals(entry1)) {
					global_index = i;
				}else if (entry.get(i).equals(entry1plus)) {
					global_index_plus = i;
				}else if (entry.get(i).equals(entry1minus)) {
					global_index_minus = i;
				}
			}	
			
			if (sortDir == -1) {
				entry.swap(global_index, global_index_minus);
			}else if (sortDir == 1){
				entry.swap(global_index, global_index_plus);
			}
			OrganiserPacketHandler.channel.sendToServer(new ClientToServer(folder, entry));
			updateList();
		} else if (button instanceof RemoveEntryButton) {
			
			String entry1 = WaystoneFunctions.createUniqueWaystone(((RemoveEntryButton) button).getWaystone(), folder_id);
			
			for(int i = 0; i < entry.size(); i++) {
				if (entry.get(i).equals(entry1)) {
					entry.remove(i);
				}
			}
			OrganiserPacketHandler.channel.sendToServer(new ClientToServer(folder, entry));
			updateList();
		} else if (button == btnRemoveAll) {
			for (WaystoneEntry waystone : waystones_in_folder) {
				String entry1 = WaystoneFunctions.createUniqueWaystone(waystone, folder_id);
				
				for(int i = 0; i < entry.size(); i++) {
					if (entry.get(i).equals(entry1)) {
						entry.remove(i);
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
		drawCenteredString(fontRenderer, "Folder: " + folder_name, width / 2, height / 2 - 85, 0xFFFFFF);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	
}
