package com.r4l.waystone_organiser.event.handler;

import com.r4l.waystone_organiser.gui.buttons.BackButton;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BackBtnHandler {
	
	@SubscribeEvent
	public void backButtonAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
		if(event.getButton() instanceof BackButton) {
			BackButton button = (BackButton) event.getButton();
			button.onPress();
		}
	}

}
