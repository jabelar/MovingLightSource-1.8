package com.blogspot.jabelarminecraft.blocksmith.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerDeconstructor;

public class GuiDeconstructor extends GuiContainer
{

    public ContainerDeconstructor container;
    private final String blockName;
    private final boolean inverted;

    public GuiDeconstructor(InventoryPlayer playerInventory, World parWorld, String parBlockName, 
    		int parX, int parY, int parZ, int parMin, int parMax)
    {
        super(new ContainerDeconstructor(playerInventory, parWorld, parX, parY, parZ, parMin, parMax));
        container = (ContainerDeconstructor) inventorySlots;
        blockName = parBlockName;
        inverted = false;
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

        fontRendererObj.drawString(blockName, xSize / 2 - fontRendererObj.getStringWidth(blockName) / 2 + 1, 5, 4210752);
        fontRendererObj.drawString(I18n.format("container.inventory"), 6, ySize - 96 + 2, 4210752);

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        mc.renderEngine.bindTexture(new ResourceLocation("blocksmith:textures/gui/container/deconstructor.png"));

        int k = width / 2 - xSize / 2;
        int l = height / 2 - ySize / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        GL11.glPopMatrix();
    }
}
