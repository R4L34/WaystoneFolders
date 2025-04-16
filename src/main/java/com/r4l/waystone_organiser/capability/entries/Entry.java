package com.r4l.waystone_organiser.capability.entries;

import java.util.ArrayList;
import java.util.List;

public class Entry implements IEntry {
	
private List<String> entries = new ArrayList<>();
	
	@Override
	public void setEntries(List<String> folders) {
		this.entries = folders;
		
	}

	@Override
	public List<String> getEntries() {
		return this.entries;
	}

	@Override
	public void add(String folder) {
		this.entries.add(folder);
		
	}

	@Override
	public void remove(int index) {
		this.entries.remove(index);	
	}

	@Override
	public int size() {
		return this.entries.size();
	}

	@Override
	public String get(int index) {
		return this.entries.get(index);
	}

	@Override
	public void set(String folder, int index) {
		this.entries.set(index, folder);
		
	}

	@Override
	public void swap(int index, int index1) {
		String tmp = this.entries.get(index);
		this.entries.set(index, this.entries.get(index1));
		this.entries.set(index1, tmp);

		
	}
}
