package com.r4l.waystone_organiser.gui.buttons;

import com.google.common.collect.Lists;

import net.blay09.mods.waystones.PlayerWaystoneHelper;
import net.blay09.mods.waystones.WarpMode;
import net.blay09.mods.waystones.WaystoneConfig;
import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiUtils;

public class WaystoneButton extends GuiButton{
	
	private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
	
	private final WaystoneEntry waystone;
	private final int xpLevelCost;

	
	@SuppressWarnings("incomplete-switch")
	public WaystoneButton(int id, int x, int y, WaystoneEntry waystone, WarpMode mode) {
		super(id, x, y, waystone.getName());
		this.waystone = waystone;
		
		EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        boolean enableXPCost = false;
        switch (mode) {
            case WARP_STONE:
                enableXPCost = WaystoneConfig.general.warpStoneXpCost;
                break;
            case INVENTORY_BUTTON:
                enableXPCost = WaystoneConfig.general.inventoryButtonXpCost;
                break;
            case WAYSTONE:
                enableXPCost = WaystoneConfig.general.waystoneXpCost;
                break;
        }

        if (!WaystoneConfig.general.globalWaystonesCostXp && waystone.isGlobal()) {
            enableXPCost = false;
        }

        this.xpLevelCost = enableXPCost ? PlayerWaystoneHelper.getTravelCostByDistance(player, waystone) : 0;

        if (waystone.getDimensionId() != Minecraft.getMinecraft().world.provider.getDimension()) {
            if (!WaystoneManager.isDimensionWarpAllowed(waystone)) {
                enabled = false;
            }
        }

        if (player.experienceLevel < xpLevelCost && !player.capabilities.isCreativeMode) {
            enabled = false;
        }
	}

	public WaystoneEntry getWaystone() {
		return waystone;
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        GlStateManager.color(1f, 1f, 1f, 1f);

        if (xpLevelCost > 0) {
            boolean canAfford = mc.player.experienceLevel >= xpLevelCost || mc.player.capabilities.isCreativeMode;
            mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
            drawTexturedModalRect(x + 2, y + 2, (Math.min(xpLevelCost, 3) - 1) * 16, 223 + (!canAfford ? 16 : 0), 16, 16);

            if (xpLevelCost > 3) {
                mc.fontRenderer.drawString("+", x + 17, y + 6, 0xC8FF8F, true);
            }

            if (hovered && mouseX <= x + 16) {
                GuiUtils.drawHoveringText(Lists.newArrayList((canAfford ? TextFormatting.GREEN : TextFormatting.RED) + I18n.format("tooltip.waystones:levelRequirement", xpLevelCost)), mouseX, mouseY + mc.fontRenderer.FONT_HEIGHT, mc.displayWidth, mc.displayHeight, 200, mc.fontRenderer);
            }
            GlStateManager.disableLighting();
        }
    }
}
