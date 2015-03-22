package com.blogspot.jabelarminecraft.blocksmith.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.containers.ContainerDeconstructor;

/**
 * @author jglrxavpok
 */
public class UnGuiHandler implements IGuiHandler
{

    private ContainerDeconstructor lastServerContainer;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        if(world.getBlockState(new BlockPos(x,y,z)).getBlock() == BlockSmith.instance.blockDeconstructor)
        {
            if(id == 0)
            {
                ContainerDeconstructor c = new ContainerDeconstructor(player.inventory, world, /*world.getBlockMetadata(x, y, z) == 1*/ true, x, y, z, BlockSmith.standardLevel, BlockSmith.maxUsedLevel);
                lastServerContainer = c;
                return c;
            }
            else if(id == 1)
            {
                if(lastServerContainer != null)
                {
                    lastServerContainer.onContainerClosed(player);
                    lastServerContainer = null;
                }
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        if(world.getBlockState(new BlockPos(x,y,z)).getBlock() == BlockSmith.instance.blockDeconstructor)
        {
            if(id == 0)
            {
                String name = I18n.format("tile.uncrafting_table.name");
                return new GuiDeconstructor(player.inventory, world, name, /*world.getBlockMetadata(x, y, z) == 1*/ false, x, y, z, BlockSmith.instance.minLvlServer, BlockSmith.instance.maxLvlServer);
            }
            /*else if(id == 1)
            {
                return new GuiUncraftOptions();
            }*/
        }
        return null;
    }

}
