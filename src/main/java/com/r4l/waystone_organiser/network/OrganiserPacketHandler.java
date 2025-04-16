package com.r4l.waystone_organiser.network;

import com.r4l.waystone_organiser.network.message.ClientToServer;
import com.r4l.waystone_organiser.network.message.RemoveWaystoneMessage;
import com.r4l.waystone_organiser.network.message.ServerToClient;
import com.r4l.waystone_organiser.reference.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OrganiserPacketHandler {

	
public static SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
	
	public static void init() {
		channel.registerMessage(ClientToServer.Handler.class, ClientToServer.class, 0, Side.SERVER);
		channel.registerMessage(ServerToClient.Handler.class, ServerToClient.class, 1, Side.CLIENT);
		//
		channel.registerMessage(RemoveWaystoneMessage.ClientToServer.Handler.class, RemoveWaystoneMessage.ClientToServer.class, 2, Side.SERVER);
	}
	
	public static IThreadListener getThreadListener(MessageContext ctx) {
		return ctx.side == Side.SERVER ? (WorldServer) ctx.getServerHandler().player.world : getClientThreadListener();
	}

	@SideOnly(Side.CLIENT)
	public static IThreadListener getClientThreadListener() {
		return Minecraft.getMinecraft();
	}
}
