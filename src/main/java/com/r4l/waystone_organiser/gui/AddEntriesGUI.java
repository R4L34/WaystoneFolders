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
import com.r4l.waystone_organiser.gui.buttons.AddEntryButton;
import com.r4l.waystone_organiser.gui.buttons.BackButton;
import com.r4l.waystone_organiser.network.OrganiserPacketHandler;
import com.r4l.waystone_organiser.network.message.ClientToServer;
import com.r4l.waystone_organiser.reference.functions.FolderFunctions;
import com.r4l.waystone_organiser.reference.functions.WaystoneFunctions;

import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class AddEntriesGUI extends GuiScreen{

	private EntityPlayer player;
	private IFolder folder;
	private IEntry entry;
	
	private String folder_name;
	private int folder_id;
	private WaystoneEntry[] waystone_entries;	
	private int waystone_entries_count;
	private List<WaystoneEntry> waystones_in_folder;
	
	private GuiButton btnPrevPage;
	private GuiButton btnNextPage;
	private GuiButton btnGoBack;
	
	private int buttonCount = 3;
	
	private int pageOffset;
	
	private final WarpMode warpMode;
	private final EnumHand hand;
	private final WaystoneEntry fromWaystone;

	
	
	public AddEntriesGUI (EntityPlayer player, int folder_id, WaystoneEntry[] waystone_entries, WarpMode warpMode, EnumHand hand, @Nullable WaystoneEntry fromWaystone) {
		this.player = player;
		this.folder = player.getCapability(FolderProvider.FOLDER_CAP, null);
		this.entry = player.getCapability(EntryProvider.ENTRY_CAP, null);
		
		this.folder_name = FolderFunctions.getFolderName(FolderFunctions.getFolderById(this.folder.getFolders(), folder_id));
		this.folder_id = folder_id;
		
		this.waystone_entries = waystone_entries;
		this.waystone_entries_count = waystone_entries.length;
		
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
		
		btnGoBack = new BackButton(2);
		buttonList.add(btnGoBack);
		
		updateList();
	}
	
	public void updateList() {
		this.waystones_in_folder = WaystoneFunctions.getWaystonesInFolder(entry.getEntries(), waystone_entries, folder_id);
		final int buttonsPerPage = 4;
		
		btnPrevPage.enabled = pageOffset > 0;
		btnNextPage.enabled = pageOffset < waystone_entries_count / buttonsPerPage;
		
		@SuppressWarnings("rawtypes")
		Iterator it = buttonList.iterator();
		while(it.hasNext()) {
			if (it.next() instanceof AddEntryButton) {
				it.remove();
			}
		}
		int y = 0;
		for(int i = 0; i < buttonsPerPage; i++) {
			int entryIndex = pageOffset * buttonsPerPage + i;
			if(entryIndex >= 0 && entryIndex < waystone_entries_count) {
				if(!waystones_in_folder.contains(waystone_entries[entryIndex])) {
					AddEntryButton btnAdd = new AddEntryButton(buttonCount + i, width / 2 + 100, height / 2 - 60 + y, waystone_entries[entryIndex], folder_id);
					buttonList.add(btnAdd);
				}
				y += 22;
			}
		}
		
		
	}
	
	
	public void updateStringList() {
		final int stringsPerPage = 4;
		int y = 0;
		for(int i = 0; i < stringsPerPage; i++) {
			int entryIndex = pageOffset * stringsPerPage + i;
			if(entryIndex >= 0 && entryIndex < waystone_entries_count) {
				drawCenteredString(fontRenderer, waystone_entries[entryIndex].getName(), width / 2, height / 2 - 60 + y, 0xFFFFFF);
				y += 22;
			}
		}
		
		
	}
	
	
	@Override
	protected void actionPerformed(GuiButton button){
		if(button == btnNextPage) {
			pageOffset++;
			updateList();
			updateStringList();
		} else if(button == btnPrevPage) {
			pageOffset--;
			updateList();
			updateStringList();
		} else if(button == btnGoBack) {
			mc.displayGuiScreen(new FolderGUI(player, folder_id, waystone_entries, warpMode, hand, fromWaystone));
		} else if(button instanceof AddEntryButton) {
			entry.add(((AddEntryButton)button).getUniqueWaystone());
			OrganiserPacketHandler.channel.sendToServer(new ClientToServer(folder, entry));
			updateList();
			updateStringList();
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
		drawCenteredString(fontRenderer, "Add Entries For: " + folder_name, width / 2, height / 2 - 85, 0xFFFFFF);
		updateStringList();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	
	
}
