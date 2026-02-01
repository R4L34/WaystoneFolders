package com.r4l.waystone_organiser.network.message;

import com.r4l.waystone_organiser.capability.FolderSet;
import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.util.Utils;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CheckMessage implements IMessage{
	
	private WaystoneEntry entry;
	
	private int folder_id;
	
	public CheckMessage () {}
	
	public CheckMessage (WaystoneEntry entry, int folder_id) {
		this.entry = entry;
		this.folder_id = folder_id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entry = WaystoneEntry.read(buf);
		folder_id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		entry.write(buf);
		buf.writeInt(folder_id);
	}

	public WaystoneEntry getEntry() {
		return entry;
	}
	
	public int getFolderId() {
		return folder_id;
	}
	
	
	public static class Handler implements IMessageHandler<CheckMessage, IMessage> {

		@Override
		public IMessage onMessage(CheckMessage message, MessageContext ctx) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
	    	PacketHandler.getThreadListener(ctx).addScheduledTask(() -> {
	    		IFolderSet folders = serverPlayer.getCapability(FolderSet.Provider.FOLDER_CAP, null);
	    		Utils.checkWaystone(message.getEntry(), message.getFolderId(), folders);
			});
			return null;
		}
		
	}

}
