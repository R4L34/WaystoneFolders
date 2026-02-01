package com.r4l.waystone_organiser.event.handler;

import com.r4l.waystone_organiser.capability.FolderSet;
import com.r4l.waystone_organiser.capability.IFolderSet;
import com.r4l.waystone_organiser.gui.FolderListGUI;
import com.r4l.waystone_organiser.gui.buttons.MainButton;
import com.r4l.waystone_organiser.util.Utils;
import com.r4l.waystone_organiser.util.WaystoneData;

import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.client.gui.GuiWaystoneList;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WaystoneHandler {

	public static MainButton mainBtn;
	
	@SubscribeEvent
	public void inject(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiWaystoneList) {
        	mainBtn = new MainButton(event.getGui());
            event.getButtonList().add(mainBtn);
        }
	}
	
	
	@SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException { 
        if (event.getButton() instanceof MainButton) {
        	
        	GuiWaystoneList gui = (GuiWaystoneList) event.getGui();
        	Minecraft mc = Minecraft.getMinecraft();
        	
        	WaystoneEntry[] entries = Utils.getField("entries", gui, WaystoneEntry[].class);
        	WarpMode warpMode =  Utils.getField("warpMode", gui, WarpMode.class);
        	EnumHand hand = Utils.getField("hand", gui, EnumHand.class);
        	WaystoneEntry fromWaystone = Utils.getField("fromWaystone", gui, WaystoneEntry.class);
        	
        	IFolderSet folders = mc.player.getCapability(FolderSet.Provider.FOLDER_CAP, null);
        	WaystoneData data = new WaystoneData(folders, entries, warpMode, hand, fromWaystone);
        	
    		mc.displayGuiScreen(new FolderListGUI(data));
    		
        }
    }
	
	@SubscribeEvent
	public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
		if (event.getGui() instanceof GuiWaystoneList) {
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
			Utils.drawCenteredString(fontRenderer, "Folders", (event.getGui().width / 4) - 23, (event.getGui().height / 2) - 65, 0xFFFFFF);
		}
		
	}
	
}
