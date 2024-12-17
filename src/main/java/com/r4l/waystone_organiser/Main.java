package com.r4l.waystone_organiser;

import com.r4l.waystone_organiser.Main;
import com.r4l.waystone_organiser.network.OrganiserPacketHandler;
import com.r4l.waystone_organiser.proxy.CommonProxy;
import com.r4l.waystone_organiser.reference.Reference;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCY, acceptedMinecraftVersions = Reference.ACCEPTED_MINECRAFT_VERSION)
public class Main {

	
	@Instance
	public static Main inctance;
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;

	
	//Event Handlers
	@EventHandler
	public static void preInit (FMLPreInitializationEvent event) {
		proxy.subscribeHandler();
	}
	
	@EventHandler
	public static void init (FMLInitializationEvent event) {
		OrganiserPacketHandler.init();
	}
	
	@EventHandler
	public static void postInit (FMLPostInitializationEvent event) {
		
	}
}