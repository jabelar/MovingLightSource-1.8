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

package com.blogspot.jabelarminecraft.blocksmith.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.blogspot.jabelarminecraft.blocksmith.recipes.GrinderRecipes;
import com.blogspot.jabelarminecraft.blocksmith.slots.SlotGrinderOutput;

/**
 * @author jabelar
 *
 */
public class ContainerGrinder 
extends Container
{
    private final IInventory tileGrinder;
    private int ticksGrindingItemSoFar;
    private int ticksPerItem;
    private int timeCanGrind;

    public ContainerGrinder(InventoryPlayer parInventoryPlayer, IInventory parIInventory)
    {
    	// DEBUG
    	System.out.println("ContainerGrinder constructor()");
    	
        tileGrinder = parIInventory;
        addSlotToContainer(new Slot(tileGrinder, 0, 56, 35));
        addSlotToContainer(new Slot(tileGrinder, 1, 56, 53));
        addSlotToContainer(new SlotGrinderOutput(parInventoryPlayer.player, tileGrinder, 2, 116, 35));
        
        // add player inventory slots
        int i;
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(parInventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // add hotbar slots
        for (i = 0; i < 9; ++i)
        {
            addSlotToContainer(new Slot(parInventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    /**
     * Add the given Listener to the list of Listeners. Method name is for legacy.
     */
    @Override
	public void addCraftingToCrafters(ICrafting listener)
    {
        super.addCraftingToCrafters(listener);
        listener.func_175173_a(this, tileGrinder);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)crafters.get(i);

            if (ticksGrindingItemSoFar != tileGrinder.getField(2))
            {
                icrafting.sendProgressBarUpdate(this, 2, tileGrinder.getField(2));
            }

            if (timeCanGrind != tileGrinder.getField(0))
            {
                icrafting.sendProgressBarUpdate(this, 0, tileGrinder.getField(0));
            }

            if (ticksPerItem != tileGrinder.getField(3))
            {
                icrafting.sendProgressBarUpdate(this, 3, tileGrinder.getField(3));
            }
        }

        ticksGrindingItemSoFar = tileGrinder.getField(2); // tick grinding item so far
        timeCanGrind = tileGrinder.getField(0); // time can grind
        ticksPerItem = tileGrinder.getField(3); // ticks per item
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        tileGrinder.setField(id, data);
    }

    @Override
	public boolean canInteractWith(EntityPlayer playerIn)
    {
        return tileGrinder.isUseableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex)
    {
    	// DEBUG
    	System.out.println("ContainerGrinder transferStackInSlot()");
    	
        ItemStack itemStack = null;
        Slot slot = (Slot)inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
        	// DEBUG
        	System.out.println("There is stack in the slot");
        	
            ItemStack itemStackAlreadyInSlot = slot.getStack();
            itemStack = itemStackAlreadyInSlot.copy();

            // if output slot
            if (slotIndex == 2)
            {
            	// DEBUG
            	System.out.println("The slot is the output slot");
            	
                if (!mergeItemStack(itemStackAlreadyInSlot, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemStackAlreadyInSlot, itemStack);
            }
            else if (slotIndex != 1 && slotIndex != 0) // if inventory slots
            {
            	// DEBUG
            	System.out.println("The slot is a player inventory slot");
            	
                if (GrinderRecipes.instance().getGrindingResult(itemStackAlreadyInSlot) != null)
                {
                    if (!mergeItemStack(itemStackAlreadyInSlot, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (slotIndex >= 3 && slotIndex < 30)
                {
                    if (!mergeItemStack(itemStackAlreadyInSlot, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (slotIndex >= 30 && slotIndex < 39 && !mergeItemStack(itemStackAlreadyInSlot, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemStackAlreadyInSlot, 3, 39, false))
            {
                return null;
            }

            if (itemStackAlreadyInSlot.stackSize == 0)
            {
            	// DEBUG
            	System.out.println("The slot is a grinder inventory slot");
            	
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemStackAlreadyInSlot.stackSize == itemStack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemStackAlreadyInSlot);
        }

        return itemStack;
    }
}