package com.r4l.waystone_organiser.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ButtonWrapper extends GuiButton{
	
	protected ResourceLocation BUTTON;
	
	protected ResourceLocation BUTTON_HOVER;

	public ButtonWrapper(int buttonId, int x, int y, int widthIn, int heightIn, String main, String hover) {
		super(buttonId, x, y, widthIn, heightIn, "");
		BUTTON = new ResourceLocation(Reference.MOD_ID, Reference.BUTTON_FOLDER + main);
		BUTTON_HOVER = new ResourceLocation(Reference.MOD_ID, Reference.BUTTON_FOLDER + hover);
	}
	
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
            super.drawButton(mc, mouseX, mouseY, partial);
            GlStateManager.color(1f, 1f, 1f, 1f);
            this.hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            if (hovered) {
            	mc.getTextureManager().bindTexture(BUTTON_HOVER);
            	drawTexturedModalRect(x, y, 0, 0, width, height);
            } else {
            	mc.getTextureManager().bindTexture(BUTTON);
            	drawTexturedModalRect(x, y, 0, 0, width, height);
            }
    }
	
}
