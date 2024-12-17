package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.reference.Reference;
import com.r4l.waystone_organiser.reference.functions.FolderFunctions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class FolderButton extends GuiButton{

	private final String folder;
	private final int id;
	
	private static final ResourceLocation BUTTON = new ResourceLocation(Reference.MODID, "buttons/folder_icon.png");
	
	public FolderButton(int id, int x, int y, String folder) {
		super(id, x, y, FolderFunctions.getFolderName(folder));
		this.folder = FolderFunctions.getFolderName(folder);
		this.id = FolderFunctions.getFolderId(folder);
	}

	public String getFolder() {
		return folder;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
            super.drawButton(mc, mouseX, mouseY, partial);
            GlStateManager.color(1f, 1f, 1f, 1f);
            	mc.getTextureManager().bindTexture(BUTTON);
            	drawTexturedModalRect(x + 6, y + 6, 0, 0, 20, 20);
    }
	
}
