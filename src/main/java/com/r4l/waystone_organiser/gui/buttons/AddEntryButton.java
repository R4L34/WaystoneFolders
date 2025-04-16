package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.reference.functions.WaystoneFunctions;

import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.GuiButton;

public class AddEntryButton extends GuiButton{

	private final WaystoneEntry waystone;
	private final int folder_id;

	public AddEntryButton(int buttonId, int x, int y, WaystoneEntry waystone, int folder_id) {
		super(buttonId, x, y, 20, 20, "+");
		this.waystone = waystone;
		this.folder_id = folder_id;
	}

	public WaystoneEntry getWaystone() {
		return waystone;
	}
	
	public String getUniqueWaystone() {
		return WaystoneFunctions.createUniqueWaystone(waystone, folder_id);
	}
}
