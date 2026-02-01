package com.r4l.waystone_organiser.network;

import com.r4l.waystone_organiser.network.message.AddMessage;
import com.r4l.waystone_organiser.network.message.BugfixMessage;
import com.r4l.waystone_organiser.network.message.CheckMessage;
import com.r4l.waystone_organiser.network.message.DeleteMessage;
import com.r4l.waystone_organiser.network.message.RenameMessage;
import com.r4l.waystone_organiser.network.message.SwapMessage;
import com.r4l.waystone_organiser.network.message.SyncMessage;
import com.r4l.waystone_organiser.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketHandler {
	
public static SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
	
	public static void init() {
		int i = 0;
		channel.registerMessage(SyncMessage.ClientToServer.Handler.class, SyncMessage.ClientToServer.class, i++, Side.SERVER);
		channel.registerMessage(SyncMessage.ServerToClient.Handler.class, SyncMessage.ServerToClient.class, i++, Side.CLIENT);
		
		channel.registerMessage(SwapMessage.Handler.class, SwapMessage.class, i++, Side.SERVER);
		channel.registerMessage(AddMessage.Handler.class, AddMessage.class, i++, Side.SERVER);
		channel.registerMessage(DeleteMessage.Handler.class, DeleteMessage.class, i++, Side.SERVER);
		channel.registerMessage(RenameMessage.Handler.class, RenameMessage.class, i++, Side.SERVER);
		channel.registerMessage(CheckMessage.Handler.class, CheckMessage.class, i++, Side.SERVER);
		channel.registerMessage(BugfixMessage.Handler.class, BugfixMessage.class, i++, Side.SERVER);
	}
	
	public static IThreadListener getThreadListener(MessageContext ctx) {
		return ctx.side == Side.SERVER ? (WorldServer) ctx.getServerHandler().player.world : getClientThreadListener();
	}

	@SideOnly(Side.CLIENT)
	public static IThreadListener getClientThreadListener() {
		return Minecraft.getMinecraft();
	}
}
