package com.r4l.waystone_organiser.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.network.message.DeleteMessage;
import com.r4l.waystone_organiser.util.WaystoneData;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ConfirmDeleteGUI extends GuiScreen{
	
	private WaystoneData waystoneData;
	
	private IFolderSet folders;
	
	private int folder_id;
	
	private GuiButton btnYes;
	
	private GuiButton btnNo;
	
	
	public ConfirmDeleteGUI(WaystoneData waystoneData, int folder_id) {
		this.waystoneData = waystoneData;
		folders = waystoneData.folders;
		this.folder_id = folder_id;
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
			folders.removeFolder(folder_id);
        	PacketHandler.channel.sendToServer(new DeleteMessage(folder_id));
        	mc.displayGuiScreen(new FolderListGUI(waystoneData));
		} else if(button == btnNo) {
			mc.displayGuiScreen(new FolderListGUI(waystoneData));
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
		drawCenteredString(fontRenderer, "Folder: \"" + folders.getFolder(folder_id).getName() + "\". Id: " + folder_id + ".", width / 2, height / 2 - 65, 0xFFFFFF);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	
}