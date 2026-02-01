package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.util.ButtonWrapper;

import net.minecraft.client.gui.GuiScreen;

public class MainButton extends ButtonWrapper{

	public MainButton(GuiScreen screen) {
		super(-1, (screen.width / 4) - 40, (screen.height / 2) - 50, 37, 37, "folder.png", "folder1.png");
	}

}
