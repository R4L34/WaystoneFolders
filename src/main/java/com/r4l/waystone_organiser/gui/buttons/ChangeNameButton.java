package com.r4l.waystone_organiser.gui.buttons;

import com.r4l.waystone_organiser.util.ButtonWrapper;

public class ChangeNameButton extends ButtonWrapper{

	private int folder_id;
	
	public ChangeNameButton(int buttonId, int x, int y, int folder_id) {
		super(buttonId, x, y, 20, 20, "change_name.png", "change_name1.png");
		this.folder_id = folder_id;
	}
	
	public int getFolderId() {
		return folder_id;
	}

}
