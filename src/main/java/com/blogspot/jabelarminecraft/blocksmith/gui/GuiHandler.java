package com.blogspot.jabelarminecraft.blocksmith.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerGrinder;

public class GuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{ 
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if (tileEntity != null)
        {
        	if (ID == BlockSmith.GUI_ENUM.GRINDER.ordinal())
        	{
                return new ContainerGrinder(player.inventory, (IInventory)tileEntity);
        	}
        }

        return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if (tileEntity != null)
        {
        	if (ID == BlockSmith.GUI_ENUM.GRINDER.ordinal())
        	{
                return new GuiGrinder(player.inventory, (IInventory)tileEntity);
        	}
        }

        return null;
	}
}
