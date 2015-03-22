package com.blogspot.jabelarminecraft.blocksmith.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerDeconstructor;

public class GuiDeconstructor extends GuiContainer
{

    public ContainerDeconstructor container;
    private final String                  blockName;
    private final boolean                 inverted;
    private final World                   worldObj;
    private final int                     x;
    private final int                     z;
    private final int                     y;
    private final EntityPlayer            player;

    public GuiDeconstructor(InventoryPlayer playerInventory, World parWorld, String parBlockName, 
    		int parX, int parY, int parZ, int parMin, int parMax)
    {
        super(new ContainerDeconstructor(playerInventory, parWorld, parX, parY, parZ, parMin, parMax));
        container = (ContainerDeconstructor) inventorySlots;
        blockName = parBlockName;
        inverted = false;
        worldObj = parWorld;
        x = parX;
        y = parY;
        z = parZ;
        player = playerInventory.player;
    }

    @Override
	public void actionPerformed(GuiButton button)
    {
    }

    @Override
	public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
    }

    @Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        if(!inverted)
        {
            fontRendererObj.drawString(blockName, xSize / 2 - fontRendererObj.getStringWidth(blockName) / 2 + 1, 5, 4210752);
            fontRendererObj.drawString(I18n.format("container.inventory"), 6, ySize - 96 + 2, 4210752);

//            Color darkGreen = new Color(75, 245, 75);
//            String string1 = I18n.format("uncrafting.compute") + ":";
//            fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + string1 + EnumChatFormatting.RESET, 24 - fontRendererObj.getStringWidth(string1) / 2 + 1, 22, 0);
//            fontRendererObj.drawString(EnumChatFormatting.GRAY + string1 + EnumChatFormatting.RESET, 24 - fontRendererObj.getStringWidth(string1) / 2, 21, 0);
//
//            fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + "" + EnumChatFormatting.UNDERLINE + "" + (BlockSmith.standardLevel + container.xp) + " levels" + EnumChatFormatting.RESET, xSize / 2 - fontRendererObj.getStringWidth((BlockSmith.standardLevel + container.xp) + " levels") / 2 + 1, ySize - 126 - 10, 0);
//            fontRendererObj.drawString(EnumChatFormatting.UNDERLINE + "" + (BlockSmith.standardLevel + container.xp) + " levels" + EnumChatFormatting.RESET, xSize / 2 - fontRendererObj.getStringWidth((BlockSmith.standardLevel + container.xp) + " levels") / 2, ySize - 127 - 10, darkGreen.getRGB());
//
//            String string = container.result;
//            if(string != null)
//            {
//                State msgType = container.type;
//                EnumChatFormatting format = EnumChatFormatting.GREEN;
//                EnumChatFormatting shadowFormat = EnumChatFormatting.DARK_GRAY;
//                if(msgType == ContainerUncraftingTable.State.ERROR)
//                {
//                    format = EnumChatFormatting.WHITE;
//                    shadowFormat = EnumChatFormatting.DARK_RED;
//                }
//
//                fontRendererObj.drawString(shadowFormat + string + EnumChatFormatting.RESET, 6 + 1, ySize - 95 + 2 - fontRendererObj.FONT_HEIGHT, 0);
//
//                fontRendererObj.drawString(format + string + EnumChatFormatting.RESET, 6, ySize - 96 + 2 - fontRendererObj.FONT_HEIGHT, 0);
//            }
        }
        else
        {
            int height = 166 - 8;
            fontRendererObj.drawString(blockName, xSize / 2 - fontRendererObj.getStringWidth(blockName) / 2 + 1, height - 5, 4210752);

            fontRendererObj.drawString(I18n.format("container.inventory"), 6, height - ySize - 96 + 2, 4210752);

//            Color darkGreen = new Color(75, 245, 75);
//            String string1 = "Calculs:";
//            fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + string1 + EnumChatFormatting.RESET, 24 - fontRendererObj.getStringWidth(string1) / 2 + 1, height - 22, 0);
//            fontRendererObj.drawString(EnumChatFormatting.GRAY + string1 + EnumChatFormatting.RESET, 24 - fontRendererObj.getStringWidth(string1) / 2, height - 21, 0);
//
//            fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + "" + EnumChatFormatting.UNDERLINE + "" + (BlockSmith.standardLevel + container.xp) + " levels" + EnumChatFormatting.RESET, xSize / 2 - fontRendererObj.getStringWidth((BlockSmith.standardLevel + container.xp) + " levels") / 2 + 1, height - (ySize - 126 - 10), 0);
//            fontRendererObj.drawString(EnumChatFormatting.UNDERLINE + "" + (BlockSmith.standardLevel + container.xp) + " levels" + EnumChatFormatting.RESET, xSize / 2 - fontRendererObj.getStringWidth((BlockSmith.standardLevel + container.xp) + " levels") / 2, height - (ySize - 127 - 10), darkGreen.getRGB());
//
//            String string = container.result;
//            if(string != null)
//            {
//                State msgType = container.type;
//                EnumChatFormatting format = EnumChatFormatting.GREEN;
//                EnumChatFormatting shadowFormat = EnumChatFormatting.DARK_GRAY;
//                if(msgType == ContainerUncraftingTable.State.ERROR)
//                {
//                    format = EnumChatFormatting.WHITE;
//                    shadowFormat = EnumChatFormatting.DARK_RED;
//                }
//
//                fontRendererObj.drawString(shadowFormat + string + EnumChatFormatting.RESET, 6 + 1, height - (ySize - 95 + 2 - fontRendererObj.FONT_HEIGHT), 0);
//
//                fontRendererObj.drawString(format + string + EnumChatFormatting.RESET, 6, height - (ySize - 96 + 2 - fontRendererObj.FONT_HEIGHT), 0);
//            }
        }
//        boolean op = false; // TODO: Check if user is OP
//        String optionsText = I18n.format("uncrafting.options.hit");
//        if(op)
//            fontRendererObj.drawString(EnumChatFormatting.UNDERLINE + optionsText, xSize - fontRendererObj.getStringWidth(optionsText) - 4, ySize - 96 + 2, 0);
//
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        if(inverted)
        {
            mc.renderEngine.bindTexture(new ResourceLocation("blocksmith:textures/gui/container/deconstructing_gui_redstoned.png"));
        }
        else
            mc.renderEngine.bindTexture(new ResourceLocation("blocksmith:textures/gui/container/deconstructing_gui.png"));

        int k = width / 2 - xSize / 2;
        int l = height / 2 - ySize / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        GL11.glPopMatrix();
    }
}
