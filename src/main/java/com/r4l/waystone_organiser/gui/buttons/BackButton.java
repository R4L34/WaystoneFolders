package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.reference.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class BackButton extends GuiButton{

	private static final ResourceLocation BUTTON = new ResourceLocation(Reference.MODID, "buttons/back.png");
	private static final ResourceLocation BUTTON1 = new ResourceLocation(Reference.MODID, "buttons/back1.png");
	
	private int xPosition = 20;
	private int yPosition = 20;
	
	public BackButton(int buttonId) {
		super(buttonId, 20, 20, 50, 30, "");
		
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
            super.drawButton(mc, mouseX, mouseY, partial);
            GlStateManager.color(1f, 1f, 1f, 1f);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (hovered) {
            	mc.getTextureManager().bindTexture(BUTTON1);
            	drawTexturedModalRect(xPosition, yPosition, 0, 0, 50, 30);
            } else {
            	mc.getTextureManager().bindTexture(BUTTON);
            	drawTexturedModalRect(xPosition, yPosition, 0, 0, 50, 30);
            }
    }

}
