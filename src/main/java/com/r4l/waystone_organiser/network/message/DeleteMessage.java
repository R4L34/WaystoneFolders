package com.r4l.waystone_organiser.network.message;

import com.r4l.waystone_organiser.capability.FolderSet;
import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.network.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DeleteMessage implements IMessage{
	
	private static WaystoneEntry dummy_entry = new WaystoneEntry("0", 0, new BlockPos(0, 0, 0), false);
	
	private boolean isFolder;
	
	private int folder_id;
	
	private WaystoneEntry entry;
	
	public DeleteMessage () {}
	
	public DeleteMessage (int folder_id) {
		this.folder_id = folder_id;
		this.entry = dummy_entry;
		isFolder = true;
	}
	
	public DeleteMessage (int folder_id, WaystoneEntry entry) {
		this.folder_id = folder_id;
		this.entry = entry;
		isFolder = false;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		folder_id = buf.readInt();
		entry = WaystoneEntry.read(buf);
		isFolder = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(folder_id);
		entry.write(buf);
		buf.writeBoolean(isFolder);
	}

	public WaystoneEntry getEntry() {
		return entry;
	}
	
	public int getFolderId() {
		return folder_id;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public static class Handler implements IMessageHandler<DeleteMessage, IMessage> {

		@Override
		public IMessage onMessage(DeleteMessage message, MessageContext ctx) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
	    	PacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
	    		//RemoveFolder
	    		if (message.isFolder()) {
	    			IFolderSet folders = serverPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
	    			folders.removeFolder(message.getFolderId());
	    			System.out.println("DeleteMessage handler");
	    		} else {
	    		//Remove Entry from folder
	    			IFolderSet folders = serverPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
	    			folders.removeFromFolder(message.getEntry(), message.getFolderId());
	    		}
	    		System.out.println("DeleteMessage recieved");
			});
			return null;
		}
		
	}
}
