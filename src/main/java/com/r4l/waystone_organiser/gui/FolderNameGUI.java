package com.r4l.waystone_organiser.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.gui.buttons.BackButton;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.network.message.AddMessage;
import com.r4l.waystone_organiser.network.message.RenameMessage;
import com.r4l.waystone_organiser.util.Folder;
import com.r4l.waystone_organiser.util.WaystoneData;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class FolderNameGUI extends GuiScreen{
	
	private WaystoneData waystoneData;
	
	private IFolderSet folders;
	
	private GuiTextField textField;
	
	private GuiButton btnDone;
	
	private int folder_id;
	
	private Folder folder;
	
	private String warning = "";
	
	private int buttonCount = 0;
	
	public FolderNameGUI (WaystoneData waystoneData) {
		this.waystoneData = waystoneData;
		this.folder_id = -1;
		folders = waystoneData.folders;
	}
	
	public FolderNameGUI (WaystoneData waystoneData, int folder_id) {
		this.waystoneData = waystoneData;
		this.folder_id = folder_id;
		folders = waystoneData.folders;
	}
	
	
	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		textField = new GuiTextField(0, fontRenderer, width / 2 - 100, height / 2 - 20, 200, 20);
		if (folder_id != -1) {
			folder = folders.getFolder(folder_id);
			textField.setText(folder.getName());
		}
		textField.setFocused(true);
		
		btnDone = new GuiButton(buttonCount++, width / 2, height / 2 + 10, 100, 20, "Done");
		buttonList.add(btnDone);
		
		BackButton btnGoBack = new BackButton(buttonCount++, new FolderListGUI(waystoneData));
		buttonList.add(btnGoBack);
	}
	
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button == btnDone) {
			if(textField.getText().equals("")) {
				warning = "Folder Name cannot be empty!";
				return;
			}
			//Adding Folder
			if(folder_id == -1) {
				folder = new Folder(textField.getText(), folders.getFreeFolderId());
				folders.addFolder(folder);
				PacketHandler.channel.sendToServer(new AddMessage(folder));
				mc.displayGuiScreen(new FolderListGUI(waystoneData));
				return;
			}
			
			folder.setName(textField.getText());
			
			PacketHandler.channel.sendToServer(new RenameMessage(textField.getText(), folder_id));
			mc.displayGuiScreen(new FolderListGUI(waystoneData));
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
