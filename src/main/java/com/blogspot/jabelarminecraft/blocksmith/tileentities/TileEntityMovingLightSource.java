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

package com.blogspot.jabelarminecraft.blocksmith.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;

/**
 * @author jabelar
 *
 */
public class TileEntityMovingLightSource extends TileEntity implements IUpdatePlayerListBox
{
    public EntityPlayer thePlayer;
    
    public TileEntityMovingLightSource()
    {
        // after constructing the tile entity instance, remember to call the setPlayer() method.
        // DEBUG
        System.out.println("Constructing");
    }
    
    /**
     * This controls whether the tile entity gets replaced whenever the block state is changed.
     * Normally only want this when block actually is replaced.
     */
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return (oldState.getBlock() != newSate.getBlock());
    }

    @Override
    public void update()
    {
        // DEBUG
        System.out.println("Updating");
        
        // check if player has moved away from the tile entity
        if (worldObj.getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(), 3.0D) == null)
        {
            // DEBUG
            System.out.println("Player has moved away from the tile entity at "+getPos());
            
            if (worldObj.getBlockState(getPos()).getBlock() == BlockSmith.blockMovingLightSource)
            {
                worldObj.setBlockToAir(getPos());
            }
        }        
    }  
    
    public void setPlayer(EntityPlayer parPlayer)
    {
        thePlayer = parPlayer;
    }
}
