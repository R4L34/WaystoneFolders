package com.r4l.waystone_organiser.util;

import java.lang.reflect.Field;
import java.util.List;

import com.r4l.waystone_organiser.capability.IFolderSet;

import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.gui.FontRenderer;

public class Utils {
	
	//Reflection shenanigans
	public static <Target, Base> Target getField(String fieldName, Base base, Class<Target> target_class) throws NoSuchFieldException, IllegalAccessException {
		Target target = null;
		
		Field[] fields = base.getClass().getDeclaredFields();
		
		for(Field field : fields) {
			
			if (field.getName().equals(fieldName)) {
				field.setAccessible(true);
				 Object value = field.get(base);
				target = target_class.cast(value);
			}
		}
		
        return target;
    }
	
	public static <Base, Value> void setField(String fieldName, Base base, Value value) throws NoSuchFieldException, IllegalAccessException {

	    Field[] fields = base.getClass().getDeclaredFields();

	    for (Field field : fields) {
	        if (field.getName().equals(fieldName)) {
	            field.setAccessible(true);
	            field.set(base, value);
	            return;
	        }
	    }

	    throw new NoSuchFieldException("Field '" + fieldName + "' not found in " + base.getClass().getName());
	}
	
	
	//Rendering
	public static void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color){
        fontRendererIn.drawStringWithShadow(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }
	
	//Waystone in world checking and deletion if necessary
	public static void checkWaystone(WaystoneEntry entry, int folder_id, IFolderSet folders) {
		TileWaystone waystone = WaystoneManager.getWaystoneInWorld(entry);
		if (waystone == null) {
			folders.removeFromFolder(entry, folder_id);
		}
	}
	
	
	//Fixed Waystone comparassion
	public static boolean compareWaystones(WaystoneEntry entry1, WaystoneEntry entry2) {
		return entry1.equals(entry2) && entry1.getName().equals(entry2.getName());
	}
	
	
	//Contains Functions
	public static boolean containsWaystone(List<WaystoneEntry> entryList, WaystoneEntry entry) {
		for(WaystoneEntry e : entryList) {
			if (compareWaystones(e, entry))
				return true;
		}
		return false;
	}
	
	public static boolean containsWaystone(WaystoneEntry[] entryList, WaystoneEntry entry) {
		for(WaystoneEntry e : entryList) {
			if (compareWaystones(e, entry))
				return true;
		}
		return false;
	}
	
}
