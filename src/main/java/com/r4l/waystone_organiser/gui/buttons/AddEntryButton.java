package com.r4l.waystone_organiser.gui.buttons;

import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;

public class AddEntryButton extends GuiButton{

	private final WaystoneEntry waystone;

	public AddEntryButton(int buttonId, int x, int y, WaystoneEntry waystone) {
		super(buttonId, x, y, 20, 20, "+");
		this.waystone = waystone;
	}

	public WaystoneEntry getWaystone() {
		return waystone;
	}
}