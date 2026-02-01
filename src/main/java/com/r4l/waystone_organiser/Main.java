package com.r4l.waystone_organiser;

import com.r4l.waystone_organiser.capability.FolderSet;
import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.event.handler.BackBtnHandler;
import com.r4l.waystone_organiser.event.handler.DeleteEntryBugfix;
import com.r4l.waystone_organiser.event.handler.SyncHandler;
import com.r4l.waystone_organiser.event.handler.WaystoneHandler;
import com.r4l.waystone_organiser.network.PacketHandler;
import com.r4l.waystone_organiser.proxy.CommonProxy;
import com.r4l.waystone_organiser.util.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, acceptedMinecraftVersions = Reference.ACCEPTED_MINECRAFT_VERSION)
public class Main {

	
	@Instance
	public static Main inctance;
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;

	
	@SuppressWarnings("deprecation")
	@EventHandler
	public static void preInit (FMLPreInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(new FolderSet.Handler());
		MinecraftForge.EVENT_BUS.register(new WaystoneHandler());
		MinecraftForge.EVENT_BUS.register(new SyncHandler());
		MinecraftForge.EVENT_BUS.register(new BackBtnHandler());
		MinecraftForge.EVENT_BUS.register(new DeleteEntryBugfix());
		
		CapabilityManager.INSTANCE.register(IFolderSet.class, new FolderSet.Storage(), FolderSet.class);
	}
	
	@EventHandler
	public static void init (FMLInitializationEvent event) {
		PacketHandler.init();
	}
	
	@EventHandler
	public static void postInit (FMLPostInitializationEvent event) {
		
	}
}