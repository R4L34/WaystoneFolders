package com.r4l.waystone_organiser.gui;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.r4l.waystone_organiser.capability.entries.EntryProvider;
import com.r4l.waystone_organiser.capability.entries.IEntry;
import com.r4l.waystone_organiser.capability.folders.FolderProvider;
import com.r4l.waystone_organiser.capability.folders.IFolder;
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

public class ConfirmDelete extends GuiScreen{

	private EntityPlayer player;
	private IFolder folder;
	private IEntry entry;
	private WaystoneEntry[] waystone_entries;
	
	private String folder_name;
	private int folder_id;
	
	private GuiButton btnYes;
	private GuiButton btnNo;
	
	private final WarpMode warpMode;
	private final EnumHand hand;
	private final WaystoneEntry fromWaystone;
	
	
	public ConfirmDelete(EntityPlayer player, int folder_id, WaystoneEntry[] waystone_entries, WarpMode warpMode, EnumHand hand, @Nullable WaystoneEntry fromWaystone) {
		this.player = player;
		this.folder = player.getCapability(FolderProvider.FOLDER_CAP, null);
		this.entry = player.getCapability(EntryProvider.ENTRY_CAP, null);
		this.folder_id = folder_id;
		this.waystone_entries = waystone_entries;
		this.folder_name = FolderFunctions.getFolderName(FolderFunctions.getFolderById(this.folder.getFolders(), this.folder_id));
		
		this.warpMode = warpMode;
        this.hand = hand;
        this.fromWaystone = fromWaystone;
	}
	
	@Override
	public void initGui () {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		btnYes = new GuiButton(0, width / 2 - 100, height / 2 + 40, 95, 20, "Yes");
		buttonList.add(btnYes);

		btnNo = new GuiButton(1, width / 2 + 5, height / 2 + 40, 95, 20, "No");
		buttonList.add(btnNo);

	}
	
	@Override
	protected void actionPerformed(GuiButton button){
		if(button == btnYes) {
			folder.remove(FolderFunctions.getEntryIndexById(folder.getFolders(), folder_id));
			for (int i = 0; i < entry.size(); i++) {
				if(WaystoneFunctions.getFolderId(entry.get(i)) == folder_id) {
					entry.remove(i);
					i--;
				}
			}
			OrganiserPacketHandler.channel.sendToServer(new ClientToServer(folder, entry));
			mc.displayGuiScreen(new FoldersListGUI(player, waystone_entries, warpMode, hand, fromWaystone));
		} else if(button == btnNo) {
			mc.displayGuiScreen(new FoldersListGUI(player, waystone_entries, warpMode, hand, fromWaystone));
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
		drawCenteredString(fontRenderer, "Are you sure, you want to delete this folder?", width / 2, height / 2 - 85, 0xFFFFFF);
		drawCenteredString(fontRenderer, "Folder: \"" + folder_name + "\". Id: " + folder_id + ".", width / 2, height / 2 - 65, 0xFFFFFF);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	
}
