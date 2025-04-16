package com.r4l.waystone_organiser.reference.functions;

import java.util.ArrayList;
import java.util.List;

import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.util.math.BlockPos;

public class WaystoneFunctions {

	public static int getFolderId(String unique_waystone) {
		int id;
		try {
			id = Integer.parseInt(RegexHandler.preg_replace(unique_waystone, "(^.+)\\|", ""));
			}
			catch (NumberFormatException e) {
				id = -1;
			}
		
		return id;
	}
	
	public static String getUniqueWaystone(String unique_waystone) {
		return RegexHandler.preg_replace(unique_waystone, "(\\|.+)$", "");
	}
	
	public static String getUniqueWaystone(WaystoneEntry entry) {
		String name = RegexHandler.preg_replace(entry.getName(), "\\|", "");
		String dim_id = "DIM" + Integer.toString(entry.getDimensionId());
		
		BlockPos pos = entry.getPos();
		String posX = "posX" + Integer.toString(pos.getX());
		String posY = "posY" + Integer.toString(pos.getY());
		String posZ = "posZ" + Integer.toString(pos.getZ());
		
		String waystone_from_entry = name + dim_id + posX + posY + posZ;
		
		return waystone_from_entry;
	}
	
	
	
	
	
	public static String createUniqueWaystone(WaystoneEntry entry, int folder_id) {
		return getUniqueWaystone(entry) + "|" + Integer.toString(folder_id);
	}
	
	public static List<String> createUniqueWaystones(WaystoneEntry[] entries, int folder_id) {
		List<String> unique_waystones_list = new ArrayList<>();		
		for (WaystoneEntry entry : entries) {
			unique_waystones_list.add(createUniqueWaystone(entry, folder_id));
		}	
		return unique_waystones_list;
	}

	
	public static List<WaystoneEntry> getWaystonesInFolder (List<String> unique_waystones, WaystoneEntry[] entries, int folder_id) {	
		List<WaystoneEntry> entry_list = new ArrayList<>();
		
		int unique_waystones_count = unique_waystones.size();
		int waystone_entries_count = entries.length;
		
		List<String> unique_waystones_from_entries = createUniqueWaystones(entries, folder_id);
		
		for(int i = 0; i < unique_waystones_count; i++) {
			for(int j = 0; j < waystone_entries_count; j++) {
				if(unique_waystones.get(i).equals(unique_waystones_from_entries.get(j))) {
					entry_list.add(entries[j]);
				}
			}
		}
		
		return entry_list;
		
	}
	
}
