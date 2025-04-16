package com.r4l.waystone_organiser.gui;

import java.io.IOException;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.r4l.waystone_organiser.capability.entries.EntryProvider;
import com.r4l.waystone_organiser.capability.entries.IEntry;
import com.r4l.waystone_organiser.capability.folders.FolderProvider;
import com.r4l.waystone_organiser.capability.folders.IFolder;
import com.r4l.waystone_organiser.gui.buttons.BackButton;
import com.r4l.waystone_organiser.network.OrganiserPacketHandler;
import com.r4l.waystone_organiser.network.message.ClientToServer;
import com.r4l.waystone_organiser.reference.functions.FolderFunctions;
import com.r4l.waystone_organiser.reference.functions.RegexHandler;

import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class FolderNameGUI extends GuiScreen{
	
	private EntityPlayer player;
	private IFolder folder;
	private IEntry entry;
	private WaystoneEntry[] waystone_entries;
	
	private GuiTextField textField;
	private GuiButton btnDone;
	private GuiButton btnGoBack;
	
	private Integer id;
	private String warning = "";
	
	private final WarpMode warpMode;
	private final EnumHand hand;
	private final WaystoneEntry fromWaystone;
	
	
	
	public FolderNameGUI (EntityPlayer player, WaystoneEntry[] waystone_entries, WarpMode warpMode, EnumHand hand, @Nullable WaystoneEntry fromWaystone) {
		this.player = player;
		this.folder = player.getCapability(FolderProvider.FOLDER_CAP, null);
		this.entry = player.getCapability(EntryProvider.ENTRY_CAP, null);
		this.waystone_entries = waystone_entries;
		
		this.warpMode = warpMode;
        this.hand = hand;
        this.fromWaystone = fromWaystone;
		
	}
	
	public FolderNameGUI (EntityPlayer player, WaystoneEntry[] waystone_entries, int id, WarpMode warpMode, EnumHand hand, @Nullable WaystoneEntry fromWaystone) {
		this.player = player;
		this.folder = player.getCapability(FolderProvider.FOLDER_CAP, null);
		this.entry = player.getCapability(EntryProvider.ENTRY_CAP, null);
		this.waystone_entries = waystone_entries;
		this.id = id;
		
		this.warpMode = warpMode;
        this.hand = hand;
        this.fromWaystone = fromWaystone;
		
	}
	
	@Override
	public void initGui () {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		textField = new GuiTextField(0, fontRenderer, width / 2 - 100, height / 2 - 20, 200, 20);
		if (id != null) { textField.setText(FolderFunctions.getFolderName(FolderFunctions.getFolderById(folder.getFolders(), id))); }
		textField.setFocused(true);
		
		btnDone = new GuiButton(0, width / 2, height / 2 + 10, 100, 20, "Done");
		buttonList.add(btnDone);
		
		btnGoBack = new BackButton(3);
		buttonList.add(btnGoBack);
	}
	
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button == btnDone) {
			if(textField.getText().equals("")) {
				warning = "Folder Name cannot be empty!";
			}else if(RegexHandler.preg_match(textField.getText(), "\\|")) {
				warning = "The following characters cannot be used: |";
			} else {
				if (id != null) {folder.set(textField.getText() + "|" + Integer.toString(id), FolderFunctions.getEntryIndexById(folder.getFolders(), id));}
				else {folder.add( textField.getText() + "|" + Integer.toString(FolderFunctions.getBiggestFolderId(folder.getFolders()) + 1)); }
				OrganiserPacketHandler.channel.sendToServer(new ClientToServer(folder, entry));
				mc.displayGuiScreen(new FoldersListGUI(player, waystone_entries, warpMode, hand, fromWaystone));
			}
		}else if(button == btnGoBack) {
			mc.displayGuiScreen(new FoldersListGUI(player, waystone_entries, warpMode, hand, fromWaystone));
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		textField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(keyCode == Keyboard.KEY_RETURN) {
			actionPerformed(btnDone);
			return;
		}
		super.keyTyped(typedChar, keyCode);
		textField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	public void updateScreen() {
		textField.updateCursorCounter();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawWorldBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);

		fontRenderer.drawString("Enter Folder Name", width / 2 - 100, height / 2 - 35, 0xFFFFFF);
		fontRenderer.drawString(warning, width / 2 - 100, height / 2 + 40, 0xFF0000);
		textField.drawTextBox();
	}
	
	
	
}

