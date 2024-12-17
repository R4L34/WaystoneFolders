package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.reference.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class PlusButton extends GuiButton{

	private static final ResourceLocation BUTTON = new ResourceLocation(Reference.MODID, "buttons/plus.png");
	private static final ResourceLocation BUTTON1 = new ResourceLocation(Reference.MODID, "buttons/plus1.png");
	
	private int xPosition;
	private int yPosition;
	
	public PlusButton(int buttonId, int x, int y) {
		super(buttonId, x, y, 30, 30, "");
		
		this.xPosition = x;
		this.yPosition = y;
	}

	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
            super.drawButton(mc, mouseX, mouseY, partial);
            GlStateManager.color(1f, 1f, 1f, 1f);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (hovered) {
            	mc.getTextureManager().bindTexture(BUTTON1);
            	drawTexturedModalRect(xPosition, yPosition, 0, 0, 30, 30);
            } else {
            	mc.getTextureManager().bindTexture(BUTTON);
            	drawTexturedModalRect(xPosition, yPosition, 0, 0, 30, 30);
            }
    }
	
}

