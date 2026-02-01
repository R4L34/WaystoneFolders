package com.r4l.waystone_organiser.capability;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.r4l.waystone_organiser.util.Folder;
import com.r4l.waystone_organiser.util.Reference;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class FolderSet implements IFolderSet {
	
	private List<Folder> folders = new ArrayList<>();

	@Override
	public List<Folder> getFolders() {
		return folders;
	}
	
	@Override
	public void setFolders(List<Folder> folders) {
		this.folders = folders;
	}

	@Override
	public Folder getFolder(int folder_id) {
		for (Folder folder : folders) {
			if (folder.getId() == folder_id)
				return folder;
		}
		return null;
	}

	@Override
	public boolean addToFolder(WaystoneEntry entry, int folder_id) {
		Folder folder = getFolder(folder_id);
		if(folder == null)
			return false;
		
		return folder.addEntry(entry);
	}
	
	@Override
	public boolean removeFromFolder(WaystoneEntry entry, int folder_id) {
		Folder folder = getFolder(folder_id);
		if(folder == null)
			return false;
		return folder.removeEntry(entry);
	}

	@Override
	public boolean removeFolder(int folder_id) {
		Folder folder = getFolder(folder_id);
		if(folder == null)
			return false;
		return folders.remove(folder);
	}
	
	
	@Override
	public void addFolder(Folder folder) {
		folders.add(folder);
	}
	
	@Override
	public int getFreeFolderId() {
	    if (folders == null || folders.isEmpty()) {
	        return 0;
	    }

	    // Sort folders by id (in-place)
	    folders.sort(Comparator.comparingInt(Folder::getId));

	    int expectedId = 0;

	    for (Folder folder : folders) {
	        int id = folder.getId();

	        if (id == expectedId) {
	            expectedId++;
	        } else if (id > expectedId) {
	            // Found a gap
	            break;
	        }
	        // if id < expectedId → duplicate or smaller, just continue
	    }

	    return expectedId;
	}
	
	public static FolderSet readNBT(NBTTagList tags) {
	    FolderSet folderSet = new FolderSet();

	    int index = 0;

	    // Read folder count (not strictly required, but keeps symmetry)
	    NBTTagCompound folderCountTag = tags.getCompoundTagAt(index++);
	    int folderCount = folderCountTag.getInteger("FolderCount");

	    for (int i = 0; i < folderCount; i++) {
	        // Read folder info
	        NBTTagCompound folderInfo = tags.getCompoundTagAt(index++);
	        String folderName = folderInfo.getString("FolderName");
	        int folderId = folderInfo.getInteger("FolderID");

	        Folder folder = new Folder(folderName, folderId);

	        // Read entry count
	        NBTTagCompound entryCountTag = tags.getCompoundTagAt(index++);
	        int entryCount = entryCountTag.getInteger("EntryCount");

	        // Read entries
	        for (int j = 0; j < entryCount; j++) {
	            NBTTagCompound entryTag = tags.getCompoundTagAt(index++);
	            WaystoneEntry entry = WaystoneEntry.read(entryTag);
	            folder.addEntry(entry);
	        }

	        folderSet.addFolder(folder);
	    }

	    return folderSet;
	}

	@Override
	public NBTTagList writeNBT() {
	    NBTTagList tags = new NBTTagList();

	    // Store folder count
	    NBTTagCompound folderCount = new NBTTagCompound();
	    folderCount.setInteger("FolderCount", folders.size());
	    tags.appendTag(folderCount);

	    for (Folder folder : folders) {

	        // Folder info
	        NBTTagCompound folderInfo = new NBTTagCompound();
	        folderInfo.setString("FolderName", folder.getName());
	        folderInfo.setInteger("FolderID", folder.getId());
	        tags.appendTag(folderInfo);

	        // Entry count 
	        NBTTagCompound entryCount = new NBTTagCompound();
	        entryCount.setInteger("EntryCount", folder.getEntries().size());
	        tags.appendTag(entryCount);

	        // Entries
	        for (WaystoneEntry entry : folder.getEntries()) {
	            tags.appendTag(entry.writeToNBT());
	        }
	    }

	    return tags;
	}

	
	@Override
	public void writeBuf(ByteBuf buf) {
        // FolderCount
        ByteBufUtils.writeVarInt(buf, folders.size(), 5);

        for (Folder folder : folders) {

            ByteBufUtils.writeUTF8String(buf, folder.getName());
            ByteBufUtils.writeVarInt(buf, folder.getId(), 5);

            List<WaystoneEntry> entries = folder.getEntries();
            ByteBufUtils.writeVarInt(buf, entries.size(), 5);
            for (WaystoneEntry entry : entries) {
            	entry.write(buf);
            }
        }
    }

    public static FolderSet readBuf(ByteBuf buf) {
        FolderSet folderSet = new FolderSet();

        int folderCount = ByteBufUtils.readVarInt(buf, 5);

        for (int i = 0; i < folderCount; i++) {
            String folderName = ByteBufUtils.readUTF8String(buf);
            int folderId = ByteBufUtils.readVarInt(buf, 5);

            Folder folder = new Folder(folderName, folderId);

            int entryCount = ByteBufUtils.readVarInt(buf, 5);
            for (int j = 0; j < entryCount; j++) {
                WaystoneEntry entry = WaystoneEntry.read(buf);
                folder.addEntry(entry);
            }

            folderSet.addFolder(folder);
        }

        return folderSet;
    }
	
	
	
	public static class Storage implements IStorage<IFolderSet>{

		@Override
		public NBTBase writeNBT(Capability<IFolderSet> capability, IFolderSet instance, EnumFacing side) {
			return instance.writeNBT();
		}

		@Override
		public void readNBT(Capability<IFolderSet> capability, IFolderSet instance, EnumFacing side, NBTBase nbt) {
			List<Folder> folders = FolderSet.readNBT((NBTTagList) nbt).getFolders();
			instance.setFolders(folders); 
		}
		
	}
	
	
	public static class Provider implements ICapabilitySerializable<NBTBase> {

		@CapabilityInject(IFolderSet.class)
	    public static final Capability<IFolderSet> FOLDER_CAP = null;

	    private IFolderSet instance = FOLDER_CAP.getDefaultInstance();
		
		
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == FOLDER_CAP;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == FOLDER_CAP ? FOLDER_CAP.<T> cast(this.instance) : null;
		}

		@Override
		public NBTBase serializeNBT() {
			return FOLDER_CAP.getStorage().writeNBT(FOLDER_CAP, this.instance, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			FOLDER_CAP.getStorage().readNBT(FOLDER_CAP, this.instance, null, nbt);
			
		}
	
	}
	
	
	
	public static class Handler {
		public static final ResourceLocation FOLDER_CAP = new ResourceLocation(Reference.MOD_ID, "_FOLDER_CAP");

	    @SubscribeEvent
	    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
	    {
	        if (!(event.getObject() instanceof EntityPlayer)) return;

	        event.addCapability(FOLDER_CAP, new Provider());
	    }
	}
	
	
}
