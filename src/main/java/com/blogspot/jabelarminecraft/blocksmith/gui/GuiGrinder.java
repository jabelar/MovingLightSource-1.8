/**
    Copyright (C) 2015 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.blocksmith.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerGrinder;
import com.blogspot.jabelarminecraft.blocksmith.tileentities.TileEntityGrinder;

/**
 * @author jabelar
 *
 */
public class GuiGrinder  extends GuiContainer
{
	private static final ResourceLocation grinderGuiTextures = new ResourceLocation(BlockSmith.MODID+":textures/gui/container/grinder.png");
    private final InventoryPlayer field_175383_v;
    private final IInventory tileGrinder;

    public GuiGrinder(InventoryPlayer p_i45501_1_, IInventory p_i45501_2_)
    {
        super(new ContainerGrinder(p_i45501_1_, p_i45501_2_));
        this.field_175383_v = p_i45501_1_;
        this.tileGrinder = p_i45501_2_;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.tileGrinder.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(this.field_175383_v.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    @Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(grinderGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        if (TileEntityGrinder.func_174903_a(this.tileGrinder))
        {
            i1 = this.func_175382_i(13);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
        }

        i1 = this.func_175381_h(24);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
    }

    private int func_175381_h(int p_175381_1_)
    {
        int j = this.tileGrinder.getField(2);
        int k = this.tileGrinder.getField(3);
        return k != 0 && j != 0 ? j * p_175381_1_ / k : 0;
    }

    private int func_175382_i(int p_175382_1_)
    {
        int j = this.tileGrinder.getField(1);

        if (j == 0)
        {
            j = 200;
        }

        return this.tileGrinder.getField(0) * p_175382_1_ / j;
    }
}