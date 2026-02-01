package com.r4l.waystone_organiser.util;

public enum SortDirection {
UP(-1),
DOWN(1);

	private int sortDir;

	SortDirection (int sortDir) {
		this.sortDir = sortDir;;
	}

	public int toInt() {
		return sortDir;
	}


}
