package com.r4l.waystone_organiser.util;

import java.util.ArrayList;
import java.util.List;

import net.blay09.mods.waystones.util.WaystoneEntry;

public class Folder {
	
	private String name;
	
	private int id;
	
	private List<WaystoneEntry> entries;
	
	public Folder (String name, int id) {
		this.name = name;
		this.id = id;
		entries = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<WaystoneEntry> getEntries() {
		return entries;
	}
	
	public boolean addEntry(WaystoneEntry entry) {
		return entries.add(entry);
	}
	
	public boolean removeEntry(WaystoneEntry entry) {
		return entries.remove(entry);
	}
	
}
