package com.r4l.waystone_organiser.capability.entries;

import com.r4l.waystone_organiser.reference.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntryHandler {
	public static final ResourceLocation ENTRY_CAP = new ResourceLocation(Reference.MODID, "entries");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (!(event.getObject() instanceof EntityPlayer)) return;

        event.addCapability(ENTRY_CAP, new EntryProvider());
    }
}
