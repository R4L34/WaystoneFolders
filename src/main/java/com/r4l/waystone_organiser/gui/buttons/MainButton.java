package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.reference.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MainButton extends GuiButton{
	private static final ResourceLocation BUTTON = new ResourceLocation(Reference.MODID, "buttons/folder.png");
	private static final ResourceLocation BUTTON1 = new ResourceLocation(Reference.MODID, "buttons/folder1.png");
	
	private int screen_width;
	private int screen_height;
	
	public MainButton(int screen_width, int screen_height) {
		super(-1, (screen_width / 4) - 40, (screen_height / 2) - 50, 37, 37, "");
		this.screen_width = screen_width;
		this.screen_height = screen_height;
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
            super.drawButton(mc, mouseX, mouseY, partial);
            GlStateManager.color(1f, 1f, 1f, 1f);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (hovered) {
            	mc.getTextureManager().bindTexture(BUTTON1);
            	drawTexturedModalRect((screen_width / 4) - 40, (screen_height / 2) - 50, 0, 0, 37, 37);
            } else {
            	mc.getTextureManager().bindTexture(BUTTON);
            	drawTexturedModalRect((screen_width / 4) - 40, (screen_height / 2) - 50, 0, 0, 37, 37);
            }
    }

}
