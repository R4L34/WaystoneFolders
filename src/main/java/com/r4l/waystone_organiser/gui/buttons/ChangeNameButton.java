package com.r4l.waystone_organiser.gui.buttons;
import com.r4l.waystone_organiser.reference.Reference;
import com.r4l.waystone_organiser.reference.functions.FolderFunctions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ChangeNameButton extends GuiButton{

	private int xPosition;
	private int yPosition;
	
	private final String folder;
	private final int id;
	
	private static final ResourceLocation BUTTON = new ResourceLocation(Reference.MODID, "buttons/change_name.png");
	private static final ResourceLocation BUTTON1 = new ResourceLocation(Reference.MODID, "buttons/change_name1.png");
	
	public ChangeNameButton(int buttonId, int x, int y, String folder) {
		super(buttonId, x, y, 20, 20, "");
		this.folder = FolderFunctions.getFolderName(folder);
		this.id = FolderFunctions.getFolderId(folder);
		
		this.xPosition = x;
		this.yPosition = y;
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
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (hovered) {
            	mc.getTextureManager().bindTexture(BUTTON1);
            	drawTexturedModalRect(xPosition, yPosition, 0, 0, 20, 20);
            } else {
            	mc.getTextureManager().bindTexture(BUTTON);
            	drawTexturedModalRect(xPosition, yPosition, 0, 0, 20, 20);
            }
    }
}
