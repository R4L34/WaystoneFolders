package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.util.Folder;
import com.r4l.waystone_organiser.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class FolderButton extends GuiButton{

	private final Folder folder;
	
	private static final ResourceLocation BUTTON = new ResourceLocation(Reference.MOD_ID, "buttons/folder_icon.png");
	
	public FolderButton(int id, int x, int y, Folder folder) {
		super(id, x, y, folder.getName());
		this.folder = folder;
	}

	public Folder getFolder() {
		return folder;
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
            super.drawButton(mc, mouseX, mouseY, partial);
            GlStateManager.color(1f, 1f, 1f, 1f);
            	mc.getTextureManager().bindTexture(BUTTON);
            	drawTexturedModalRect(x + 6, y + 6, 0, 0, 20, 20);
    }
	
}