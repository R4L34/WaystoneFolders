package com.r4l.waystone_organiser.capability.entries;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class EntryStorage implements IStorage<IEntry>{

	@Override
	public NBTBase writeNBT(Capability<IEntry> capability, IEntry instance, EnumFacing side) {
		List<String> entries = instance.getEntries();
		NBTTagList nbt_entries = new NBTTagList();
		if (entries.size() == 0) {return nbt_entries;}
		for (int i = 0; i < entries.size(); i++) {
			nbt_entries.appendTag(new NBTTagString(entries.get(i)));
		}
		return nbt_entries;
	}

	@Override
	public void readNBT(Capability<IEntry> capability, IEntry instance, EnumFacing side, NBTBase nbt) {
		NBTTagList nbt_entries = (NBTTagList) nbt;
		List<String> entries = new ArrayList<>();
		for (int i = 0; i < nbt_entries.tagCount(); i++) {
			String folder = ((NBTTagString)nbt_entries.get(i)).getString();
			entries.add(folder);
		}
		
		instance.setEntries(entries);
	}
	
}
