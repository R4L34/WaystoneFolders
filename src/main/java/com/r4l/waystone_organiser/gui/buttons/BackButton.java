package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.util.ButtonWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class BackButton extends ButtonWrapper{
	
	private GuiScreen targetScreen;

	public BackButton(int buttonId, GuiScreen targetScreen) {
		super(buttonId, 20, 20, 50, 30, "back.png", "back1.png");
		this.targetScreen = targetScreen;
	}

	public GuiScreen getTargetScreen() {
		return targetScreen;
	}
	
	public void onPress() {
		Minecraft.getMinecraft().displayGuiScreen(targetScreen);
	}

}
