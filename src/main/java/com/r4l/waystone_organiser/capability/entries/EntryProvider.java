package com.r4l.waystone_organiser.capability.entries;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class EntryProvider implements ICapabilitySerializable<NBTBase>{
	
	@CapabilityInject(IEntry.class)
    public static final Capability<IEntry> ENTRY_CAP = null;

    private IEntry instance = ENTRY_CAP.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == ENTRY_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == ENTRY_CAP ? ENTRY_CAP.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return ENTRY_CAP.getStorage().writeNBT(ENTRY_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		ENTRY_CAP.getStorage().readNBT(ENTRY_CAP, this.instance, null, nbt);
		
	}

}
