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

package com.blogspot.jabelarminecraft.blocksmith.items;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.utilities.Utilities;

/**
 * @author jabelar
 *
 */
public class ItemCowHide extends Item
{
	public ItemCowHide() 
    {
		super();
        setUnlocalizedName("cowhide");
        setCreativeTab(CreativeTabs.tabMaterials);
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack parItemStack) 
    {
        return (Utilities.stringToRainbow(StatCollector.translateToLocal(getUnlocalizedNameInefficiently(parItemStack) + ".name")).trim());
    }
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
	public ItemStack onItemRightClick(ItemStack parItemStack, World parWorld, EntityPlayer parPlayer)
    {
        MovingObjectPosition movingObjectPosition = getMovingObjectPositionFromPlayer(parWorld, parPlayer, false);

        if (movingObjectPosition == null)
        {
            return parItemStack;
        }
        else
        {
            if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos blockPos = movingObjectPosition.getBlockPos();

                if (!parPlayer.canPlayerEdit(blockPos.offset(movingObjectPosition.sideHit), movingObjectPosition.sideHit, parItemStack))
                {
                    return parItemStack;
                }

                IBlockState theBlockState = parWorld.getBlockState(blockPos);
                Block theBlock = theBlockState.getBlock();
                if (theBlock == BlockSmith.blockTanningRack)
                {
                	// DEBUG
                	System.out.println("ItemCowHide onRightClick() interacting with Tanning Rack");
                	parPlayer.triggerAchievement(BlockSmith.achievementTanningAHide);
                    parPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                    return exchangeItemStack(parItemStack, parPlayer, Items.leather);
                }
            }

            return parItemStack;
        }
    }

    
    private ItemStack exchangeItemStack(ItemStack parHeldItemStack, EntityPlayer parPlayer, Item parNewItem)
    {
        if (parPlayer.capabilities.isCreativeMode)
        {
            return parHeldItemStack;
        }
        else if (--parHeldItemStack.stackSize <= 0)
        {
        	// DEBUG
        	System.out.println("ItemCowHide exchangeItemStack() tanned a hide");
            return new ItemStack(parNewItem);
        }
        else
        {
            if (!parPlayer.inventory.addItemStackToInventory(new ItemStack(parNewItem)))
            {
                parPlayer.dropPlayerItemWithRandomChoice(new ItemStack(parNewItem, 1, 0), false);
            }

            return parHeldItemStack;
        }
    }

}
