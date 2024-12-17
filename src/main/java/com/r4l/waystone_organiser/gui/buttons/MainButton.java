package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.reference.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MainButton extends GuiButton{
	
	private static final ResourceLocation BUTTON = new ResourceLocation(Reference.MODID, "buttons/folder.png");
	private static final ResourceLocation BUTTON1 = new ResourceLocation(Reference.MODID, "buttons/folder1.png");
	
	private int xPosition = 20;
	private int yPosition = 50;
	
	public MainButton() {
		super(-1, 20, 50, 75, 75, "");
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
            super.drawButton(mc, mouseX, mouseY, partial);
            GlStateManager.color(1f, 1f, 1f, 1f);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (hovered) {
            	mc.getTextureManager().bindTexture(BUTTON1);
            	drawTexturedModalRect(xPosition, yPosition, 0, 0, 75, 75);
            } else {
            	mc.getTextureManager().bindTexture(BUTTON);
            	drawTexturedModalRect(xPosition, yPosition, 0, 0, 75, 75);
            }
    }

}
