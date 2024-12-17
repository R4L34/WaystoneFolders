package com.r4l.waystone_organiser.reference.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import net.minecraft.util.math.BlockPos;

public class FolderFunctions {
	
	//private BlockPos pos = new BlockPos(0, 0, 0);
	//private int oo = pos.getX();

	public static int getFolderId(String unique_folder) {
		int id;
		try {
			id = Integer.parseInt(RegexHandler.preg_replace(unique_folder, "(^.+)\\|", ""));
			}
			catch (NumberFormatException e) {
				id = -1;
			}
		
		return id;
	}
	
	public static String getFolderName(String unique_folder) {
		return RegexHandler.preg_replace(unique_folder, "(\\|.+)$", "");
	}
	
	public static String getFolderById(List<String> folders_list, int id) {
		if (folders_list.isEmpty()) {return "";}
		for(int i = 0; i < folders_list.size(); i++) {
			String folder = folders_list.get(i);
			if (getFolderId(folder) == id) {
				return folder;
			}
		}
		return "";
	}
	
	
	public static int getEntryIndexById(List<String> folders_list, int id) {
		if (folders_list.isEmpty()) {return 0;}
		for(int i = 0; i < folders_list.size(); i++) {
			String folder = folders_list.get(i);
			if (getFolderId(folder) == id) {
				return i;
			}
		}
		return 0;
	}
	
	
	public static int getBiggestFolderId (List<String> folders_list) {
		if (folders_list.isEmpty()) {return 0;}
		List<Integer> inputs = new ArrayList<>();
		for(int i = 0; i < folders_list.size(); i++) {
			inputs.add(getFolderId(folders_list.get(i)));
		}
		int largest = Collections.max(inputs);	
		return largest;
	}
	
	
	
}
