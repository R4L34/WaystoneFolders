package com.r4l.waystone_organiser.util;

import com.r4l.waystone_organiser.capability.IFolderSet;

import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.util.EnumHand;

public class WaystoneData {
	
	public IFolderSet folders;
	
	public WaystoneEntry[] entries;
	
	public WarpMode warpMode;
	
	public EnumHand hand;
	
	public WaystoneEntry fromWaystone;
	
	public WaystoneData () {}

	public WaystoneData (IFolderSet folders, WaystoneEntry[] entries, WarpMode warpMode, EnumHand hand, WaystoneEntry fromWaystone) {
		this.folders = folders;
		this.entries = entries;
		this.warpMode = warpMode;
		this.hand = hand;
		this.fromWaystone = fromWaystone;
	}
	
}
