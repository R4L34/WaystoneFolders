package com.r4l.waystone_organiser.gui.buttons;

import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class SortButton extends GuiButtonExt {
	private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
	
	private  GuiButton parentButton;
	private final int sortDir;

	public SortButton(int id, int x, int y, WaystoneButton parentButton, int sortDir) {
		super(id, x, y, "");
		this.width = 11;
		this.height = 7;
		this.parentButton = parentButton;
		this.sortDir = sortDir;
	}
	
	
	public SortButton(int id, int x, int y, FolderButton parentButton, int sortDir) {
		super(id, x, y, "");
		this.width = 11;
		this.height = 7;
		this.parentButton = parentButton;
		this.sortDir = sortDir;
	}

	public WaystoneEntry getWaystone() {
		return ((WaystoneButton)parentButton).getWaystone();
	}
	
	public String getFolder() {
		return ((FolderButton)parentButton).getFolder();
	}
	
	public int getFolderId() {
		return ((FolderButton)parentButton).getId();
	}

	public int getSortDir() {
		return sortDir;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		if (this.visible && mouseY >= parentButton.y && mouseY < parentButton.y + parentButton.height) {
			GlStateManager.color(1f, 1f, 1f, 1f);
			mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			if (hovered) {
				Gui.drawModalRectWithCustomSizedTexture(x - 5, y - 5 - (sortDir == 1 ? 15 : 0), 96f - (sortDir == 1 ? 32f : 0f), 32f, 32, 32, 256f, 256f);
			} else {
				Gui.drawModalRectWithCustomSizedTexture(x - 5, y - 5 - (sortDir == 1 ? 15 : 0), 96f - (sortDir == 1 ? 32f : 0f), 0f, 32, 32, 256f, 256f);
			}
		}
	}
}
