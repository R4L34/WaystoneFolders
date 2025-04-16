package com.r4l.waystone_organiser.capability.entries;

import java.util.List;

public interface IEntry {
	
	public void setEntries(List<String> folders);
	
	public List<String> getEntries();
	
	public String get(int index);
	
	public void set(String folder, int index);
	
	public void add(String folder);
	
	public void remove(int index);
    
    public int size();
    
    public void swap(int index, int index1);
}
