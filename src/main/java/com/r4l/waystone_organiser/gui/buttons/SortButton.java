package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.util.SortDirection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class SortButton extends GuiButtonExt {
	
	private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
	
	private int index;
	
	private int parent_height;
	
	private SortDirection sortDir;

	public SortButton(int id, int x, int y, int parent_height, int index, SortDirection sortDir) {
		super(id, x, y, 11, 7, "");
		this.parent_height = parent_height;
		this.index = index;
		this.sortDir = sortDir;
	}

	public SortDirection getSortDir() {
		return sortDir;
	}

	public int getIndex() {
		return index;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		//10 is parent button height
		if (this.visible && mouseY >= parent_height && mouseY < parent_height + 20) {
			GlStateManager.color(1f, 1f, 1f, 1f);
			mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			if (hovered) {
				Gui.drawModalRectWithCustomSizedTexture(x - 5, y - 5 - (sortDir.toInt() == 1 ? 15 : 0), 96f - (sortDir.toInt() == 1 ? 32f : 0f), 32f, 32, 32, 256f, 256f);
			} else {
				Gui.drawModalRectWithCustomSizedTexture(x - 5, y - 5 - (sortDir.toInt() == 1 ? 15 : 0), 96f - (sortDir.toInt() == 1 ? 32f : 0f), 0f, 32, 32, 256f, 256f);
			}
		}
	}

}
